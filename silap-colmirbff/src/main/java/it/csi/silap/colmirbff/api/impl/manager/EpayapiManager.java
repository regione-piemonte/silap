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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import it.csi.silap.colmirbff.api.dto.Parametro;
import it.csi.silap.colmirbff.api.dto.PosizioneDebitoria;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.response.CustomPaymentDataResutResponse;
import it.csi.silap.colmirbff.api.dto.response.CustomPaymentNoticeResponse;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.exception.ServiceException;
import it.csi.silap.colmirbff.integration.epayapi.dto.DebtPositionData;
import it.csi.silap.colmirbff.integration.epayapi.dto.DebtPositionReference;
import it.csi.silap.colmirbff.integration.epayapi.dto.Result;

@Dependent
public class EpayapiManager extends BaseApiServiceImpl {
	
	@ConfigProperty(name = "epayapi.url")
	String epayapiUri;

	@ConfigProperty(name = "epayapi.user")
	String epayapiUser;

	@ConfigProperty(name = "epayapi.password")
	String epayapiPassword;
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
	
	public PosizioneDebitoria createDebtPositio(String codFisAzienda, String ragioneSocialeAzienda,String emailAzienda, 
			String identificativoPagamento,
			BigDecimal importo,
			Long annoRiferimento,Long numRate, Long numRata) throws BusinessException {
		
		Parametro organization = getParametroByCod(SilapConstants.PARAM_COD_ORGPA);
		Parametro paymentType = getParametroByCod(SilapConstants.PARAM_COD_TYPPA);
		String url = epayapiUri + "/" + organization.getDsValore() + "/paymenttypes/" +  paymentType.getDsValore() + "/debtpositions";

		
		Parametro causalePagamento = getParametroByCod(SilapConstants.PARAM_COD_CAUPA);
		
		DebtPositionData data = new DebtPositionData();
		
		data.setCausale(causalePagamento.getDsValore());
		data.setCodiceFiscalePartitaIVAPagatore(codFisAzienda);
		data.setRagioneSociale(ragioneSocialeAzienda);
		
		data.setDataInizioValidita(new Date());
		data.setDataFineValidita(getDataAnno(SilapConstants.PARAM_COD_FIIUV, annoRiferimento.intValue()+1));
		
		if (numRate.intValue() == 1) {
			data.setDataScadenza(getDataAnno(SilapConstants.PARAM_COD_PDAT2, annoRiferimento.intValue()+1));
		}
		else {
			if (numRata.intValue() == 1) 
				data.setDataScadenza(getDataAnno(SilapConstants.PARAM_COD_PDAT1, annoRiferimento.intValue()+1));
			else data.setDataScadenza(getDataAnno(SilapConstants.PARAM_COD_PDAT2, annoRiferimento.intValue()+1));
		}
		
		data.setEmail(emailAzienda);
		
		data.setIdentificativoPagamento(identificativoPagamento);
		data.setImporto(importo);
		
		
		
		Map<String, Object> headerParams = new HashMap<String, Object>();
		headerParams.put("Content-Type", "application/json");
		
		DebtPositionReference debtPositionReference =  postServiceFullTollerance(
				url,  null, headerParams, data, DebtPositionReference.class, epayapiUser,epayapiPassword);
		
		if (debtPositionReference == null) {
			throw BusinessException.createError("Servizio " + url + " risponde con un oggetto nullo!!!");
		}
		
		
		if ("000".equals(debtPositionReference.getCodiceEsito())) {
			PosizioneDebitoria posizioneDebitoria = new PosizioneDebitoria();
			posizioneDebitoria.setCodIuv(debtPositionReference.getIuv());
			posizioneDebitoria.setCodAvviso(debtPositionReference.getCodiceAvviso());
			posizioneDebitoria.setImportoAtteso(data.getImporto());
			posizioneDebitoria.setDtInizioValidita(data.getDataInizioValidita());
			posizioneDebitoria.setDtFineValidita(data.getDataFineValidita());
			posizioneDebitoria.setDtScadenza(data.getDataScadenza());
			posizioneDebitoria.setNumRata(numRata);
			posizioneDebitoria.setDsCausale(data.getCausale());
			
			
			// TODO 
			posizioneDebitoria.setCodVersamento("0000");
			posizioneDebitoria.setCodIdPagamento(identificativoPagamento);
			return posizioneDebitoria;
		}
			
		throw BusinessException.createError(
				debtPositionReference.getCodiceEsito() + " - " + debtPositionReference.getDescrizioneEsito() + "; identificativoPagamento:" + debtPositionReference.getIdentificativoPagamento());
	}
	
	public byte[] getPaymentNotice(String iuv) throws BusinessException {

		Parametro organization = getParametroByCod(SilapConstants.PARAM_COD_ORGPA);
		Parametro paymentType = getParametroByCod(SilapConstants.PARAM_COD_TYPPN);
		String url = epayapiUri + "/" + organization.getDsValore() + "/paymenttypes/" + paymentType.getDsValore()
				+ "/debtpositions/" + iuv + "/paymentnotice";

		try {
			CustomPaymentNoticeResponse paymentNotice = getService(url, null, null, 
					CustomPaymentNoticeResponse.class, Result.class, epayapiUser,
					epayapiPassword);
			return paymentNotice.getPaymentnotice();
		} catch (ServiceException err) {
			Result result = (Result) err.getResult();
			throw BusinessException.createError(result.getCode() + " - " + result.getDescription());
		}

	}
	

	
	public PosizioneDebitoria getDebtPositionData(String iuv) throws BusinessException {
		
		Parametro organization = getParametroByCod(SilapConstants.PARAM_COD_ORGPA);
		Parametro paymentType = getParametroByCod(SilapConstants.PARAM_COD_TYPPA);
		String url = epayapiUri + "/" + organization.getDsValore() + "/paymenttypes/" + paymentType.getDsValore()
		+ "/debtpositions/" + iuv + "/paymentdata";
		
		PosizioneDebitoria posizioneDebitoria = null;
		
		try {
			
			CustomPaymentDataResutResponse positionData = getService(url, null, null, CustomPaymentDataResutResponse.class, Result.class, epayapiUser,
					epayapiPassword);
			
			posizioneDebitoria = new PosizioneDebitoria();
			posizioneDebitoria.setCodIuv(iuv);
			posizioneDebitoria.setCodIur(positionData.getIdentificativoUnicoRiscossione());
			posizioneDebitoria.setDsInfoAggiuntive(positionData.getInfoAggiuntive());
			
			posizioneDebitoria.setDtEsito(positionData.getDataEsitoPagamento());
			posizioneDebitoria.setDtOperazione(positionData.getDataEOraOperazione());
			
			
			if (positionData.getImportoPagato() != null)
				posizioneDebitoria.setImportoPagato(positionData.getImportoPagato());
			else posizioneDebitoria.setImportoPagato(positionData.getImportoPagatoTotale());
			
			posizioneDebitoria.setCodAvviso(positionData.getCodiceAvviso());
			posizioneDebitoria.setCodTransazione(positionData.getNumeroTransazione());

			
		} catch (ServiceException err) {
			Result result = (Result) err.getResult();
			throw BusinessException.createError(result.getCode() + " - " + result.getDescription());
		}	
		
		return posizioneDebitoria;
	}
	
	
	// ==========================================================================
	// PRIVATE - METHODS
	
	private Date getDataAnno(String paramName, int anno) {
		Parametro param = getParametroByCod(paramName);
		
		try {
			Date date = dateFormatter.parse(param.getDsValore());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.YEAR, anno);
			return cal.getTime();
		}
		catch (Exception err) {
			err.printStackTrace();
		}
		return null;
	}
	
	
}
