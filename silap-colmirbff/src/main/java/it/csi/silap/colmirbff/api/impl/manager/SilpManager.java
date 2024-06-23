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
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.ws.soap.SOAPFaultException;

import io.quarkiverse.cxf.annotation.CXFClient;
import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.Azienda;
import it.csi.silap.colmirbff.api.dto.Ccnl;
import it.csi.silap.colmirbff.api.dto.Comune;
import it.csi.silap.colmirbff.api.dto.CreditoResiduo;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.cxf.silpsvaa.AnagraficaAziende;
import it.csi.silap.colmirbff.cxf.silpsvaa.ElencoAziende;
import it.csi.silap.colmirbff.cxf.silpsvaa.ElencoSedi;
import it.csi.silap.colmirbff.cxf.silpsvaa.EstrazioneAnagraficaAziende;
import it.csi.silap.colmirbff.cxf.silpsvaa.ServiziSilpException_Exception;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoTCreditoResiduo;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamento;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoStato;
import it.csi.silap.colmirbff.integration.entity.SilapDComune;
import it.csi.silap.colmirbff.util.CommonUtils;

@Dependent
public class SilpManager extends BaseApiServiceImpl {

	@Inject
	@CXFClient("silpsvaa")
	private EstrazioneAnagraficaAziende silpsvaa;

	/*
	 * Ritorna elenco aziende dal servizio di silp ricercaElencoAziende
	 */
	private ElencoAziende getElencoAziende(String codiceFiscaleAzienda) {
		ElencoAziende elencoAziende = new ElencoAziende();
		try {
			ElencoAziende elencoAziendeFilter = new ElencoAziende();
			elencoAziendeFilter.setCodFiscale(codiceFiscaleAzienda);
			List<ElencoAziende> elencoAziendes = silpsvaa.ricercaElencoAziende(SilapConstants.COMONL_CALLER,
					elencoAziendeFilter);

			if (elencoAziendes == null || elencoAziendes.isEmpty()) {
				return null;
			}

			elencoAziende = null;
			int aziendeConFlagS = 0;
			if (elencoAziendes.size() == 1) {
				// prendo l'unica trovata
				elencoAziende = elencoAziendes.get(0);
			} else if (elencoAziendes.size() > 1) {
				// Se ci sono più aziende senza flag Master il sistema non carica e segnala che
				// non è stata trovata azienda.
				// Se ci sono più azienda con flag master ne carica una
				for (var iterator = elencoAziendes.iterator(); iterator.hasNext();) {
					ElencoAziende elencoAziendeItem = iterator.next();
					if (elencoAziendeItem.getFlgMaster() != null) {
						if (elencoAziendeItem.getFlgMaster().equalsIgnoreCase("S")) {
							elencoAziende = elencoAziendeItem;
							aziendeConFlagS++;
						}
					} else {
						continue;
					}
				}
				if (aziendeConFlagS == 0 || aziendeConFlagS > 1) {
					throw new BusinessException(
							new ApiMessage(getMsg(SilapConstants.MSG_AZIENDES_CON_FLAG_S, codiceFiscaleAzienda)));

				}
			}

			if (elencoAziende == null) {
				return null;
			}

		} catch (ServiziSilpException_Exception e) {
			e.printStackTrace();
			if (SilapConstants.MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_AZ.equalsIgnoreCase(e.getFaultInfo().getCodice())
					|| SilapConstants.MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_SEDE_AZ
							.equalsIgnoreCase(e.getFaultInfo().getCodice())
					|| SilapConstants.MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_SEDE_AZ_2
							.equalsIgnoreCase(e.getFaultInfo().getCodice())) {
				throw new BusinessException(new ApiMessage(
						getMsg(SilapConstants.MSG_ERRORE_SISTEMA_DATI_ANAGRAFICI_AZ, codiceFiscaleAzienda)));
			} else {
				throw new BusinessException(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_DATI_AZIENDA,
						elencoAziende.getRagioneSociale(), codiceFiscaleAzienda)));
			}
		}

		catch (SOAPFaultException e) {
			e.printStackTrace();
			throw new BusinessException(
					new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_SISTEMA_DATI_ANAGRAFICI_AZ, codiceFiscaleAzienda)));
		}
		return elencoAziende;

	}

	/*
	 * Ritorna la sede legale per l'azienda selezionata con idAzienda dal servizio
	 * silp ricercaSediAzienda
	 */
	private ElencoSedi getElencoSedi(String idAzienda, String codiceFiscaleAzienda) {
		ElencoSedi laSedeLegaleRecuperataDaSilp = new ElencoSedi();
		try {
			ElencoSedi elencoSediFilter = new ElencoSedi();
			elencoSediFilter.setIdAzienda(idAzienda);
			elencoSediFilter.setTipoSede(SilapConstants.TIPO_SEDE_LEGALE_1);
			List<ElencoSedi> elencoSedis = silpsvaa.ricercaSediAzienda(SilapConstants.COMONL_CALLER, elencoSediFilter);
			laSedeLegaleRecuperataDaSilp = null;
			if (elencoSedis == null || elencoSedis.isEmpty()) {
				return null;
			} else {

				if (elencoSedis.size() > 1) {
					for (ElencoSedi laSedeLegaleDaControllareSeValida : elencoSedis) {
						/* Ricerca della sede valida tra le sedi legali restituite da silp */
						if (laSedeLegaleDaControllareSeValida.getIdNazioneSede() != null) {
							throw new BusinessException(new ApiMessage(getMsg(SilapConstants.MSG_SEDE_LEGALE_ESTERA,
									laSedeLegaleRecuperataDaSilp.getRagioneSocialeSede(), codiceFiscaleAzienda)));
						}
						if (laSedeLegaleDaControllareSeValida.getDataFineAttivita() == null || (CommonUtils
								.confrontaData1MaggioreData2(laSedeLegaleDaControllareSeValida.getDataFineAttivita(),
										CommonUtils.getCurrentDateGGMMAAAA())
								&& laSedeLegaleDaControllareSeValida.getDataFineAttivita() != null)) {
							laSedeLegaleRecuperataDaSilp = laSedeLegaleDaControllareSeValida;
						} else {
							throw new BusinessException(new ApiMessage(getMsg(SilapConstants.MSG_SEDE_LEGALE_CESSATA,
									laSedeLegaleRecuperataDaSilp.getRagioneSocialeSede(), codiceFiscaleAzienda)));
						}
					}
				} else {
					laSedeLegaleRecuperataDaSilp = elencoSedis.get(0);
					if (laSedeLegaleRecuperataDaSilp.getIdNazioneSede() != null) {
						throw new BusinessException(new ApiMessage(getMsg(SilapConstants.MSG_SEDE_LEGALE_ESTERA,
								laSedeLegaleRecuperataDaSilp.getRagioneSocialeSede(), codiceFiscaleAzienda)));
					}
					if (!(laSedeLegaleRecuperataDaSilp.getDataFineAttivita() == null || (CommonUtils
							.confrontaData1MaggioreData2(laSedeLegaleRecuperataDaSilp.getDataFineAttivita(),
									CommonUtils.getCurrentDateGGMMAAAA())
							&& laSedeLegaleRecuperataDaSilp.getDataFineAttivita() != null))) {
						throw new BusinessException(new ApiMessage(getMsg(SilapConstants.MSG_SEDE_LEGALE_CESSATA,
								laSedeLegaleRecuperataDaSilp.getRagioneSocialeSede(), codiceFiscaleAzienda)));
					}

				}
			}
		} catch (ServiziSilpException_Exception e) {
			e.printStackTrace();
			if (SilapConstants.MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_AZ.equalsIgnoreCase(e.getFaultInfo().getCodice())
					|| SilapConstants.MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_SEDE_AZ
							.equalsIgnoreCase(e.getFaultInfo().getCodice())) {
				throw new BusinessException(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_SISTEMA_SEDE_LEGALE,
						laSedeLegaleRecuperataDaSilp.getRagioneSocialeSede(), codiceFiscaleAzienda)));
			} else {
				throw new BusinessException(new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_ESTRAZIONE_SEDE_LEGALE,
						laSedeLegaleRecuperataDaSilp.getRagioneSocialeSede(), codiceFiscaleAzienda)));
			}
		}
		return laSedeLegaleRecuperataDaSilp;
	}

	private AnagraficaAziende getAnagraficaAzienda(String idAzienda, String codiceFiscaleAzienda) {
		AnagraficaAziende anagraficaAzienda = new AnagraficaAziende();
		try {
			anagraficaAzienda = silpsvaa.visualizzaDettaglioAzienda(SilapConstants.COMONL_CALLER, idAzienda);
		} catch (ServiziSilpException_Exception e) {
			e.printStackTrace();
			throw new BusinessException(
					new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_SISTEMA_DATI_ANAGRAFICI_AZ, codiceFiscaleAzienda)));

		}

		catch (SOAPFaultException e) {
			e.printStackTrace();
			throw new BusinessException(
					new ApiMessage(getMsg(SilapConstants.MSG_ERRORE_SISTEMA_DATI_ANAGRAFICI_AZ, codiceFiscaleAzienda)));
		}

		return anagraficaAzienda;
	}

	private String setIndirizzo(String toponimo, String descrIndirizzo, String numCivico) {
		StringBuilder indirizzoCompleto = new StringBuilder();

		if (toponimo != null)
			indirizzoCompleto.append(toponimo.trim()).append(" ");
		if (descrIndirizzo != null)
			indirizzoCompleto.append(descrIndirizzo.trim()).append(" ");
		if (numCivico != null)
			indirizzoCompleto.append(numCivico);

		return indirizzoCompleto.toString();
	}

	@Transactional
	public Azienda ricercaDatiAzienda(String codiceFiscaleAzienda, Long idVersamento) {
		ElencoAziende elencoAziende = new ElencoAziende();
		ElencoSedi elencoSedi = new ElencoSedi();
		AnagraficaAziende anagraficaAzienda = new AnagraficaAziende();

		Azienda azienda = new Azienda();
		try {
			elencoAziende = getElencoAziende(codiceFiscaleAzienda);
			if (elencoAziende == null) {
				throw new BusinessException(new ApiMessage(
						getMsg(SilapConstants.MSG_COD_FISCALE_AZIENDA_NON_PRESENTE, codiceFiscaleAzienda)));
			}
			String idAzienda = elencoAziende.getIdAzienda();
			elencoSedi = getElencoSedi(idAzienda, codiceFiscaleAzienda);
			anagraficaAzienda = getAnagraficaAzienda(idAzienda, codiceFiscaleAzienda);
		} catch (BusinessException e) {
			throw e;
		}

		String idComuneSilp = elencoSedi.getIdComuneSede();
		SilapDComune silapDComune = SilapDComune.findById(idComuneSilp);
		Comune comune = mappers.COMUNE.toModel(silapDComune);
		azienda.setCodFiscale(codiceFiscaleAzienda);
		azienda.setDenomAzienda(elencoAziende.getRagioneSociale());
		azienda.setCapSede(elencoSedi.getCapSede());
		azienda.setComuneSede(comune);
		azienda.setIndirizzoSede(setIndirizzo(elencoSedi.getDescrToponimoSede(), elencoSedi.getDescrIndirizzoSede(),
				elencoSedi.getDescrNumCivicoSede()));
		azienda.setIdSilAziAnagrafica(Long.parseLong(anagraficaAzienda.getIdAzienda()));
		azienda.setIdSilAziSede(Long.parseLong(elencoSedi.getIdSedeAzienda()));
		azienda.setRagioneSocialeSede(elencoSedi.getRagioneSocialeSede());
		Ccnl ccnl = new Ccnl();
		ccnl.setId(anagraficaAzienda.getIdCcnlAzienda());
		ccnl.setCod(anagraficaAzienda.getIdCcnlAzienda());
		ccnl.setDescr(anagraficaAzienda.getDescrCcnlAzienda());
		ccnl.setDsSettore(anagraficaAzienda.getDescrCcnlAzienda()); // da verificare
		azienda.setSilapDCcnl(ccnl);

		List<EsoTCreditoResiduo> esoTCreditoResiduo = EsoTCreditoResiduo.list("codFiscale", codiceFiscaleAzienda);
		if (esoTCreditoResiduo == null || esoTCreditoResiduo.size() <= 0) {
			azienda.setNumCreditoResiduo(BigDecimal.ZERO);
			return azienda;
		}
		CreditoResiduo creditoResiduo = mappers.CREDITO_RESIDUO.toModel(esoTCreditoResiduo.get(0));
		List<EsoTVersamentoStato> esoTVersamentoStatos = new ArrayList<EsoTVersamentoStato>();
		if (idVersamento == null) {
			boolean creditoUtilizzato = false;
			esoTVersamentoStatos = EsoTVersamentoStato.list(
					"esoTVersamento.codFiscale=?1 and esoTVersamento.esoTCreditoResiduo.idEsoTCreditoResiduo=?2 and flgCurrentRecord='S' and esoDVersamentoStato.id!=?3 and esoDVersamentoStato.id!=?4 and esoDVersamentoStato.id!=?5",
					codiceFiscaleAzienda, creditoResiduo.getIdEsoTCreditoResiduo(), SilapConstants.STATO_ELIMINATA,
					SilapConstants.STATO_ANNULLATA, SilapConstants.STATO_NON_AUTORIZZATA);
			if (esoTVersamentoStatos != null && esoTVersamentoStatos.size() > 0) {
				creditoUtilizzato = true;
			}
			if (!creditoUtilizzato) {
				azienda.setNumCreditoResiduo(creditoResiduo.getNumValore());
				azienda.setIdEsoTCreditoResiduo(creditoResiduo.getIdEsoTCreditoResiduo());
			} else {
				azienda.setNumCreditoResiduo(BigDecimal.ZERO);
				// azienda.setIdEsoTCreditoResiduo(creditoResiduo.getIdEsoTCreditoResiduo());
			}
			return azienda;
		} else {
			boolean creditoUtilizzato = false;
			esoTVersamentoStatos = EsoTVersamentoStato.list(
					"esoTVersamento.codFiscale=?1 and esoTVersamento.esoTCreditoResiduo.idEsoTCreditoResiduo=?2 and flgCurrentRecord='S' and esoDVersamentoStato.id!=?3 and esoDVersamentoStato.id!=?4 and esoDVersamentoStato.id!=?5",
					codiceFiscaleAzienda, creditoResiduo.getIdEsoTCreditoResiduo(), SilapConstants.STATO_ELIMINATA,
					SilapConstants.STATO_ANNULLATA, SilapConstants.STATO_NON_AUTORIZZATA);
			if (esoTVersamentoStatos != null && esoTVersamentoStatos.size() == 1) {
				EsoTVersamento esoTVersamento = EsoTVersamento.findById(idVersamento);
				if (esoTVersamento.getEsoTCreditoResiduo() == null) {
					creditoUtilizzato = true;
				}
			}
			if (!creditoUtilizzato) {
				azienda.setNumCreditoResiduo(creditoResiduo.getNumValore());
				azienda.setIdEsoTCreditoResiduo(creditoResiduo.getIdEsoTCreditoResiduo());
			} else {
				azienda.setNumCreditoResiduo(BigDecimal.ZERO);
				// azienda.setIdEsoTCreditoResiduo(creditoResiduo.getIdEsoTCreditoResiduo());
			}
		}
		return azienda;

	}

}
