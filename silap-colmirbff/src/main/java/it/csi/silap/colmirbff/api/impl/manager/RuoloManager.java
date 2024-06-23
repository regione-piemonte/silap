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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.Ruolo;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.profilazione.ImpresaInfoc;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.integration.entity.SilapDOperatore;
import it.csi.silap.colmirbff.integration.entity.SilapDRuolo;
import it.csi.silap.colmirbff.integration.entity.SilapROperatoreRuolo;
import it.csi.silap.colmirbff.integration.entity.SilapRRuoloFunzione;

@Dependent
public class RuoloManager extends BaseApiServiceImpl {

	@ConfigProperty(name = "comonl_srv_url_rest")
	String restUri;

	@ConfigProperty(name = "comonl_srv_user_rest")
	String restUser;

	@ConfigProperty(name = "comonl_srv_password_rest")
	String restPassword;
	
	@Inject
	private AAEPManager aaepManager;
	
	private static final String COMONL_PROFILAZIONE_ID_CHIAMANTE = "SILAP.FLAG_ABILITAZIONE_SERVIZIO_PROFILAZIONE";
	
	private static final String RUOLO_CONSULENTE_TIPO = "C";
	private static final String RUOLO_PERSONA_AUTORIZZATA_TIPO = "E";
	private static final String RUOLO_TUTOR_TIPO = "T";
	private static final String RUOLO_DELEGATO_TIPO = "D";
	
	
	public Ruolo getRuoli(Long idRuolo) {
		return mappers.RUOLO.toModel(SilapDRuolo.findById(idRuolo));
	}

	public List<Ruolo> getRuoli(String codiceFiscale) {
		
		
		List<Ruolo> ruoliApp = mappers.RUOLO.toModels(SilapDRuolo.listAll());
		List<Ruolo> returnRuoli = new ArrayList<Ruolo>();
		
		
		// RECUPERO RUOLI DATABASE LOCALE
		List<SilapDOperatore> list = SilapDOperatore.list("codFiscale", codiceFiscale);
		if (list != null && list.size() > 0) {
			SilapDOperatore operatore = list.get(0);

			List<SilapDRuolo> silwebRuoli = new ArrayList<SilapDRuolo>();
			for (SilapROperatoreRuolo operatoreRuolo : operatore.getSilapROperatoreRuolos()) {
				silwebRuoli.add(operatoreRuolo.getSilapDRuolo());
			}

			List<Ruolo> ruoli = mappers.RUOLO.toModels(silwebRuoli);
			for (Ruolo ruolo : ruoli) {
				if (addRuolo(returnRuoli, ruolo))
					returnRuoli.add(ruolo);
			}
		}
		printRuoliDebug("RECUPERO RUOLI DATABASE LOCALE", returnRuoli);
		


		// RECUPERO RUOLI COMONL_SRV
		List<it.csi.silap.colmirbff.integration.comonl.dto.Ruolo> res = getService(
				restUri + "/ruoli/" + COMONL_PROFILAZIONE_ID_CHIAMANTE + "/" + codiceFiscale, null, null,
				new GenericType<List<it.csi.silap.colmirbff.integration.comonl.dto.Ruolo>>() {
				}, restUser, restPassword);
		printRuoliComonlDebug("RECUPERO RUOLI COMONL_SRV servizio", res);
		

		if(res != null && res.size() > 0) {
			for(it.csi.silap.colmirbff.integration.comonl.dto.Ruolo r: res) {
				if(!RUOLO_TUTOR_TIPO.equals(r.getTipoAnagrafica())) {
					Ruolo ruolo = getRuolo(ruoliApp, r.getTipoAnagrafica(), null);
					if (ruolo != null) {
						ruolo.setDenominazioneSoggettoAbilitato(r.getDenominazioneAzienda());
						ruolo.setCodiceFiscaleSoggettoAbilitato(r.getCodiceFiscaleAzienda());
						if(r.isConsulenteRespo()) {
							ruolo.setCodiceFiscaleSoggettoAbilitato(r.getCodiceFiscaleStudioProfessionale());
							ruolo.setDenominazioneSoggettoAbilitato(r.getDescrizioneStudioProfessionale());
						}
						if(ruolo.getCodiceFiscaleSoggettoAbilitato() != null) {
							if (addRuolo(returnRuoli, ruolo))
								returnRuoli.add(ruolo);
						}
					}
				}
			}
		}
		printRuoliDebug("RECUPERO RUOLI COMONL_SRV AGGIUNTI", returnRuoli);
		
		
		// RECUPERO RUOLI AAEP
		try {
			List<ImpresaInfoc> listImpresaInfoc =  aaepManager.cercaElencoAziende(codiceFiscale);
			if (listImpresaInfoc != null) {
				for (ImpresaInfoc impresaInfoc : listImpresaInfoc) {
					Ruolo ruolo = getRuolo(ruoliApp, null, SilapConstants.RUOLO_LEGALE_RAPPRESENTANTE);
					if (ruolo != null) {
						ruolo.setDenominazioneSoggettoAbilitato(impresaInfoc.getRagioneSociale());
						ruolo.setCodiceFiscaleSoggettoAbilitato(impresaInfoc.getCodiceFiscale());
						if (addRuolo(returnRuoli, ruolo))
							returnRuoli.add(ruolo);
					}
				}
			}			
		} catch (Throwable e) {
			Log.error("RuoloManager - getRuoli errore in AAEPManager", e);
		}
		printRuoliDebug("RECUPERO RUOLI AAEP", returnRuoli);
		
		Collections.sort(returnRuoli, new Comparator<Ruolo>() {
			@Override
            public int compare(Ruolo o1, Ruolo o2) {
				int i = o1.getDsSilapDRuolo().compareTo(o2.getDsSilapDRuolo());
                if (i == 0)
                    i = o1.getCodiceFiscaleSoggettoAbilitato().compareTo(o2.getCodiceFiscaleSoggettoAbilitato());
                return i;
            }
		});
		
		return returnRuoli;
	}
	
	private Ruolo getRuolo(List<Ruolo> ruoliApp, String tipoAnagrafica, Long idRuolo) {
		
		Long id = null;
		
		if (idRuolo != null)
			id = idRuolo;
		else if (tipoAnagrafica != null) {
			if (RUOLO_CONSULENTE_TIPO.equals(tipoAnagrafica))
				id = 3l;
			else if (RUOLO_PERSONA_AUTORIZZATA_TIPO.equals(tipoAnagrafica))
				id = 5l;
			else if (RUOLO_DELEGATO_TIPO.equals(tipoAnagrafica))
				id = 4l;
		}
		
		if (id != null) {
			for (Ruolo r : ruoliApp) {
				if (r.getIdSilapDRuolo().longValue() == id.longValue()) {
					Ruolo retRuolo = new Ruolo(r);
					List<SilapRRuoloFunzione> listFunzioni = SilapRRuoloFunzione.list("silapDRuolo.idSilapDRuolo", id);
					if (listFunzioni != null)
						retRuolo.setRuoloFunzioni(mappers.RUOLO_FUNZIONE.toModels(listFunzioni));
					return retRuolo;
				}
			}
		}

		return null;
	}
	
	
	@SuppressWarnings("unused")
	private void printRuoliDebug(String titolo, List<Ruolo> ruoli) {
		Log.info(titolo);
		for (Ruolo ruolo : ruoli)
			Log.info(ruolo);
		Log.info("==============================");
	}
	
	@SuppressWarnings("unused")
	private void printRuoliComonlDebug(String titolo, List<it.csi.silap.colmirbff.integration.comonl.dto.Ruolo> ruoli) {
		Log.info(titolo);
		for (it.csi.silap.colmirbff.integration.comonl.dto.Ruolo ruolo : ruoli)
			Log.info(ruolo);
		Log.info("==============================");
	}
	
	
	
	
	private boolean addRuolo(List<Ruolo> ruoli, Ruolo addRuolo) {
		if (ruoli != null) {
			for (Ruolo ruolo : ruoli) {
				if (ruolo.getIdSilapDRuolo().longValue() == addRuolo.getIdSilapDRuolo().longValue() &&
						ruolo.getCodiceFiscaleSoggettoAbilitato().equalsIgnoreCase(addRuolo.getCodiceFiscaleSoggettoAbilitato()))
					return false;
			}
		}
		return true;
	}

}
