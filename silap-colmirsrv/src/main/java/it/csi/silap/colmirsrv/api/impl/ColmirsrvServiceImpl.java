/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.api.impl;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.datatype.XMLGregorianCalendar;

import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import it.csi.silap.colmirsrv.api.cxf.dto.epaytypes.ResultType;
import it.csi.silap.colmirsrv.api.cxf.dto.epaytypes.TrasmettiNotifichePagamentoResponseType;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.ArrayOfNotificaPagamentoType;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.NotificaPagamentoType;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.TrasmettiNotifichePagamentoRequest;
import it.csi.silap.colmirsrv.api.dto.SilapConstants;
import it.csi.silap.colmirsrv.integration.entity.EsoDVersamentoStato;
import it.csi.silap.colmirsrv.integration.entity.EsoTNotificaPagamentoLog;
import it.csi.silap.colmirsrv.integration.entity.EsoTPosizioneDebitoria;
import it.csi.silap.colmirsrv.integration.entity.EsoTVersamento;
import it.csi.silap.colmirsrv.integration.entity.EsoTVersamentoStato;
import it.csi.silap.colmirsrv.util.JsonUtils;

@ApplicationScoped
public class ColmirsrvServiceImpl {

	private static final String USER = "epay";

	public TrasmettiNotifichePagamentoResponseType trasmettiNotifichePagamento(
			TrasmettiNotifichePagamentoRequest trasmettiNotifichePagamentoRequest) {

		logInfo("TrasmettiNotifichePagamento", "BEGIN");
		String esitoStr = "";

		try {

			QuarkusTransaction.begin();
			tracciaNotificaChiamata(trasmettiNotifichePagamentoRequest);
			QuarkusTransaction.commit();
			
			
			QuarkusTransaction.begin();
			if (trasmettiNotifichePagamentoRequest.getCorpoNotifichePagamento() != null
					&& trasmettiNotifichePagamentoRequest.getCorpoNotifichePagamento()
							.getElencoNotifichePagamento() != null) {

				ArrayOfNotificaPagamentoType elencoNotifichePagamento = trasmettiNotifichePagamentoRequest
						.getCorpoNotifichePagamento().getElencoNotifichePagamento();

				if (elencoNotifichePagamento != null) {
					for (NotificaPagamentoType notificaPagamento : elencoNotifichePagamento.getNotificaPagamento()) {
						System.out.println(notificaPagamento);
						esitoStr = processaNotifica(notificaPagamento);
						tracciaNotifica(esitoStr, trasmettiNotifichePagamentoRequest, notificaPagamento);
					}
				}
			}
			QuarkusTransaction.commit();
			return componiResponse("000");
		} catch (Exception err) {
			err.printStackTrace();
			logError("TrasmettiNotifichePagamento", " Errore in ricezione notifica di pagamento.", err);
			
			QuarkusTransaction.begin();
			tracciaNotifica("Errore in ricezione notifica di pagamento. " + err.getMessage(), trasmettiNotifichePagamentoRequest, null);
			QuarkusTransaction.commit();
			
			
			return componiResponse("100");
		} finally {
			logInfo("TrasmettiNotifichePagamento", "END");
		}

	}
	
	private boolean hasStato(long idVersamento, long stato) {
		List<EsoTVersamentoStato> list = EsoTVersamentoStato.list("idEsoTVersamento = ?1 and esoDVersamentoStato.id = ?2 and flgCurrentRecord = 'S'",
				idVersamento, stato);
		if (list != null && list.size()>0)
			return true;
		return false;
	}

	private String processaNotifica(NotificaPagamentoType notificaPagamento) {

		List<EsoTPosizioneDebitoria> list = EsoTPosizioneDebitoria.list("codIuv", notificaPagamento.getIUV());
		if (list != null && list.size() > 0) {

			EsoTPosizioneDebitoria posizione = list.get(0);

			if (notificaPagamento.getImportoPagato() != null
					&& notificaPagamento.getImportoPagato().doubleValue() > 0) {

				EsoTVersamento esoTVersamento = EsoTVersamento.findById(posizione.getIdEsoTVersamento());
				
				Long numRate = esoTVersamento.getNumRate();
				Long numRata = posizione.getNumRata();
				if (numRate.intValue() == 1) {
					if (!hasStato(esoTVersamento.getIdEsoTVersamento(), SilapConstants.STATO_SALDO))
						updateStatoVersamento(posizione.getIdEsoTVersamento(), SilapConstants.STATO_SALDO);
					else return "Versamento id:" + esoTVersamento.getIdEsoTVersamento() + " IUV:" + notificaPagamento.getIUV() + ". Notifica gia' processata";
				} else {
					if (numRata.intValue() == 1) {
						if (!hasStato(esoTVersamento.getIdEsoTVersamento(), SilapConstants.STATO_ACCONTO))
							updateStatoVersamento(posizione.getIdEsoTVersamento(), SilapConstants.STATO_ACCONTO);
						else return "Versamento id:" + esoTVersamento.getIdEsoTVersamento() + " IUV:" + notificaPagamento.getIUV() + ". Notifica gia' processata";
					}
					else {
						if (!hasStato(esoTVersamento.getIdEsoTVersamento(), SilapConstants.STATO_SALDO))
							updateStatoVersamento(posizione.getIdEsoTVersamento(), SilapConstants.STATO_SALDO);
						else return "Versamento id:" + esoTVersamento.getIdEsoTVersamento() + " IUV:" + notificaPagamento.getIUV() + ". Notifica gia' processata";
					}
				}

				posizione.setImportoPagato(notificaPagamento.getImportoPagato());
				posizione.setDsCausale(notificaPagamento.getDescrizioneCausaleVersamento());
				posizione.setDtEsito(toDate(notificaPagamento.getDataEsitoPagamento()));
				posizione.setDsNote(notificaPagamento.getNote());
				posizione.setCodAvviso(notificaPagamento.getCodiceAvviso());

				String dsDebitore = "";
				String cfDebitore = "";
				if (notificaPagamento.getSoggettoDebitore() != null) {
					if (notificaPagamento.getSoggettoDebitore().getPersonaFisica() != null) {
						dsDebitore = notificaPagamento.getSoggettoDebitore().getPersonaFisica().getCognome() + " "
								+ notificaPagamento.getSoggettoDebitore().getPersonaFisica().getNome();
					}
					if (notificaPagamento.getSoggettoDebitore().getPersonaGiuridica() != null) {
						dsDebitore = notificaPagamento.getSoggettoDebitore().getPersonaGiuridica().getRagioneSociale();
					}
					cfDebitore = notificaPagamento.getSoggettoDebitore().getIdentificativoUnivocoFiscale();

				}

				String dsVersante = "";
				String cfVersante = "";
				if (notificaPagamento.getSoggettoVersante() != null) {
					if (notificaPagamento.getSoggettoVersante().getPersonaFisica() != null) {
						dsDebitore = notificaPagamento.getSoggettoVersante().getPersonaFisica().getCognome() + " "
								+ notificaPagamento.getSoggettoVersante().getPersonaFisica().getNome();
					}
					if (notificaPagamento.getSoggettoVersante().getPersonaGiuridica() != null) {
						dsDebitore = notificaPagamento.getSoggettoVersante().getPersonaGiuridica().getRagioneSociale();
					}
					cfVersante = notificaPagamento.getSoggettoVersante().getIdentificativoUnivocoFiscale();
				}

				posizione.setCfDebitore(cfDebitore + " " + cfVersante);
				posizione.setDsDebitore(dsDebitore + " " + dsVersante);

				if (notificaPagamento.getDatiTransazionePSP() != null) {
					posizione.setDtOperazione(
							toDate(notificaPagamento.getDatiTransazionePSP().getDataOraAutorizzazione()));
					posizione.setCodIur(notificaPagamento.getDatiTransazionePSP().getIUR());
					
					posizione.setCodTransazione(notificaPagamento.getDatiTransazionePSP().getIdFlussoRendicontazionePSP());

					String ragSoc = notificaPagamento.getDatiTransazionePSP().getRagioneSocialePSP();
					if (ragSoc == null)
						ragSoc = " ";
					ragSoc += " " + notificaPagamento.getDatiTransazionePSP().getIdPSP();
					posizione.setDsPsp(ragSoc);
				}

				Date now = new Date();
				posizione.setDtAggiorn(now);
				posizione.setCodUserAggiorn(USER);
				posizione.setNtimestamp(posizione.getNtimestamp() + 1);
				posizione.persist();
				return null;
			}
			return "Importo versato minore o uguale a 0";
		}
		return "IUV non presente";

	}

	private EsoTVersamentoStato updateStatoVersamento(Long idEsoTVersamento, Long idStato) {

		String codUtente = USER;

		Date now = new Date();

		EsoTVersamentoStato.update(
				"flgCurrentRecord=?1, nTimestamp = (nTimestamp + 1) where idEsoTVersamento=?2", null,
				idEsoTVersamento);

		EsoTVersamentoStato stato = new EsoTVersamentoStato();
		EsoTVersamento esoTVersamento = new EsoTVersamento();
		esoTVersamento.setIdEsoTVersamento(idEsoTVersamento);
		stato.setIdEsoTVersamento(idEsoTVersamento);

		EsoDVersamentoStato dStato = new EsoDVersamentoStato();
		dStato.setId(idStato);
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

	private void tracciaNotifica(String esitoStr, TrasmettiNotifichePagamentoRequest request,
			NotificaPagamentoType notificaPagamento) {

		EsoTNotificaPagamentoLog esoTNotificaPagamentoLog = new EsoTNotificaPagamentoLog();
		esoTNotificaPagamentoLog.setCodIdMessaggio(request.getTestata().getIdMessaggio());
		
		if (notificaPagamento != null)
			esoTNotificaPagamentoLog.setCodIuv(notificaPagamento.getIUV());
		
		esoTNotificaPagamentoLog.setDsMessaggio(JsonUtils.toJson(request));
		esoTNotificaPagamentoLog.setEsito((esitoStr == null) ? "OK" : "KO");
		esoTNotificaPagamentoLog.setDsEsito(esitoStr);
		Date now = new Date();
		esoTNotificaPagamentoLog.setDtInserim(now);
		esoTNotificaPagamentoLog.setCodUserInserim(USER);
		esoTNotificaPagamentoLog.setDtAggiorn(now);
		esoTNotificaPagamentoLog.setCodUserAggiorn(USER);
		esoTNotificaPagamentoLog.setNtimestamp(0l);
		esoTNotificaPagamentoLog.persist();
	}

	private void tracciaNotificaChiamata(TrasmettiNotifichePagamentoRequest request) {

		EsoTNotificaPagamentoLog esoTNotificaPagamentoLog = new EsoTNotificaPagamentoLog();
		esoTNotificaPagamentoLog.setCodIdMessaggio(request.getTestata().getIdMessaggio());
		esoTNotificaPagamentoLog.setDsMessaggio(JsonUtils.toJson(request));
		esoTNotificaPagamentoLog.setEsito("OK");

		Date now = new Date();
		esoTNotificaPagamentoLog.setDtInserim(now);
		esoTNotificaPagamentoLog.setCodUserInserim(USER);
		esoTNotificaPagamentoLog.setDtAggiorn(now);
		esoTNotificaPagamentoLog.setCodUserAggiorn(USER);
		esoTNotificaPagamentoLog.setNtimestamp(0l);

		esoTNotificaPagamentoLog.persist();
	}

	private TrasmettiNotifichePagamentoResponseType componiResponse(String cod) {

		ResultType resultType = new ResultType();

		switch (cod) {
		case "000":
			resultType.setMessaggio("OK");
			resultType.setCodice("000");
			break;
		case "100":
		default:
			resultType.setMessaggio("KO");
			resultType.setCodice("100");
			break;
		}

		TrasmettiNotifichePagamentoResponseType r = new TrasmettiNotifichePagamentoResponseType();
		r.setResult(resultType);

		return r;

	}

	private void logInfo(String methodName, String message) {
		Log.info("[" + getClass().getSimpleName() + "::" + methodName + "] " + message);
	}

	private void logError(String methodName, String message, Throwable exception) {
		Log.error("[" + getClass().getSimpleName() + "::" + methodName + "] " + message, exception);
	}

	private Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}

}
