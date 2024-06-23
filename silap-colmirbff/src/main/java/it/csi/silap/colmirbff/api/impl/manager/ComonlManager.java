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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.enterprise.context.Dependent;
import javax.ws.rs.core.GenericType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.ibm.icu.util.Calendar;

import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.GecoComunicazioneObbligatoria;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.TipoMessaggio;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.exception.ServiceException;
import it.csi.silap.colmirbff.integration.comonl.dto.ApiError;
import it.csi.silap.colmirbff.integration.comonl.dto.ComunicazioneObbligatoria;
import it.csi.silap.colmirbff.integration.comonl.dto.FilterComunicazioniObbligatorie;
import it.csi.silap.colmirbff.integration.entity.EsoTParametro;
import it.csi.silap.colmirbff.util.CommonUtils;

/**
 * @author G
 *
 */
@Dependent
public class ComonlManager extends BaseApiServiceImpl {

	@ConfigProperty(name = "comonl_srv_url_rest")
	String comonlUri;

	@ConfigProperty(name = "comonl_srv_user_rest")
	String comonlUser;

	@ConfigProperty(name = "comonl_srv_password_rest")
	String comonlPassword;
	
	
	private boolean caricaCodiciEsclusione = true;
	private List<String> ccnle = new ArrayList<String>();
	private List<String> conte = new ArrayList<String>();
	private List<String> tcobe = new ArrayList<String>();

	private List<String> listaProvince = new ArrayList<String>(
			List.of(SilapConstants.COD_PROV_TO, SilapConstants.COD_PROV_VC, SilapConstants.COD_PROV_NO,
					SilapConstants.COD_PROV_CN, SilapConstants.COD_PROV_AT, SilapConstants.COD_PROV_AL,
					SilapConstants.COD_PROV_BI, SilapConstants.COD_PROV_VB));

	private List<String> listaTipoTracciato = new ArrayList<String>(List.of(SilapConstants.COD_GECO_UNILAV));
	
	
	public List<GecoComunicazioneObbligatoria> getElencoComunicazioni(String codFiscaleAzienda,
			String annoRiferimento) {
		return getElencoComunicazioni(codFiscaleAzienda,annoRiferimento, null);
	}

	/**
	 * chiama servizio comunicazioni obbligatorie esposto da comonl (geco) filtra
	 * per province piemontesi e tipo comunicazioni UNILAV dal primo gennaio al 31
	 * dicembre dell'anno riferimento
	 * 
	 * @param codFiscaleAzienda
	 * @param annoRiferimento
	 * @return
	 */
	public List<GecoComunicazioneObbligatoria> getElencoComunicazioni(String codFiscaleAzienda,
			String annoRiferimento, String codProvincia) {
		List<GecoComunicazioneObbligatoria> elenco = new ArrayList<GecoComunicazioneObbligatoria>();
		FilterComunicazioniObbligatorie filtro = new FilterComunicazioniObbligatorie();
		filtro.setCodiceFiscale(codFiscaleAzienda);
		
		if (codProvincia != null) {
			List<String> codPrvs = new ArrayList<String>();
			codPrvs.add(codProvincia);
			filtro.setCodProvincia(codPrvs);
		}
		else filtro.setCodProvincia(listaProvince);
		filtro.setTipoTracciato(listaTipoTracciato);

		Date dataInizio = CommonUtils.convertiStringaInData("31/12/" + (Integer.parseInt(annoRiferimento)-1));
		filtro.setDtInizio(dataInizio);
		
		
		

		Date dataFine = CommonUtils.convertiStringaInData("31/12/" + annoRiferimento);
		Calendar cal = Calendar.getInstance();
		if (Integer.parseInt(annoRiferimento) == cal.get(Calendar.YEAR))
			dataFine = new Date();
		
		
		filtro.setDtFine(dataFine);
		List<ComunicazioneObbligatoria> elencoServ = new ArrayList<ComunicazioneObbligatoria>();
		try {
			elencoServ = postService(comonlUri + "/comunicazioni/comobb/SILAP.FLAG_ABILITAZIONE_SERVIZIO_COMOBBL", null,
					null, filtro, new GenericType<List<ComunicazioneObbligatoria>>() {
					}, comonlUser, comonlPassword, new GenericType<List<ApiError>>() {
					});
		} catch (ServiceException ex) {
			@SuppressWarnings("unchecked")
			List<ApiError> listaErr = (List<ApiError>) ex.getResult();
			Messaggio msg = new Messaggio();
			msg.setDsSilapDMessaggio(listaErr.get(0).getErrorMessage());
			TipoMessaggio tipo = new TipoMessaggio();
			tipo.setIdSilapDTipoMessaggio("E");
			msg.setSilapDTipoMessaggio(tipo);
			throw new BusinessException(new ApiMessage(msg));
		}

		if (CommonUtils.isNotVoid(elencoServ)) {
			elenco = mappers.COMUNICAZIONE_OBBLIGATORIA.toModels(elencoServ);

		}
		return elenco;
	}

	/**
	 * escluse dall'elenco le comunicazioni che non devono essere considerate dal
	 * calcolo (par. 5.8.1)
	 * 
	 * @param elencoIn
	 * @return
	 */
	public List<GecoComunicazioneObbligatoria> escludeComunicazioniNonInerentiCalcolo(
			List<GecoComunicazioneObbligatoria> elencoIn, int annoRiferimento) {

		List<GecoComunicazioneObbligatoria> elencoOut = new ArrayList<GecoComunicazioneObbligatoria>();
		for (Iterator<GecoComunicazioneObbligatoria> iterator = elencoIn.iterator(); iterator.hasNext();) {
			GecoComunicazioneObbligatoria comObblIn = (GecoComunicazioneObbligatoria) iterator.next();
			if (isComObblDaIncludereNelCalcolo(comObblIn, annoRiferimento)) {
				elencoOut.add(comObblIn);
			}			
		}

		return elencoOut;
	}

	private boolean isComObblDaIncludereNelCalcolo(GecoComunicazioneObbligatoria comObblIn, int annoRiferimento) {
		
		if (caricaCodiciEsclusione) {
			ccnle = getParametro("CCNLE");
			conte = getParametro("CONTE");
			tcobe = getParametro("TCOBE");
			caricaCodiciEsclusione = false;
		}
		
		for (String c : ccnle) {
			if (c.equalsIgnoreCase(comObblIn.getCodiceCCNL()))
				return false;
		}

		for (String c : conte) {
			if (c.equalsIgnoreCase(comObblIn.getCodTipoContratto()))
				return false;
		}
		
		for (String c : tcobe) {
			if (c.equalsIgnoreCase(comObblIn.getCodTipoTrasformazione()))
				return false;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(comObblIn.getDataComunicazione());
		if (cal.get(Calendar.YEAR) < annoRiferimento) {
			if ("CES".equalsIgnoreCase(comObblIn.getCodTipoComunicazione()))
				return true;
			else return false;
		}
		
		
		if ("PRO".equalsIgnoreCase(comObblIn.getCodTipoComunicazione())) {
			return true;
		}
		
		
		if (comObblIn.getDataFineRapporto() != null) {
			cal.setTime(comObblIn.getDataFineRapporto());
			if (cal.get(Calendar.YEAR) < annoRiferimento && 
					!"CES".equalsIgnoreCase(comObblIn.getCodTipoComunicazione()))
				return true;
			else if (cal.get(Calendar.YEAR) == annoRiferimento)
				return true;
			return false;
		}
		
		
		return true;
	}

	
	
	private List<String> getParametro(String codice) {
		
		List<String> ret = new ArrayList<String>();
		List<EsoTParametro> esoTParametro = EsoTParametro.list("codParametro", codice);
		if (esoTParametro != null && esoTParametro.size() > 0) {
			StringTokenizer st = new StringTokenizer(esoTParametro.get(0).getDsValore(), ";");
			while (st.hasMoreTokens())
				ret.add(st.nextToken());
		}
		return ret;
	}
	
	
	public String getParametroValido(String codice) {
		Date now = new Date();
		List<EsoTParametro> list = EsoTParametro.list("codParametro = ?1 and dInizioValidita<=?2 and (dFineValidita is null or dFineValidita>?3)", codice, now, now);
		if (list != null && list.size()>0) {
			return list.get(0).getDsValore();
		}
		return "";
	}
}
