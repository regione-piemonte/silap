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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.ibm.icu.util.Calendar;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.Azienda;
import it.csi.silap.colmirbff.api.dto.DatiServiziProdisSilp;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.VersamentoProspetto;
import it.csi.silap.colmirbff.api.dto.VersamentoProvincia;
import it.csi.silap.colmirbff.api.dto.VersamentoPvEsonero;
import it.csi.silap.colmirbff.api.dto.VersamentoPvPeriodo;
import it.csi.silap.colmirbff.api.dto.VersamentoPvProspetto;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.exception.ServiceException;
import it.csi.silap.colmirbff.integration.prodis.dto.ApiError;
import it.csi.silap.colmirbff.integration.prodis.dto.FilterServiziProdis;
import it.csi.silap.colmirbff.integration.prodis.dto.Prospetto;
import it.csi.silap.colmirbff.integration.prodis.dto.ProspettoProvincia;
import it.csi.silap.colmirbff.integration.prodis.dto.TestataProspetto;
import it.csi.silap.colmirbff.util.CommonUtils;


@Dependent
public class ProdisManager extends BaseApiServiceImpl {

	@ConfigProperty(name = "prodis.url")
	String prodisUrl;

	@ConfigProperty(name = "prodis.user")
	String prodisUser;

	@ConfigProperty(name = "prodis.password")
	String prodisPassword;

	@Inject
	private SilprestManager silprestManager;

	public DatiServiziProdisSilp getDatiProdis(Azienda azienda, String codProvinciaMin, String annoRif) {

		DatiServiziProdisSilp risposta = new DatiServiziProdisSilp();
		risposta.setAzienda(azienda);
		risposta.setMessaggi(new ArrayList<ApiMessage>());

		FilterServiziProdis body = new FilterServiziProdis();
		List<String> elencoCF = new ArrayList<String>();
		elencoCF.add(azienda.getCodFiscale());
		body.setListCodiceFiscaleAzienda(elencoCF);
		body.setCodProvinciaMin(codProvinciaMin);
		body.setCaller(SilapConstants.PRODIS_CALLER);


		/*
		 * 13 Il sistema chiama il servizio seguente per recuperare elenco dei prospetti informativi disabili findByFilterTestataProspetto (Prodis). 
		 *  Input: IdChiamante, CodiceFiscaleAzienda (recuperato al passo 3) 
		 * questo passo genera una variante:
		 * a.	Non esiste un prospetto informativo su PRODIS    msg bloccante 100013
		 * b.	Il servizio ricerca dei prospetti disabili ritorna un errore di sistema
		 * c.	Il servizio di ricerca dei prospetti disabili ritorna un errore sui dati
		 */
		try {

			List<TestataProspetto> elencoProspetti = postService(prodisUrl + "/findByFilterTestataProspetto", null,
					null, body, new GenericType<List<TestataProspetto>>() {
			}, prodisUser, prodisPassword, new GenericType<List<ApiError>>() {
			});

			if (CommonUtils.isNotVoid(elencoProspetti)) {
				int annoRiferimento = Integer.valueOf(annoRif).intValue();
				/**
				 * cerca tra i prospetti Prodis con stato 3, che sono ordinati per data decrescente il primo che abbia dataRiferimento
				 * relativa all'anno riferimento indicato 
				 *     se lo trova cerca anche il prospetto relativo all'anno riferimento meno uno
				 * 
				 * se non trova per anno riferimento restituisce il primo indipendentemente
				 * dalla data riferimento
				 */

				risposta.setProspetti(new ArrayList<VersamentoProspetto>());
				// cerco il primo con data uguale a anno riferimento
				VersamentoProspetto prospettoRif = getProspettoAnnoRiferimento(elencoProspetti, annoRiferimento);
				if (prospettoRif != null) {
					risposta.getProspetti().add(prospettoRif);
					// cerco il primo con data uguale a anno riferimento meno 1
					VersamentoProspetto prospettoPrec = getProspettoAnnoRiferimento(elencoProspetti,
							annoRiferimento - 1);
					if (prospettoPrec != null) {
						risposta.getProspetti().add(prospettoPrec);
					}
				} else {
					// cerco il primo indipendentemente da anno riferimento
					prospettoRif = getProspettoAnnoRiferimento(elencoProspetti, 0);
					risposta.getProspetti().add(prospettoRif);
				}

				risposta = ricercaEsoneri( annoRif, codProvinciaMin, risposta);
				if (isEsitoPositivo(risposta.getMessaggi())) {
					ricercaConvenzioni( annoRif, risposta);
				}

			} else {
				// 13 a.	Non esiste un prospetto informativo su PRODIS  -  msg bloccante 100013

				String demonAzioenda = azienda.getDenomAzienda();
				if (demonAzioenda == null) demonAzioenda = "";
				risposta.getMessaggi().add( new ApiMessage(getMsg(SilapConstants.MSG_ERRORE__PROSPETTO_NON_TROVATO, demonAzioenda, azienda.getCodFiscale())) );
			}
		} catch (ServiceException ex) {
			// 13 b. Il servizio ricerca dei prospetti disabili ritorna un errore di sistema
			// 13 c. Il servizio di ricerca dei prospetti disabili ritorna un errore sui
			// dati
			@SuppressWarnings("unchecked")
			List<ApiError> listaErr = (List<ApiError>) ex.getResult();
			for (ApiError apiError : listaErr) {
				switch (apiError.getCode()) {
				case "ANA0001":
					risposta.getMessaggi()
							.add(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE__PROSPETTO_NON_TROVATO, azienda.getDenomAzienda(), azienda.getCodFiscale())));
					break;
				case "CR00001":
					risposta.getMessaggi()
							.add(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_DATI_PROSPETTO, azienda.getCodFiscale(), annoRif)));
					break;
				case "CR00002":
					risposta.getMessaggi()
							.add(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_DATI_PROSPETTO, azienda.getCodFiscale(), annoRif)));
					break;
				case "SEC00001":
					risposta.getMessaggi()
							.add(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_SISTEMA_PROSPETTO, azienda.getCodFiscale(), annoRif)));
					break;
				case "DBA00001":
					risposta.getMessaggi()
							.add(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_SISTEMA_PROSPETTO, azienda.getCodFiscale(), annoRif)));
					break;
				}
			}
			//risposta = addProdisErrorToRisposta(risposta, listaErr);

		}

		return risposta;

	}

	private DatiServiziProdisSilp ricercaConvenzioni(  String annoRif, DatiServiziProdisSilp risposta) {
		/*
		 * Il sistema chiama il servizio seguente per recuperare i dati delle eventuali convenzioni di un'azienda in base ai criteri impostati (vedi 5.3  - Dati delle eventuali convenzioni). cercaConvenzione (SILP). 
		 * Input: idChiamante, codfiscale (recuperato al passo 3). Questo passo genera delle varianti:
		 * a.	Non sono presenti delle convenzioni
		 * b.	Errore nel calcolo delle posizioni aperte
		 * c.	Il servizio di ricerca delle convenzioni ritorna un errore di sistema
		 * d.	Il servizio di ricerca delle convenzioni ritorna un errore sui dati

		 */

		DatiServiziProdisSilp convResp = silprestManager.findConvenzioni(null, risposta.getAzienda().getCodFiscale(), null, annoRif);

		if (CommonUtils.isNotVoid(  convResp.getProvinceConvenzioni() ) ) {
			risposta.setProvinceConvenzioni( convResp.getProvinceConvenzioni() );
		}
		if (CommonUtils.isNotVoid(convResp.getMessaggi())) {
			risposta.getMessaggi().addAll(convResp.getMessaggi());
		}
		return risposta;
	}

	private DatiServiziProdisSilp ricercaEsoneri( String annoRif, String codProvinciaMin, DatiServiziProdisSilp risposta) {
		/*
		 * 16 Il sistema chiama il servizio per recuperare l'elenco degli esoneri dell'azienda, per l'anno di riferimento (vedi 5.3  - Dati dell'esonero). 
		 * Il servizio cercaEsonero (SILP). Input: idChiamante, codice fiscale (recuperato al passo 3), anno di riferimento. Questo passo genera delle varianti: 
		 *  a.	Non esiste una richiesta d'esonero per anno di riferimento (vedi 5.2)
		 *  b.	Esistono esoneri incongruenti (vedi 5.1)
		 *  c.	Il servizio di ricerca degli esoneri ritorna un errore di sistema
		 *  d.	Il servizio di ricerca degli esoneri ritorna un errore sui dati
		 *  
		 *  ATTENZIONE il punto 17 è gestito direttamente nel servizio di silprest: dove per ciascun esonero individuato determina
		 *   l’ultima comunicazione d’esonero (data comunicazione maggiore ed a parità di data ordinata per identificativo comunicazione) 
		 *   ed estrae il tipo comunicazione, la data classificazione ed il numero di classificazione. 
		 *   Il sistema determina l’ultima comunicazione d’esonero (data comunicazione maggiore ed a parità di data ordinata per identificativo comunicazione) ed estrae il tipo comunicazione, 
		 *   la data classificazione ed il numero di classificazione. 
		 */

		DatiServiziProdisSilp esonResp = silprestManager.findEsoneri(null, risposta.getAzienda().getCodFiscale(), null, annoRif, codProvinciaMin);
		if (CommonUtils.isNotVoid(  esonResp.getProvinceEsoneri() ) ) {
			risposta.setProvinceEsoneri( esonResp.getProvinceEsoneri() );
			if (CommonUtils.isNotVoid(esonResp.getMessaggi())) {
				risposta.getMessaggi().addAll(esonResp.getMessaggi());
			} else {
				// 16.b verifica congruenza dati
				risposta = congruenzaEsoneri(risposta);
			}
		} else {
			// 16.a Non esiste una richiesta d’esonero per anno di riferimento
			// Il sistema mostra un messaggio di errore bloccante che comunica che non è possibile procedere in quanto non presente un esonero parziale richiesto per l’anno di riferimento. [100028]
			
			String demonAzioenda = risposta.getAzienda().getDenomAzienda();
			if (demonAzioenda == null) demonAzioenda = "";
			risposta.getMessaggi().add( new ApiMessage(getMsg("100028", demonAzioenda, risposta.getAzienda().getCodFiscale(), annoRif)) );
		}
		return risposta;
	}
	
	
	public VersamentoProvincia calcoloPeriodioPerProvincia(VersamentoProvincia versProv) {
		if (versProv != null && versProv.getEsoTVersamentoPvEsoneros() != null) {
			Log.info("[ProdisManager::calcoloPeriodioPerProvincia]" +  versProv);
						
			Calendar cal = Calendar.getInstance();
			Long idEsonero = 0l;
			for (Iterator<VersamentoPvEsonero> iterPvEson = versProv.getEsoTVersamentoPvEsoneros().iterator(); iterPvEson.hasNext();) {
				VersamentoPvEsonero eson = (VersamentoPvEsonero) iterPvEson.next();
				++idEsonero;
				if (CommonUtils.isNotVoid(eson.getDRichiesta())) {
					if (CommonUtils.isNotVoid(eson.getDDiniego())) {
						// presente data diniego
						cal.setTime(eson.getDDiniego());
						cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-1);
						versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDRichiesta(),cal.getTime(),eson.getPercRichiesta(),idEsonero));
						
					} else if (CommonUtils.isVoid(eson.getDConcessione())) {
						// non c'e' data concessione
						versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDRichiesta(),eson.getDScadenza(), eson.getPercRichiesta(),idEsonero));
					} else if (eson.getPercRichiesta().compareTo(eson.getPercConcessione()) == 0) {
						// se percentuale richiesta = percentuale concessione
						versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDRichiesta(),eson.getDScadenza(), eson.getPercRichiesta(),idEsonero));
					} else {
						// percentuale richiesta diversa da percentuale concessione   creo due periodi
						//     uno con inizio data richiesta, fine data concessione meno uno e percentuale richiesta
						
						
						if (eson.getDScadenza().compareTo(eson.getDConcessione()) == 0 ||
								eson.getDScadenza().after(eson.getDConcessione())) {
						
							versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(
									eson.getDRichiesta(),
									menoUnGiorno(eson.getDConcessione()), 
									eson.getPercRichiesta(),idEsonero));
							
							//     uno con inizio data concessione, fine data scadenza e percentuale concessione
							versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(
									eson.getDConcessione(), 
									eson.getDScadenza() , 
									eson.getPercConcessione(),idEsonero));
						}
						else {
							versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(
									eson.getDRichiesta(), 
									eson.getDScadenza() , 
									eson.getPercRichiesta(),idEsonero));
						}
					}
				} 
			}
		}
		return versProv;
	}

	/**
	 * 
	 * @param risposta
	 * @param annoRif
	 * @return
	 */
	private DatiServiziProdisSilp congruenzaEsoneri(DatiServiziProdisSilp risposta) {

		/*
		 * costruisco i periodi con il seguente criterio
		 * 
		 *  
		 * |---se------------------------------|---data inizio----|---data fine-----------|----quota---------------|
		 * |-presente data diniego ------------|-- richiesta -----|-- diniego ------------|--% richiesta-----------|  
		 * |-----------------------------------|------------------|-----------------------|------------------------| 
		 * |-non presente data concessione-----|-- richiesta -----|-- scadenza -----------|--% richiesta-----------|
		 * |-----------------------------------|------------------|-----------------------|------------------------|   
		 * |-% richiesta = % concessione-------|-- richiesta -----|-- scadenza------------|--% richiesta-----------|
		 * |-----------------------------------|------------------|-----------------------|------------------------|   
		 * |-% richiesta diversa % concessione-|-- richiesta -----|-- concessione - 1g ---|--% richiesta-----------|
		 * |-----------------------------------|-- concessione ---|-- scadenza -----------|--% concessione --------|   
		 * |-----------------------------------|------------------|-----------------------|------------------------| 
		 */
		boolean incongruente = false;
		for (Iterator<VersamentoProvincia> iterProv = risposta.getProvinceEsoneri().iterator(); iterProv.hasNext() && !incongruente;) {
			VersamentoProvincia versProv  = (VersamentoProvincia) iterProv.next();
			versProv.setEsoTVersamentoPvPeriodos(new ArrayList<VersamentoPvPeriodo>());
			Long idEsonero = 0L;
			for (Iterator<VersamentoPvEsonero> iterPvEson = versProv.getEsoTVersamentoPvEsoneros().iterator(); iterPvEson.hasNext();) {
				VersamentoPvEsonero eson = (VersamentoPvEsonero) iterPvEson.next();
				++idEsonero;
				if (CommonUtils.isNotVoid(eson.getDRichiesta())) {
					if (CommonUtils.isNotVoid(eson.getDDiniego())) {
						// presente data diniego
						versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDRichiesta(),eson.getDDiniego(),eson.getPercRichiesta(),idEsonero));
					} else if (CommonUtils.isVoid(eson.getDConcessione())) {
						// non c'e' data concessione
						versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDRichiesta(),eson.getDScadenza(), eson.getPercRichiesta(),idEsonero));
					} else if (eson.getPercRichiesta().compareTo(eson.getPercConcessione()) == 0) {
						// se percentuale richiesta = percentuale concessione
						versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDRichiesta(),eson.getDScadenza(), eson.getPercRichiesta(),idEsonero));
					} else {
						// percentuale richiesta diversa da percentuale concessione   creo due periodi
						//     uno con inizio data richiesta, fine data concessione meno uno e percentuale richiesta
						versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDRichiesta(),menoUnGiorno(eson.getDConcessione()), eson.getPercRichiesta(),idEsonero));
						//     uno con inizio data concessione, fine data scadenza e percentuale concessione
						if(eson.getDConcessione().compareTo(eson.getDScadenza()) <= 0) {
							versProv.getEsoTVersamentoPvPeriodos().add(creaPvPeriodo(eson.getDConcessione(), eson.getDScadenza() , eson.getPercConcessione(),idEsonero));
						}
							
					}
				} else {
					// errore nei dati manca la data richiesta	- dati incongruenti

					incongruente = true;
					break;
				}
			}
			if (!incongruente && CommonUtils.isNotVoid(versProv.getEsoTVersamentoPvPeriodos()) && versProv.getEsoTVersamentoPvPeriodos().size() > 1) {
				// controllo che i periodi non siano sovrapposti

				for (int iTest = 0; iTest < versProv.getEsoTVersamentoPvPeriodos().size() && !incongruente; iTest++) {
					VersamentoPvPeriodo periodoTest = versProv.getEsoTVersamentoPvPeriodos().get(iTest);
					for(int i = 0; i < versProv.getEsoTVersamentoPvPeriodos().size() && !incongruente; i++) {
						VersamentoPvPeriodo periodo = versProv.getEsoTVersamentoPvPeriodos().get(i);
						if (i != iTest && !periodoTest.getIdEsonero().equals(periodo.getIdEsonero())) {
							if (CommonUtils.isDataCompresa(periodoTest.getDInizio(), periodo.getDInizio(),periodo.getDFine()) 
									|| CommonUtils.isDataCompresa(periodoTest.getDFine(), periodo.getDInizio(),periodo.getDFine()))  {
								// data inizio o data fine sono comprese in un altro periodo

								incongruente = true;

							}
						}
					}
				}
			}
			if (incongruente) {
				// Il sistema mostra un messaggio di errore bloccante che comunica che non è possibile procedere in quanto presenti piu' richieste di esonero incongruenti. [100012]
				
				String demonAzioenda = risposta.getAzienda().getDenomAzienda();
				if (demonAzioenda == null) demonAzioenda = "";
				risposta.getMessaggi().add( new ApiMessage(getMsg("100012", demonAzioenda, risposta.getAzienda().getCodFiscale())) );
			}

		}
		return risposta;
	}

	private Date menoUnGiorno(Date input) {
		return CommonUtils.aggiungiGiorniAData(input, -1); // sottraggo un giorno
	}



	private VersamentoPvPeriodo creaPvPeriodo(Date dataInizio, Date dataFine, Long percentuale, Long idEsonero) {
		VersamentoPvPeriodo periodo = new VersamentoPvPeriodo();

		periodo.setDInizio(dataInizio);
		periodo.setDFine(dataFine);
		periodo.setQuotaRiserva(percentuale);
		periodo.setIdEsonero(idEsonero);

		return periodo;
	}


	@SuppressWarnings("unused")
	private DatiServiziProdisSilp addProdisErrorToRisposta(DatiServiziProdisSilp risposta, List<ApiError> listaErr) {
		for (Iterator<ApiError> errM = listaErr.iterator(); errM.hasNext();) {
			ApiError apiError = (ApiError) errM.next();
			ApiMessage msg = new ApiMessage();

			msg.setCode(apiError.getCode());
			msg.setMessage(apiError.getErrorMessage());

			if ("ERROR".equals(apiError.getType()))
				msg.setTipo("E");
			else
				msg.setTipo(apiError.getType());
			
			msg.setError("ERROR".equals(apiError.getType()));
			risposta.getMessaggi().add(msg);

		}
		return risposta;
	}

	/**
	 * 
	 * @param elencoProspetti
	 * @param annoRiferimento restituisce il prospetto (stato 3) relativo all'anno
	 *                        riferimento indicato oppure il primo se
	 *                        annoRiferimento = 0
	 * @return
	 */

	private VersamentoProspetto getProspettoAnnoRiferimento(List<TestataProspetto> elencoProspetti,
			int annoRiferimento) {
		VersamentoProspetto out = null;
		if (CommonUtils.isNotVoid(elencoProspetti)) {
			for (Iterator<TestataProspetto> iterator = elencoProspetti.iterator(); iterator.hasNext();) {
				TestataProspetto in = (TestataProspetto) iterator.next();
				if ((annoRiferimento == 0
						|| annoRiferimento == CommonUtils.getYearFromDate(in.getDataRiferimentoProspetto()))
						&& in.getStatoProspettoId().equals(3L)) {
					out = new VersamentoProspetto();
					out.setAnnoRiferimento(Long.valueOf(CommonUtils.getYearFromDate(in.getDataRiferimentoProspetto())));
					out = getDettaglioProspettoProdis(out, in.getIdProspetto());

					break;
				}
			}
		}

		return out;
	}

	/**
	 * 
	 * @param out
	 * @param idProspetto chiama servizio prodis Dettaglio Prospetto per idProspetto
	 * @return
	 */
	private VersamentoProspetto getDettaglioProspettoProdis(VersamentoProspetto out, Long idProspetto) {

		/*
		 * 14  Il sistema chiama il servizio di dettaglio dei dati del prospetto dell'ultimo prospetto dell'anno di riferimento in stato "presentato (3)".
		 *  Input: IdChiamante (=SILAP), idProspetto (recuperato al passo precedente). 
		 *  
		 *     Descritto nel  5.3  - Dati dell'ultimo prospetto informativo disabili.
		 */

		FilterServiziProdis body = new FilterServiziProdis();

		body.setIdProspetto(idProspetto);
		body.setCaller(SilapConstants.PRODIS_CALLER);

		try {

			Prospetto prospProdis = postService(prodisUrl + "/getDettaglioProspettoCompleto", null,
					null, body, Prospetto.class, prodisUser, prodisPassword, new GenericType<List<ApiError>>() {
			});
			if (CommonUtils.isAllNotVoid(prospProdis)) {

				out.setBaseComputoNazionale(getLong(prospProdis.getNumLavorInForzaNazionale()));
				if(prospProdis.getCategoriaAzienda()!=null) {
				out.setCategoriaAzienda(prospProdis.getCategoriaAzienda().getCodCategoriaAzienda());
				}
				out.setCodiceRegionale(prospProdis.getCodiceComunicazione());
				Long AnnoRiferimento = Long.valueOf(CommonUtils.getYearFromDate(prospProdis.getDataRiferimentoProspetto()));
				out.setAnnoRiferimento(AnnoRiferimento );

				// prospProdis.getFlgSospensionePerMobilita();
				out.setEsoTVersamentoPvProspettos(new ArrayList<VersamentoPvProspetto>());
				for (Iterator<ProspettoProvincia> iterator = prospProdis.getProspettoProvincias().iterator(); iterator
						.hasNext();) {

					VersamentoPvProspetto prospPv = new VersamentoPvProspetto();

					ProspettoProvincia prospPvProdis = (ProspettoProvincia) iterator.next();
					// restituisce solo le province piemontesi
					if (prospPvProdis.getProvincia() != null && CommonUtils.in(prospPvProdis.getProvincia().getCodProvinciaMin(), 
							SilapConstants.COD_PROV_TO, 
							SilapConstants.COD_PROV_VC,
							SilapConstants.COD_PROV_NO,
							SilapConstants.COD_PROV_CN,
							SilapConstants.COD_PROV_AT,
							SilapConstants.COD_PROV_AL,
							SilapConstants.COD_PROV_BI,
							SilapConstants.COD_PROV_VB)) {

						it.csi.silap.colmirbff.api.dto.Provincia prov = new it.csi.silap.colmirbff.api.dto.Provincia();	
						prov.setId(prospPvProdis.getProvincia().getCodProvinciaMin());
						prov.setDsSilapDProvincia(prospPvProdis.getProvincia().getDsProTProvincia());	
						prospPv.setSilapDProvincia(prov);

						prospPv.setNumDisabiliInForza(getLong(prospPvProdis.getRiepilogoProvinciale().getNumDisabiliInForza()));
						prospPv.setCatCompensazioneDisabili(prospPvProdis.getRiepilogoProvinciale().getCatCompensazioneDisabili());
						prospPv.setBaseComputoProvinciale(
								getLong(prospPvProdis.getRiepilogoProvinciale().getBaseComputoArt3()));
						prospPv.setNumSoggettiCompensatiDisabili(
								getLong(prospPvProdis.getRiepilogoProvinciale().getNumCompensazioneDisabili()));
						
						
						if (prospPvProdis.getDatiProvinciali().getProvEsoneroAutocert().getNumLavEsoneroAutocert() != null)
							prospPv.setNumEsoneratiAutocertificati(
								getLong(prospPvProdis.getDatiProvinciali().getProvEsoneroAutocert().getNumLavEsoneroAutocert()));
						else prospPv.setNumEsoneratiAutocertificati(0l);
						
						/*
						prospPv.setNumEsoneratiAutocertificati(
								getLong(prospPvProdis.getRiepilogoProvinciale().getNumPosizioniEsonerate()));
						*/

						prospPv.setQuotaRiservaDisabili(
								getLong(prospPvProdis.getRiepilogoProvinciale().getQuotaRiservaDisabili()));

						out.getEsoTVersamentoPvProspettos().add(prospPv);
					}
				}
			}

		} catch (Exception ex) {
			// 14.a 
			return null;
		}

		return out;
	}

	private Long getLong(BigDecimal in) {
		if (in == null)
			return null;
		return Long.valueOf(in.longValue());
	}

	private boolean isEsitoPositivo(List<ApiMessage> msgList) {
		for (ApiMessage message : msgList) {
			if (message.getError()) {
				return false;
			}
		}
		return true;
	}



}
