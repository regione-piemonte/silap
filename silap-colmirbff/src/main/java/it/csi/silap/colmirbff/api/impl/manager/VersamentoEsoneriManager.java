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
package it.csi.silap.colmirbff.api.impl.manager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.ibm.icu.util.Calendar;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.Azienda;
import it.csi.silap.colmirbff.api.dto.DatiServiziProdisSilp;
import it.csi.silap.colmirbff.api.dto.GecoComunicazioneObbligatoria;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.Provincia;
import it.csi.silap.colmirbff.api.dto.RiconoscimentiInabilita;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.TipoMessaggio;
import it.csi.silap.colmirbff.api.dto.Utente;
import it.csi.silap.colmirbff.api.dto.Versamento;
import it.csi.silap.colmirbff.api.dto.VersamentoProspetto;
import it.csi.silap.colmirbff.api.dto.VersamentoProvincia;
import it.csi.silap.colmirbff.api.dto.VersamentoPvConvenzione;
import it.csi.silap.colmirbff.api.dto.VersamentoPvEsonero;
import it.csi.silap.colmirbff.api.dto.VersamentoPvGgExtraFestivi;
import it.csi.silap.colmirbff.api.dto.VersamentoPvPeriodo;
import it.csi.silap.colmirbff.api.dto.VersamentoPvPeriodoMaster;
import it.csi.silap.colmirbff.api.dto.VersamentoPvProspetto;
import it.csi.silap.colmirbff.api.dto.VersamentoPvRicIn;
import it.csi.silap.colmirbff.api.dto.VersamentoPvSospensione;
import it.csi.silap.colmirbff.api.dto.VersamentoStato;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.api.impl.manager.IupsManager.Protocollo;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoDVersamentoMotivoSospensione;
import it.csi.silap.colmirbff.integration.entity.EsoDVersamentoStato;
import it.csi.silap.colmirbff.integration.entity.EsoDVersamentoTipoConvenzione;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchExec;
import it.csi.silap.colmirbff.integration.entity.EsoTCreditoResiduo;
import it.csi.silap.colmirbff.integration.entity.EsoTParametro;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamento;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoCobLav;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProspetto;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProvincia;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvCob;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvConvenzione;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvEsonero;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvGgExtraFestivi;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvPeriodo;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvProspetto;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvRicIn;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvSospensione;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoSede;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoStato;
import it.csi.silap.colmirbff.integration.entity.SilapDCategoriaAzienda;
import it.csi.silap.colmirbff.integration.entity.SilapDCcnl;
import it.csi.silap.colmirbff.integration.entity.SilapDComune;
import it.csi.silap.colmirbff.integration.entity.SilapDProvincia;
import it.csi.silap.colmirbff.util.CommonUtils;
import it.csi.silap.colmirbff.util.Format;
import it.csi.silap.colmirbff.util.GiorniLavorativiUtils;
import it.csi.silap.colmirbff.util.MailUtils;
import it.csi.silap.colmirbff.util.MathUtils;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;
import it.csi.silap.colmirbff.util.report.ReportUtilities;

@Dependent
public class VersamentoEsoneriManager extends BaseApiServiceImpl {

	@Inject
	private ProdisManager prodisManager;

	@Inject
	private ComonlManager comonlManager;

	@Inject
	private VersamentoEsoneriCalcoloManager versamentoEsoneriCalcoloManager;

	@Inject
	private SilprestManager silpRestManager;
	
	@Inject
	private IupsManager iupsManager;
	
	@Inject
	private MailUtils mailUtils;
	
	@Inject
	private VersamentoEsoneriStampaManager versamentoEsoneriStampaManager;
	
	@ConfigProperty(name = "file.system.home")
	private String fileSystemHome;

	
	private static final SimpleDateFormat paramDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	
	public EsoTVersamentoStato updateStatoVersamento(Long idEsoTVersamento, Long idDVersamentoStato) {

		String codUtente = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		Date now = new Date();

		EsoTVersamentoStato.update(
				"flgCurrentRecord=?1, nTimestamp = (nTimestamp + 1) where esoTVersamento.idEsoTVersamento=?2", null,
				idEsoTVersamento);

		EsoTVersamentoStato stato = new EsoTVersamentoStato();
		EsoTVersamento esoTVersamento = new EsoTVersamento();
		esoTVersamento.setIdEsoTVersamento(idEsoTVersamento);
		stato.setEsoTVersamento(esoTVersamento);

		EsoDVersamentoStato dStato = new EsoDVersamentoStato();
		dStato.setId(idDVersamentoStato);
		stato.setEsoDVersamentoStato(dStato);

		stato.setCodUserInserim(codUtente);
		stato.setCodUserAggiorn(codUtente);
		stato.setDInserim(now);
		stato.setDAggiorn(now);
		stato.setNTimestamp(0L);
		stato.setFlgCurrentRecord("S");
		stato.setDtStato(now);

		stato.persist();

		return stato;
	}
	
	@Transactional
	public EsoTVersamentoStato updateStatoVersamentoBatch(Long idEsoTVersamento, Long idDVersamentoStato, Utente utente) {

		String codUtente = utente.getCodFisc();
		Date now = new Date();

		EsoTVersamentoStato.update(
				"flgCurrentRecord=?1, nTimestamp = (nTimestamp + 1) where esoTVersamento.idEsoTVersamento=?2", null,
				idEsoTVersamento);

		EsoTVersamentoStato stato = new EsoTVersamentoStato();
		EsoTVersamento esoTVersamento = new EsoTVersamento();
		esoTVersamento.setIdEsoTVersamento(idEsoTVersamento);
		stato.setEsoTVersamento(esoTVersamento);

		EsoDVersamentoStato dStato = new EsoDVersamentoStato();
		dStato.setId(idDVersamentoStato);
		stato.setEsoDVersamentoStato(dStato);

		stato.setCodUserInserim(codUtente);
		stato.setCodUserAggiorn(codUtente);
		stato.setDInserim(now);
		stato.setDAggiorn(now);
		stato.setNTimestamp(0L);
		stato.setFlgCurrentRecord("S");
		stato.setDtStato(now);

		stato.persist();

		return stato;
	}
	
	public void sendEmail(Versamento versamento, Long idDStato, String note) {
		if (versamento != null) {

			BigDecimal totImporto = new BigDecimal(0);
			List<VersamentoProvincia> versamentoProvincias = versamento.getEsoTVersamentoProvincias();
			List<EsoTVersamentoPvPeriodo> esoTversamentoPvPeriodos = new ArrayList<EsoTVersamentoPvPeriodo>();
			if (versamentoProvincias != null && versamentoProvincias.size() > 0) {
				for (VersamentoProvincia versamentoProvincia : versamentoProvincias) {
					esoTversamentoPvPeriodos = EsoTVersamentoPvPeriodo.list(
							"esoTVersamentoProvincia.idEsoTVersamentoProvincia=?1 and flgTipo!=?2",
							versamentoProvincia.getIdEsoTVersamentoProvincia(), "C");
					List<VersamentoPvPeriodo> versamentoPvPeriodos = mappers.VERSAMENTO_PV_PERIODO
							.toModels(esoTversamentoPvPeriodos);
					if (versamentoPvPeriodos != null && versamentoPvPeriodos.size() > 0) {
						for (VersamentoPvPeriodo versamentoPvPeriodo : versamentoPvPeriodos) {
							if (!"C".equals(versamentoPvPeriodo.getFlgTipo()))
								totImporto = totImporto.add(versamentoPvPeriodo.getImporto());
						}
					}
				}
			}
			DecimalFormat format = new DecimalFormat("#,##0.00");
			totImporto = totImporto.subtract(versamento.getNumCreditoResiduo());
			String totaleImporto = format.format(totImporto);
			Integer numRate = versamento.getNumRate().intValue();
			String rateVersamento = "";
			if (numRate == 1) {
				rateVersamento = "unica soluzione entro il 15 ottobre";
			} else {
				rateVersamento = "due rate entro rispettivamente il 31 luglio e il 15 ottobre";
			}

			Collection<String> to = new ArrayList<String>();
			to.add(versamento.getDsEmailRiferimento());

			if (SilapConstants.STATO_AUTORIZZATA.equals(idDStato)) {
				try {

					String subjectAutorizza = comonlManager.getParametroValido("MAUTO");
					if (subjectAutorizza != null) {
						subjectAutorizza = subjectAutorizza.replaceAll("'", "''");
						Object[] values = new Object[] {
							versamento.getAnnoRiferimento() != null ? versamento.getAnnoRiferimento().toString().trim(): "-",
							versamento.getDsDenominazioneAzienda() != null ? versamento.getDsDenominazioneAzienda().trim() : "-",
							versamento.getCodFiscale() != null ? versamento.getCodFiscale().trim() : "-"
						};
						subjectAutorizza = MessageFormat.format(subjectAutorizza,
								CommonUtils.formatTextMessage(values));
					} else
						subjectAutorizza = "Testo non presente base dati (cod:MAUTO)";

					String textAutorizza = comonlManager.getParametroValido("MAUTT");
					if (textAutorizza != null) {
						textAutorizza = textAutorizza.replaceAll("'", "''");
						
						Object[] values = new Object[] {
							versamento.getAnnoRiferimento() != null ? versamento.getAnnoRiferimento().toString().trim(): "-",
							versamento.getDsDenominazioneAzienda() != null ? versamento.getDsDenominazioneAzienda().trim() : "-",
							versamento.getCodFiscale() != null ? versamento.getCodFiscale().trim() : "-",
							totaleImporto,
							rateVersamento
						};
						textAutorizza = MessageFormat.format(textAutorizza, CommonUtils.formatTextMessage(values));
					} else
						textAutorizza = "Testo non presente base dati (cod:MAUTT)";

					mailUtils.sendHtml(to, subjectAutorizza, textAutorizza, null);

				} catch (BusinessException e) {
					throw BusinessException.createWarning(getDescrMsg(SilapConstants.MSG_ERRORE_INVIO_MAIL,
							versamento.getDsEmailRiferimento(), "Autorizzata", versamento.getAnnoRiferimento(),
							versamento.getDsDenominazioneAzienda(), versamento.getCodFiscale()));
				}
			}

			if (SilapConstants.STATO_NON_AUTORIZZATA.equals(idDStato)) {
				try {

					
					
					String subjectNonAutorizza = comonlManager.getParametroValido("MMAUO");
					if (subjectNonAutorizza != null) {
						subjectNonAutorizza = subjectNonAutorizza.replaceAll("'", "''");
						Object[] values = new Object[] {
								versamento.getAnnoRiferimento() != null ? versamento.getAnnoRiferimento().toString().trim(): "-",
								versamento.getDsDenominazioneAzienda() != null ? versamento.getDsDenominazioneAzienda().trim() : "-",
								versamento.getCodFiscale() != null ? versamento.getCodFiscale().trim() : "-"
							};
						subjectNonAutorizza = MessageFormat.format(subjectNonAutorizza,CommonUtils.formatTextMessage(values));
					} else
						subjectNonAutorizza = "Testo non presente base dati (cod:MMAUO)";

					String textNonAutorizza = comonlManager.getParametroValido("MMAUT");
					if (textNonAutorizza != null) {
						textNonAutorizza = textNonAutorizza.replaceAll("'", "''");
						Object[] values = new Object[] {
								versamento.getAnnoRiferimento() != null ? versamento.getAnnoRiferimento().toString().trim(): "-",
								versamento.getDsDenominazioneAzienda() != null ? versamento.getDsDenominazioneAzienda().trim() : "-",
								versamento.getCodFiscale() != null ? versamento.getCodFiscale().trim() : "-",
								note
							};
						textNonAutorizza = MessageFormat.format(textNonAutorizza,CommonUtils.formatTextMessage(values));
					} else
						textNonAutorizza = "Testo non presente base dati (cod:MMAUT)";

					mailUtils.sendHtml(to, subjectNonAutorizza, textNonAutorizza, null);
				} catch (BusinessException e) {
					throw BusinessException.createWarning(getDescrMsg(SilapConstants.MSG_ERRORE_INVIO_MAIL,
							versamento.getDsEmailRiferimento(), "Non autorizzata", versamento.getAnnoRiferimento(),
							versamento.getDsDenominazioneAzienda(), versamento.getCodFiscale()));
				}
			}

		}

	}

	public boolean annoRiferimentoCompatibile(Long annoRiferimento) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		final int annoCorrente = calendar.get(Calendar.YEAR);
		final int annoPrecedente = calendar.get(Calendar.YEAR) - 1;
		final int meseCorrente = calendar.get(Calendar.MONTH) + 1;
		if (annoRiferimento.intValue() == annoPrecedente)
			return true;
		if (annoRiferimento.intValue() == annoCorrente && (meseCorrente != SilapConstants.MESE_GENNAIO && meseCorrente != SilapConstants.MESE_FEBBRAIO))
			return true;
		return false;

	}
	//Verifica che sia presente un esonero su silp per l'anno di riferimento del versamento CDU 03 passo 2a.1
	public boolean esoneriSilpEsiste(Versamento versamento) {
		DatiServiziProdisSilp datiSilp = silpRestManager.findEsoneri(null, versamento.getCodFiscale(), null,
				versamento.getAnnoRiferimento().toString(), null);
		boolean esisteEsoneroSilp = false;

		List<VersamentoProvincia> versamentoProvinciasSilp = new ArrayList<VersamentoProvincia>();
		versamentoProvinciasSilp = datiSilp.getProvinceEsoneri();
		if (versamentoProvinciasSilp != null && !versamentoProvinciasSilp.isEmpty()) {
			for (VersamentoProvincia versamentoProvinciaSilp : versamentoProvinciasSilp) {	
				List<VersamentoPvEsonero> esonerosSilp = versamentoProvinciaSilp
				.getEsoTVersamentoPvEsoneros();
				if (esonerosSilp != null & !esonerosSilp.isEmpty()) {
					for (VersamentoPvEsonero esoneroSilp : esonerosSilp) {
						if (esoneroSilp!=null)
							esisteEsoneroSilp = true;
						if (esisteEsoneroSilp)
							return true;
							
					}
				}
			}
		}
		return esisteEsoneroSilp;

	}
	//Verifica che sia presente un prospetto su prodis per l'anno di riferimento del versamento CDU 03 passo 2b.1
	public boolean prospettiProdisEsiste(Versamento versamento) {

		Azienda azienda = new Azienda();
		azienda.setCodFiscale(versamento.getCodFiscale());
		azienda.setDenomAzienda(versamento.getDsDenominazioneAzienda());
		String annoRiferimento = "" + versamento.getAnnoRiferimento();

		DatiServiziProdisSilp datiProdis = prodisManager.getDatiProdis(azienda, null, annoRiferimento);

		List<VersamentoProspetto> versamentoProspettosSilp = new ArrayList<VersamentoProspetto>();
		versamentoProspettosSilp = datiProdis.getProspetti();
		
		
		if(versamentoProspettosSilp!=null && !versamentoProspettosSilp.isEmpty() ) {
			return true;
		}
		return false;
	}

	@Transactional
	public Versamento getVersamento(Long idEsoTVersamento) {
		Versamento versamento = new Versamento();
		EsoTVersamento esoTVersamento = EsoTVersamento.findById(idEsoTVersamento);
		List<EsoTCreditoResiduo> esoTCreditoResiduo = EsoTCreditoResiduo.list("codFiscale",
				esoTVersamento.getCodFiscale());
		if (esoTCreditoResiduo != null && esoTCreditoResiduo.size() > 0) {
			if (esoTVersamento.getEsoTCreditoResiduo() != null) {
				updateCreditoResiduo(esoTVersamento, esoTCreditoResiduo.get(0));
				versamento = mappers.VERSAMENTO.toModel(esoTVersamento);
				versamento.setNumCreditoResiduo(esoTCreditoResiduo.get(0).getNumValore());
				return versamento;
			}
		}
		versamento = mappers.VERSAMENTO.toModel(esoTVersamento);

		return versamento;
	}


	private void updateCreditoResiduo(EsoTVersamento esoTVersamento, EsoTCreditoResiduo esoTCreditoResiduo) {
		EsoTVersamento.update(
				"numCreditoResiduo=?1 where codFiscale=?2 and esoTCreditoResiduo.idEsoTCreditoResiduo=?3",
				esoTCreditoResiduo.getNumValore(), esoTVersamento.getCodFiscale(),
				esoTCreditoResiduo.getIdEsoTCreditoResiduo());
	}
	
	public EsoTVersamento impostaPeriodiProivincia(EsoTVersamento versamento, Long idEsoTVersamentoProvincia, StringBuffer printCalcoloAutomatico,boolean ripristina) {
		return impostaPeriodiProivincia(versamento, idEsoTVersamentoProvincia, printCalcoloAutomatico, true, ripristina);
	}
	

	@Transactional
	public EsoTVersamento impostaPeriodiProivincia(EsoTVersamento versamento, Long idEsoTVersamentoProvincia, StringBuffer printCalcoloAutomatico, boolean persist,boolean ripristina) {		
		
		double importoGiornalieroEsonero = 0d;
		try {
			List<EsoTParametro> listParam = EsoTParametro.list("codParametro", "IMPGG");
			if (listParam != null)
				importoGiornalieroEsonero = Double.parseDouble(listParam.get(0).getDsValore().trim());
		} catch (Exception err) {
			err.printStackTrace();
		}

		EsoTVersamentoProvincia provincia = null;
		for (EsoTVersamentoProvincia prov : versamento.getEsoTVersamentoProvincias()) {
			if (prov.getIdEsoTVersamentoProvincia().longValue() == idEsoTVersamentoProvincia) {
				provincia = prov;
				break;
			}
		}
		
		printCalcoloAutomatico.append("\n======================================================\n");
		String dsProvincia = "<>";
		if (provincia.getSilapDProvincia() != null) {
			dsProvincia = provincia.getSilapDProvincia().getDsSilapDProvincia() != null ? " - " + provincia.getSilapDProvincia().getDsSilapDProvincia() : "";
		}
		printCalcoloAutomatico.append("PROVINCIA: "+ provincia.getSilapDProvincia().getId() + dsProvincia +"\n\n");

		Calendar cal = Calendar.getInstance();
		int annoRiferimento = versamento.getAnnoRiferimento().intValue();

		// PROSPETTO ANNO RIFERIMENTO E PROVINCIALE
		EsoTVersamentoProspetto prospetto = null;
		EsoTVersamentoPvProspetto prospettoProvincia = null;
		EsoTVersamentoProspetto prospettoPrec = null;
		EsoTVersamentoPvProspetto prospettoProvinciaPrec = null;
		for (EsoTVersamentoProspetto prosp : versamento.getEsoTVersamentoProspettos()) {
			if (prosp.getAnnoRiferimento().longValue() == versamento.getAnnoRiferimento().longValue() -1)
				prospetto = prosp;
			else if (prosp.getAnnoRiferimento().longValue() != versamento.getAnnoRiferimento().longValue())
				prospettoPrec = prosp;
		}
		
		if (prospetto == null && prospettoPrec != null) {
			prospetto = prospettoPrec;
			prospettoPrec = null;
		}
		
		if (prospetto == null && versamento.getEsoTVersamentoProspettos().size()>0) 
			prospetto = versamento.getEsoTVersamentoProspettos().get(0);
		
		
		for (EsoTVersamentoPvProspetto prospPv : prospetto.getEsoTVersamentoPvProspettos()) {
			if (prospPv.getSilapDProvincia().getId().equalsIgnoreCase(provincia.getSilapDProvincia().getId())) {
				prospettoProvincia = prospPv;
				break;
			}
		}
		if (prospettoPrec != null) {
			for (EsoTVersamentoPvProspetto prospPv : prospettoPrec.getEsoTVersamentoPvProspettos()) {
				if (prospPv.getSilapDProvincia().getId().equalsIgnoreCase(provincia.getSilapDProvincia().getId())) {
					prospettoProvinciaPrec = prospPv;
					break;
				}
			}
		}
		

		// ESONERI
		VersamentoProvincia versProv = new VersamentoProvincia();
		versProv.setEsoTVersamentoPvPeriodos(new ArrayList<VersamentoPvPeriodo>());
		versProv.setEsoTVersamentoPvEsoneros(
				mappers.VERSAMENTO_PV_ESONERO.toModels(provincia.getEsoTVersamentoPvEsoneros()));
		versProv = prodisManager.calcoloPeriodioPerProvincia(versProv);
		List<EsoTVersamentoPvPeriodo> periodiEsoneri = mappers.VERSAMENTO_PV_PERIODO
				.toEntities(versProv.getEsoTVersamentoPvPeriodos());

		// verifica primo periodo inizio anno riferimento
		if (periodiEsoneri != null && periodiEsoneri.size() > 0) {
			cal.setTime(periodiEsoneri.get(0).getDInizio());
			if (cal.get(Calendar.YEAR) < annoRiferimento) {
				cal.set(Calendar.YEAR, annoRiferimento);
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				periodiEsoneri.get(0).setDInizio(cal.getTime());
			}
		}
		

		// SOSPENSIONI
		List<EsoTVersamentoPvPeriodo> periodiSospensioni = new ArrayList<EsoTVersamentoPvPeriodo>();
		List<EsoTVersamentoPvSospensione> sospensioni = provincia.getEsoTVersamentoPvSospensiones();
		if (sospensioni != null) {
			for (EsoTVersamentoPvSospensione sospensione : sospensioni) {
				EsoTVersamentoPvPeriodo per = new EsoTVersamentoPvPeriodo();
				per.setDInizio(sospensione.getDInizioSospensione());
				per.setDFine(sospensione.getDFineSospensione());
				per.setQuotaRiserva(sospensione.getPercSospensione());
				per.setEsoDVersamentoMotivoSospensione(sospensione.getEsoDVersamentoMotivoSospensione());
				per.setNumLavoratoriSospesi(sospensione.getNumLavoratoriSospesi());
				periodiSospensioni.add(per);
			}
		}
		
		
		// CONVENZIONI
		List<EsoTVersamentoPvPeriodo> periodiConvenzioni = new ArrayList<EsoTVersamentoPvPeriodo>();
		List<EsoTVersamentoPvConvenzione> convenzioni = provincia.getEsoTVersamentoPvConvenziones();
		if (convenzioni != null) {
			for (EsoTVersamentoPvConvenzione convenzione : convenzioni) {
				if (convenzione.getDScadenza() != null) {
					EsoTVersamentoPvPeriodo per = new EsoTVersamentoPvPeriodo();
					per.setDInizio(convenzione.getDStipula());
					per.setDFine(convenzione.getDScadenza());
					per.setQuotaRiserva(convenzione.getNumPosizioniAperte());
					periodiConvenzioni.add(per);
				}				
			}
		}
		
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

		// COBS PROVINCIA
		String partitaIvaCodFiscale = versamento.getPartivaIva();
		if (partitaIvaCodFiscale == null) {
			partitaIvaCodFiscale = versamento.getCodFiscale();
		}
		List<GecoComunicazioneObbligatoria> listCob = comonlManager.getElencoComunicazioni(partitaIvaCodFiscale,
				annoRiferimento + "", provincia.getSilapDProvincia().getId());
		
		if (listCob != null && listCob.size()>0) {
			printCalcoloAutomatico.append("COBS DA GECO\n");
			for (GecoComunicazioneObbligatoria cob : listCob) {
				
				printCalcoloAutomatico.append(cob.getCodComunicazione() 
						+ "; Data com.:" + df.format(cob.getDataComunicazione())
						+ "; Tipo com.:" + cob.getCodTipoComunicazione()
						+ "; prov:" + cob.getSiglaProvinciaSedeLavoro() + "; prov prec.:" + cob.getSiglaProvinciaSedeLavoroPrec()  + "\n");
			}
			printCalcoloAutomatico.append("===================================\n");
		}
		
		
		listCob = comonlManager.escludeComunicazioniNonInerentiCalcolo(listCob, annoRiferimento);
		
		if (listCob != null && listCob.size()>0) {
			printCalcoloAutomatico.append("COBS PRIMA ESCLUSIONE\n");
			for (GecoComunicazioneObbligatoria cob : listCob) {
				printCalcoloAutomatico.append(cob.getCodComunicazione() 
						+ "; Data com.:" + df.format(cob.getDataComunicazione())
						+ "; Tipo com.:" + cob.getCodTipoComunicazione()
						+ "; prov:" + cob.getSiglaProvinciaSedeLavoro() + "; prov prec.:" + cob.getSiglaProvinciaSedeLavoroPrec() + "\n");
			}
			printCalcoloAutomatico.append("===================================\n");
		}
		
		List<EsoTVersamentoPvCob> esoTVersamentoPvCobs = valutaInserisciCobPerProvincia(
				provincia.getIdEsoTVersamentoProvincia(), provincia.getSilapDProvincia().getId(), listCob, annoRiferimento, printCalcoloAutomatico, persist);
		
		
		// RICONOSCIMENTI INABILITA
		List<EsoTVersamentoPvRicIn> esoTVersamentoPvRicIns = provincia.getEsoTVersamentoPvRicIns();
		

		// calcolo automatico
		List<EsoTVersamentoPvPeriodo> periodiCalcolati = versamentoEsoneriCalcoloManager.calcoloAutomatico(
				importoGiornalieroEsonero, annoRiferimento, provincia, prospetto, prospettoProvincia, prospettoPrec,
				prospettoProvinciaPrec, periodiEsoneri, periodiSospensioni, periodiConvenzioni, 
				esoTVersamentoPvCobs, esoTVersamentoPvRicIns, printCalcoloAutomatico);
		
		if (persist)
			salvaPeriodiAutomaticiProvincia(idEsoTVersamentoProvincia,periodiCalcolati,ripristina);
		else {
		
			provincia.setEsoTVersamentoPvPeriodos(periodiCalcolati);
			List<EsoTVersamentoProvincia> provAdd = new ArrayList<>();
			for (EsoTVersamentoProvincia pro : versamento.getEsoTVersamentoProvincias()) {
				if (pro.getIdEsoTVersamentoProvincia().longValue() == idEsoTVersamentoProvincia) {
					provAdd.add(provincia);
				}
				else provAdd.add(pro);
			}
			versamento.setEsoTVersamentoProvincias(provAdd);
		}
		return versamento;
	}
	
	
	@Transactional
	public Long modificaGGLavorativiProvincia(VersamentoProvincia versamentoProvincia) {
		
		EsoTVersamentoProvincia esoTVersamentoProvincia = EsoTVersamentoProvincia
				.findById(versamentoProvincia.getIdEsoTVersamentoProvincia());
		
		
		String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		String userAggiorn = userInserim;
		Date now = new Date();
		Long timestamp = 0L;
		
		
		esoTVersamentoProvincia.setEsoTVersamentoPvConvenziones(null);
		esoTVersamentoProvincia.setEsoTVersamentoPvCobs(null);
		esoTVersamentoProvincia.setEsoTVersamentoPvEsoneros(null);
		esoTVersamentoProvincia.setEsoTVersamentoPvPeriodos(null);
		esoTVersamentoProvincia.setEsoTVersamentoPvSospensiones(null);
		esoTVersamentoProvincia.setEsoTVersamentoPvGgExtraFestivis(null);

		
		EsoTVersamento esoTVersamento = new EsoTVersamento();
		esoTVersamento.setIdEsoTVersamento(esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento());
		esoTVersamentoProvincia.setEsoTVersamento(esoTVersamento);
		
		if (esoTVersamentoProvincia.getSilapDProvincia() != null) {
			SilapDProvincia silapDProvincia = new SilapDProvincia();
			silapDProvincia.setId(esoTVersamentoProvincia.getSilapDProvincia().getId());
			esoTVersamentoProvincia.setSilapDProvincia(silapDProvincia);
		}		
		
		esoTVersamentoProvincia.setNumGgLavorativiSettimanali(versamentoProvincia.getNumGgLavorativiSettimanali());
		esoTVersamentoProvincia.setDFestaPatronale(versamentoProvincia.getDFestaPatronale());
		esoTVersamentoProvincia.setCodUserAggiorn(userAggiorn);
		esoTVersamentoProvincia.setDAggiorn(now);
		esoTVersamentoProvincia.setNTimestamp(esoTVersamentoProvincia.getNTimestamp()+1);
		esoTVersamentoProvincia.persist();
		
		EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
		esoTVersamentoProvinciaPersist.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());

		EsoTVersamentoPvGgExtraFestivi.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia", 
				esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());

		if (versamentoProvincia.getEsoTVersamentoPvGgExtraFestivis() != null) {
			for (VersamentoPvGgExtraFestivi ex : versamentoProvincia.getEsoTVersamentoPvGgExtraFestivis()) {
				EsoTVersamentoPvGgExtraFestivi esoEx = mappers.VERSAMENTO_PV_GG_EXTRA_FESTIVI.toEntity(ex);
				esoEx.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);
				esoEx.setIdEsoTVersamentoPvGgExtraFestivi(null);

				esoEx.setCodUserAggiorn(userAggiorn);
				esoEx.setCodUserInserim(userInserim);
				esoEx.setDInserim(now);
				esoEx.setDAggiorn(now);
				esoEx.setNTimestamp(timestamp);
				esoEx.persist();
			}
		}
		return esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento();
	}
	

	@Transactional
	public Long salvaGGLavorativiProvincia(VersamentoProvincia versamentoProvincia) {
		EsoTVersamentoProvincia esoTVersamentoProvincia = EsoTVersamentoProvincia
				.findById(versamentoProvincia.getIdEsoTVersamentoProvincia());
		esoTVersamentoProvincia.setNumGgLavorativiSettimanali(versamentoProvincia.getNumGgLavorativiSettimanali());
		esoTVersamentoProvincia.setDFestaPatronale(versamentoProvincia.getDFestaPatronale());
		esoTVersamentoProvincia.persist();

		EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
		esoTVersamentoProvinciaPersist
				.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());
		String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		String userAggiorn = userInserim;
		Date now = new Date();
		Long timestamp = 0L;

		if (versamentoProvincia.getEsoTVersamentoPvGgExtraFestivis() != null) {
			for (VersamentoPvGgExtraFestivi ex : versamentoProvincia.getEsoTVersamentoPvGgExtraFestivis()) {
				EsoTVersamentoPvGgExtraFestivi esoEx = mappers.VERSAMENTO_PV_GG_EXTRA_FESTIVI.toEntity(ex);
				esoEx.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);

				esoEx.setCodUserAggiorn(userAggiorn);
				esoEx.setCodUserInserim(userInserim);
				esoEx.setDInserim(now);
				esoEx.setDAggiorn(now);
				esoEx.setNTimestamp(timestamp);
				esoEx.persist();
			}
		}
		return esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento();
	}
	
	
	@Transactional
	public Long modificaSospensioniProvincia(VersamentoProvincia versamentoProvincia) {
		EsoTVersamentoProvincia esoTVersamentoProvincia = EsoTVersamentoProvincia
				.findById(versamentoProvincia.getIdEsoTVersamentoProvincia());

		EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
		esoTVersamentoProvinciaPersist.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());
		String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		String userAggiorn = userInserim;
		Date now = new Date();
		Long timestamp = 0L;
		
		List<EsoTVersamentoProvincia> esoTVersamentoProvincias = EsoTVersamentoProvincia.list("esoTVersamento.idEsoTVersamento =?1", esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento());
		
		List<EsoTVersamentoPvSospensione> esoTVersamentoPvSospensioneClient = mappers.VERSAMENTO_PV_SOSPENZIONE.toEntities(versamentoProvincia.getEsoTVersamentoPvSospensiones());
		List<EsoTVersamentoPvSospensione> esoTVersamentoPvSospensioneDb = esoTVersamentoProvincia.getEsoTVersamentoPvSospensiones();
		
		if(esoTVersamentoPvSospensioneDb != null && esoTVersamentoPvSospensioneDb.size() > 0) {
			boolean thereIsLicenziamentoCollettivoDb = thereIsLincenziamentoCollettivo(esoTVersamentoPvSospensioneDb);
			boolean thereIsLicenziamentoCollettivoClient = thereIsLincenziamentoCollettivo(esoTVersamentoPvSospensioneClient);
			
			if(thereIsLicenziamentoCollettivoDb && !thereIsLicenziamentoCollettivoClient) {
				for(EsoTVersamentoProvincia esoTVersamentoProvinciaTmp: esoTVersamentoProvincias) {
					EsoTVersamentoPvSospensione.delete("esoDVersamentoMotivoSospensione.id =?1 AND esoTVersamentoProvincia.idEsoTVersamentoProvincia =?2", 
							SilapConstants.MOTIVO_SOSPENSIONE_LICENZIAMENTO_COLLETTIVO,
							esoTVersamentoProvinciaTmp.getIdEsoTVersamentoProvincia());
					EsoTVersamentoPvPeriodo.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia = ?1", esoTVersamentoProvinciaTmp.getIdEsoTVersamentoProvincia());
					
				}
				
			}
		}
		
		
		EsoTVersamentoPvSospensione.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia", 
				esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());
		

		if (versamentoProvincia.getEsoTVersamentoPvSospensiones() != null && versamentoProvincia.getEsoTVersamentoPvSospensiones().size() > 0 ) {
			for (VersamentoPvSospensione sospensione : versamentoProvincia.getEsoTVersamentoPvSospensiones()) {
				EsoTVersamentoPvSospensione esoSospensione = mappers.VERSAMENTO_PV_SOSPENZIONE.toEntity(sospensione);
				esoSospensione.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);
				esoSospensione.setIdEsoTVersamentoPvSospensione(null);
				
				EsoDVersamentoMotivoSospensione motivo = new EsoDVersamentoMotivoSospensione();
				motivo.setId(esoSospensione.getEsoDVersamentoMotivoSospensione().getId());
				esoSospensione.setEsoDVersamentoMotivoSospensione(motivo);

				esoSospensione.setCodUserAggiorn(userAggiorn);
				esoSospensione.setCodUserInserim(userInserim);
				esoSospensione.setDInserim(now);
				esoSospensione.setDAggiorn(now);
				esoSospensione.setNTimestamp(timestamp);
				
				esoSospensione.persist();
				
				
				if(SilapConstants.MOTIVO_SOSPENSIONE_LICENZIAMENTO_COLLETTIVO.equals(esoSospensione.getEsoDVersamentoMotivoSospensione().getId())) {
					
					for(EsoTVersamentoProvincia provincia: esoTVersamentoProvincias) {
						
						
						if(!provincia.getIdEsoTVersamentoProvincia().equals(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia() ) && !thereIsLincenziamentoCollettivo(provincia.getEsoTVersamentoPvSospensiones()))   {
							EsoTVersamentoPvPeriodo.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia = ?1", provincia.getIdEsoTVersamentoProvincia());
							
							EsoTVersamentoPvSospensione esoSospensioneLicenziamentoCollettivo = new EsoTVersamentoPvSospensione();
							esoSospensioneLicenziamentoCollettivo.setDInizioSospensione(esoSospensione.getDInizioSospensione());
							esoSospensioneLicenziamentoCollettivo.setDFineSospensione(esoSospensione.getDFineSospensione());
							
							esoSospensioneLicenziamentoCollettivo.setPercSospensione(esoSospensione.getPercSospensione());
							
							esoSospensioneLicenziamentoCollettivo.setNumLavoratoriSospesi(esoSospensione.getNumLavoratoriSospesi());							
							esoSospensioneLicenziamentoCollettivo.setEsoTVersamentoProvincia(provincia);
							esoSospensioneLicenziamentoCollettivo.setIdEsoTVersamentoPvSospensione(null);
							
							EsoDVersamentoMotivoSospensione motivox = new EsoDVersamentoMotivoSospensione();
							motivox.setId(SilapConstants.MOTIVO_SOSPENSIONE_LICENZIAMENTO_COLLETTIVO);
							esoSospensioneLicenziamentoCollettivo.setEsoDVersamentoMotivoSospensione(motivox);
							
							esoSospensioneLicenziamentoCollettivo.setEsoDVersamentoMotivoSospensione(motivox);

							esoSospensioneLicenziamentoCollettivo.setCodUserAggiorn(userAggiorn);
							esoSospensioneLicenziamentoCollettivo.setCodUserInserim(userInserim);
							esoSospensioneLicenziamentoCollettivo.setDInserim(now);
							esoSospensioneLicenziamentoCollettivo.setDAggiorn(now);
							esoSospensioneLicenziamentoCollettivo.setNTimestamp(timestamp);
							
							esoSospensioneLicenziamentoCollettivo.persist();
						}
					}
				}
			}
		}

		return esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento();
	}
	
	
	private boolean thereIsLincenziamentoCollettivo(List<EsoTVersamentoPvSospensione> esoTVersamentoPvSospensioni) {
		boolean hasLicenziamento = false;
		
		
		if(esoTVersamentoPvSospensioni != null && esoTVersamentoPvSospensioni.size() > 0) {
			for(int i = 0; i < esoTVersamentoPvSospensioni.size() && !hasLicenziamento; i++) {
				hasLicenziamento = SilapConstants.MOTIVO_SOSPENSIONE_LICENZIAMENTO_COLLETTIVO.equals(esoTVersamentoPvSospensioni.get(i).getEsoDVersamentoMotivoSospensione().getId());
			}
		}
		
		return hasLicenziamento;
	}

	@Transactional
	public Long salvaSospensioniProvincia(VersamentoProvincia versamentoProvincia) {
		EsoTVersamentoProvincia esoTVersamentoProvincia = EsoTVersamentoProvincia
				.findById(versamentoProvincia.getIdEsoTVersamentoProvincia());

		EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
		esoTVersamentoProvinciaPersist
				.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());
		String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		String userAggiorn = userInserim;
		Date now = new Date();
		Long timestamp = 0L;

		if (versamentoProvincia.getEsoTVersamentoPvSospensiones() != null) {
			for (VersamentoPvSospensione sospensione : versamentoProvincia.getEsoTVersamentoPvSospensiones()) {
				EsoTVersamentoPvSospensione esoSospensione = mappers.VERSAMENTO_PV_SOSPENZIONE.toEntity(sospensione);
				esoSospensione.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);

				EsoDVersamentoMotivoSospensione motivo = new EsoDVersamentoMotivoSospensione();
				motivo.setId(esoSospensione.getEsoDVersamentoMotivoSospensione().getId());
				esoSospensione.setEsoDVersamentoMotivoSospensione(motivo);

				esoSospensione.setCodUserAggiorn(userAggiorn);
				esoSospensione.setCodUserInserim(userInserim);
				esoSospensione.setDInserim(now);
				esoSospensione.setDAggiorn(now);
				esoSospensione.setNTimestamp(timestamp);
				esoSospensione.persist();
			}
		}
		return esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento();
	}
	
	
	@Transactional
	public Long modificaDatiAziendali(Versamento versamento) {
		Azienda azienda = new Azienda();
		azienda.setCodFiscale(versamento.getCodFiscale());
		azienda.setDenomAzienda(versamento.getDsDenominazioneAzienda());
		String annoRiferimento = "" + versamento.getAnnoRiferimento();
		
		DatiServiziProdisSilp datiServiziProdisSilp = prodisManager.getDatiProdis(azienda, null, annoRiferimento);
		if (datiServiziProdisSilp.getMessaggi() != null && datiServiziProdisSilp.getMessaggi().size() > 0) {
			for (ApiMessage message : datiServiziProdisSilp.getMessaggi()) {
				if (message.getError()) {
					throw new BusinessException("DatiDichiarazioniProvincieManager.getDati",
							datiServiziProdisSilp.getMessaggi());
				}
			}
		}
		
		
		List<RiconoscimentiInabilita> riconoscimentiInabilitaSilp = silpRestManager.findRiconoscimentiInabilita(
				versamento.getCodFiscale(), null, annoRiferimento);
		
		
		EsoTVersamento esoTVersamento = EsoTVersamento.findById(versamento.getIdEsoTVersamento());
		esoTVersamento.setDsEmailRiferimento(versamento.getDsEmailRiferimento());
		esoTVersamento.setNumRate(versamento.getNumRate());
		
		esoTVersamento = salvaDatiAziendaliInternal(esoTVersamento, datiServiziProdisSilp, true);
		return salvaProspettoEsoneriConvenzioniInternal(esoTVersamento, datiServiziProdisSilp, riconoscimentiInabilitaSilp, true);
	}

	
	
	public Long modificaDatiAziendaliEssenziali(Versamento versamento, EsoTVersamento esoTVersamento) {
		
		Azienda azienda = new Azienda();
		azienda.setCodFiscale(versamento.getCodFiscale());
		azienda.setDenomAzienda(versamento.getDsDenominazioneAzienda());
		String annoRiferimento = "" + versamento.getAnnoRiferimento();
		
		DatiServiziProdisSilp datiServiziProdisSilp = prodisManager.getDatiProdis(azienda, null, annoRiferimento);
		if (datiServiziProdisSilp.getMessaggi() != null && datiServiziProdisSilp.getMessaggi().size() > 0) {
			for (ApiMessage message : datiServiziProdisSilp.getMessaggi()) {
				if (message.getError()) {
					throw new BusinessException("DatiDichiarazioniProvincieManager.getDati",
							datiServiziProdisSilp.getMessaggi());
				}
			}
		}
		esoTVersamento.setDsEmailRiferimento(versamento.getDsEmailRiferimento());
		esoTVersamento.setNumRate(versamento.getNumRate());
		if (versamento.getEsoTCreditoResiduo()!=null) {
			EsoTCreditoResiduo creditoResiduo = mappers.CREDITO_RESIDUO.toEntity(versamento.getEsoTCreditoResiduo());
			esoTVersamento.setEsoTCreditoResiduo(creditoResiduo);
		}
		esoTVersamento = salvaDatiAziendaliInternal(esoTVersamento, datiServiziProdisSilp, true);
		return esoTVersamento.getIdEsoTVersamento();
	}


	@Transactional
	public Long salvaDatiAziendali(Versamento versamento) {

		Azienda azienda = new Azienda();
		azienda.setCodFiscale(versamento.getCodFiscale());
		azienda.setDenomAzienda(versamento.getDsDenominazioneAzienda());
		String annoRiferimento = "" + versamento.getAnnoRiferimento();

		EsoTVersamento esoTVersamento = mappers.VERSAMENTO.toEntity(versamento);

		List<EsoTVersamento> list = EsoTVersamento.list("annoRiferimento = ?1 and codFiscale = ?2",
				versamento.getAnnoRiferimento(), versamento.getCodFiscale());
		if (list != null && list.size() > 0) {
			for (EsoTVersamento ver : list) {
				if (ver.getEsoTVersamentoStatos() != null) {
					for (EsoTVersamentoStato stato : ver.getEsoTVersamentoStatos()) {
						if ("S".equalsIgnoreCase(stato.getFlgCurrentRecord())) {

							if (stato.getEsoDVersamentoStato() != null
									&& stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_BOZZA) {
								throw BusinessException.createError(
										getDescrMsg(SilapConstants.MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_BOZZA,
												ver.getDsDenominazioneAzienda(), ver.getCodFiscale(),
												ver.getAnnoRiferimento()));
							}

							if (stato.getEsoDVersamentoStato() != null
									&& stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_ACCETTATA
									|| stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_MODIFICATA) {
								throw BusinessException.createError(
										getDescrMsg(SilapConstants.MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_ACCETTATA,
												ver.getDsDenominazioneAzienda(), ver.getCodFiscale(),
												ver.getAnnoRiferimento(), stato.getEsoDVersamentoStato().getDescr()));
							}

							if (stato.getEsoDVersamentoStato() != null
									&& stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_AUTORIZZATA) {
								throw BusinessException.createError(
										getDescrMsg(SilapConstants.MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_AUTORIZZATA,
												ver.getDsDenominazioneAzienda(), ver.getCodFiscale(),
												ver.getAnnoRiferimento()));
							}
							
							if (stato.getEsoDVersamentoStato() != null
									&& (stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_AVVISO_INVIATO ||
											stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_ACCONTO ||
											stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_SALDO) ) {
								
								String statoMsg = SilapConstants.STATO_AVVISO_INVIATO_DESCR;
								if (stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_ACCONTO)
									statoMsg = SilapConstants.STATO_ACCONTO_DESCR;
								else if (stato.getEsoDVersamentoStato().getId() == SilapConstants.STATO_SALDO)
									statoMsg = SilapConstants.STATO_SALDO_DESCR;
								
								throw BusinessException.createError(
										getDescrMsg(SilapConstants.MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_PAGAMENTO,
												ver.getDsDenominazioneAzienda(), ver.getCodFiscale(),
												ver.getAnnoRiferimento(), statoMsg));								
							}
							
							break;
						}
					}
				}
			}
		}

		DatiServiziProdisSilp datiServiziProdisSilp = prodisManager.getDatiProdis(azienda, null, annoRiferimento);
		if (datiServiziProdisSilp.getMessaggi() != null && datiServiziProdisSilp.getMessaggi().size() > 0) {
			for (ApiMessage message : datiServiziProdisSilp.getMessaggi()) {
				if (message.getError()) {
					throw new BusinessException("DatiDichiarazioniProvincieManager.getDati",
							datiServiziProdisSilp.getMessaggi());
				}
			}
		}
		
		List<RiconoscimentiInabilita> riconoscimentiInabilitaSilp = silpRestManager.findRiconoscimentiInabilita(
				versamento.getCodFiscale(), null, annoRiferimento);
		
		esoTVersamento = salvaDatiAziendaliInternal(esoTVersamento, datiServiziProdisSilp, false);
		
		
		// versamento stato
		EsoTVersamento esoTVersamentoPersist = new EsoTVersamento();
		esoTVersamentoPersist.setIdEsoTVersamento(esoTVersamento.getIdEsoTVersamento());
		if (esoTVersamento.getEsoTVersamentoStatos() != null && esoTVersamento.getEsoTVersamentoStatos().size() > 0) {
			EsoTVersamentoStato vesramentoStato = esoTVersamento.getEsoTVersamentoStatos().get(0);
			vesramentoStato.setEsoTVersamento(esoTVersamentoPersist);

			if (vesramentoStato.getEsoDVersamentoStato() != null
					&& vesramentoStato.getEsoDVersamentoStato().getId() != null) {
				EsoDVersamentoStato stato = new EsoDVersamentoStato();
				stato.setId(vesramentoStato.getEsoDVersamentoStato().getId());
				vesramentoStato.setEsoDVersamentoStato(stato);
			}

			String user = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
			vesramentoStato.setCodUserAggiorn(user);
			vesramentoStato.setCodUserInserim(user);
			vesramentoStato.setDInserim(new Date());
			vesramentoStato.setDAggiorn(new Date());
			vesramentoStato.setNTimestamp(0L);
			vesramentoStato.persist();
		}
		
		
		
		return salvaProspettoEsoneriConvenzioniInternal(esoTVersamento, datiServiziProdisSilp, riconoscimentiInabilitaSilp, false);
	}
	
	
	private boolean aggiungiCob(List<GecoComunicazioneObbligatoria> addedCobs, GecoComunicazioneObbligatoria cob) {
		
		for (GecoComunicazioneObbligatoria addedCob : addedCobs) {
			
			if (addedCob.equals(cob))
				return false;
		}
		return true;
	}
	
	
	private Long addLavoratori(GecoComunicazioneObbligatoria cob, Long numLavoratori, boolean somma) {
		
		if ("S".equalsIgnoreCase(cob.getFlgFullTime())) {
			if (somma)
				return numLavoratori+1;
			else return numLavoratori-1;
		}
		else if (cob.getOreSettimanaliMedie() != null) {
			if (somma)
				return numLavoratori + MathUtils.round(cob.getOreSettimanaliMedie()/40d);
			else return numLavoratori - MathUtils.round(cob.getOreSettimanaliMedie()/40d);
		}
		else {
			if (somma)
				return numLavoratori+1;
			else return numLavoratori-1;
		}
	}
	
	private List<EsoTVersamentoPvCob> valutaInserisciCobPerProvincia(Long idEsoTVersamentoProvincia,
			String codProvincia, List<GecoComunicazioneObbligatoria> listInput, int annoRiferimanto, StringBuffer printCalcoloAutomatic, boolean persist) {
		
		List<GecoComunicazioneObbligatoria> list = new ArrayList<GecoComunicazioneObbligatoria>();
		Calendar calInput = Calendar.getInstance();
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		// creazioni cessazioni automatiche
		for (GecoComunicazioneObbligatoria cob : listInput) {
			
			
			
			if (cob.getDataFineRapporto() == null) {
				list.add(cob);
				continue;
			}
			
			
			
			calInput.setTime(cob.getDataFineRapporto());
				
				
			if ("CES".equalsIgnoreCase(cob.getCodTipoComunicazione())) {
				list.add(cob);
			}
			else {
				list.add(cob);
			
				boolean hasSucc = false;
				for (GecoComunicazioneObbligatoria succ : listInput) {
					if (succ.getCodiceFiscaleLavoratore().equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
							&& Format.convertiDataInStringa(succ.getDataInizioRapporto()).equals(Format.convertiDataInStringa(cob.getDataInizioRapporto()))
							&& succ.getDataComunicazione().after(cob.getDataComunicazione())) {
						hasSucc = true;
						break;
					}
				}
				
				if (!hasSucc) {
					GecoComunicazioneObbligatoria succ = GecoComunicazioneObbligatoria.copy(cob);
					succ.setCodTipoComunicazione("CES");
					succ.setDsTipoComunicazione("CESSAZIONE AUTOMATICA");
					succ.setCodComunicazione(cob.getCodComunicazione());
					succ.setDataComunicazione(cob.getDataFineRapporto());
					succ.setDataCessazione(cob.getDataFineRapporto());

					if (aggiungiCob(list, succ)) {
						list.add(succ);
						printCalcoloAutomatic.append("CESSAZIONE AUTOMATICA INSERITA cod:" + cob.getCodComunicazione() + " data fine rapporto:" + df.format(cob.getDataFineRapporto()) + "\n");
					}
					
				}
			}
		}
		

		List<EsoTVersamentoPvCob> esoTVersamentoPvCobs = new ArrayList<EsoTVersamentoPvCob>();

		// ragguppare le comunicazioni per giorno
		Map<String, List<GecoComunicazioneObbligatoria>> cobGiornaliere = new HashMap<String, List<GecoComunicazioneObbligatoria>>();
		Calendar cal = Calendar.getInstance();

		
		List<GecoComunicazioneObbligatoria> addedCobs = new ArrayList<GecoComunicazioneObbligatoria>();
		
		for (GecoComunicazioneObbligatoria cob : list) {
			
			if (!aggiungiCob(addedCobs, cob))
				continue;
			
			String key = getKetDate(cob);
			int annoCob = Integer.parseInt(key.substring(0,4));
			if (annoCob == annoRiferimanto) {
				if (cobGiornaliere.containsKey(key)) {
					if (cobGiornaliere.get(key) == null) {
						List<GecoComunicazioneObbligatoria> cobs = new ArrayList<GecoComunicazioneObbligatoria>();
						cobs.add(cob);
						cobGiornaliere.put(key, cobs);
					} else
						cobGiornaliere.get(key).add(cob);
				} else {
					List<GecoComunicazioneObbligatoria> cobs = new ArrayList<GecoComunicazioneObbligatoria>();
					cobs.add(cob);
					cobGiornaliere.put(key, cobs);
				}
				
				addedCobs.add(cob);
			}
		}
		
		
		
		
		
		LinkedHashMap<String, List<GecoComunicazioneObbligatoria>> sortedMap = cobGiornaliere.entrySet().stream()
				.sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		

		for (String key : sortedMap.keySet()) {
			cal.set(Calendar.YEAR, Integer.parseInt(key.substring(0, 4)));
			cal.set(Calendar.MONTH, Integer.parseInt(key.substring(4, 6)) - 1);
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(key.substring(6)));

			EsoTVersamentoPvCob esoTVersamentoPvCob = new EsoTVersamentoPvCob();
			EsoTVersamentoProvincia esoTVersamentoProvincia = new EsoTVersamentoProvincia();
			esoTVersamentoProvincia.setIdEsoTVersamentoProvincia(idEsoTVersamentoProvincia);
			esoTVersamentoPvCob.setEsoTVersamentoProvincia(esoTVersamentoProvincia);
			

			esoTVersamentoPvCob.setDCob(cal.getTime());
			

			long numLavoratori = 0l;
			long numLavoratoriDisabili = 0l;
			for (GecoComunicazioneObbligatoria cob : sortedMap.get(key)) {
				
				if ("ASS".equalsIgnoreCase(cob.getCodTipoComunicazione())) {
					
					Calendar calMonth = Calendar.getInstance();
					calMonth.setTime(cob.getDataInizioRapporto());
					calMonth.add(Calendar.DATE, 180);
					
					
					if (cob.getDataFineRapporto() != null) {
						if ("D".equalsIgnoreCase(cob.getFlgForma()) || "E".equalsIgnoreCase(cob.getFlgForma())) {
							
							if (cob.getDataFineRapporto().before(calMonth.getTime())) {
								// qui dentro i 6 mesi
								// vedere se ci sono proroghe successive
								GecoComunicazioneObbligatoria cobSuccessivaUltima = null;
								for (String keySuccessiva : sortedMap.keySet()) {
									for (GecoComunicazioneObbligatoria cobSuccessiva : sortedMap.get(keySuccessiva)) {
										if (cobSuccessiva.getDataComunicazione().after(cob.getDataComunicazione())) {
											if (cobSuccessiva.getCodiceFiscaleLavoratore()
													.equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
													&& Format.convertiDataInStringa(cobSuccessiva.getDataInizioRapporto()).equals(
															Format.convertiDataInStringa(cob.getDataInizioRapporto())) &&
													"PRO".equalsIgnoreCase(cobSuccessiva.getCodTipoComunicazione())) {										
												cobSuccessivaUltima = cobSuccessiva;
											}
										}	
									}
								}
								if (cobSuccessivaUltima != null) {		
									// guardo se la successiva e oltre 6 mese
									calMonth = Calendar.getInstance();
									calMonth.setTime(cobSuccessivaUltima.getDataInizioRapporto());
									calMonth.add(Calendar.DATE, 180);
									
									if (cobSuccessivaUltima.getDataFineRapporto() != null && 
											cobSuccessivaUltima.getDataFineRapporto().after(calMonth.getTime())) {
										
										if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
											numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
										else numLavoratori = addLavoratori(cob, numLavoratori, true);
										
									}
									else {
										// c'e' proroga che non supera i 6 mesi
										GecoComunicazioneObbligatoria cobTrasformazzioneSuccessiva = null;
										for (String keySuccessiva : sortedMap.keySet()) {
											for (GecoComunicazioneObbligatoria cobSuccessiva : sortedMap.get(keySuccessiva)) {
												if (cobSuccessiva.getDataComunicazione().after(cob.getDataComunicazione())) {
													if (cobSuccessiva.getCodiceFiscaleLavoratore()
															.equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
															&& Format.convertiDataInStringa(cobSuccessiva.getDataInizioRapporto()).equals(
																	Format.convertiDataInStringa(cob.getDataInizioRapporto())) &&
															"TRS".equalsIgnoreCase(cobSuccessiva.getCodTipoComunicazione()) &&
															"DI".equalsIgnoreCase(cobSuccessiva.getCodTipoTrasformazione())) {										
														cobTrasformazzioneSuccessiva = cobSuccessiva;
														break;
													}
												}	
											}
										}
										if (cobTrasformazzioneSuccessiva != null) {
											if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
												numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
											else numLavoratori = addLavoratori(cob, numLavoratori, true);
										}
									}
								}
								else {
									// non c'e proroga
									GecoComunicazioneObbligatoria cobTrasformazzioneSuccessiva = null;
									for (String keySuccessiva : sortedMap.keySet()) {
										for (GecoComunicazioneObbligatoria cobSuccessiva : sortedMap.get(keySuccessiva)) {
											if (cobSuccessiva.getDataComunicazione().after(cob.getDataComunicazione())) {
												if (cobSuccessiva.getCodiceFiscaleLavoratore()
														.equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
														&& Format.convertiDataInStringa(cobSuccessiva.getDataInizioRapporto()).equals(
																Format.convertiDataInStringa(cob.getDataInizioRapporto())) &&
														"TRS".equalsIgnoreCase(cobSuccessiva.getCodTipoComunicazione()) &&
														"DI".equalsIgnoreCase(cobSuccessiva.getCodTipoTrasformazione())) {										
													cobTrasformazzioneSuccessiva = cobSuccessiva;
													break;
												}
											}	
										}
									}
									if (cobTrasformazzioneSuccessiva != null) {
										if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
											numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
										else numLavoratori = addLavoratori(cob, numLavoratori, true);
									}
								}
							}
							else {
								if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
									numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
								else numLavoratori = addLavoratori(cob, numLavoratori, true);
							}
						}
					}
					else {
						// data fine rapporto nulla
						if (!"D".equalsIgnoreCase(cob.getFlgForma())) {
							if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
								numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
							else numLavoratori = addLavoratori(cob, numLavoratori, true);
						}
					}
					
					
				} else if ("CES".equalsIgnoreCase(cob.getCodTipoComunicazione())) {
					
					if ("D".equalsIgnoreCase(cob.getFlgForma()) || 
							("E".equalsIgnoreCase(cob.getFlgForma()) && cob.getDataFineRapporto() != null)) {
						Calendar calMonth = Calendar.getInstance();
						calMonth.setTime(cob.getDataInizioRapporto());
						calMonth.add(Calendar.DATE, 180);
						
						if ((cob.getDataFineRapporto() != null && cob.getDataFineRapporto().after(calMonth.getTime()))) {
							if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
								numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, false);
							else numLavoratori = addLavoratori(cob, numLavoratori, false);
						}
					}
					else if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
						numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, false);
					else numLavoratori = addLavoratori(cob, numLavoratori, false);
					
				} else if ("TRD".equalsIgnoreCase(cob.getCodTipoComunicazione())) {

					if (cob.getCodProvinciaSedeLavoroMinPrec() != null) {
						if (!cob.getCodProvinciaSedeLavoroMinPrec().equals(codProvincia)) {
							
							if (!cob.getCodProvinciaSedeLavoroMinPrec().equals(cob.getCodProvinciaSedeLavoroMin())) {
								
								boolean somma = true;
								if (cob.getCodProvinciaSedeLavoroMinPrec().equals(codProvincia)) {
									somma = false;
								}
								else if (cob.getCodProvinciaSedeLavoroMin().equals(codProvincia)) {
									somma = true;
								}
								else somma = false;
								
								
								if ("D".equalsIgnoreCase(cob.getFlgForma()) || 
										("E".equalsIgnoreCase(cob.getFlgForma()) && cob.getDataFineRapporto() != null)) {
									
									
									Calendar calMonth = Calendar.getInstance();
									calMonth.setTime(cob.getDataInizioRapporto());
									calMonth.add(Calendar.DATE, 180);
									
									if ((cob.getDataFineRapporto() != null && cob.getDataFineRapporto().after(calMonth.getTime()))) {
										if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
											numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, somma);
										else numLavoratori = addLavoratori(cob, numLavoratori, somma);
									}
									else {
										// vado avedere se c'e' una proroga successiva
										GecoComunicazioneObbligatoria cobSuccessivaUltima = null;
										for (String keySuccessiva : sortedMap.keySet()) {
											for (GecoComunicazioneObbligatoria cobSuccessiva : sortedMap.get(keySuccessiva)) {
												if (cobSuccessiva.getDataComunicazione().after(cob.getDataComunicazione())) {
													if (cobSuccessiva.getCodiceFiscaleLavoratore()
															.equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
															&& Format.convertiDataInStringa(cobSuccessiva.getDataInizioRapporto()).equals(
																	Format.convertiDataInStringa(cob.getDataInizioRapporto())) &&
															"PRO".equalsIgnoreCase(cobSuccessiva.getCodTipoComunicazione())) {										
														cobSuccessivaUltima = cobSuccessiva;
													}
												}
											}
										}
										if (cobSuccessivaUltima != null) {		
											// guardo se la successiva e oltre 6 mese
											calMonth = Calendar.getInstance();
											calMonth.setTime(cobSuccessivaUltima.getDataInizioRapporto());
											calMonth.add(Calendar.DATE, 180);
											
											if (cobSuccessivaUltima.getDataFineRapporto() != null && 
													cobSuccessivaUltima.getDataFineRapporto().after(calMonth.getTime())) {
												if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
													numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
												else numLavoratori = addLavoratori(cob, numLavoratori, true);
											}
										}
									}
								}
								else {
									if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
										numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, somma);
									else numLavoratori = addLavoratori(cob, numLavoratori, somma);
								}
							}
						}
					}
					
				} else if ("TRS".equalsIgnoreCase(cob.getCodTipoComunicazione())) {

					// Numero ore settimanali precedenti
					Long ore = 0l;
					if (cob.getOreSettimanaliMediePrec() != null)
						ore = 1l - MathUtils.round(cob.getOreSettimanaliMediePrec()/40d);
					else if (cob.getOreSettimanaliMedie() != null)
						ore = 1l - MathUtils.round(cob.getOreSettimanaliMedie()/40d);
					
					if ("PP".equalsIgnoreCase(cob.getCodTipoTrasformazione())) {
						if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
							numLavoratoriDisabili = numLavoratoriDisabili + ore;
						else
							numLavoratori = numLavoratori + ore;
					} else if ("TP".equalsIgnoreCase(cob.getCodTipoTrasformazione())) {
						if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
							numLavoratoriDisabili = numLavoratoriDisabili - ore;
						else
							numLavoratori = numLavoratori - ore;
					} else if ("AI".equalsIgnoreCase(cob.getCodTipoTrasformazione())) {
						
						if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
							numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
						else numLavoratori = addLavoratori(cob, numLavoratori, true);
						
					} else if ("FI".equalsIgnoreCase(cob.getCodTipoTrasformazione())) {
						if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
							numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
						else numLavoratori = addLavoratori(cob, numLavoratori, true);
					}
				}  else if ("PRO".equalsIgnoreCase(cob.getCodTipoComunicazione())) {
					
					
					GecoComunicazioneObbligatoria cobLegataPro = null;
					for (String keySuccessiva : sortedMap.keySet()) {
						for (GecoComunicazioneObbligatoria cobSuccessiva : sortedMap.get(keySuccessiva)) {
							if (cobSuccessiva.getDataComunicazione().after(cob.getDataComunicazione())) {
								if (cobSuccessiva.getCodiceFiscaleLavoratore()
										.equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
										&& Format.convertiDataInStringa(cobSuccessiva.getDataInizioRapporto()).equals(
												Format.convertiDataInStringa(cob.getDataInizioRapporto())) &&
										!"PRO".equalsIgnoreCase(cobSuccessiva.getCodTipoComunicazione())) {										
									cobLegataPro = cobSuccessiva;
								}
							}
						}
					}
					
					if (cobLegataPro != null) {
						
						GecoComunicazioneObbligatoria assunzionePrecedenteAllaPro = null;
						
						boolean stop = false;
						for (String keySuccessiva : sortedMap.keySet()) {
							for (GecoComunicazioneObbligatoria cobSuccessiva : sortedMap.get(keySuccessiva)) {
								if (cobSuccessiva.getDataComunicazione().before(cob.getDataComunicazione())) {
									if (cobSuccessiva.getCodiceFiscaleLavoratore().equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
											&& Format.convertiDataInStringa(cobSuccessiva.getDataInizioRapporto()).equals(
													Format.convertiDataInStringa(cob.getDataInizioRapporto())) &&
											"ASS".equalsIgnoreCase(cobSuccessiva.getCodTipoComunicazione())) {										
										assunzionePrecedenteAllaPro = cobSuccessiva;
										stop = true;
										break;
									}
								}
							}
							if (stop)
								break;
						}
						
						if (assunzionePrecedenteAllaPro != null) {
							// esiste una assunzione per questa proroga in quest'anno allora non si considera
						}
						else {
							
							// esiste una assunzione nell'anno precedente e quindi 
							// la consideriamo come piu uno se supera i 180 gg
							
							Calendar calMonth = null;
							GecoComunicazioneObbligatoria cobSuccessivaUltima = null;
							for (String keySuccessiva : sortedMap.keySet()) {
								for (GecoComunicazioneObbligatoria cobSuccessiva : sortedMap.get(keySuccessiva)) {
									if (cobSuccessiva.getDataComunicazione().after(cob.getDataComunicazione())) {
										if (cobSuccessiva.getCodiceFiscaleLavoratore()
												.equalsIgnoreCase(cob.getCodiceFiscaleLavoratore())
												&& Format.convertiDataInStringa(cobSuccessiva.getDataInizioRapporto()).equals(
														Format.convertiDataInStringa(cob.getDataInizioRapporto())) &&
												"PRO".equalsIgnoreCase(cobSuccessiva.getCodTipoComunicazione())) {										
											cobSuccessivaUltima = cobSuccessiva;
										}
									}	
								}
							}
							if (cobSuccessivaUltima != null) {		
								// guardo se la successiva proroga e oltre 6 mese
								calMonth = Calendar.getInstance();
								calMonth.setTime(cobSuccessivaUltima.getDataInizioRapporto());
								calMonth.add(Calendar.DATE, 180);
								
								if (cobSuccessivaUltima.getDataFineProroga() != null && 
										cobSuccessivaUltima.getDataFineProroga().after(calMonth.getTime())) {
									if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
										numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
									else numLavoratori = addLavoratori(cob, numLavoratori, true);
								}
							}
							else {
								
								// guardo se stessa e oltre 6 mese
								calMonth = Calendar.getInstance();
								calMonth.setTime(cob.getDataInizioRapporto());
								calMonth.add(Calendar.DATE, 180);
								
								if (cob.getDataFineProroga() != null && 
										cob.getDataFineProroga().after(calMonth.getTime())) {
									if ("S".equalsIgnoreCase(cob.getFlgLegge68()))
										numLavoratoriDisabili = addLavoratori(cob, numLavoratoriDisabili, true);
									else numLavoratori = addLavoratori(cob, numLavoratori, true);
								}
							}
						}
					}
				}
				
			}
			
			esoTVersamentoPvCob.setNumLavoratori(numLavoratori);
			esoTVersamentoPvCob.setNumLavoratoriDisabili(numLavoratoriDisabili);
			
			// persist
			if (esoTVersamentoPvCob.getNumLavoratori().longValue() != 0 || esoTVersamentoPvCob.getNumLavoratoriDisabili().longValue() != 0) {
					
				if (persist)
					esoTVersamentoPvCob.persist();
	
				List<EsoTVersamentoCobLav> esoTVersamentoCobLavs = new ArrayList<EsoTVersamentoCobLav>();
	
				EsoTVersamentoPvCob esoTVersamentoPvCobPersist = new EsoTVersamentoPvCob();
				esoTVersamentoPvCobPersist.setIdEsoTVersamentoPvCob(esoTVersamentoPvCob.getIdEsoTVersamentoPvCob());
				for (GecoComunicazioneObbligatoria cob : sortedMap.get(key)) {
					EsoTVersamentoCobLav esoTVersamentoCobLav = new EsoTVersamentoCobLav();
					esoTVersamentoCobLav.setEsoTVersamentoPvCob(esoTVersamentoPvCobPersist);
	
					esoTVersamentoCobLav.setCodFiscale(cob.getCodiceFiscaleLavoratore());
					esoTVersamentoCobLav.setCodiceComunicazioneReg(cob.getCodComunicazione());
					esoTVersamentoCobLav.setDsCognome(cob.getCognomeLavoratore());
					esoTVersamentoCobLav.setDsNome(cob.getNomeLavoratore());
					esoTVersamentoCobLav.setDsTipoComunicazione(cob.getDsTipoComunicazione());
					esoTVersamentoCobLav.setDsTipoContratto(cob.getDsTipoContratto());
					esoTVersamentoCobLav.setFlgFulltime(cob.getFlgFullTime());
					esoTVersamentoCobLav.setFlgL68(cob.getFlgLegge68());
					esoTVersamentoCobLav.setNumOreSettMed(cob.getOreSettimanaliMedie());
					esoTVersamentoCobLav.setSiglaPv(cob.getSiglaProvinciaSedeLavoro());
					
					// persist
					if (persist)
						esoTVersamentoCobLav.persist();
	
					esoTVersamentoCobLavs.add(esoTVersamentoCobLav);
				}
	
				esoTVersamentoPvCob.setEsoTVersamentoCobLavs(esoTVersamentoCobLavs);
				esoTVersamentoPvCobs.add(esoTVersamentoPvCob);
			}
		}

		Collections.sort(esoTVersamentoPvCobs, new Comparator<EsoTVersamentoPvCob>() {
			public int compare(EsoTVersamentoPvCob o1, EsoTVersamentoPvCob o2) {
				return o1.getDCob().compareTo(o2.getDCob());
			}
		});
		return esoTVersamentoPvCobs;
	}

	// ==============================================================================
	// PRIVATE - METHODS

	private Long salvaPeriodiAutomaticiProvincia(Long idEsoTVersamentoProvincia, List<EsoTVersamentoPvPeriodo> periodi,boolean ripristina) {
		EsoTVersamentoProvincia esoTVersamentoProvincia = EsoTVersamentoProvincia.findById(idEsoTVersamentoProvincia);
		
		EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
		esoTVersamentoProvinciaPersist.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());
		String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		String userAggiorn = userInserim;
		Date now = new Date();
		Long timestamp = 0L;
		
		if(ripristina)
			EsoTVersamentoPvPeriodo.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia = ?1", idEsoTVersamentoProvincia);
		else
			EsoTVersamentoPvPeriodo.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia = ?1 and flgTipo in ('A','C')", idEsoTVersamentoProvincia);
		
		double importoAutomatico = 0d;
 		if (periodi != null) {
			for (EsoTVersamentoPvPeriodo periodo : periodi) {
				periodo.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);
				periodo.setCodUserInserim(userInserim);
				periodo.setDInserim(now);
				periodo.setCodUserAggiorn(userAggiorn);
				periodo.setDAggiorn(now);
				periodo.setNTimestamp(timestamp);
				periodo.persist();
				
				importoAutomatico += periodo.getImporto().doubleValue();
			}
		}
				
 		EsoTVersamentoProvincia.update("importoAutomatico = ?1 where idEsoTVersamentoProvincia = ?2", new BigDecimal(importoAutomatico), esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());
		return esoTVersamentoProvincia.getEsoTVersamento().getIdEsoTVersamento();
	}
	
	private String getKetDate(GecoComunicazioneObbligatoria cob) {

		Calendar cal = Calendar.getInstance();
		Date date = cob.getDataComunicazione();
		cal.setTime(date);

		if ("CES".equalsIgnoreCase(cob.getCodTipoComunicazione())) {
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		}

		String month = "" + (cal.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String day = "" + cal.get(Calendar.DAY_OF_MONTH);
		if (day.length() == 1)
			day = "0" + day;
		return cal.get(Calendar.YEAR) + month + day;
	}

	

	
	
	
	public Versamento getVersamentoAttuale(Versamento versamento) {
		
		Azienda azienda = new Azienda();
		azienda.setCodFiscale(versamento.getCodFiscale());
		azienda.setDenomAzienda(versamento.getDsDenominazioneAzienda());
		String annoRiferimento = "" + versamento.getAnnoRiferimento();
		
		DatiServiziProdisSilp datiServiziProdisSilp = prodisManager.getDatiProdis(azienda, null, annoRiferimento);
		if (datiServiziProdisSilp.getMessaggi() != null && datiServiziProdisSilp.getMessaggi().size() > 0) {
			for (ApiMessage message : datiServiziProdisSilp.getMessaggi()) {
				if (message.getError()) {
					throw new BusinessException("DatiDichiarazioniProvincieManager.getDati",
							datiServiziProdisSilp.getMessaggi());
				}
			}
		}
		
		
		versamento = new Versamento();
		versamento.setEsoTVersamentoProspettos(datiServiziProdisSilp.getProspetti());
		

		Map<String, VersamentoProvincia> versamentoProvincieMap = new HashMap<String, VersamentoProvincia>();
		// versamentoProvincia - versamentoPvEsonero
		if (datiServiziProdisSilp.getProvinceEsoneri() != null) {
			for (VersamentoProvincia versamentoProvinciaProdis : datiServiziProdisSilp.getProvinceEsoneri()) {
				if (versamentoProvinciaProdis.getSilapDProvincia() != null) {

					// versamentoProvincia
					VersamentoProvincia versamentoProvincia = versamentoProvincieMap.get(versamentoProvinciaProdis.getSilapDProvincia().getId());
					if (versamentoProvincia == null) {
						versamentoProvincia = new VersamentoProvincia();
						versamentoProvincia.setEsoTVersamentoPvEsoneros(new ArrayList<VersamentoPvEsonero>());
						Provincia dProv = new Provincia();
						dProv.setId(versamentoProvinciaProdis.getSilapDProvincia().getId());
						versamentoProvincia.setSilapDProvincia(dProv);
						versamentoProvincieMap.put(versamentoProvincia.getSilapDProvincia().getId(),versamentoProvincia);
					}


					// versamentoPvEsonero
					if (versamentoProvinciaProdis.getEsoTVersamentoPvEsoneros() != null) {
						
						
						if (versamentoProvincia.getEsoTVersamentoPvEsoneros() == null) 
							versamentoProvincia.setEsoTVersamentoPvEsoneros(new ArrayList<VersamentoPvEsonero>());
						
						versamentoProvincia.getEsoTVersamentoPvEsoneros().addAll(versamentoProvinciaProdis.getEsoTVersamentoPvEsoneros());
					}
				}
			}
		}

		// versamentoProvincia - versamentoPvConvenzione
		if (datiServiziProdisSilp.getProvinceConvenzioni() != null) {
			for (VersamentoProvincia versamentoProvinciaProdis : datiServiziProdisSilp.getProvinceConvenzioni()) {
				
				if (versamentoProvinciaProdis.getSilapDProvincia() != null) {

					// versamentoProvincia
					VersamentoProvincia versamentoProvincia = versamentoProvincieMap.get(versamentoProvinciaProdis.getSilapDProvincia().getId());
					if (versamentoProvincia == null) {
						versamentoProvincia = new VersamentoProvincia();
						versamentoProvincia.setEsoTVersamentoPvEsoneros(new ArrayList<VersamentoPvEsonero>());
						Provincia dProv = new Provincia();
						dProv.setId(versamentoProvinciaProdis.getSilapDProvincia().getId());
						versamentoProvincia.setSilapDProvincia(dProv);
						versamentoProvincieMap.put(versamentoProvincia.getSilapDProvincia().getId(),versamentoProvincia);
					}
					
					
					// versamentoPvEsonero
					if (versamentoProvinciaProdis.getEsoTVersamentoPvConvenziones() != null) {
						
						if (versamentoProvincia.getEsoTVersamentoPvConvenziones() == null) 
							versamentoProvincia.setEsoTVersamentoPvConvenziones(new ArrayList<VersamentoPvConvenzione>());
						
						versamentoProvincia.getEsoTVersamentoPvConvenziones().addAll(versamentoProvinciaProdis.getEsoTVersamentoPvConvenziones());
					}
				}
			}
		}

		// versamentoProvincia - versamentoPvRicIn
		List<RiconoscimentiInabilita> riconoscimentiInabilitaSilp = silpRestManager.findRiconoscimentiInabilita(
				azienda.getCodFiscale(), null, annoRiferimento);
		if (riconoscimentiInabilitaSilp != null) {
			for (VersamentoProvincia versamentoProvinciaProdis : datiServiziProdisSilp.getProvinceEsoneri()) {
				if (versamentoProvinciaProdis.getSilapDProvincia() != null) {
					List<VersamentoPvRicIn> versamentoPvRicIns = new ArrayList<VersamentoPvRicIn>();
					VersamentoProvincia versamentoProvincia = versamentoProvincieMap.get(versamentoProvinciaProdis.getSilapDProvincia().getId());
					if (versamentoProvincia != null) {
						for (RiconoscimentiInabilita riconoscimentiInabilita : riconoscimentiInabilitaSilp) {
							if (versamentoProvinciaProdis.getSilapDProvincia().getId().equals(riconoscimentiInabilita.getIdSilapDProvincia())) {
								VersamentoPvRicIn versamentoPvRicIn = new VersamentoPvRicIn();
								versamentoPvRicIn.setDRiconoscimentoInvalidita(riconoscimentiInabilita.getDRiconoscInvalidita());
								versamentoPvRicIn.setDScadenza(riconoscimentiInabilita.getDScadenza());
								versamentoPvRicIn.setNumOreSettMed(riconoscimentiInabilita.getNumOreSettMed());
								versamentoPvRicIns.add(versamentoPvRicIn);
							}
						}
						versamentoProvincia.setEsoTVersamentoPvRicIns(versamentoPvRicIns);
					}
				}
			}
		}
		
		versamento.setEsoTVersamentoProvincias(new ArrayList<VersamentoProvincia>());
	    for (Map.Entry<String, VersamentoProvincia> entry : versamentoProvincieMap.entrySet())
	    	versamento.getEsoTVersamentoProvincias().add(entry.getValue());
		
		return versamento;
	}
	
	
	// ===============================================================
	
	
	private EsoTVersamento salvaDatiAziendaliInternal(EsoTVersamento esoTVersamento, DatiServiziProdisSilp datiServiziProdisSilp, boolean aggiornamento) {

		String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		String userAggiorn = userInserim;
		Date now = new Date();
		Long timestamp = 0L;

		// salvare:
		
		// sede legale
		if (esoTVersamento.getVersamentoSede() != null) {

			EsoTVersamentoSede versamentoSedePersist = esoTVersamento.getVersamentoSede();
			
			
			if (esoTVersamento.getVersamentoSede().getComune() != null && esoTVersamento.getVersamentoSede().getComune().getId() != null) {
				SilapDComune silapDComune = new SilapDComune();
				silapDComune.setId(esoTVersamento.getVersamentoSede().getComune().getId());
				versamentoSedePersist.setComune(silapDComune);
			}
			
			if (aggiornamento) {
				if (esoTVersamento.getVersamentoSede().getNTimestamp() != null)
					versamentoSedePersist.setNTimestamp(esoTVersamento.getVersamentoSede().getNTimestamp()+1);
				else versamentoSedePersist.setNTimestamp(0L);
			}
			else {
				versamentoSedePersist.setCodUserInserim(userInserim);
				versamentoSedePersist.setDInserim(now);
				versamentoSedePersist.setNTimestamp(timestamp);
			}
			versamentoSedePersist.setDAggiorn(now);
			versamentoSedePersist.setCodUserAggiorn(userAggiorn);
			versamentoSedePersist.persist();

			EsoTVersamentoSede versamentoSede = new EsoTVersamentoSede();
			versamentoSede.setIdEsoTVersamentoSede(esoTVersamento.getVersamentoSede().getIdEsoTVersamentoSede());
			esoTVersamento.setVersamentoSede(versamentoSede);
		}

		// credito residuo
		if (esoTVersamento.getEsoTCreditoResiduo() != null && esoTVersamento.getEsoTCreditoResiduo().getIdEsoTCreditoResiduo() != null) {
			
			EsoTCreditoResiduo credito = EsoTCreditoResiduo.findById(esoTVersamento.getEsoTCreditoResiduo().getIdEsoTCreditoResiduo());
			if (credito != null) {
				esoTVersamento.setNumCreditoResiduo(credito.getNumValore());
				
				EsoTCreditoResiduo esoTCreditoResiduo = new EsoTCreditoResiduo();
				esoTCreditoResiduo.setIdEsoTCreditoResiduo(esoTVersamento.getEsoTCreditoResiduo().getIdEsoTCreditoResiduo());
				esoTVersamento.setEsoTCreditoResiduo(esoTCreditoResiduo);
			}
		}

		// cnnl
		if (esoTVersamento.getSilapDCcnl() != null && esoTVersamento.getSilapDCcnl().getId() != null) {
			SilapDCcnl silapDCcnl = new SilapDCcnl();
			silapDCcnl.setId(esoTVersamento.getSilapDCcnl().getId());
			esoTVersamento.setSilapDCcnl(silapDCcnl);
		}

		if (!aggiornamento) {
			Long idRuolo = SilapThreadLocalContainer.ID_RUOLO.get();
			String codiceFiscaleSoggettoAbilitato = SilapThreadLocalContainer.ID_COD_FISC_SOGG_ABILITATO.get();
			if (codiceFiscaleSoggettoAbilitato != null && SilapConstants.RUOLO_CONSULENTE.longValue() == idRuolo.longValue()
					&& codiceFiscaleSoggettoAbilitato.trim().length() > 0) {
				// versamento
				if (codiceFiscaleSoggettoAbilitato != null
						&& SilapConstants.RUOLO_CONSULENTE.longValue() == idRuolo.longValue()
						&& codiceFiscaleSoggettoAbilitato.trim().length() > 0) {
					esoTVersamento.setCodFiscaleSoggetto(codiceFiscaleSoggettoAbilitato);
				}
			}
		}
		
		
		if (aggiornamento) {
			esoTVersamento.setNTimestamp(esoTVersamento.getNTimestamp()+1);
		}
		else {
			esoTVersamento.setCodUserInserim(userInserim);
			esoTVersamento.setDInserim(now);
			esoTVersamento.setNTimestamp(timestamp);
		}
		esoTVersamento.setDAggiorn(now);
		esoTVersamento.setCodUserAggiorn(userAggiorn);
		
		esoTVersamento.persist();
		return esoTVersamento;
	}
	
	
	
	private Long salvaProspettoEsoneriConvenzioniInternal(EsoTVersamento esoTVersamento, 
			DatiServiziProdisSilp datiServiziProdisSilp,
			List<RiconoscimentiInabilita> riconoscimentiInabilitaSilp,
			boolean aggiornamento) {


		String userInserim = SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc();
		String userAggiorn = userInserim;
		Date now = new Date();
		Long timestamp = 0L;
		
		EsoTVersamento esoTVersamentoPersist = new EsoTVersamento();
		esoTVersamentoPersist.setIdEsoTVersamento(esoTVersamento.getIdEsoTVersamento());

		// cancellazione prospetto, esoneri e convenzioni
		if (aggiornamento) {
			if (esoTVersamento.getEsoTVersamentoProspettos() != null) {
				for (EsoTVersamentoProspetto prospetto : esoTVersamento.getEsoTVersamentoProspettos()) {
					EsoTVersamentoPvProspetto.delete("esoTVersamentoProspetto.idEsoTVersamentoProspetto", prospetto.getIdEsoTVersamentoProspetto());
					EsoTVersamentoProspetto.delete("idEsoTVersamentoProspetto", prospetto.getIdEsoTVersamentoProspetto());
				}
			}
			if (esoTVersamento.getEsoTVersamentoProvincias() != null) {
				
				for (EsoTVersamentoProvincia provincia : esoTVersamento.getEsoTVersamentoProvincias()) {
					EsoTVersamentoPvEsonero.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia", provincia.getIdEsoTVersamentoProvincia());
					EsoTVersamentoPvConvenzione.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia", provincia.getIdEsoTVersamentoProvincia());
					EsoTVersamentoPvPeriodo.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia", provincia.getIdEsoTVersamentoProvincia());
					//EsoTVersamentoPvSospensione.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia", provincia.getIdEsoTVersamentoProvincia());
					EsoTVersamentoPvRicIn.delete("esoTVersamentoProvincia.idEsoTVersamentoProvincia", provincia.getIdEsoTVersamentoProvincia());
					
					
					if (provincia.getEsoTVersamentoPvCobs() != null) {
						for (EsoTVersamentoPvCob cob : provincia.getEsoTVersamentoPvCobs()) {
							EsoTVersamentoCobLav.delete("esoTVersamentoPvCob.idEsoTVersamentoPvCob", cob.getIdEsoTVersamentoPvCob());
							EsoTVersamentoPvCob.delete("idEsoTVersamentoPvCob", cob.getIdEsoTVersamentoPvCob());
						}
					}
					
					
					
					//EsoTVersamentoProvincia.delete("idEsoTVersamentoProvincia",  provincia.getIdEsoTVersamentoProvincia());
				}
			}
			
	
			
			esoTVersamento.setEsoTVersamentoProspettos(null);
			esoTVersamento.setEsoTVersamentoProvincias(null);
		}
		
		

		// versamento prospetto
		if (datiServiziProdisSilp.getProspetti() != null) {
			for (VersamentoProspetto prospetto : datiServiziProdisSilp.getProspetti()) {

				EsoTVersamentoProspetto esoTVersamentoProspetto = mappers.VERSAMENTO_PROSPETTO.toEntity(prospetto);
				esoTVersamentoProspetto.setEsoTVersamento(esoTVersamentoPersist);
				esoTVersamentoProspetto.setNTimestamp(timestamp);
				esoTVersamentoProspetto.setCodUserAggiorn(userAggiorn);
				esoTVersamentoProspetto.setCodUserInserim(userInserim);
				esoTVersamentoProspetto.setDInserim(now);
				esoTVersamentoProspetto.setDAggiorn(now);

				esoTVersamentoProspetto.persist();
				EsoTVersamentoProspetto esoTVersamentoProspettoPersist = new EsoTVersamentoProspetto();
				esoTVersamentoProspettoPersist
						.setIdEsoTVersamentoProspetto(esoTVersamentoProspetto.getIdEsoTVersamentoProspetto());

				// versamento prospetto provincia
				if (prospetto.getEsoTVersamentoPvProspettos() != null) {
					for (VersamentoPvProspetto pvProspetto : prospetto.getEsoTVersamentoPvProspettos()) {

						EsoTVersamentoPvProspetto esoTVersamentoPvProspetto = mappers.VERSAMENTO_PV_PROSPETTO
								.toEntity(pvProspetto);
						esoTVersamentoPvProspetto.setEsoTVersamentoProspetto(esoTVersamentoProspettoPersist);

						// provincia
						if (pvProspetto.getSilapDProvincia() != null
								&& pvProspetto.getSilapDProvincia().getId() != null) {
							SilapDProvincia silapDProvincia = new SilapDProvincia();
							silapDProvincia.setId(pvProspetto.getSilapDProvincia().getId());
							esoTVersamentoPvProspetto.setSilapDProvincia(silapDProvincia);
						}

						esoTVersamentoPvProspetto.setNTimestamp(timestamp);
						esoTVersamentoPvProspetto.setCodUserAggiorn(userAggiorn);
						esoTVersamentoPvProspetto.setCodUserInserim(userInserim);
						esoTVersamentoPvProspetto.setDInserim(now);
						esoTVersamentoPvProspetto.setDAggiorn(now);

						esoTVersamentoPvProspetto.persist();
					}
				}
			}
		}
		
		
		

		Map<String, EsoTVersamentoProvincia> versamentoProvincieMap = new HashMap<String, EsoTVersamentoProvincia>();

		// versamentoProvincia - versamentoPvEsonero
		if (datiServiziProdisSilp.getProvinceEsoneri() != null) {
			for (VersamentoProvincia versamentoProvincia : datiServiziProdisSilp.getProvinceEsoneri()) {
				if (versamentoProvincia.getSilapDProvincia() != null) {

					// versamentoProvincia
					EsoTVersamentoProvincia esoTVersamentoProvincia = versamentoProvincieMap.get(versamentoProvincia.getSilapDProvincia().getId());
					if (esoTVersamentoProvincia == null) {
						
						
						List<EsoTVersamentoProvincia> provincie = EsoTVersamentoProvincia.list("esoTVersamento.idEsoTVersamento = ?1 and silapDProvincia.id = ?2", 
								esoTVersamento.getIdEsoTVersamento(),
								versamentoProvincia.getSilapDProvincia().getId());
						
						if (provincie != null && provincie.size()>0 && provincie.get(0).getIdEsoTVersamentoProvincia() != null) {
							esoTVersamentoProvincia = provincie.get(0);
						}
						else {
							// inserimento versamento provincia
							esoTVersamentoProvincia = mappers.VERSAMENTO_PROVINCIA.toEntity(versamentoProvincia);
							esoTVersamentoProvincia.setEsoTVersamento(esoTVersamentoPersist);
	
							// provincia
							SilapDProvincia silapDProvincia = new SilapDProvincia();
							silapDProvincia.setId(versamentoProvincia.getSilapDProvincia().getId());
							esoTVersamentoProvincia.setSilapDProvincia(silapDProvincia);
	
							esoTVersamentoProvincia.setNTimestamp(timestamp);
							esoTVersamentoProvincia.setCodUserAggiorn(userAggiorn);
							esoTVersamentoProvincia.setCodUserInserim(userInserim);
							esoTVersamentoProvincia.setDInserim(now);
							esoTVersamentoProvincia.setDAggiorn(now);
							esoTVersamentoProvincia.setNumGgLavorativiSettimanali(5L);
	
							esoTVersamentoProvincia.persist();
						}
						
						versamentoProvincieMap.put(versamentoProvincia.getSilapDProvincia().getId(),
								esoTVersamentoProvincia);
					}
					// else gia' inserita

					EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
					esoTVersamentoProvinciaPersist
							.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());

					// versamentoPvEsonero
					if (versamentoProvincia.getEsoTVersamentoPvEsoneros() != null) {

						for (VersamentoPvEsonero pvEsonero : versamentoProvincia.getEsoTVersamentoPvEsoneros()) {
							EsoTVersamentoPvEsonero esoTVersamentoPvEsonero = mappers.VERSAMENTO_PV_ESONERO
									.toEntity(pvEsonero);
							esoTVersamentoPvEsonero.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);

							esoTVersamentoPvEsonero.setNTimestamp(timestamp);
							esoTVersamentoPvEsonero.setCodUserAggiorn(userAggiorn);
							esoTVersamentoPvEsonero.setCodUserInserim(userInserim);
							esoTVersamentoPvEsonero.setDInserim(now);
							esoTVersamentoPvEsonero.setDAggiorn(now);

							esoTVersamentoPvEsonero.persist();
						}
					}
				}
			}
		}

		// versamentoProvincia - versamentoPvConvenzione
		if (datiServiziProdisSilp.getProvinceConvenzioni() != null) {
			for (VersamentoProvincia versamentoProvincia : datiServiziProdisSilp.getProvinceConvenzioni()) {
				if (versamentoProvincia.getSilapDProvincia() != null) {

					// versamentoProvincia
					EsoTVersamentoProvincia esoTVersamentoProvincia = versamentoProvincieMap
							.get(versamentoProvincia.getSilapDProvincia().getId());
					if (esoTVersamentoProvincia == null) {
						
						List<EsoTVersamentoProvincia> provincie = EsoTVersamentoProvincia.list("esoTVersamento.idEsoTVersamento = ?1 and silapDProvincia.id = ?2", 
								esoTVersamento.getIdEsoTVersamento(),
								versamentoProvincia.getSilapDProvincia().getId());
						
						if (provincie != null && provincie.size()>0 && provincie.get(0).getIdEsoTVersamentoProvincia() != null) {
							esoTVersamentoProvincia = provincie.get(0);
						}
						else {
							// inserimento versamento provincia
							esoTVersamentoProvincia = mappers.VERSAMENTO_PROVINCIA.toEntity(versamentoProvincia);
							esoTVersamentoProvincia.setEsoTVersamento(esoTVersamentoPersist);

							// provincia
							SilapDProvincia silapDProvincia = new SilapDProvincia();
							silapDProvincia.setId(versamentoProvincia.getSilapDProvincia().getId());
							esoTVersamentoProvincia.setSilapDProvincia(silapDProvincia);
							esoTVersamentoProvincia.setNumGgLavorativiSettimanali(5L);

							esoTVersamentoProvincia.setNTimestamp(timestamp);
							esoTVersamentoProvincia.setCodUserAggiorn(userAggiorn);
							esoTVersamentoProvincia.setCodUserInserim(userInserim);
							esoTVersamentoProvincia.setDInserim(now);
							esoTVersamentoProvincia.setDAggiorn(now);

							esoTVersamentoProvincia.persist();
						}
						
						versamentoProvincieMap.put(versamentoProvincia.getSilapDProvincia().getId(),
								esoTVersamentoProvincia);
					}
					// else gia' inserita

					EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
					esoTVersamentoProvinciaPersist
							.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());

					// versamentoPvConvenzione
					if (versamentoProvincia.getEsoTVersamentoPvConvenziones() != null) {

						for (VersamentoPvConvenzione pvConvenzione : versamentoProvincia
								.getEsoTVersamentoPvConvenziones()) {
							EsoTVersamentoPvConvenzione esoTVersamentoPvConvenzione = mappers.VERSAMENTO_PV_CONVENZIONE
									.toEntity(pvConvenzione);
							esoTVersamentoPvConvenzione.setEsoTVersamentoProvincia(esoTVersamentoProvinciaPersist);

							// tipo convenzione
							if (pvConvenzione.getEsoDVersamentoTipoConvenzione() != null && pvConvenzione
									.getEsoDVersamentoTipoConvenzione().getIdSilL68TipoConvenzione() != null) {
								
								List<EsoDVersamentoTipoConvenzione> listTipi = EsoDVersamentoTipoConvenzione.list("idSilL68TipoConvenzione", pvConvenzione
										.getEsoDVersamentoTipoConvenzione().getIdSilL68TipoConvenzione());
								
								
								if (listTipi != null && listTipi.size()>0) {
									EsoDVersamentoTipoConvenzione esoDVersamentoTipoConvenzione = new EsoDVersamentoTipoConvenzione();
									esoDVersamentoTipoConvenzione.setIdEsoDVersamentoTipoConvenzione(listTipi.get(0).getIdEsoDVersamentoTipoConvenzione());
									esoTVersamentoPvConvenzione.setEsoDVersamentoTipoConvenzione(esoDVersamentoTipoConvenzione);
								}
							}

							esoTVersamentoPvConvenzione.setNTimestamp(timestamp);
							esoTVersamentoPvConvenzione.setCodUserAggiorn(userAggiorn);
							esoTVersamentoPvConvenzione.setCodUserInserim(userInserim);
							esoTVersamentoPvConvenzione.setDInserim(now);
							esoTVersamentoPvConvenzione.setDAggiorn(now);

							if (esoTVersamentoPvConvenzione.getDScadenza() != null)
								esoTVersamentoPvConvenzione.persist();
						}
					}
				}
			}
		}
		
		
		// versamentoProvincia - versamentoPvRicIn
		if (riconoscimentiInabilitaSilp != null) {
			
			for (VersamentoProvincia versamentoProvincia : datiServiziProdisSilp.getProvinceEsoneri()) {
				for (RiconoscimentiInabilita riconoscimentiInabilita : riconoscimentiInabilitaSilp) {
					if (versamentoProvincia.getSilapDProvincia().getId().equals(riconoscimentiInabilita.getIdSilapDProvincia())) {
						
						EsoTVersamentoProvincia esoTVersamentoProvincia = versamentoProvincieMap
								.get(versamentoProvincia.getSilapDProvincia().getId());
						if (esoTVersamentoProvincia != null) {
							
							EsoTVersamentoProvincia esoTVersamentoProvinciaPersist = new EsoTVersamentoProvincia();
							esoTVersamentoProvinciaPersist
									.setIdEsoTVersamentoProvincia(esoTVersamentoProvincia.getIdEsoTVersamentoProvincia());
							
							
							EsoTVersamentoPvRicIn esoTVersamentoPvRicIn = new EsoTVersamentoPvRicIn();
							
							esoTVersamentoPvRicIn.setDRiconoscimentoInvalidita(riconoscimentiInabilita.getDRiconoscInvalidita());
							esoTVersamentoPvRicIn.setDScadenza(riconoscimentiInabilita.getDScadenza());
							esoTVersamentoPvRicIn.setNumOreSettMed(riconoscimentiInabilita.getNumOreSettMed());
							esoTVersamentoPvRicIn.setEsoTVersamentoProvincia(esoTVersamentoProvincia);
							esoTVersamentoPvRicIn.setNTimestamp(timestamp);
							esoTVersamentoPvRicIn.setCodUserAggiorn(userAggiorn);
							esoTVersamentoPvRicIn.setCodUserInserim(userInserim);
							esoTVersamentoPvRicIn.setDInserim(now);
							esoTVersamentoPvRicIn.setDAggiorn(now);
							esoTVersamentoPvRicIn.persist();
						}
					}
				}
			}			
		}
		
		

		return esoTVersamentoPersist.getIdEsoTVersamento();
	}
	
	
	public VersamentoPvPeriodo impostaPeriodoOperatore(Long idProvincia, VersamentoPvPeriodo periodo) {
		EsoTVersamentoProvincia provincia = EsoTVersamentoProvincia.findById(idProvincia);
		EsoTVersamento versamento = EsoTVersamento.findById(provincia.getEsoTVersamento().getIdEsoTVersamento());
		
		//1 solo -- ci deve essere un esonero
		List<EsoTVersamentoPvPeriodo> periodiEsoneri = null;
		boolean error = true;
		EsoTVersamentoPvPeriodo periodoEsonero = null;
		if (provincia.getEsoTVersamentoPvEsoneros() != null && provincia.getEsoTVersamentoPvEsoneros().size()>0) {
			// ESONERI
			VersamentoProvincia versProv = new VersamentoProvincia();
			versProv.setEsoTVersamentoPvPeriodos(new ArrayList<VersamentoPvPeriodo>());
			versProv.setEsoTVersamentoPvEsoneros(
					mappers.VERSAMENTO_PV_ESONERO.toModels(provincia.getEsoTVersamentoPvEsoneros()));
			versProv = prodisManager.calcoloPeriodioPerProvincia(versProv);
			periodiEsoneri = mappers.VERSAMENTO_PV_PERIODO
					.toEntities(versProv.getEsoTVersamentoPvPeriodos());

			// verifica primo periodo inizio anno riferimento
			Calendar cal = Calendar.getInstance();
			if (periodiEsoneri != null && periodiEsoneri.size() > 0) {
				cal.setTime(periodiEsoneri.get(0).getDInizio());
				if (cal.get(Calendar.YEAR) < versamento.getAnnoRiferimento().intValue()) {
					cal.set(Calendar.YEAR, versamento.getAnnoRiferimento().intValue());
					cal.set(Calendar.MONTH, 0);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					periodiEsoneri.get(0).setDInizio(cal.getTime());
				}
			}
			error = !versamentoEsoneriCalcoloManager.inPeriodo(periodiEsoneri, periodo.getDInizio(), periodo.getDFine());
		}
		if (error)
			throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_ESONERO_MANCANTE));
		
		
		//prendo l'esonero da considerare
		periodoEsonero = versamentoEsoneriCalcoloManager.getPeriodoEsonero(periodiEsoneri, periodo.getDInizio(), periodo.getDFine());
		
		
		//la sospensione se c'e' una solo che copre l'intero periodo
		error = false;
		EsoTVersamentoPvPeriodo sospensione = null;
		if (provincia.getEsoTVersamentoPvSospensiones() != null && provincia.getEsoTVersamentoPvSospensiones().size()>0) {
			
			List<EsoTVersamentoPvPeriodo> periodiSospensioni = new ArrayList<EsoTVersamentoPvPeriodo>();
			for (EsoTVersamentoPvSospensione sospensioneUn : provincia.getEsoTVersamentoPvSospensiones()) {
				EsoTVersamentoPvPeriodo per = new EsoTVersamentoPvPeriodo();
				per.setDInizio(sospensioneUn.getDInizioSospensione());
				per.setDFine(sospensioneUn.getDFineSospensione());
				per.setQuotaRiserva(sospensioneUn.getPercSospensione());
				per.setEsoDVersamentoMotivoSospensione(sospensioneUn.getEsoDVersamentoMotivoSospensione());
				per.setNumLavoratoriSospesi(sospensioneUn.getNumLavoratoriSospesi());
				periodiSospensioni.add(per);
			}

			if (versamentoEsoneriCalcoloManager.esisteInPeriodoCollocamentoMirato(periodiSospensioni, periodo.getDInizio(),
					periodo.getDFine())) {
				error = !versamentoEsoneriCalcoloManager.inPeriodo(periodiSospensioni, periodo.getDInizio(),
						periodo.getDFine());
			}
			
			sospensione = versamentoEsoneriCalcoloManager.getPeriodo(periodiSospensioni, periodo.getDInizio(),periodo.getDFine());
			
		} 
		if (error) 
			throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_CONTROLLO_PERIODI_SOSPENSIONE));
		

		
		//la convenzione se c'e' una solo che copre l'intero periodo
		error = false;
		EsoTVersamentoPvPeriodo convenzione = null;
		if (provincia.getEsoTVersamentoPvConvenziones() != null && provincia.getEsoTVersamentoPvConvenziones().size()>0) {
			
			List<EsoTVersamentoPvPeriodo> periodiConvenzioni = new ArrayList<EsoTVersamentoPvPeriodo>();
			List<EsoTVersamentoPvConvenzione> convenzioni = provincia.getEsoTVersamentoPvConvenziones();
			if (convenzioni != null) {
				for (EsoTVersamentoPvConvenzione convenzioneDb : convenzioni) {
					if (convenzioneDb.getDScadenza() != null) {
						EsoTVersamentoPvPeriodo per = new EsoTVersamentoPvPeriodo();
						per.setDInizio(convenzioneDb.getDStipula());
						per.setDFine(convenzioneDb.getDScadenza());
						per.setQuotaRiserva(convenzioneDb.getNumPosizioniAperte());
						periodiConvenzioni.add(per);
					}				
				}
			}

			if (versamentoEsoneriCalcoloManager.esisteInPeriodoCollocamentoMirato(periodiConvenzioni, periodo.getDInizio(),
					periodo.getDFine())) {
				error = !versamentoEsoneriCalcoloManager.inPeriodo(periodiConvenzioni, periodo.getDInizio(),
						periodo.getDFine());
			}
			
			
			convenzione = versamentoEsoneriCalcoloManager.getPeriodo(periodiConvenzioni, periodo.getDInizio(),periodo.getDFine());

		} 
		if (error) 
			throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_CONTROLLO_PERIODI_CONVENZIONE));
				

		
		// PROSPETTO ANNO RIFERIMENTO E PROVINCIALE
		EsoTVersamentoProspetto prospetto = null;
		EsoTVersamentoProspetto prospettoPrec = null;
		for (EsoTVersamentoProspetto prosp : versamento.getEsoTVersamentoProspettos()) {
			if (prosp.getAnnoRiferimento().longValue() == versamento.getAnnoRiferimento().longValue() -1)
				prospetto = prosp;
			else if (prosp.getAnnoRiferimento().longValue() != versamento.getAnnoRiferimento().longValue())
				prospettoPrec = prosp;
		}
		
		if (prospetto == null && prospettoPrec != null) {
			prospetto = prospettoPrec;
			prospettoPrec = null;
		}
		
		if (prospetto == null && versamento.getEsoTVersamentoProspettos().size()>0) 
			prospetto = versamento.getEsoTVersamentoProspettos().get(0);
		
		EsoTVersamentoPvProspetto prospettoProvincia = null;
		EsoTVersamentoPvProspetto prospettoProvinciaPrec = null;
		for (EsoTVersamentoPvProspetto prospPv : prospetto.getEsoTVersamentoPvProspettos()) {
			if (prospPv.getSilapDProvincia().getId().equalsIgnoreCase(provincia.getSilapDProvincia().getId())) {
				prospettoProvincia = prospPv;
				break;
			}
		}
		if (prospettoPrec != null) {
			for (EsoTVersamentoPvProspetto prospPv : prospettoPrec.getEsoTVersamentoPvProspettos()) {
				if (prospPv.getSilapDProvincia().getId().equalsIgnoreCase(provincia.getSilapDProvincia().getId())) {
					prospettoProvinciaPrec = prospPv;
					break;
				}
			}
		}
		
		
		
		
		
			
		double importoGiornalieroEsonero = 0d;
		try {
			List<EsoTParametro> listParam = EsoTParametro.list("codParametro", "IMPGG");
			if (listParam != null)
				importoGiornalieroEsonero = Double.parseDouble(listParam.get(0).getDsValore().trim());
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		List<Date> giorniExtraFestivi = null;
		List<EsoTVersamentoPvGgExtraFestivi> esoTVersamentoPvGgExtraFestivis = provincia
				.getEsoTVersamentoPvGgExtraFestivis();
		if (esoTVersamentoPvGgExtraFestivis != null && esoTVersamentoPvGgExtraFestivis.size() > 0) {
			for (EsoTVersamentoPvGgExtraFestivi esoTVersamentoPvGgExtraFestivi : esoTVersamentoPvGgExtraFestivis) {
				if (giorniExtraFestivi == null)
					giorniExtraFestivi = new ArrayList<Date>();
				giorniExtraFestivi.add(esoTVersamentoPvGgExtraFestivi.getDGgFestivo());
			}
		}
		int giorniLavorativi = GiorniLavorativiUtils.getGiorniLavorativi(periodo.getDInizio(), periodo.getDFine(),
				provincia.getNumGgLavorativiSettimanali(), provincia.getDFestaPatronale(), giorniExtraFestivi);
		periodo.setNumGgLavorativi(Long.valueOf(giorniLavorativi));
		
		int baseComputo = periodo.getBaseComputo() != null ? periodo.getBaseComputo().intValue() : 0;
		int disabiliInForza = periodo.getNumDisabiliInForza() != null ? periodo.getNumDisabiliInForza().intValue() : 0;
		int numRiallineamentoNazionale = periodo.getNumRiallineamentoNazionale() != null ? periodo.getNumRiallineamentoNazionale().intValue() : 0;
		

		int numeroPosizioniAperte = 0;
		if (convenzione != null) {
			numeroPosizioniAperte = convenzione.getQuotaRiserva().intValue();
		}
		
		int quotaRiserva = 0;
		
		
		SilapDCategoriaAzienda categoriaAzienda = SilapDCategoriaAzienda.findById(periodo.getSilapDCategoriaAzienda().getId());
		if ("A".equalsIgnoreCase(categoriaAzienda.getCodCategoriaAzienda()))
			quotaRiserva = MathUtils.calcolaQuotaRiserva(Long.valueOf(baseComputo));
		else if ("B".equalsIgnoreCase(categoriaAzienda.getCodCategoriaAzienda()))
			quotaRiserva = 2;
		else if ("C".equalsIgnoreCase(categoriaAzienda.getCodCategoriaAzienda()))
			quotaRiserva = 1;
		
		
		if (sospensione != null) {
			if (SilapConstants.MOTIVO_SOSPENSIONE_CIGS.intValue() == sospensione.getEsoDVersamentoMotivoSospensione().getId().intValue()) {
				if (sospensione.getQuotaRiserva() != null)
					quotaRiserva = Math.round(quotaRiserva*(1- MathUtils.round(sospensione.getQuotaRiserva().doubleValue()/100d)));
				else if (sospensione.getNumLavoratoriSospesi() != null) {
					int totDip = prospettoProvincia.getBaseComputoProvinciale().intValue();
					if (prospettoProvinciaPrec != null)
						totDip = prospettoProvinciaPrec.getBaseComputoProvinciale().intValue();
					quotaRiserva = MathUtils.round((double)quotaRiserva *  ((totDip-sospensione.getNumLavoratoriSospesi())/totDip));
				}
			}
			else if (SilapConstants.MOTIVO_SOSPENSIONE_LICENZIAMENTO_COLLETTIVO.intValue() ==sospensione
					.getEsoDVersamentoMotivoSospensione().getId().intValue()) {
				quotaRiserva = 0;
			}
		}
		
		quotaRiserva = quotaRiserva + (int)numRiallineamentoNazionale;
		if (quotaRiserva < 0)
			quotaRiserva = 0;
		
		
		int numCompensati = periodo.getNumSoggettiCompensati() != null ? periodo.getNumSoggettiCompensati().intValue() : 0;
		int numAutocertificati = periodo.getNumEsoneratiAutocertificati() != null ? periodo.getNumEsoneratiAutocertificati().intValue() : 0;
		int numEsoneriDaPagare = 0;
		int numLavoratoriEsonerati = 0;			
		
		/*
		se categoria nazionale azienda = C (15-35 dip) ==> per QR=1 se azzerare il numero di esoneri da pagare ==> sarà l'unico caso che azzera in automatico le unità di esonero.
		Per le altre due casistiche di categoria nazionale azienda si applica regolarmente la formula generale:
		 dove la quota di esonero_max è 
		- se categoria nazionale azienda = B (36-50 dip) ==>  per QR  = 2  quindi  quota_esonero_max = (1 – n. esoneri_autocertificati)
		- se categoria nazionale azienda = A (>50 dip)     ==> per QR qualsiasi quindi quota_esonero_max = arrotonda ((quota riserva * esonero %) / 100) - n.esoneri_autocertificati,
		   e la percentuale d’esonero è quella della richiesta nel periodo tra la data richiesta e la data concessione -1 o tra la data richiesta e la data di diniego -1,
		   della concessione nel periodo tra la data di concessione alla data scadenza.
		 */
		int esoneroMax = 0;
		if (quotaRiserva<=1 && "C".equalsIgnoreCase(periodo.getSilapDCategoriaAzienda().getCodCategoriaAzienda())) 
			esoneroMax = 0;
		else if (quotaRiserva == 2 && "B".equalsIgnoreCase(periodo.getSilapDCategoriaAzienda().getCodCategoriaAzienda())) 
			esoneroMax = 1 - numAutocertificati;
		else if ("A".equalsIgnoreCase(periodo.getSilapDCategoriaAzienda().getCodCategoriaAzienda()))
			esoneroMax =  MathUtils.round((double)(quotaRiserva * periodoEsonero.getQuotaRiserva())/100 - numAutocertificati);
		
		if (esoneroMax<0)
			esoneroMax = 0;
		
		numLavoratoriEsonerati = (quotaRiserva - disabiliInForza - numAutocertificati - numeroPosizioniAperte + numCompensati);
		if (numLavoratoriEsonerati <0)
			numLavoratoriEsonerati = 0;

		numEsoneriDaPagare = Math.min(numLavoratoriEsonerati, esoneroMax);
		
		
		periodo.setNumEsoneratiAutocertificati(Long.valueOf(numAutocertificati));
		periodo.setQuotaRiserva(Long.valueOf(quotaRiserva));
		periodo.setNumSoggettiCompensati(Long.valueOf(numCompensati));
		periodo.setNumLavoratoriEsonerati(Long.valueOf(numEsoneriDaPagare));
		periodo.setImporto(new BigDecimal(numEsoneriDaPagare*importoGiornalieroEsonero*periodo.getNumGgLavorativi()));
		periodo.setFlgTipo("O");
		periodo.setNumRiallineamentoNazionale(Long.valueOf(numRiallineamentoNazionale));
		periodo.setSilapDCategoriaAzienda(mappers.CATEGORIA_AZIENDA.toModel(categoriaAzienda));
		
		return periodo;
		
	}
	

	
	public EsoTVersamento confermaVersamentoProvincie(EsoTVersamento esoTVersamento) {
		for (EsoTVersamentoProvincia provincia : esoTVersamento.getEsoTVersamentoProvincias()) {
			boolean sovra = verificaSovrapposizioniPeriodi(mappers.VERSAMENTO.toModel(esoTVersamento), 
					mappers.VERSAMENTO_PROVINCIA.toModel(provincia));
			
			if (sovra) {
				throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_CONTROLLO_PERIODI_SOVRAPPOSTI, 
						provincia.getSilapDProvincia().getDsSilapDProvincia()));
			}
		}

		
		Calendar cal = Calendar.getInstance();
		boolean periodoMancante = false;
		String provinciaMancante = "";
		VersamentoProvincia versProv = null;
		List<EsoTVersamentoPvPeriodo> periodiEsoneri = null;
		
		
		
		for (EsoTVersamentoProvincia provincia : esoTVersamento.getEsoTVersamentoProvincias()) {
			
			
			// ESONERI
			versProv = new VersamentoProvincia();
			versProv.setEsoTVersamentoPvPeriodos(new ArrayList<VersamentoPvPeriodo>());
			versProv.setEsoTVersamentoPvEsoneros(
					mappers.VERSAMENTO_PV_ESONERO.toModels(provincia.getEsoTVersamentoPvEsoneros()));
			versProv = prodisManager.calcoloPeriodioPerProvincia(versProv);
			periodiEsoneri = mappers.VERSAMENTO_PV_PERIODO.toEntities(versProv.getEsoTVersamentoPvPeriodos());

			// verifica primo periodo inizio anno riferimento
			if (periodiEsoneri != null && periodiEsoneri.size() > 0) {
				cal.setTime(periodiEsoneri.get(0).getDInizio());
				if (cal.get(Calendar.YEAR) < esoTVersamento.getAnnoRiferimento().intValue()) {
					cal.set(Calendar.YEAR, esoTVersamento.getAnnoRiferimento().intValue());
					cal.set(Calendar.MONTH, 0);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					periodiEsoneri.get(0).setDInizio(cal.getTime());
				}
			}
			
			LocalDate startDate = LocalDate.of(esoTVersamento.getAnnoRiferimento().intValue(), Month.JANUARY, 1);
			LocalDate endDate = startDate.plusYears(1);
			while (startDate.isBefore(endDate)) {

				EsoTVersamentoPvPeriodo eso = versamentoEsoneriCalcoloManager.inPeriodo(periodiEsoneri, startDate);
				if (eso != null) {
					EsoTVersamentoPvPeriodo periodo = versamentoEsoneriCalcoloManager.inPeriodo(provincia.getEsoTVersamentoPvPeriodos(),startDate);
					if (periodo == null) {
						periodoMancante = true;
						provinciaMancante += provincia.getSilapDProvincia().getDsSilapDProvincia() + ", ";
						break;
					}
				}
				startDate = startDate.plusDays(1);
			}
			
			if (periodoMancante)
				break;
		}
		
		if (periodoMancante) {
			provinciaMancante = provinciaMancante.substring(0, provinciaMancante.length()-2);
			throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_CONTROLLO_PERIODI, 
					provinciaMancante));
		}
		
		return esoTVersamento;
	}
	
	
	public VersamentoStato getCurrentStato(Versamento versamento) {
		
		if (versamento != null && versamento.getEsoTVersamentoStatos() != null) {
			for (VersamentoStato stato : versamento.getEsoTVersamentoStatos()) {
				if ("S".equalsIgnoreCase(stato.getFlgCurrentRecord()))
					return stato;
			}
		}
		return null;
	}

	public void autorizzaVersamento(VersamentoStato versamentoStato) {

		Long idDStato = versamentoStato.getEsoDVersamentoStato().getId();
		Long idVersamento = versamentoStato.getIdEsoTVersamento();
		Versamento versamento = new Versamento();

		// updateStatoVersamento con il nuovo idDStato
		try {
			updateStatoVersamento(idVersamento, idDStato);
		} catch (Exception err) {
			throw new BusinessException(err.getMessage());
		}

		// cerco il nuovo versamento stato appena inserito
		List<EsoTVersamentoStato> verStato = EsoTVersamentoStato.list(
				"esoDVersamentoStato.id=?1 and esoTVersamento.idEsoTVersamento=?2 and flgCurrentRecord=?3", idDStato,
				idVersamento, "S");
		VersamentoStato versStato = new VersamentoStato();
		// seleziono il versamentoStato appena creato con idDStato autorizzato/non
		// autorizzato e flg = S
		for (EsoTVersamentoStato stato : verStato) {
			if (SilapConstants.STATO_AUTORIZZATA.equals(stato.getEsoDVersamentoStato().getId())
					|| SilapConstants.STATO_NON_AUTORIZZATA.equals(stato.getEsoDVersamentoStato().getId())
							&& stato.getFlgCurrentRecord().equals("S")) {
				versStato = mappers.VERSAMENTO_STATO.toModel(stato);
			}
		}

		// salva note su EsoTVersamentoStato e invio mail
		if (SilapConstants.STATO_AUTORIZZATA.equals(idDStato)
				|| SilapConstants.STATO_NON_AUTORIZZATA.equals(idDStato)) {

			EsoTVersamentoStato.update("dsNote=?1 where idEsoTVersamentoStato=?2", versamentoStato.getDsNote(),
					versStato.getIdEsoTVersamentoStato());

			versamento = getVersamento(idVersamento);
			if (versamento == null) {
				throw BusinessException.createError(SilapConstants.MSG_ERRORE_GET_VERSAMENTO);
			}
			try {
				String note = "";
				note = versamentoStato.getDsNote();
				sendEmail(versamento, idDStato,note);
			} catch (BusinessException ex) {
				throw new BusinessException(ex.getMessage());
			}
		}
	}
	
	public void autorizzaVersamentoBatch(VersamentoStato versamentoStato, Utente utente) {

		Long idDStato = SilapConstants.STATO_AUTORIZZATA;
		Long idVersamento = versamentoStato.getIdEsoTVersamento();
		Versamento versamento = new Versamento();

		// updateStatoVersamento con il nuovo idDStato
		try {
			updateStatoVersamentoBatch(idVersamento, idDStato, utente);
		} catch (Exception err) {
			throw new BusinessException(err.getMessage());
		}

		// cerco il nuovo versamento stato appena inserito
		List<EsoTVersamentoStato> verStato = EsoTVersamentoStato.list(
				"esoDVersamentoStato.id=?1 and esoTVersamento.idEsoTVersamento=?2 and flgCurrentRecord=?3", idDStato,
				idVersamento, "S");
		VersamentoStato versStato = new VersamentoStato();
		// seleziono il versamentoStato appena creato con idDStato autorizzato/non
		// autorizzato e flg = S
		for (EsoTVersamentoStato stato : verStato) {
			if (SilapConstants.STATO_AUTORIZZATA.equals(stato.getEsoDVersamentoStato().getId())
					|| SilapConstants.STATO_NON_AUTORIZZATA.equals(stato.getEsoDVersamentoStato().getId())
							&& stato.getFlgCurrentRecord().equals("S")) {
				versStato = mappers.VERSAMENTO_STATO.toModel(stato);
			}
		}

		// salva note su EsoTVersamentoStato e invio mail
		if (SilapConstants.STATO_AUTORIZZATA.equals(idDStato)
				|| SilapConstants.STATO_NON_AUTORIZZATA.equals(idDStato)) {

			EsoTVersamentoStato.update("dsNote=?1 where idEsoTVersamentoStato=?2", versamentoStato.getDsNote(),
					versStato.getIdEsoTVersamentoStato());

			versamento = getVersamento(idVersamento);
			if (versamento == null) {
				throw BusinessException.createError(SilapConstants.MSG_ERRORE_GET_VERSAMENTO);
			}
			try {
				String note = "";
				note = versamentoStato.getDsNote();
				sendEmail(versamento, idDStato,note);
			} catch (BusinessException ex) {
				throw new BusinessException(ex.getMessage());
			}
		}
	}
	public Messaggio confermaInviaVersamento(Versamento versamento) throws BusinessException {

		List<EsoTVersamentoStato> stati = EsoTVersamentoStato.list("esoDVersamentoStato.id = ?1 and esoTVersamento.codFiscale = ?2 and esoTVersamento.annoRiferimento = ?3", 
				SilapConstants.STATO_NON_AUTORIZZATA,
				versamento.getCodFiscale(),
				versamento.getAnnoRiferimento());

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		Date termine1 = null;
		List<EsoTParametro> fine1List = EsoTParametro.list("codParametro = ?1", "FINE1");
		if (fine1List != null && fine1List.size()>0) {
			try {
				String val = fine1List.get(0).getDsValore();
				termine1 = paramDateFormat.parse(val);
				cal.setTime(termine1);
				cal.set(Calendar.YEAR, versamento.getAnnoRiferimento().intValue()+1);
				termine1 = cal.getTime();
			}
			catch (Exception err) {
				cal.set(Calendar.YEAR, versamento.getAnnoRiferimento().intValue()+1);
				cal.set(Calendar.MONTH, Calendar.FEBRUARY);
				cal.set(Calendar.DAY_OF_MONTH, 28);
				termine1 = cal.getTime();
			}
		}
		Date termine2 = null;
		List<EsoTParametro> fine2List = EsoTParametro.list("codParametro = ?1", "FINE2");
		if (fine2List != null && fine2List.size()>0) {
			try {
				String val = fine2List.get(0).getDsValore();
				termine2 = paramDateFormat.parse(val);
				cal.setTime(termine2);
				cal.set(Calendar.YEAR, versamento.getAnnoRiferimento().intValue()+1);
				termine2 = cal.getTime();
			}
			catch (Exception err) {
				cal.set(Calendar.YEAR, versamento.getAnnoRiferimento().intValue()+1);
				cal.set(Calendar.MONTH, Calendar.JUNE);
				cal.set(Calendar.DAY_OF_MONTH, 30);
				termine2 = cal.getTime();
			}
		}
		
		if (stati != null && stati.size()>0) {
			if (now.after(termine2)) {
				throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_ESONERO_FUORI_TERMINE_GIUGNO,
						versamento.getAnnoRiferimento(), versamento.getDsDenominazioneAzienda(),
						versamento.getCodFiscale(), termine2));
			}
			else if (now.after(termine1) && now.after(termine2)) {
				throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_ESONERO_FUORI_TERMINE_FEBBRAIO,
						versamento.getAnnoRiferimento(), versamento.getDsDenominazioneAzienda(),
						versamento.getCodFiscale(), termine1));
			}
		}
		else if (now.after(termine1)) {
			throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_ESONERO_FUORI_TERMINE_FEBBRAIO,
					versamento.getAnnoRiferimento(), versamento.getDsDenominazioneAzienda(),
					versamento.getCodFiscale(), termine1));
		}
		
		boolean variazione = verificaVariazioneImportoTotale(versamento);
		VersamentoStato currentStato = getCurrentStato(versamento);
		try {
			if (variazione)
				updateStatoVersamento(versamento.getIdEsoTVersamento(), SilapConstants.STATO_MODIFICATA);
			else 
				updateStatoVersamento(versamento.getIdEsoTVersamento(), SilapConstants.STATO_ACCETTATA);
		}
		catch (Exception err) {
			Log.error("[VersamentoEsoneriManager::confermaInviaVersamento]", err);
			throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_ESONERO_ERROR_STATO, 
					currentStato.getEsoDVersamentoStato().getDescr(),
					versamento.getDsDenominazioneAzienda(),
					versamento.getCodFiscale(),
					(versamento.getAnnoRiferimento().intValue()+1)));
		}
		
		Date dataProtocollo = null;
		Long numeroProtocollo = null;
		Long annoProtocollo = null;
		try {
			Protocollo protocollo = iupsManager.protocolla(versamento);
			if (protocollo == null)
				throw new BusinessException("errore iup");
			
			dataProtocollo = protocollo.getDataProtocollo();
			numeroProtocollo = protocollo.getNumeroProtocollo();
			annoProtocollo = protocollo.getAnnoProtocollo();
		}
		catch (Exception err) {
			throw BusinessException.createError(getDescrMsg(SilapConstants.MSG_ERRORE_PROTOCOLLAZIONE));
		}
		

		try {
			EsoTVersamento.update(
					"annoProtocollo = ?1,  numProtocollo = ?2, dProtocollo = ?3 where idEsoTVersamento = ?4",
					annoProtocollo, numeroProtocollo, dataProtocollo, versamento.getIdEsoTVersamento());
			
			versamento.setAnnoProtocollo(annoProtocollo);
			versamento.setNumProtocollo(numeroProtocollo);
			versamento.setDProtocollo(dataProtocollo);
			
			
		} catch (Exception err) {
			Log.error(err);
			throw BusinessException.createError("Errore in aggiornameto dati protocollazione");
		}

		if (versamento != null) {
			String filename = versamento.getIdEsoTVersamento().toString().trim() + "_"
					+ versamento.getAnnoRiferimento().toString().trim() + "_" + versamento.getCodFiscale().trim();
			try {
				ReportUtilities.makePdfFile(fileSystemHome,
						versamentoEsoneriStampaManager.stampaVersamentoEsoneri(versamento), filename,
						versamento.getAnnoRiferimento());

			} catch (Exception e) {
				e.printStackTrace();
				Log.error("Errore stampa tabellare pdf/xls ", e);
				throw BusinessException.createError(
						getDescrMsg(SilapConstants.MSG_ERRORE_GENERAZIONE_PDF, versamento.getAnnoRiferimento(),
								versamento.getDsDenominazioneAzienda(), versamento.getCodFiscale()));
			}
		}
		return getMsg(SilapConstants.MSG_SUCCESSO_INVIO, 
				versamento.getDsDenominazioneAzienda(),
				versamento.getCodFiscale(),
				versamento.getAnnoRiferimento(),
				annoProtocollo, numeroProtocollo,(variazione ? SilapConstants.STATO_MODIFICATA_DESCR : SilapConstants.STATO_ACCETTATA_DESCR));
	}
	
	private boolean verificaVariazioneImportoTotale(Versamento versamento) {
		double totalePeriodi = 0D;
		double totaleAutomaticoProvincia = 0D;
		for (VersamentoProvincia provincia : versamento.getEsoTVersamentoProvincias()) {
			if (provincia.getImportoAutomatico() != null)
				totaleAutomaticoProvincia += provincia.getImportoAutomatico().doubleValue();
			for (VersamentoPvPeriodo periodo : provincia.getEsoTVersamentoPvPeriodos()) {
				if (!"C".equalsIgnoreCase(periodo.getFlgTipo()) && periodo.getImporto() != null)
					totalePeriodi += periodo.getImporto().doubleValue();
			}
		}
		return MathUtils.mathRound(totaleAutomaticoProvincia, 2) != MathUtils.mathRound(totalePeriodi, 2);
	}
	
	
	public boolean verificaSovrapposizioniPeriodi(Versamento versamento, VersamentoProvincia provincia) {
		boolean sovrapposizione = false;
		for (VersamentoProvincia prov : versamento.getEsoTVersamentoProvincias()) {
			if (prov.getIdEsoTVersamentoProvincia().longValue() == provincia.getIdEsoTVersamentoProvincia().longValue()) {
				for (VersamentoPvPeriodo periodo : prov.getEsoTVersamentoPvPeriodos()) {
					if ("O".equalsIgnoreCase(periodo.getFlgTipo())) {
						boolean sovra = versamentoEsoneriCalcoloManager.inPeriodo(prov.getEsoTVersamentoPvPeriodos(), periodo);
						if (sovra) {
							periodo.setSovrapposto(true);
							sovrapposizione = true;
						}
					}
				}
			}
		}
		return sovrapposizione;
	}
	
	
	public BigDecimal getImportoTotaleVersamento(EsoTVersamento versamento) {
		BigDecimal ret = new BigDecimal(0L);
		if (versamento.getEsoTVersamentoProvincias() != null) {
			for (EsoTVersamentoProvincia prov : versamento.getEsoTVersamentoProvincias()) {
				if (prov.getEsoTVersamentoPvPeriodos() != null) {
					for (EsoTVersamentoPvPeriodo periodo : prov.getEsoTVersamentoPvPeriodos()) {
						ret.add(periodo.getImporto());
					}
				}
			}
		}
		return ret;
	}
	
	
	
public Messaggio checkPosizioniDebitorieBatch() {
		
		
		List<EsoTBatchExec> esoTBatchExecList = EsoTBatchExec.list(
				"codEsoTBatchExec = ?1 and dFineExec is null", SilapConstants.BATCH_CREAZIONE_POSIZIONI_DEBITORIE);
		
		if (esoTBatchExecList != null && esoTBatchExecList.size() >0) {
			Messaggio msg = getMsg(SilapConstants.MSG_BATCH_CREAZIONE_DEBUTORIA_ATTIVO);
			TipoMessaggio tipo = new TipoMessaggio();
			tipo.setIdSilapDTipoMessaggio("E");
			msg.setSilapDTipoMessaggio(tipo);
			return msg;
		}
		
		
		Long numRecordDaElaborare = 0l;
		List<EsoTVersamentoStato> stati = new ArrayList<EsoTVersamentoStato>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		long annoRiferimento = cal.get(Calendar.YEAR)-1;
		stati = EsoTVersamentoStato.list("esoDVersamentoStato.id = ?1 and flgCurrentRecord = 'S' and esoTVersamento.annoRiferimento = ?2",
				SilapConstants.STATO_AUTORIZZATA,annoRiferimento);
		
		
		numRecordDaElaborare = (long) stati.size();
		EsoTVersamento versamento = null;
		for (EsoTVersamentoStato stato : stati) {
			versamento = stato.getEsoTVersamento();
			double importoTotale = 0d;
			
			if (versamento.getEsoTVersamentoProvincias() != null) {
				for (EsoTVersamentoProvincia prov : versamento.getEsoTVersamentoProvincias()) {
					if (prov.getEsoTVersamentoPvPeriodos() != null) {
						for (EsoTVersamentoPvPeriodo periodo : prov.getEsoTVersamentoPvPeriodos()) {
							if (!"C".equalsIgnoreCase(periodo.getFlgTipo())) {
								importoTotale += periodo.getImporto().doubleValue();
							}
						}
					}
				}
			}
			
			if (importoTotale>0 && versamento.getNumCreditoResiduo() != null && versamento.getNumCreditoResiduo().doubleValue()>0) {
				importoTotale = importoTotale-versamento.getNumCreditoResiduo().doubleValue();
			}
			
			if (importoTotale<=0) {
				numRecordDaElaborare-=1;
				continue;
			}
		}

		if (numRecordDaElaborare <= 0) {
			Messaggio msg = getMsg(SilapConstants.MSG_BATCH_CREAZIONE_DEBUTORIA_NESSUNA_DICHIARAZIONE);
			TipoMessaggio tipo = new TipoMessaggio();
			tipo.setIdSilapDTipoMessaggio("E");
			msg.setSilapDTipoMessaggio(tipo);
			return msg;
		}
		
		
		Messaggio msg = getMsg(SilapConstants.MSG_BATCH_CREAZIONE_DEBUTORIA_CONFERMA, numRecordDaElaborare);
		TipoMessaggio tipo = new TipoMessaggio();
		tipo.setIdSilapDTipoMessaggio("I");
		msg.setSilapDTipoMessaggio(tipo);
		return msg;
		
	}


	public Versamento raggruppaPeriodi(Versamento versamento) {
		
		if (versamento.getEsoTVersamentoProvincias() != null) {
			
			for (VersamentoProvincia vp : versamento.getEsoTVersamentoProvincias()) {
				
				List<VersamentoPvPeriodoMaster> listMaster = new ArrayList<VersamentoPvPeriodoMaster>();
				
				if (vp.getEsoTVersamentoPvPeriodos() != null) {
					VersamentoPvPeriodoMaster master = new VersamentoPvPeriodoMaster();
					master.setDInizio(new Date());
					master.setDFine(new Date());
					master.setListaPeriodi(vp.getEsoTVersamentoPvPeriodos());
					listMaster.add(master);
				}
				
				vp.setEsoTVersamentoPvPeriodoMasters(listMaster);			}
		}
		
		return versamento;
	}
	
}
