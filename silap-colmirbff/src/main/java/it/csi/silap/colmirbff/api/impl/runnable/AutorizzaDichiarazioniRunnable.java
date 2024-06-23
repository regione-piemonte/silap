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
package it.csi.silap.colmirbff.api.impl.runnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.TransactionManager;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.Utente;
import it.csi.silap.colmirbff.api.dto.Versamento;
import it.csi.silap.colmirbff.api.dto.VersamentoStato;
import it.csi.silap.colmirbff.api.dto.mappers.SilapMappers;
import it.csi.silap.colmirbff.api.impl.manager.VersamentoEsoneriManager;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchExec;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchLog;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoStato;

@ApplicationScoped
public class AutorizzaDichiarazioniRunnable implements Runnable {

	@Inject
	private TransactionManager transactionManager;

	@Inject
	protected SilapMappers mappers;

	@Inject
	private VersamentoEsoneriManager versamentoEsoneriManager;

	private Utente utente;

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Override
	public void run() {
		Log.info("AutorizzaDichiarazioniRunnable - START");
		String codUtente = utente.getCodFisc();
		EsoTBatchExec batchExec = new EsoTBatchExec();
		List<EsoTBatchLog> batchLogs = new ArrayList<EsoTBatchLog>();
		Long numRecordOk = 0L;
		Long numRecordKo = 0L;
		Long numRecordDaElaborare = 0L;
		
		try {
			Date inizio = new Date();
			transactionManager.begin();
			batchExec.setCodEsoTBatchExec(SilapConstants.BATCH_VERSAMENTO_ACCETTAZIONE_AUTO);
			batchExec.setDInizioExec(inizio);
			batchExec.setDFineExec(inizio);
			batchExec.setNumRecordDaElaborare(0L);
			batchExec.setNumRecordKo(0L);
			batchExec.setNumRecordOk(0L);
			batchExec.setCodUserInserim(codUtente);
			batchExec.setCodUserAggiorn(codUtente);
			batchExec.setDInserim(inizio);
			batchExec.setDAggiorn(inizio);
			batchExec.setNTimestamp(0L);
			batchExec.persist();
			transactionManager.commit();

			EsoTBatchExec batchExecPersist = new EsoTBatchExec();
			batchExecPersist.setIdEsoTBatchExec(batchExec.getIdEsoTBatchExec());
			List<EsoTVersamentoStato> stati = new ArrayList<EsoTVersamentoStato>();
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTime(inizio);
				int annoCorrente = cal.get(Calendar.YEAR);
				stati = EsoTVersamentoStato.list("esoDVersamentoStato.id = ?1 and esoTVersamento.annoRiferimento=?2 and flgCurrentRecord = 'S'",
						SilapConstants.STATO_ACCETTATA,
						Long.valueOf(annoCorrente - 1));
			} catch (Exception errQuery) {
				Log.error(errQuery);
				errQuery.printStackTrace();
			}
			if (stati == null) {
				throw new Exception("Errore consistenza dati");
			}
			if (stati.size() == 0 ) {
				Log.info("Nessuna dichiarazione da aggiornare");
			}
			numRecordDaElaborare = (long) stati.size();
			Versamento versamento = null;
			for (EsoTVersamentoStato stato : stati) {

				versamento = mappers.VERSAMENTO.toModel(stato.getEsoTVersamento());
				System.out.println(
						"Azienda: " + versamento.getCodFiscale() + " Anno rif.: " + versamento.getAnnoRiferimento());
				try {
					Date now = new Date();
					transactionManager.begin();
					EsoTBatchLog batchLog = new EsoTBatchLog();
					batchLog.setEsoTBatchExec(batchExecPersist);
					batchLog.setCodFiscale(versamento.getCodFiscale());
					batchLog.setCodUserInserim(codUtente);
					batchLog.setCodUserAggiorn(codUtente);
					batchLog.setDInserim(now);
					batchLog.setDAggiorn(now);
					batchLog.setNTimestamp(0L);
					try {
						VersamentoStato versStato = mappers.VERSAMENTO_STATO.toModel(stato);
						versStato.setIdEsoTVersamento(versamento.getIdEsoTVersamento());
						versamentoEsoneriManager.autorizzaVersamentoBatch(versStato, utente);
						batchLog.setEsito("OK");
						numRecordOk++;
						batchLogs.add(batchLog);
						batchLog.persist();
					} catch (BusinessException errAutorizza) {
						Log.error(errAutorizza);
						numRecordKo++;
						batchLog.setEsito("KO");
						batchLog.setLogExec(errAutorizza.getMessage());
						batchLogs.add(batchLog);
						batchLog.persist();
					}

				} catch (Exception errSingleExecutor) {
					Log.info(errSingleExecutor.getMessage());
					try {
						transactionManager.rollback();
					} catch (Exception errRollback) {
						throw new Exception(errRollback.getMessage());
					}
					Log.error(errSingleExecutor);
				} finally {
					transactionManager.commit();
				}
			}

		} catch (Exception err) {
			Log.error(err);
			err.printStackTrace();
		} finally {
			try {
				transactionManager.begin();
				Date fine = new Date();
				EsoTBatchExec batchExecUpdate = EsoTBatchExec.findById(batchExec.getIdEsoTBatchExec());
				batchExecUpdate.setNumRecordDaElaborare(numRecordDaElaborare);
				batchExecUpdate.setEsoTBatchLogs(batchLogs);
				batchExecUpdate.setDFineExec(fine);
				batchExecUpdate.setNumRecordKo(numRecordKo);
				batchExecUpdate.setNumRecordOk(numRecordOk);
				batchExecUpdate.setNTimestamp(1L);

				batchExecUpdate.persist();
				transactionManager.commit();
			} catch (Exception err) {
				Log.error(err);
				err.printStackTrace();
			}
		}

		Log.info("AutorizzaDichiarazioniRunnable - END");
	}

}
