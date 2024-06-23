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
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.TransactionManager;

import com.ibm.icu.util.Calendar;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.Utente;
import it.csi.silap.colmirbff.api.dto.mappers.SilapMappers;
import it.csi.silap.colmirbff.api.impl.manager.PosizioneDebitoriaManager;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchExec;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchLog;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamento;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProvincia;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvPeriodo;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoStato;

@ApplicationScoped
public class CreazionePosizioniDebitorieRunnable implements Runnable {

	@Inject
	private TransactionManager transactionManager;

	@Inject
	protected SilapMappers mappers;

	@Inject
	private PosizioneDebitoriaManager posizioneDebitoriaManager;

	private Utente utente;

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Override
	public void run() {
		Log.info("CreazionePosizioniDebitorieRunnable - START");
		String codUtente = utente.getCodFisc();
		EsoTBatchExec batchExec = new EsoTBatchExec();
		List<EsoTBatchLog> batchLogs = new ArrayList<EsoTBatchLog>();
		Long numRecordOk = 0L;
		Long numRecordKo = 0L;
		Long numRecordDaElaborare = 0L;
		
		try {
			Date inizio = new Date();
			transactionManager.begin();
			batchExec.setCodEsoTBatchExec(SilapConstants.BATCH_CREAZIONE_POSIZIONI_DEBITORIE);
			batchExec.setDInizioExec(inizio);
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
				cal.setTime(new Date());
				long annoRiferimento = cal.get(Calendar.YEAR)-1;
				stati = EsoTVersamentoStato.list("esoDVersamentoStato.id = ?1 and flgCurrentRecord = 'S' and esoTVersamento.annoRiferimento = ?2",
						SilapConstants.STATO_AUTORIZZATA,annoRiferimento);
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
			EsoTVersamento versamento = null;
			for (EsoTVersamentoStato stato : stati) {

				versamento = stato.getEsoTVersamento();
				System.out.println(
						"Azienda: " + versamento.getCodFiscale() + " Anno rif.: " + versamento.getAnnoRiferimento());
				
				
				
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
						
						posizioneDebitoriaManager.creaPosizioneDebitoria(versamento.getIdEsoTVersamento());
						
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
					errSingleExecutor.printStackTrace();
					Log.info(errSingleExecutor.getMessage());
					try {
						transactionManager.rollback();
					} catch (Exception errRollback) {
						throw new Exception(errRollback.getMessage());
					}
					Log.error(errSingleExecutor);
				} finally {
					
					if (transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION)
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

		Log.info("CreazionePosizioniDebitorieRunnable - END");
	}

}
