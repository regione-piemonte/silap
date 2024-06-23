/*-
 * ========================LICENSE_START=================================
 * colmirbff
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirbff.api.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ConfigUtils;
import it.csi.silap.colmirbff.api.VersamentoEsoneriApi;
import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.Azienda;
import it.csi.silap.colmirbff.api.dto.DVersamentoStato;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.Parametro;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.Versamento;
import it.csi.silap.colmirbff.api.dto.VersamentoEsoneriRidotto;
import it.csi.silap.colmirbff.api.dto.VersamentoProvincia;
import it.csi.silap.colmirbff.api.dto.VersamentoPvPeriodo;
import it.csi.silap.colmirbff.api.dto.VersamentoSede;
import it.csi.silap.colmirbff.api.dto.VersamentoStato;
import it.csi.silap.colmirbff.api.dto.common.ReportResponse;
import it.csi.silap.colmirbff.api.dto.request.FormPrevisioniVersamentoEsoneri;
import it.csi.silap.colmirbff.api.dto.request.FormRicercaVersamentoEsoneri;
import it.csi.silap.colmirbff.api.dto.response.AziendaResponse;
import it.csi.silap.colmirbff.api.dto.response.MsgResponse;
import it.csi.silap.colmirbff.api.dto.response.ParametroResponse;
import it.csi.silap.colmirbff.api.dto.response.RicercaVersamentoEsoneriResponse;
import it.csi.silap.colmirbff.api.dto.response.VersamentoPvPeriodoResponse;
import it.csi.silap.colmirbff.api.dto.response.VersamentoPvPeriodosResponse;
import it.csi.silap.colmirbff.api.dto.response.VersamentoResponse;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.api.impl.manager.PosizioneDebitoriaManager;
import it.csi.silap.colmirbff.api.impl.manager.SilpManager;
import it.csi.silap.colmirbff.api.impl.manager.VersamentoEsoneriBatchManager;
import it.csi.silap.colmirbff.api.impl.manager.VersamentoEsoneriManager;
import it.csi.silap.colmirbff.api.impl.manager.VersamentoEsoneriStampaManager;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoTPosizioneDebitoria;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamento;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProvincia;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvPeriodo;
import it.csi.silap.colmirbff.interceptor.Audited;
import it.csi.silap.colmirbff.interceptor.UserControl;
import it.csi.silap.colmirbff.util.QueryUtils;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;
import it.csi.silap.colmirbff.util.SqlComplexResult;
import it.csi.silap.colmirbff.util.report.ReportTabellare;
import it.csi.silap.colmirbff.util.report.ReportUtilities;

@Provider
public class VersamentoEsoneriApiServiceImpl extends BaseApiServiceImpl implements VersamentoEsoneriApi {

	@Inject
	private VersamentoEsoneriManager versamentoEsoneriManager;
	
	@Inject
	private VersamentoEsoneriBatchManager versamentoEsoneriBatchManager;


	@Inject
	private SilpManager silpManager;

	@Inject
	private VersamentoEsoneriStampaManager versamentoEsoneriStampaManager;
	
	@Inject
	private PosizioneDebitoriaManager posizioneDebitoriaManager;

	@ConfigProperty(name = "file.system.home")
	private String fileSystemHome;

	@UserControl
	@Audited
	@Override
	public Response ricercaVersamentoEsoneri(FormRicercaVersamentoEsoneri formRicercaVersamentoEsoneri, int page,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "ricercaVersamentoEsoneri";

		SqlComplexResult<VersamentoEsoneriRidotto> ris = ricercaVersamentoEsoneriInternal(formRicercaVersamentoEsoneri,
				page);

		return buildManagedResponseLogEnd(httpHeaders,
				new RicercaVersamentoEsoneriResponse.Builder().setRecordOfPage(ris.getList().size())
						.setRecordPage(SilapConstants.NUMERO_RECORD_PER_PAGINA).setTotalResult(ris.getTotalResult())
						.setTotalPage(ris.getTotalPage()).setCurrentPage(page).setList(ris.getList()).build(),
				methodName);

	}

	@UserControl
	@Audited
	@Override
	public Response stampaRicercaVersamentoEsoneri(FormRicercaVersamentoEsoneri formRicercaVersamentoEsoneri,
			String format, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "stampaRicercaVersamentoEsoneri";

		SqlComplexResult<VersamentoEsoneriRidotto> ris = ricercaVersamentoEsoneriInternal(formRicercaVersamentoEsoneri);

		if (ris != null) {
			try {
				ReportTabellare reportTabellare = stampaRicerca(ris.getList());
				reportTabellare.setLandscape(true);
				ReportResponse response = new ReportResponse("Versamento_Esoneri");
				ReportUtilities.makeReportResponse(format, response, reportTabellare);
				return response.composeResponse();
			} catch (Exception e) {
				Log.error("[VersamentoEsoneriApiServiceImpl::stampaRicercaVersamentoEsoneri] Errore stampa tabellare pdf/xls ", e);
			}
		}

		return buildManagedResponseLogEndNegative(getMsg(SilapConstants.MSG_ERRORE_STAMPA), httpHeaders, methodName);

	}

	private SqlComplexResult<VersamentoEsoneriRidotto> ricercaVersamentoEsoneriInternal(
			FormRicercaVersamentoEsoneri formRicercaVersamentoEsoneri) {
		return ricercaVersamentoEsoneriInternal(formRicercaVersamentoEsoneri, -1);
	}

	private SqlComplexResult<VersamentoEsoneriRidotto> ricercaVersamentoEsoneriInternal(
			FormRicercaVersamentoEsoneri formRicercaVersamentoEsoneri, int page) {

		String sqlSelect = dao.loadSqlFromFileIfNecessary("query.findVersamentoEsoneri");

		QueryUtils query = new QueryUtils();
		query.sql(sqlSelect);

		Long idRuolo = SilapThreadLocalContainer.ID_RUOLO.get();
		String codiceFiscaleSoggettoAbilitato = SilapThreadLocalContainer.ID_COD_FISC_SOGG_ABILITATO.get();
		if (codiceFiscaleSoggettoAbilitato != null && SilapConstants.RUOLO_CONSULENTE.longValue() == idRuolo.longValue()
				&& codiceFiscaleSoggettoAbilitato.trim().length() > 0) {
			sqlSelect += " and ver.cod_fiscale_soggetto = :codFiscaleSoggetto ";
			query.addNativeParameter("codFiscaleSoggetto", codiceFiscaleSoggettoAbilitato);
		}

		if (formRicercaVersamentoEsoneri.getCodFisc() != null
				&& formRicercaVersamentoEsoneri.getCodFisc().trim().length() > 0) {
			sqlSelect += " and ver.cod_fiscale like :codFiscale ";
			query.addNativeParameter("codFiscale", formRicercaVersamentoEsoneri.getCodFisc().trim() + "%");
		}

		if (formRicercaVersamentoEsoneri.getAnnoProtocollo() != null) {
			sqlSelect += " and ver.anno_protocollo = :annoProtocollo ";
			query.addNativeParameter("annoProtocollo", formRicercaVersamentoEsoneri.getAnnoProtocollo());
		}

		if (formRicercaVersamentoEsoneri.getNumProtocollo() != null) {
			sqlSelect += " and ver.num_protocollo = :numProtocollo ";
			query.addNativeParameter("numProtocollo", formRicercaVersamentoEsoneri.getNumProtocollo());
		}
		
		
		if ("S".equals(formRicercaVersamentoEsoneri.getFlgUnicaSoluzione()) && !"S".equals(formRicercaVersamentoEsoneri.getFlgDueRate())) {
			sqlSelect += " and ver.num_rate = 1 ";
		}
		else if ("S".equals(formRicercaVersamentoEsoneri.getFlgDueRate()) && !"S".equals(formRicercaVersamentoEsoneri.getFlgUnicaSoluzione())) {
			sqlSelect += " and ver.num_rate = 2 ";
		}
		

		if (idRuolo != null && (SilapConstants.RUOLO_AMMINISTRATORE.longValue() == idRuolo.longValue()
				|| SilapConstants.RUOLO_REGIONE.longValue() == idRuolo.longValue()
				|| SilapConstants.RUOLO_CONSULENTE.longValue() == idRuolo.longValue())) {
			if (formRicercaVersamentoEsoneri.getCodFisc() == null
					&& formRicercaVersamentoEsoneri.getDenomAzienda() != null
					&& formRicercaVersamentoEsoneri.getDenomAzienda().trim().length() > 0) {
				sqlSelect += " and ver.ds_denominazione_azienda like :denomAziendaLike ";
				query.addNativeParameter("denomAziendaLike",
						"%" + formRicercaVersamentoEsoneri.getDenomAzienda().trim().toUpperCase() + "%");
			} else {
				if (formRicercaVersamentoEsoneri.getDenomAzienda() != null
						&& formRicercaVersamentoEsoneri.getDenomAzienda().trim().length() > 0) {
					sqlSelect += " and ver.ds_denominazione_azienda like :denomAziendaLike ";
					query.addNativeParameter("denomAziendaLike",
							"%" + formRicercaVersamentoEsoneri.getDenomAzienda().trim().toUpperCase() + "%");
				}
			}
		}

		if (formRicercaVersamentoEsoneri.getDataInizio() != null) {
			sqlSelect += " and stato.d_stato >= :dataInizio";
			query.addNativeParameter("dataInizio",
					QueryUtils.atStartOfDay(formRicercaVersamentoEsoneri.getDataInizio()));
		}

		if (formRicercaVersamentoEsoneri.getDataFine() != null) {
			sqlSelect += " and stato.d_stato <= :dataFine";
			query.addNativeParameter("dataFine", QueryUtils.atEndOfDay(formRicercaVersamentoEsoneri.getDataFine()));
		}

		if (formRicercaVersamentoEsoneri.getStatoDichiarazione() != null
				&& formRicercaVersamentoEsoneri.getStatoDichiarazione().size() > 0) {
			sqlSelect += " and esoDStato.id_eso_d_versamento_stato ";
			sqlSelect += query.addNativeInParameter("versamentoStato",
					formRicercaVersamentoEsoneri.getStatoDichiarazione());
		}

		String sqlOrder = " order by ver.anno_protocollo desc, ver.num_protocollo desc ";

		return dao.findNativeQuerySql(sqlSelect, query.getParams(), sqlOrder, page, VersamentoEsoneriRidotto.class);
	}

	private ReportTabellare stampaRicerca(List<VersamentoEsoneriRidotto> list) {
		ReportTabellare reportTabellare = new ReportTabellare("Versamento esoneri");
		// reportTabellare.setLandscape(false);

		reportTabellare.addNomiColonne("Azienda");
		reportTabellare.addNomiColonne("Anno rif.");
		reportTabellare.addNomiColonne("Data stato");
		reportTabellare.addNomiColonne("Stato");
		reportTabellare.addNomiColonne("N. protocollo");
		reportTabellare.addNomiColonne("Importo");
		
		
		reportTabellare.addNomiColonne("N. Rate");
		reportTabellare.addNomiColonne("Importo versato");
		reportTabellare.addNomiColonne("Data versamento");
		
		
		DecimalFormat formatDecimal = new DecimalFormat("#,##0.00");
		
			
		for (VersamentoEsoneriRidotto ver : list) {
			List<Object> cols = new ArrayList<Object>();

			cols.add(ver.getCodFiscale());
			cols.add(ver.getAnnoRiferimento());
			cols.add(ReportUtilities.formatDate(ver.getDtStato()));
			cols.add(ver.getDescStato());
			cols.add(ver.getNumProtocollo());
			
			cols.add(ReportUtilities.formatNumber(ver.getImporto(), formatDecimal));
			cols.add(ver.getNumRata());
			cols.add(ReportUtilities.formatNumber(ver.getImportoPagato(), formatDecimal));
			cols.add(ReportUtilities.formatDate(ver.getDtEsito()));

			reportTabellare.addValori(cols);
		}
		return reportTabellare;
	}

	@UserControl
	@Audited
	@Override
	public Response ricercaAzienda(String cf,Long idVersamento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		final String methodName = "ricercaAzienda";

		AziendaResponse response = new AziendaResponse();
		try {
			Azienda azienda = silpManager.ricercaDatiAzienda(cf,idVersamento);
			response.setAzienda(azienda);
		} catch (BusinessException e) {
			response.setEsitoPositivo(false);
			response.setApiMessages(e.getMessages());
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);

	}

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response controlloModifica(Versamento versamento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		final String methodName = "controlloModifica";
		VersamentoResponse response = new VersamentoResponse();

		try {

			EsoTVersamento esoTVersamento = EsoTVersamento.findById(versamento.getIdEsoTVersamento());
			Versamento versamentoDB = mappers.VERSAMENTO.toModel(esoTVersamento);
			Versamento versamentoAttuale = versamentoEsoneriManager.getVersamentoAttuale(versamento);

			if (!versamentoDB.logicalCompare(versamentoAttuale)) {
				response.addApiMessage(getMsg(SilapConstants.MSG_CONTROLLO_MODIFICA));
				response.setEsitoPositivo(true);
				return buildManagedResponseLogEnd(httpHeaders, response, methodName);
			} else {
				Long idVersamento = versamentoEsoneriManager.modificaDatiAziendaliEssenziali(versamento,
						esoTVersamento);
				response.setVersamento(mappers.VERSAMENTO.toModel(EsoTVersamento.findById(idVersamento)));
			}
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response salvaDatiAziendali(Versamento versamento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		final String methodName = "salvaDatiAziendali";

		VersamentoResponse response = new VersamentoResponse();

		try {
			Long idVersamento = versamentoEsoneriManager.salvaDatiAziendali(versamento);
			response.setVersamento(mappers.VERSAMENTO.toModel(EsoTVersamento.findById(idVersamento)));
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response modificaDatiAziendali(Versamento versamento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		final String methodName = "modificaDatiAziendali";

		VersamentoResponse response = new VersamentoResponse();

		try {
			Long idVersamento = versamentoEsoneriManager.modificaDatiAziendali(versamento);
			response.setVersamento(mappers.VERSAMENTO.toModel(EsoTVersamento.findById(idVersamento)));
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response salvaGGLavorativiProvincia(VersamentoProvincia versamentoProvincia,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "salvaGGLavorativiProvincia";

		VersamentoResponse response = new VersamentoResponse();
		try {
			Long idVersamento = versamentoEsoneriManager.salvaGGLavorativiProvincia(versamentoProvincia);
			response.setVersamento(mappers.VERSAMENTO.toModel(EsoTVersamento.findById(idVersamento)));
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response modificaGGLavorativiProvincia(VersamentoProvincia versamentoProvincia,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "modificaGGLavorativiProvincia";

		VersamentoResponse response = new VersamentoResponse();
		try {
			Long idVersamento = versamentoEsoneriManager.modificaGGLavorativiProvincia(versamentoProvincia);
			response.setVersamento(mappers.VERSAMENTO.toModel(EsoTVersamento.findById(idVersamento)));
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response salvaSospensioniProvincia(VersamentoProvincia versamentoProvincia,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "salvaSospensioniProvincia";

		VersamentoResponse response = new VersamentoResponse();
		try {
			Long idVersamento = versamentoEsoneriManager.salvaSospensioniProvincia(versamentoProvincia);
			EsoTVersamento versamento = EsoTVersamento.findById(idVersamento);

			StringBuffer printCalcoloAutomatico = new StringBuffer();
			long timeMillis = System.currentTimeMillis();
			versamentoEsoneriManager.impostaPeriodiProivincia(versamento,
					versamentoProvincia.getIdEsoTVersamentoProvincia(), printCalcoloAutomatico, false);
			versamento = EsoTVersamento.findById(idVersamento);

			// PRINT DEV E TST
			if (ConfigUtils.isProfileActive("dev")) {
				long timeSeconds = System.currentTimeMillis() - timeMillis;
				printCalcoloAutomatico.append("\nTEMPO ESECUZIONE millisecondi:" + timeSeconds);
				
				Log.info(printCalcoloAutomatico.toString());
				System.out.println(printCalcoloAutomatico.toString());
				versamento.setPrintCalcoloAutomatico(printCalcoloAutomatico.toString().replaceAll("\n", "<br>"));
			}
			else {
				long timeSeconds = System.currentTimeMillis() - timeMillis;
				Log.info("TEMPO ESECUZIONE CALCOLO AUTOMATICO millisecondi: " + timeSeconds);
			}
			
			Versamento ret = mappers.VERSAMENTO.toModel(versamento);
			ret = versamentoEsoneriManager.raggruppaPeriodi(ret);
			response.setVersamento(ret);
		} catch (BusinessException ex) {
			Log.error("[VersamentoEsoneriApiServiceImpl::stampaRicercaVersamentoEsoneri] calcolo ", ex);
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response modificaSospensioniProvincia(VersamentoProvincia versamentoProvincia,
			boolean ripristina,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "modificaSospensioniProvincia";

		VersamentoResponse response = new VersamentoResponse();
		try {
			Long idVersamento = versamentoEsoneriManager.modificaSospensioniProvincia(versamentoProvincia);
			EsoTVersamento versamento = EsoTVersamento.findById(idVersamento);

			StringBuffer printCalcoloAutomatico = new StringBuffer();
			long timeMillis = System.currentTimeMillis();
			versamentoEsoneriManager.impostaPeriodiProivincia(versamento,
					versamentoProvincia.getIdEsoTVersamentoProvincia(), printCalcoloAutomatico, ripristina);
			versamento = EsoTVersamento.findById(idVersamento);

			// PRINT DEV E TST
			if (ConfigUtils.isProfileActive("dev")) {
				long timeSeconds = System.currentTimeMillis() - timeMillis;
				printCalcoloAutomatico.append("\nTEMPO ESECUZIONE millisecondi:" + timeSeconds);
				System.out.println(printCalcoloAutomatico.toString());
				versamento.setPrintCalcoloAutomatico(printCalcoloAutomatico.toString().replaceAll("\n", "<br>"));
			}
			
			Versamento ret = mappers.VERSAMENTO.toModel(versamento);
			ret = versamentoEsoneriManager.raggruppaPeriodi(ret);
			
			boolean sovrapposizioni = versamentoEsoneriManager.verificaSovrapposizioniPeriodi(ret, versamentoProvincia);
			if (sovrapposizioni)
				response.addApiMessage(getMsg(SilapConstants.MSG_CONTROLLO_PERIODI_SOVRAPPOSTI_MODIFICA));
		
			response.setVersamento(ret);
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response cancellaPeriodoAutomaticoProvincia(Long idProvincia, VersamentoPvPeriodo periodo,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "cancellaPeriodoAutomaticoProvincia";

		VersamentoPvPeriodosResponse response = new VersamentoPvPeriodosResponse();
		try {
			EsoTVersamentoPvPeriodo.update("flgTipo = 'C' where idEsoTVersamentoPvPeriodo = ?1",
					periodo.getIdEsoTVersamentoPvPeriodo());
			
			EsoTVersamentoProvincia esoTVersamentoProvincia = EsoTVersamentoProvincia.findById(idProvincia);
			EsoTVersamento esoTVersamento = EsoTVersamento.findById(esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento());
			
			
			Versamento ret = mappers.VERSAMENTO.toModel(esoTVersamento);
			versamentoEsoneriManager.verificaSovrapposizioniPeriodi(ret, mappers.VERSAMENTO_PROVINCIA.toModel(esoTVersamentoProvincia));
			
			for (VersamentoProvincia prov : ret.getEsoTVersamentoProvincias()) {
				if (prov.getIdEsoTVersamentoProvincia().longValue() == idProvincia) {
					response.setPeriodi(prov.getEsoTVersamentoPvPeriodos());
				}
			}
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}
	
	
	
	@UserControl
	@Audited
	@Override
	@Transactional
	public Response cancellaPeriodiProvincia(Long idProvincia, List<VersamentoPvPeriodo> periodi,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "cancellaPeriodiProvincia";

		VersamentoPvPeriodosResponse response = new VersamentoPvPeriodosResponse();
		try {
			for (VersamentoPvPeriodo periodo : periodi) {
				if ("A".equalsIgnoreCase(periodo.getFlgTipo())) {
					EsoTVersamentoPvPeriodo.update("flgTipo = 'C' where idEsoTVersamentoPvPeriodo = ?1",
							periodo.getIdEsoTVersamentoPvPeriodo());
				}
				else if ("O".equalsIgnoreCase(periodo.getFlgTipo())) {
					EsoTVersamentoPvPeriodo peridodoDB = EsoTVersamentoPvPeriodo
							.findById(periodo.getIdEsoTVersamentoPvPeriodo());
					peridodoDB.delete();
				}
			}
			
			EsoTVersamentoProvincia esoTVersamentoProvincia = EsoTVersamentoProvincia.findById(idProvincia);
			EsoTVersamento esoTVersamento = EsoTVersamento.findById(esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento());
			Versamento ret = mappers.VERSAMENTO.toModel(esoTVersamento);
			versamentoEsoneriManager.verificaSovrapposizioniPeriodi(ret, mappers.VERSAMENTO_PROVINCIA.toModel(esoTVersamentoProvincia));
			
			for (VersamentoProvincia prov : ret.getEsoTVersamentoProvincias()) {
				if (prov.getIdEsoTVersamentoProvincia().longValue() == idProvincia) {
					response.setPeriodi(prov.getEsoTVersamentoPvPeriodos());
				}
			}
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}
	
	
	

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response inserisciPeriodoProvincia(Long idProvincia, VersamentoPvPeriodo periodo,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "inserisciPeriodoProvincia";
		VersamentoPvPeriodoResponse response = new VersamentoPvPeriodoResponse();

		try {

			periodo = versamentoEsoneriManager.impostaPeriodoOperatore(idProvincia, periodo);
			EsoTVersamentoPvPeriodo peridodoDB = mappers.VERSAMENTO_PV_PERIODO.toEntity(periodo);

			String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
			String userAggiorn = userInserim;
			Date now = new Date();
			Long timestamp = 0L;

			EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
			esoTVersamentoProvinciaPersist.setIdEsoTVersamentoProvincia(idProvincia);
			peridodoDB.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);

			peridodoDB.setCodUserInserim(userInserim);
			peridodoDB.setDInserim(now);
			peridodoDB.setCodUserAggiorn(userAggiorn);
			peridodoDB.setDAggiorn(now);
			peridodoDB.setNTimestamp(timestamp);
			peridodoDB.persist();

			response.setPeriodo(mappers.VERSAMENTO_PV_PERIODO.toModel(peridodoDB));
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response cancellaPeriodoProvincia(VersamentoPvPeriodo periodo, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		final String methodName = "cancellaPeriodoProvincia";

		VersamentoPvPeriodoResponse response = new VersamentoPvPeriodoResponse();
		try {
			EsoTVersamentoPvPeriodo peridodoDB = EsoTVersamentoPvPeriodo
					.findById(periodo.getIdEsoTVersamentoPvPeriodo());
			peridodoDB.delete();
			response.setPeriodo(periodo);
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response confermaVersamentoProvincie(Long idVersamento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		final String methodName = "confermaVersamentoProvincie";

		VersamentoResponse response = new VersamentoResponse();
		try {
			EsoTVersamento esoTVersamento = EsoTVersamento.findById(idVersamento);
			esoTVersamento = versamentoEsoneriManager.confermaVersamentoProvincie(esoTVersamento);
			response.setVersamento(mappers.VERSAMENTO.toModel(esoTVersamento));
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response confermaInviaVersamento(Long idVersamento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		final String methodName = "confermaInviaVersamento";

		VersamentoResponse response = new VersamentoResponse();
		try {
			Versamento versamento = mappers.VERSAMENTO.toModel(EsoTVersamento.findById(idVersamento));
			response.addApiMessage(versamentoEsoneriManager.confermaInviaVersamento(versamento));
			response.setVersamento(versamento);
		} catch (BusinessException ex) {
			response.setApiMessages(ex.getMessages());
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response autorizzaVersamento(VersamentoStato versamentoStato, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		final String methodName = "autorizzaVersamento";
		MsgResponse response = new MsgResponse();
		try {
			versamentoEsoneriManager.autorizzaVersamento(versamentoStato);
		}catch (BusinessException err) {
			response.setEsitoPositivo(false);
			response.setApiMessages(err.getMessages());
			return buildManagedResponseLogEnd(httpHeaders, response, methodName);
		}
		Messaggio msg = new Messaggio();
		msg.setDsSilapDMessaggio("Dichiarazione aggiornata, mail inviata.");
		response.setMsg(msg);
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);

	}

	@UserControl
	@Audited
	@Override
	public Response getNuovoVersamento(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		final String methodName = "getNuovoVersamento";

		Versamento versamento = new Versamento();

		versamento.setNumRate(2L);

		List<VersamentoStato> stati = new ArrayList<VersamentoStato>();
		VersamentoStato versamentoStato = new VersamentoStato();
		versamentoStato.setFlgCurrentRecord("S");
		versamentoStato.setDtStato(new Date());

		DVersamentoStato esoDVersamentoStato = new DVersamentoStato();
		esoDVersamentoStato.setId(SilapConstants.STATO_BOZZA);
		versamentoStato.setEsoDVersamentoStato(esoDVersamentoStato);
		stati.add(versamentoStato);
		versamento.setEsoTVersamentoStatos(stati);

		VersamentoSede sede = new VersamentoSede();
		versamento.setVersamentoSede(sede);

		versamento.setNumCreditoResiduo(new BigDecimal(0));

		VersamentoResponse response = new VersamentoResponse();
		response.setVersamento(versamento);
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	@Transactional
	public Response updateStatoVersamento(Long idEsoTVersamento, Long idDVersamentoStato,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "updateStatoVersamento";
		try {
			versamentoEsoneriManager.updateStatoVersamento(idEsoTVersamento, idDVersamentoStato);
		} catch (Exception err) {
			Log.error("[VersamentoEsoneriApiServiceImpl::updateStatoVersamento]", err);
			return buildManagedResponseLogEndNegative(getMsg(SilapConstants.MSG_ERRORE_GET_VERSAMENTO), httpHeaders,
					methodName);
		}
		EsoTVersamento esoTVersamento = EsoTVersamento.findById(idEsoTVersamento);
		VersamentoResponse response = new VersamentoResponse();
		if (SilapConstants.STATO_ANNULLATA.compareTo(idDVersamentoStato) == 0) {
			response.addApiMessage(
					getMsg(SilapConstants.MSG_CONFERMA_STATO_ANNULLATA, esoTVersamento.getAnnoRiferimento(),
							esoTVersamento.getDsDenominazioneAzienda(), esoTVersamento.getCodFiscale()));
		}
		response.setVersamento(mappers.VERSAMENTO.toModel(esoTVersamento));
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response getParametroByCod(String codParametro, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		final String methodName = "getParametroByCod";

		ParametroResponse response = new ParametroResponse();

		Parametro parametro = getParametroByCod(codParametro);

		response.setParametro(parametro);

		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response getVersamento(Long idEsoTVersamento, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		final String methodName = "getVersamento";
		Versamento versamento = versamentoEsoneriManager.getVersamento(idEsoTVersamento);
		VersamentoResponse response = new VersamentoResponse();
		if (versamento != null) {
			response.setVersamento(versamento);
		} else {
			response.addApiMessage(getMsg(SilapConstants.MSG_ERRORE_GET_VERSAMENTO));
			response.setEsitoPositivo(false);
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response stampaVersamento(Long idEsoTVersamento, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		final String methodName = "stampaVersamento";
		Versamento versamento = new Versamento();
		versamento = versamentoEsoneriManager.getVersamento(idEsoTVersamento);
		if (versamento != null) {

			String filename = versamento.getIdEsoTVersamento().toString().trim() + "_"
					+ versamento.getAnnoRiferimento().toString().trim() + "_" + versamento.getCodFiscale().trim();
			ReportResponse responseStampa = new ReportResponse(filename);

			try {
				VersamentoStato stato = versamentoEsoneriManager.getCurrentStato(versamento);
				if (stato != null && stato.getEsoDVersamentoStato() != null
						&& (stato.getEsoDVersamentoStato().getId().longValue() == SilapConstants.STATO_AUTORIZZATA
								|| stato.getEsoDVersamentoStato().getId()
										.longValue() == SilapConstants.STATO_NON_AUTORIZZATA)) {

					File basePath = new File(fileSystemHome);
					if (basePath != null && basePath.exists() && basePath.isDirectory()) {
						File stampaFile = new File(basePath, versamento.getAnnoRiferimento() + "/" + filename + ".pdf");
						if (stampaFile.exists()) {
							ReportUtilities.makePdfFileResponse(responseStampa, stampaFile,
									responseStampa.getFileNameTemplate());
							return responseStampa.composeResponse();
						}
					}
				}
				ReportUtilities.makePdfResponse(responseStampa,
						versamentoEsoneriStampaManager.stampaVersamentoEsoneri(versamento),
						responseStampa.getFileNameTemplate());
			} catch (Exception e) {
				Log.error("[VersamentoEsoneriApiServiceImpl::stampaVersamento] Errore stampa tabellare pdf/xls ", e);
			}
			return responseStampa.composeResponse();

		}
		return buildManagedResponseLogEndNegative(getMsg(SilapConstants.MSG_ERRORE_GET_VERSAMENTO), httpHeaders,
				methodName);

	}

	@UserControl
	@Audited
	@Override
	public Response modificaVersamento(Long idEsoTVersamento, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		final String methodName = "modificaVersamento";

		Versamento versamento = versamentoEsoneriManager.getVersamento(idEsoTVersamento);
		VersamentoResponse response = new VersamentoResponse();

		boolean annoRifCompatibile = false;
		boolean esoneriCompatibili = false;
		boolean prospettiCompatibili = false;

		if (versamento == null) {
			response.addApiMessage(getMsg(SilapConstants.MSG_ERRORE_GET_VERSAMENTO));
			response.setEsitoPositivo(false);
			return buildManagedResponseLogEnd(httpHeaders, response, methodName);
		}

		if (versamento.getAnnoRiferimento() != null)
			annoRifCompatibile = versamentoEsoneriManager.annoRiferimentoCompatibile(versamento.getAnnoRiferimento());

		esoneriCompatibili = versamentoEsoneriManager.esoneriSilpEsiste(versamento);
		prospettiCompatibili = versamentoEsoneriManager.prospettiProdisEsiste(versamento);

		if (!annoRifCompatibile) {
			response.addApiMessage(getMsg(SilapConstants.MSG_ANNO_BOZZA_VECCHIO, versamento.getDsDenominazioneAzienda(),
					versamento.getCodFiscale(), versamento.getAnnoRiferimento()));
			response.setEsitoPositivo(false);
			return buildManagedResponseLogEnd(httpHeaders, response, methodName);
		}
		if (!esoneriCompatibili) {
			response.addApiMessage(
					getMsg(SilapConstants.MSG_ERRORE_CONDIZIONI_VARIATE, versamento.getDsDenominazioneAzienda(),
							versamento.getCodFiscale(), versamento.getAnnoRiferimento()));
			response.setEsitoPositivo(false);
			return buildManagedResponseLogEnd(httpHeaders, response, methodName);
		}
		if (!prospettiCompatibili) {
			response.addApiMessage(
					getMsg(SilapConstants.MSG_COD_FISCALE_AZIENDA_NON_PRESENTE, versamento.getCodFiscale()));
			response.setEsitoPositivo(false);
			return buildManagedResponseLogEnd(httpHeaders, response, methodName);
		}

		response.setVersamento(versamento);
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}

	@UserControl
	@Audited
	@Override
	public Response autorizzaDichiarazioniBatch(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		final String methodName = "autorizzaDichiarazioniBatch";
		MsgResponse response = new MsgResponse();

		versamentoEsoneriBatchManager.autorizzaDichiarazioniBatch();

		Messaggio msg = new Messaggio();
		msg.setDsSilapDMessaggio("Autorizzazione automatica delle richieste accettate lanciata con successo.");
		response.setMsg(msg);
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}
	
	@UserControl
	@Audited
	@Override
	public Response downloadPrevisioneDichiarazioniTemplate(
			SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		final String methodName = "downloadPrevisioneDichiarazioniTemplate";
		
		try {
			ReportTabellare reportTabellare = new ReportTabellare("Template batch previsione dichiarazioni esoneri");
			reportTabellare.addNomiColonne("Codice fiscale azienda");
			List<Object> cols = new ArrayList<Object>();
			cols.add("XXXXXXXXXXX");
			reportTabellare.addValori(cols);
			reportTabellare.setLandscape(false);
			ReportResponse response = new ReportResponse("previsione_dichiarazioni");
			ReportUtilities.makeReportResponse("xls", response, reportTabellare);
			return response.composeResponse();
		} catch (Exception e) {
			Log.error("[VersamentoEsoneriApiServiceImpl::downloadPrevisioneDichiarazioniTemplate] Errore stampa template batch previsione dichiarazioni esoneri xls ", e);
		}
		return buildManagedResponseLogEndNegative(getMsg(SilapConstants.MSG_ERRORE_STAMPA), httpHeaders, methodName);
	}
	
	
	@UserControl
	@Audited
	@Override
	public Response uploadPrevisioneDichiarazioni(FormPrevisioniVersamentoEsoneri formPrevisioniVersamentoEsoneri,
			SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		final String methodName = "uploadPrevisioneDichiarazioniBatch";
		MsgResponse response = new MsgResponse();
		try {
			versamentoEsoneriBatchManager.previsioneDichiarazioniBatch(
					formPrevisioniVersamentoEsoneri.getAnno(),
					formPrevisioniVersamentoEsoneri.getEmail(),
					formPrevisioniVersamentoEsoneri.getAttachment());
			response.setMsg(getMsg(SilapConstants.MSG_PAGOPA_PREVISIONE));
		}
		catch (BusinessException err) {
			response.setMsg(getMsg(SilapConstants.MSG_PAGOPA_ERR_PREVISIONE));
		}
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}
	
	
	@UserControl
	@Audited
	@Override
	public Response creazionePosizioniDebitorieBatch(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		final String methodName = "creazionePosizioniDebitorieBatch";
		MsgResponse response = new MsgResponse();
		versamentoEsoneriBatchManager.creazionePosizioniDebitorieBatch();
		response.setMsg(getMsg(SilapConstants.MSG_PAGOPA_CREAZIONE_POSIZIONI));
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}
	
	@UserControl
	@Audited
	@Override
	public Response checkPosizioniDebitorieBatch(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		final String methodName = "checkPosizioniDebitorieBatch";
		MsgResponse response = new MsgResponse();
		Messaggio apiMsg = versamentoEsoneriManager.checkPosizioniDebitorieBatch();
		response.setEsitoPositivo(!"E".equals(apiMsg.getSilapDTipoMessaggio().getIdSilapDTipoMessaggio()));
		response.addApiMessage(new ApiMessage(apiMsg));
		response.setMsg(getMsg(SilapConstants.MSG_PAGOPA_CHECK_POSIZIONI));
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}
	
	
	
	@UserControl
	@Audited
	@Override
	public Response getPdfAvvisoPagamento(String idVersamento, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		final String methodName = "getPdfAvvisoPagamento";
		File pdf = posizioneDebitoriaManager.getFilePdfAvvisoPagemento(Long.parseLong(idVersamento));
		if (pdf != null && pdf.exists()) {
			try {
				ReportResponse response = new ReportResponse("Versamento_Esoneri");
				ReportUtilities.makePdfFileResponse(response, pdf, pdf.getName());
				return response.composeResponse();
			} catch (Exception e) {
				Log.error("[VersamentoEsoneriApiServiceImpl::getPdfAvvisoPagamento] Errore stampa tabellare pdf/xls ", e);
			}
		}
		else {
			EsoTVersamento esoTVersamento = EsoTVersamento.findById(Long.valueOf(idVersamento));			
			List<EsoTPosizioneDebitoria> esoTPosizioneDebitorias = esoTVersamento.getEsoTPosizioneDebitorias();
			if (esoTPosizioneDebitorias != null && esoTPosizioneDebitorias.size()>0) {
				String codiceIuv1 = null;
				String codiceIuv2 = null;
				for (EsoTPosizioneDebitoria pd : esoTPosizioneDebitorias) {
					if (pd.getNumRata() == 1)
						codiceIuv1 = pd.getCodIuv();
					else if (pd.getNumRata() == 2)
						codiceIuv2 = pd.getCodIuv();
				}
				pdf = posizioneDebitoriaManager.ricercaESalvaPdf(esoTVersamento, codiceIuv1,codiceIuv2);
				try {
					ReportResponse response = new ReportResponse("Versamento_Esoneri");
					ReportUtilities.makePdfFileResponse(response, pdf, pdf.getName());
					return response.composeResponse();
				} catch (Exception e) {
					Log.error("[VersamentoEsoneriApiServiceImpl::getPdfAvvisoPagamento] Errore stampa tabellare pdf/xls ", e);
				}
				
			}
			else buildManagedResponseLogEndNegativeApiError(getMsg(SilapConstants.MSG_ERRORE_STAMPA), httpHeaders, methodName);
		}
		return buildManagedResponseLogEndNegativeApiError(getMsg(SilapConstants.MSG_ERRORE_STAMPA), httpHeaders, methodName);
	}
	
	
	
	@UserControl
	@Audited
	@Override
	public Response getPosizioneDebitoria(String idVersamento, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		final String methodName = "getPosizioneDebitoria";
		MsgResponse response = new MsgResponse();
		Messaggio msg = new Messaggio();
		try {
			msg = posizioneDebitoriaManager.getPosizioneDebitoria(Long.parseLong(idVersamento));
		}catch (BusinessException err) {
			response.setEsitoPositivo(false);
			response.setApiMessages(err.getMessages());
			return buildManagedResponseLogEnd(httpHeaders, response, methodName);
		}
		response.setMsg(msg);
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);

	}

}
