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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.TransactionManager;
import javax.ws.rs.NotFoundException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import it.csi.silap.colmirbff.api.dto.Parametro;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.Utente;
import it.csi.silap.colmirbff.api.dto.mappers.SilapMappers;
import it.csi.silap.colmirbff.api.impl.manager.PosizioneDebitoriaManager;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchExec;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchLog;
import it.csi.silap.colmirbff.integration.entity.EsoTParametro;
import it.csi.silap.colmirbff.integration.entity.EsoTPosizioneDebitoria;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamento;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoStato;

@Startup
@ApplicationScoped
public class InviaPromemoriaPagamentoRunnable implements Runnable {

	@Inject
	private TransactionManager transactionManager;

	@Inject
	protected SilapMappers mappers;

	@Inject
	private PosizioneDebitoriaManager posizioneDebitoriaManager;

	@ConfigProperty(name = "colmirbff.inviaPromemoriaPagamentoRunnable.schedule.ip")
	private String ipServerCandidate;

	private Utente utente;

	@PostConstruct
	private void init() {
		Log.info("[InviaPromemoriaPagamentoRunnable::init] start");
		utente = new Utente();
		utente.setCodFisc("Scheduled-user");
		printAddress();
	}

	@Override
	@Scheduled(cron = "{colmirbff.inviaPromemoriaPagamentoRunnable.schedule.start}")
	public void run() {

		Log.info("InviaPromemoriaPagamentoRunnable - START");

		if (ipServerCandidate != null && !isServerCandidate(ipServerCandidate)) {
			Log.info("InviaPromemoriaPagamentoRunnable - END (server non candidato)");
			return;
		}

		Parametro ggPreavvisoParam = getParametroByCod(SilapConstants.PARAM_COD_PROGG);
		Date dataScadenzaPrimaRata = getDataAnno(SilapConstants.PARAM_COD_PDAT1);
		Date dataScadenzaSecondaRata = getDataAnno(SilapConstants.PARAM_COD_PDAT2);
		if (ggPreavvisoParam == null || dataScadenzaPrimaRata == null || dataScadenzaSecondaRata == null) {
			String params = SilapConstants.PARAM_COD_PROGG + ", " + SilapConstants.PARAM_COD_PDAT1 + ", "
					+ SilapConstants.PARAM_COD_PDAT2;
			Log.error("InviaPromemoriaPagamentoRunnable - END (" + params + " parametri non impostati!!!)");
			return;
		}

		int ggPreavviso = Integer.parseInt(ggPreavvisoParam.getDsValore().trim());
		int numRataPreavviso = getRataPreavviso(ggPreavviso, dataScadenzaPrimaRata, dataScadenzaSecondaRata);
		if (numRataPreavviso <= 0) {
			Log.info("InviaPromemoriaPagamentoRunnable - END (Non in periodo di preavviso)");
			return;
		}

		String codUtente = utente.getCodFisc();
		EsoTBatchExec batchExec = new EsoTBatchExec();
		List<EsoTBatchLog> batchLogs = new ArrayList<EsoTBatchLog>();
		Long numRecordOk = 0L;
		Long numRecordKo = 0L;
		Long numRecordDaElaborare = 0L;

		try {
			Date inizio = new Date();
			transactionManager.begin();
			batchExec.setCodEsoTBatchExec(SilapConstants.BATCH_INVIA_AVVISI_PAGAMENTO);
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
				if (numRataPreavviso == 1)
					stati = EsoTVersamentoStato.list("esoDVersamentoStato.id = ?1 and flgCurrentRecord = 'S'",
							SilapConstants.STATO_AVVISO_INVIATO);
				else
					stati = EsoTVersamentoStato.list(
							"(esoDVersamentoStato.id = ?1 or esoDVersamentoStato.id = ?2) and flgCurrentRecord = 'S'",
							SilapConstants.STATO_AVVISO_INVIATO, SilapConstants.STATO_ACCONTO);
			} catch (Exception errQuery) {
				Log.error(errQuery);
				errQuery.printStackTrace();
			}
			if (stati == null) {
				throw new Exception("Errore consistenza dati");
			}
			if (stati.size() == 0) {
				Log.info("Nessuna dichiarazione da aggiornare");
			}
			numRecordDaElaborare = (long) stati.size();
			EsoTVersamento versamento = null;
			for (EsoTVersamentoStato stato : stati) {

				versamento = stato.getEsoTVersamento();
				System.out.println(
						"Azienda: " + versamento.getCodFiscale() + " Anno rif.: " + versamento.getAnnoRiferimento());

				List<EsoTPosizioneDebitoria> esoTPosizioneDebitorias = versamento.getEsoTPosizioneDebitorias();
				if (esoTPosizioneDebitorias == null || esoTPosizioneDebitorias.size()<=0) {
					numRecordDaElaborare -= 1;
					continue;
				}

				EsoTPosizioneDebitoria esoTPosizioneDebitoria = esoTPosizioneDebitorias.get(0);
				if (esoTPosizioneDebitoria.getNumRata().intValue() == numRataPreavviso) {
					if (esoTPosizioneDebitoria.getDtInvioPromemoria() != null) {
						numRecordDaElaborare -= 1;
						continue;
					}
				}
				
				
				if (numRataPreavviso == 1) {
					if (esoTPosizioneDebitorias.size()==2) {
						for (EsoTPosizioneDebitoria d : esoTPosizioneDebitorias) {
							if (d.getNumRata().intValue() == 1)
								esoTPosizioneDebitoria = d;
						}
					}
					else {
						numRecordDaElaborare -= 1;
						continue;
					}
				}
				else if (numRataPreavviso == 2) {
					if (esoTPosizioneDebitorias.size()==1) {
						esoTPosizioneDebitoria = esoTPosizioneDebitorias.get(0);
					}
					else if (esoTPosizioneDebitorias.size()==2) {
						for (EsoTPosizioneDebitoria d : esoTPosizioneDebitorias) {
							if (d.getNumRata().intValue() == 2)
								esoTPosizioneDebitoria = d;
						}
					}
					else {
						numRecordDaElaborare -= 1;
						continue;
					}
				}
				else {
					numRecordDaElaborare -= 1;
					continue;
				}

				
	
				if (esoTPosizioneDebitoria.getNumRata().intValue() == numRataPreavviso) {
					if (esoTPosizioneDebitoria.getDtInvioPromemoria() != null) {
						numRecordDaElaborare -= 1;
						continue;
					}
				}

				try {
					Date now = new Date();
					EsoTBatchLog batchLog = new EsoTBatchLog();
					batchLog.setEsoTBatchExec(batchExecPersist);
					batchLog.setCodFiscale(versamento.getCodFiscale());
					batchLog.setCodUserInserim(codUtente);
					batchLog.setCodUserAggiorn(codUtente);
					batchLog.setDInserim(now);
					batchLog.setDAggiorn(now);
					batchLog.setNTimestamp(0L);
					try {
						posizioneDebitoriaManager.sendMailPromemoriaDiScadenza(versamento.getIdEsoTVersamento(),
								Long.valueOf(numRataPreavviso));
						batchLog.setEsito("OK");
						numRecordOk++;
						batchLogs.add(batchLog);
						transactionManager.begin();
						batchLog.persist();
					} catch (BusinessException errAutorizza) {
						Log.error(errAutorizza);
						numRecordKo++;
						batchLog.setEsito("KO");
						batchLog.setLogExec(errAutorizza.getMessage());
						batchLogs.add(batchLog);
						transactionManager.begin();
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

		Log.info("InviaPromemoriaPagamentoRunnable - END");
	}

	// ==========================================================================
	// PRIVATE - METHODS

	private int getRataPreavviso(int ggPreavviso, Date dataScadenzaPrimaRata, Date dataScadenzaSecondaRata) {

		Date dataPreavviso = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dataScadenzaPrimaRata);
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - ggPreavviso);
		Date dataPrimaRata = cal.getTime();

		if (dataPreavviso.after(dataPrimaRata) && dataPreavviso.before(dataScadenzaPrimaRata))
			return 1;

		cal.setTime(dataScadenzaSecondaRata);
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - ggPreavviso);
		Date dataSecondaRata = cal.getTime();

		if (dataPreavviso.after(dataSecondaRata) && dataPreavviso.before(dataScadenzaSecondaRata))
			return 2;

		return 0; // non e' in periodo di preavviso
	}

	private boolean isServerCandidate(String cadidateIp) {

		if (cadidateIp == null || cadidateIp.trim().length() <= 0)
			return true;

		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					if (cadidateIp.equals(i.getHostAddress()))
						return true;
				}
			}
		} catch (Exception err) {
			return true;
		}

		return false;
	}

	private String printAddress() {
		String addresses = "";
		try {

			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					addresses += i.getHostAddress() + "; ";
				}
			}
			Log.info("Server addresses:" + addresses);
		} catch (Exception err) {
		}
		return addresses;
	}

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	private Parametro getParametroByCod(String codParametro) {
		Optional<EsoTParametro> parametroOpt = EsoTParametro.find("codParametro", codParametro).singleResultOptional();
		if (!parametroOpt.isPresent()) {
			throw new NotFoundException("getParametroByCod: " + codParametro);
		}
		return mappers.PARAMETRO.toModel(parametroOpt.get());
	}

	private Date getDataAnno(String paramName) {
		Parametro param = getParametroByCod(paramName);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int anno = cal.get(Calendar.YEAR);

		try {
			Date date = dateFormatter.parse(param.getDsValore());
			cal.setTime(date);
			cal.set(Calendar.YEAR, anno);
			return cal.getTime();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return null;
	}
}
