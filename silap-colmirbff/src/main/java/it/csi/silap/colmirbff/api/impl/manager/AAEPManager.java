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

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.wrapper.UserException;
import it.csi.servizioaaep2.interfaces.AAEPInterface2;
import it.csi.servizioaaep2.vo.AziendaAAEP;
import it.csi.servizioaaep2.vo.Carica;
import it.csi.servizioaaep2.vo.ImpresaInfoc;
import it.csi.servizioaaep2.vo.ListaPersona;
import it.csi.servizioaaep2.vo.ListaPersonaCaricaInfoc;
import it.csi.servizioaaep2.vo.ListaSediInfoc;
import it.csi.servizioaaep2.vo.Persona;
import it.csi.servizioaaep2.vo.SedeInfoc;
import it.csi.silap.colmirbff.util.pdpa.PDConfigReaderQuarkus;
import it.csi.util.performance.StopWatch;

@Dependent
public class AAEPManager {


	@ConfigProperty(name = "app.component.name")
	String componentName;
	
	@ConfigProperty(name = "aaep.url")
	String aaepUrl;

	public static final String FONTE_DATO_INFOCAMERE = "3";

	private static final String PD_SERVICE = "/wsdl/defpd_aaep_soap.xml";

	private static AAEPInterface2 service;
	
	private static SimpleDateFormat myFormat = null;

	public AAEPManager() {
		myFormat = (SimpleDateFormat) DateFormat.getDateInstance();
		myFormat.applyPattern("dd/MM/yyyy");
	}

	/**
	 * Restituisce l'interfaccia della Porta Delegata SOAP
	 */
	private AAEPInterface2 getService() throws Exception {
		if (service == null) {
			InputStream pd = this.getClass().getResourceAsStream(PD_SERVICE);
			InfoPortaDelegata info = PDConfigReaderQuarkus.read(pd);
			info.getPlugins()[0].setUrlPortaApplicativa(aaepUrl);
			service = (AAEPInterface2) PDProxy.newInstance(info);
		}
		return service;
	}

	public List<it.csi.silap.colmirbff.api.dto.profilazione.ImpresaInfoc> cercaElencoAziende(String codiceFiscale)
			throws Exception {
		Log.debug("[AdapterAAEP::cercaElencoAziende] BEGIN codiceFiscale=" + codiceFiscale);

		StopWatch watcher = new StopWatch(componentName);
		watcher.start();

		try {

			Date today = new Date();
			Map<ImpresaInfoc, List<SedeInfoc>> elencoAziendeRis = null;
			ListaPersonaCaricaInfoc[] elencoPersonaCaricaInfoc = null;

			try {
				elencoPersonaCaricaInfoc = getService().cercaPerFiltriCodFiscFonte(codiceFiscale,
						FONTE_DATO_INFOCAMERE);
			} catch (UserException e) {
				//Log.error("UserException---->>>> [AdapterAAEP::cercaElencoAziende] " + e.getMessage(), e);
				
				Log.warn("UserException---->>>> [AdapterAAEP::cercaElencoAziende] " + e.getMessage());
			} catch (Exception e) {
				Log.error("Exception---->>>> [AdapterAAEP::cercaElencoAziende] ", e);
				throw new Exception(e);
			}

			if (elencoPersonaCaricaInfoc != null && elencoPersonaCaricaInfoc.length > 0) {
				elencoAziendeRis = new HashMap<>();

				for (ListaPersonaCaricaInfoc personaCaricaInfoc : elencoPersonaCaricaInfoc) {
					try {
						ImpresaInfoc aziendaInfoc = getService().cercaPerCodiceFiscaleINFOC(
								personaCaricaInfoc.getCodFiscaleAzienda(), FONTE_DATO_INFOCAMERE, "", "", "");
	
						GregorianCalendar dataCessaz = stringToDate(aziendaInfoc.getDataCessaz());
						GregorianCalendar dataDenunciaCessaz = stringToDate(aziendaInfoc.getDataDenunciaCessaz());

						if ((dataCessaz == null || dataCessaz.after(today))
								&& (dataDenunciaCessaz == null || dataDenunciaCessaz.after(today))
								&& (aziendaInfoc.getCodCausCessaz() == null)) {
							boolean found = false;
							// ListaSediInfoc[] listaSedi = aziendaInfoc.getListaSediInfoc();
							List<SedeInfoc> elencoSediInfoc = new ArrayList<>();
							elencoSediInfoc.add(null); // spazio sede legale

							ListaPersona listaPersona = getService().cercaPerCodiceFiscalePersConCaricaFonteInfoc(
									personaCaricaInfoc.getCodFiscaleAzienda(), "N", FONTE_DATO_INFOCAMERE);

							Persona[] pp = listaPersona.getListaPersone();

							for (int k = 0; k < pp.length; k++) {
								
								if (codiceFiscale != null && codiceFiscale.equalsIgnoreCase(pp[k].getCodiceFiscale())) {
									Log.debug("[AdapterAAEP::cercaElencoAziende] trovata carica per azienda CF = "
											+ personaCaricaInfoc.getCodFiscaleAzienda() + " - Carica: "
											+ personaCaricaInfoc.getDescrCarica() + " ("
											+ personaCaricaInfoc.getCodiceCarica() + ")");
									found = true;

									// modifica per passare anche l'informazione se l'operatore e' assimilabile ad
									// un legale rappresentante per l'azienda oppure no.
									// In tal caso sara' assimilato al ruolo di "persona con carica aziendale"
									Carica[] listaCariche = pp[k].getListaCariche();
									for (int a = 0; a < listaCariche.length; a++) {
										if (listaCariche[a].getFlagRappresentanteLegale() != null
												&& listaCariche[a].getFlagRappresentanteLegale().equals("S")) {
											// uso di questo campo perche' inutilizzato attualmente
											aziendaInfoc.setDescrFonte("LR");
											break;
										}
									}
								}
							}

							if (found) {
								elencoAziendeRis.put(aziendaInfoc, elencoSediInfoc);
							}
						}

					} catch (Exception e) {
						Log.error("Exception----->  AdapterAAEP::cercaElencoAziende", e);
					}
				}
			}

			if (elencoAziendeRis == null)
				return null;

			List<it.csi.silap.colmirbff.api.dto.profilazione.ImpresaInfoc> result = new ArrayList<>();
			for (ImpresaInfoc key : elencoAziendeRis.keySet()) {
				result.add(converti(key));
			}
			return result;
		} finally {
			watcher.dumpElapsed("AAEPManager", "cercaElencoAziende()",
					"invocazione servizi AAEP  AAEPManager.cercaElencoAziende",
					"");
			watcher.stop();
		}
	}

	/**
	 * Converte i dati restituiti dal servizio di AAEP in DTO di prodis.
	 */
	private it.csi.silap.colmirbff.api.dto.profilazione.ImpresaInfoc converti(ImpresaInfoc iWS) {
		it.csi.silap.colmirbff.api.dto.profilazione.ImpresaInfoc i = new it.csi.silap.colmirbff.api.dto.profilazione.ImpresaInfoc();
		i.setAnnoDenunciaAddetti(iWS.getAnnoDenunciaAddetti());
		i.setCodCausCessaz(iWS.getCodCausCessaz());
		i.setCodCausCessazFunSede(iWS.getCodCausCessazFunSede());
		i.setCodFonte(iWS.getCodFonte());
		i.setCodiceFiscale(iWS.getCodiceFiscale());
		i.setCodNaturaGiuridica(iWS.getCodNaturaGiuridica());
		i.setDataCancellazRea(iWS.getDataCancellazRea());
		i.setDataCessaz(iWS.getDataCessaz());
		i.setDataCessazFunSede(iWS.getDataCessazFunSede());
		i.setDataDenunciaCessaz(iWS.getDataDenunciaCessaz());
		i.setDataDlbIscrAlboArtigiano(iWS.getDataDlbIscrAlboArtigiano());
		i.setDataInizioAtt(iWS.getDataInizioAtt());
		i.setDataIscrizioneRea(iWS.getDataIscrizioneRea());
		i.setDataIscrRegistroImpr(iWS.getDataIscrRegistroImpr());
		i.setDataUltimoAggiorn(iWS.getDataUltimoAggiorn());
		i.setDataUltimoAggRi(iWS.getDataUltimoAggRi());
		i.setDescrCausCessaz(iWS.getDescrCausCessaz());
		i.setDescrFonte(iWS.getDescrFonte());
		i.setDescrIndicStatoAttiv(iWS.getDescrIndicStatoAttiv());
		i.setDescrIndicTrasfSede(iWS.getDescrIndicTrasfSede());
		i.setDescrIterIscrAlboArt(iWS.getDescrIterIscrAlboArt());
		i.setDescrNaturaGiuridica(iWS.getDescrNaturaGiuridica());
		i.setFlgAggiornamento(iWS.getFlgAggiornamento());
		i.setFlgIterIscrAlboArt(iWS.getFlgIterIscrAlboArt());
		i.setIdAAEPAziendaSL(iWS.getIdAAEPAziendaSL());
		i.setIdAAEPFonteDatoSL(iWS.getIdAAEPFonteDatoSL());
		i.setImpresaCessata(iWS.getImpresaCessata());
		i.setIndicStatoAttiv(iWS.getIndicStatoAttiv());
		i.setIndicTrasfSede(iWS.getIndicTrasfSede());
		if (iWS.getListaSediInfoc() != null) {
			List<it.csi.silap.colmirbff.api.dto.profilazione.ListaSediInfoc> listaSediInfoc = new ArrayList<>();
			for (ListaSediInfoc lWS : iWS.getListaSediInfoc()) {
				listaSediInfoc.add(converti(lWS));
			}
			i.setListaSediInfoc(listaSediInfoc);
		}
		i.setLocalizzazPiemonte(iWS.getLocalizzazPiemonte());
		i.setNumAddettiFam(iWS.getNumAddettiFam());
		i.setNumAddettiSubord(iWS.getNumAddettiSubord());
		i.setNumeroIscrAlboArtigiano(iWS.getNumeroIscrAlboArtigiano());
		i.setNumIscrizRea(iWS.getNumIscrizRea());
		i.setNumIscrizReaSede(iWS.getNumIscrizReaSede());
		i.setNumRegistroImpr(iWS.getNumRegistroImpr());
		i.setPartitaIva(iWS.getPartitaIva());
		i.setProgrSedeSL(iWS.getProgrSedeSL());
		i.setProvinciaIscrAlboArtigiano(iWS.getProvinciaIscrAlboArtigiano());
		i.setRagioneSociale(iWS.getRagioneSociale());
		i.setSiglaProvRea(iWS.getSiglaProvRea());
		i.setSiglaProvReaSede(iWS.getSiglaProvReaSede());
		return i;
	}

	/**
	 * Converte i dati restituiti dal servizio di AAEP in DTO di prodis.
	 */
	private it.csi.silap.colmirbff.api.dto.profilazione.ListaSediInfoc converti(ListaSediInfoc lWS) {
		it.csi.silap.colmirbff.api.dto.profilazione.ListaSediInfoc l = new it.csi.silap.colmirbff.api.dto.profilazione.ListaSediInfoc();
		l.setDenominazioneSede(lWS.getDenominazioneSede());
		l.setDescrComuneUL(lWS.getDescrComuneUL());
		l.setDescrTipoLocalizzazione(lWS.getDescrTipoLocalizzazione());
		l.setIdAAEPAzienda(lWS.getIdAAEPAzienda());
		l.setIdAAEPFonteDato(lWS.getIdAAEPFonteDato());
		l.setIndirizzo(lWS.getIndirizzo());
		l.setNumIscrizREA(lWS.getNumIscrizREA());
		l.setProgrSede(lWS.getProgrSede());
		l.setSiglaProvCCIAA(lWS.getSiglaProvCCIAA());
		l.setSiglaProvUL(lWS.getSiglaProvUL());
		return l;
	}

	public AziendaAAEP cercaPerCodiceFiscale(String codiceFiscale) throws Exception {
		Log.debug("[AAEPManager::cercaPerCodiceFiscale] BEGIN codiceFiscale=" + codiceFiscale);

		StopWatch watcher = new StopWatch(componentName);
		watcher.start();

		try {

			AziendaAAEP azienda = getService().cercaPerCodiceFiscaleAAEP(codiceFiscale, "", "");
			return azienda;
		} finally {
			watcher.dumpElapsed("AAEPManager", "cercaPerCodiceFiscale", "invocazione servizio AAEP", "");
			watcher.stop();
		}
	}
	
	
	public GregorianCalendar stringToDate(String inputString) {
		if (inputString == null)
			return null;
		if (inputString.length() != 10)
			return null;
		if (inputString.charAt(2) != '/' || inputString.charAt(5) != '/')
			return null;
		String giorno = inputString.substring(0, 2);
		String mese = inputString.substring(3, 5);
		String anno = inputString.substring(6, 10);
		int dd, mm, yy;
		try {
			dd = Integer.parseInt(giorno);
			mm = Integer.parseInt(mese);
			yy = Integer.parseInt(anno);
		} catch (NumberFormatException e) {
			return null;
		}
		if (mm < 1 || mm > 12)
			return null;
		if (dd < 1 || dd > 31)
			return null;
		if (yy < 1800 || yy > 2200)
			return null;
		if ((mm == 4 || mm == 6 || mm == 9 || mm == 11) && dd > 30)
			return null;
		int giorniFeb;
		if ((yy % 4) == 0) {
			giorniFeb = 29;
			for (int j = 1; j < 4; j++) {
				if (yy == 1600 + 100 * j)
					giorniFeb = 28;
			}
			for (int j = 5; j < 8; j++) {
				if (yy == 1600 + 100 * j)
					giorniFeb = 28;
			}
		} else
			giorniFeb = 28;
		if (mm == 2 && dd > giorniFeb)
			return null;
		GregorianCalendar ret = (GregorianCalendar) GregorianCalendar.getInstance();
		try {
			ret.setTime(myFormat.parse(inputString));
		} catch (Exception e) {
			ret = null;
		}
		return ret;
	}

}
