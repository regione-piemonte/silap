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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.TransactionManager;

import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ConfigUtils;
import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.Azienda;
import it.csi.silap.colmirbff.api.dto.DatiServiziProdisSilp;
import it.csi.silap.colmirbff.api.dto.RiconoscimentiInabilita;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.Utente;
import it.csi.silap.colmirbff.api.dto.VersamentoProspetto;
import it.csi.silap.colmirbff.api.dto.VersamentoProvincia;
import it.csi.silap.colmirbff.api.dto.VersamentoPvConvenzione;
import it.csi.silap.colmirbff.api.dto.VersamentoPvEsonero;
import it.csi.silap.colmirbff.api.dto.VersamentoPvProspetto;
import it.csi.silap.colmirbff.api.dto.common.ReportResponse;
import it.csi.silap.colmirbff.api.dto.mappers.SilapMappers;
import it.csi.silap.colmirbff.api.impl.manager.ProdisManager;
import it.csi.silap.colmirbff.api.impl.manager.SilprestManager;
import it.csi.silap.colmirbff.api.impl.manager.VersamentoEsoneriManager;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoDVersamentoTipoConvenzione;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchExec;
import it.csi.silap.colmirbff.integration.entity.EsoTBatchLog;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamento;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProspetto;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProvincia;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvConvenzione;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvEsonero;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvPeriodo;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvProspetto;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvRicIn;
import it.csi.silap.colmirbff.integration.entity.SilapDProvincia;
import it.csi.silap.colmirbff.util.Attachment;
import it.csi.silap.colmirbff.util.MailUtils;
import it.csi.silap.colmirbff.util.mime.MimeTypeContainer.MimeType;
import it.csi.silap.colmirbff.util.report.ReportTabellare;
import it.csi.silap.colmirbff.util.report.ReportUtilities;
import lombok.Data;

@ApplicationScoped
public class PrevisioneDichiarazioniRunnable implements Runnable {

	@Inject
	private VersamentoEsoneriManager versamentoEsoneriManager;

	@Inject
	private ProdisManager prodisManager;

	@Inject
	protected SilapMappers mappers;

	@Inject
	private TransactionManager transactionManager;
	
	@Inject
	private SilprestManager silprestManager;

	@Inject
	private MailUtils mailUtils;

	private Utente utente;
	private Long annoRiferimento;
	private String email;
	private List<String> cfAziende;

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public void setAnnoRiferimento(Long annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCfAziende(List<String> cfAziende) {
		this.cfAziende = cfAziende;
	}

	@Override
	public void run() {
		Log.info("PrevisioneDichiarazioniRunnable - START");

		Date startDate = new Date();
		EsoTBatchExec batchExec = null;
		EsoTBatchExec batchExecKeyPersist = null;
		List<PrevisioneAzienda> previsioni = null;
		
		Long numRecordTotali = 0L;

		try {
			batchExec = createLog(startDate);
			numRecordTotali = Long.valueOf(cfAziende.size());
			batchExec.setNumRecordDaElaborare(Long.valueOf(numRecordTotali));

			batchExecKeyPersist = new EsoTBatchExec();
			batchExecKeyPersist.setIdEsoTBatchExec(batchExec.getIdEsoTBatchExec());
			previsioni = elabora(cfAziende, annoRiferimento, batchExecKeyPersist);

			if (previsioni != null) {
				if (ConfigUtils.isProfileActive("dev")) {
					System.out.println("");
					System.out.println("");
					System.out.println("=====================================");
					System.out.println("PREVISIONI");
					for (PrevisioneAzienda previsione : previsioni) {
						System.out.println(previsione.toString());
					}
					System.out.println("=====================================");
				}
			}

			ReportResponse report = creaXLS(annoRiferimento, previsioni);

			// send mail
			Collection<String> to = new ArrayList<String>();
			to.add(email);

			Attachment attachment = new Attachment(report.getBytes(), MimeType.EXCEL_WORKBOOK,
					report.getFileNameTemplate() + ".xslx");
			List<Attachment> attachments = new ArrayList<Attachment>();
			attachments.add(attachment);

			mailUtils.send(to, "Previsione dichiarazioni esoneri anno: " + annoRiferimento,
					"In allegato la previsione dichiarazioni esoneri per l'anno di riferimento " + annoRiferimento,
					attachments);

		} catch (Exception err) {

			Log.error("[PrevisioneDichiarazioniRunnable::run]", err);
			err.printStackTrace();

		} finally {

			if (batchExec != null && batchExecKeyPersist != null) {
				try {
					transactionManager.begin();

					EsoTBatchExec esoTBatchExeEnd = EsoTBatchExec.findById(batchExecKeyPersist.getIdEsoTBatchExec());

					long numRecordKo = 0;
					long numRecordOk = 0;
					if (previsioni == null) {
						numRecordKo = numRecordTotali;
					} else {
						for (PrevisioneAzienda previsione : previsioni) {
							if (previsione.isError())
								numRecordKo++;
							else
								numRecordOk++;
						}
					}
					
					esoTBatchExeEnd.setNumRecordDaElaborare(numRecordTotali);
					esoTBatchExeEnd.setDFineExec(new Date());
					esoTBatchExeEnd.setNTimestamp(1L);
					esoTBatchExeEnd.setNumRecordKo(numRecordKo);
					esoTBatchExeEnd.setNumRecordOk(numRecordOk);
					esoTBatchExeEnd.persist();
					transactionManager.commit();
				} catch (Exception err) {
					Log.error("[PrevisioneDichiarazioniRunnable::run]", err);
				}
			}

		}

		Log.info("PrevisioneDichiarazioniRunnable - END");
	}

	@SuppressWarnings("deprecation")
	public List<PrevisioneAzienda> elabora(List<String> codFiscaleAziende, Long annoRiferimento,
			EsoTBatchExec batchExecKeyPersist) throws Exception {

		List<PrevisioneAzienda> previsioni = new ArrayList<PrevisioneAzienda>();

		for (String codFiscaleAzienda : codFiscaleAziende) {

			PrevisioneAzienda previsioneAzienda = new PrevisioneAzienda();
			previsioneAzienda.setCodiceFiscale(codFiscaleAzienda);
			previsioneAzienda.setAnnoRiferimento(annoRiferimento);
			previsioneAzienda.setProvincie(new ArrayList<>());

			Date now = new Date();
			EsoTBatchLog batchLog = new EsoTBatchLog();
			batchLog.setEsoTBatchExec(batchExecKeyPersist);
			batchLog.setCodFiscale(codFiscaleAzienda);
			batchLog.setCodUserInserim(utente.getCodFisc());
			batchLog.setCodUserAggiorn(utente.getCodFisc());
			batchLog.setDInserim(now);
			batchLog.setDAggiorn(now);
			batchLog.setNTimestamp(0L);

			try {

				EsoTVersamento versamento = new EsoTVersamento();
				versamento.setCodFiscale(codFiscaleAzienda);
				versamento.setPartivaIva(codFiscaleAzienda);

				versamento.setAnnoRiferimento(annoRiferimento);

				System.out.println(
						"Azienda: " + versamento.getCodFiscale() + " Anno rif.: " + versamento.getAnnoRiferimento());

				Azienda azienda = new Azienda();
				azienda.setCodFiscale(versamento.getCodFiscale());

				DatiServiziProdisSilp datiServiziProdisSilp = prodisManager.getDatiProdis(azienda, null,
						"" + annoRiferimento);
				if (datiServiziProdisSilp.getMessaggi() != null && datiServiziProdisSilp.getMessaggi().size() > 0) {
					for (ApiMessage message : datiServiziProdisSilp.getMessaggi()) {
						if (message.getError()) {
							throw new BusinessException("DatiDichiarazioniProvincieManager.getDati",
									datiServiziProdisSilp.getMessaggi());
						}
					}
				}
				versamento = compilaProspettoEsoneriConvenzioni(versamento, datiServiziProdisSilp);

				double totaleAzienda = 0d;
				if (versamento.getEsoTVersamentoProvincias() != null) {

					for (EsoTVersamentoProvincia versamentoProvincia : versamento.getEsoTVersamentoProvincias()) {

						StringBuffer printCalcoloAutomatico = new StringBuffer();
						EsoTVersamento versProv = versamentoEsoneriManager.impostaPeriodiProivincia(versamento,
								versamentoProvincia.getIdEsoTVersamentoProvincia(), printCalcoloAutomatico, false, false);
						
						
						EsoTVersamentoProvincia versamentoProvinciaPeriodi = null;
						if (versProv.getEsoTVersamentoProvincias() != null) {
							for (EsoTVersamentoProvincia pro : versProv.getEsoTVersamentoProvincias()) {
								if (pro.getIdEsoTVersamentoProvincia().longValue() == versamentoProvincia.getIdEsoTVersamentoProvincia().longValue()) {
									versamentoProvinciaPeriodi = pro; 
									break;
								}
							}
						}
						

						double totaleProvincia = 0d;
						if (versamentoProvinciaPeriodi != null) {
							if (versamentoProvinciaPeriodi.getEsoTVersamentoPvPeriodos() != null) {
								for (EsoTVersamentoPvPeriodo periodo : versamentoProvinciaPeriodi.getEsoTVersamentoPvPeriodos()) {
									if (!"C".equals(periodo.getFlgTipo()) && periodo.getImporto() != null ) {
										totaleProvincia += periodo.getImporto().doubleValue();
										totaleAzienda += periodo.getImporto().doubleValue();
									}
								}
							}
						}

						previsioneAzienda.getProvincie().add(new PrevisioneAziendaProvincia(
								versamentoProvincia.getSilapDProvincia().getDsSilapDProvincia(), totaleProvincia));

						printCalcoloAutomatico.append("\nTOTALE provincia: " + totaleProvincia + "\n");
						if (ConfigUtils.isProfileActive("dev")) {
							System.out.println(printCalcoloAutomatico.toString());
						}
					}
				}

				BigDecimal imp = new BigDecimal(totaleAzienda);
				imp.setScale(2, BigDecimal.ROUND_HALF_UP);
				imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (ConfigUtils.isProfileActive("dev")) {
					System.out.println("=====================================");
					System.out.println("TOTALE PREVISIONE AZIENDA: " + imp);
					System.out.println("=====================================");
				}

				previsioneAzienda.setImportoPrevisioneAzienda(totaleAzienda);
				previsioneAzienda.setError(false);
				batchLog.setEsito("OK");
				batchLog.setLogExec(imp.toString());

			} catch (BusinessException err) {
				previsioneAzienda.setError(true);
				batchLog.setEsito("KO");
				String errString = err.getMessage();
				if (err.getMessages() != null) {
					errString = "";
					for (ApiMessage m : err.getMessages())
						errString += m.getCode() + " - " + m.getMessage() + " | ";
				}
				batchLog.setLogExec(errString);
				
				Log.error("[PrevisioneDichiarazioniRunnable::elabora]"  + errString, err);
				err.printStackTrace();
			} catch (Exception err) {
				previsioneAzienda.setError(true);
				batchLog.setEsito("KO");
				batchLog.setLogExec(err.getMessage());
				Log.error("[PrevisioneDichiarazioniRunnable::elabora]", err);
				err.printStackTrace();
			}

			previsioni.add(previsioneAzienda);
			transactionManager.begin();
			batchLog.persist();
			transactionManager.commit();
		}
		return previsioni;
	}

	private EsoTVersamento compilaProspettoEsoneriConvenzioni(EsoTVersamento esoTVersamento,
			DatiServiziProdisSilp datiServiziProdisSilp) {

		// versamento prospetto
		if (datiServiziProdisSilp.getProspetti() != null) {
			for (VersamentoProspetto prospetto : datiServiziProdisSilp.getProspetti()) {

				EsoTVersamentoProspetto esoTVersamentoProspetto = mappers.VERSAMENTO_PROSPETTO.toEntity(prospetto);

				if (esoTVersamento.getEsoTVersamentoProspettos() == null)
					esoTVersamento.setEsoTVersamentoProspettos(new ArrayList<EsoTVersamentoProspetto>());
				esoTVersamento.getEsoTVersamentoProspettos().add(esoTVersamentoProspetto);

				// versamento prospetto provincia
				if (prospetto.getEsoTVersamentoPvProspettos() != null) {
					for (VersamentoPvProspetto pvProspetto : prospetto.getEsoTVersamentoPvProspettos()) {

						EsoTVersamentoPvProspetto esoTVersamentoPvProspetto = mappers.VERSAMENTO_PV_PROSPETTO
								.toEntity(pvProspetto);
						// provincia
						if (pvProspetto.getSilapDProvincia() != null
								&& pvProspetto.getSilapDProvincia().getId() != null) {
							SilapDProvincia silapDProvincia = new SilapDProvincia();
							silapDProvincia.setId(pvProspetto.getSilapDProvincia().getId());
							silapDProvincia
									.setDsSilapDProvincia(pvProspetto.getSilapDProvincia().getDsSilapDProvincia());
							esoTVersamentoPvProspetto.setSilapDProvincia(silapDProvincia);
						}

						if (esoTVersamentoProspetto.getEsoTVersamentoPvProspettos() == null)
							esoTVersamentoProspetto
									.setEsoTVersamentoPvProspettos(new ArrayList<EsoTVersamentoPvProspetto>());
						esoTVersamentoProspetto.getEsoTVersamentoPvProspettos().add(esoTVersamentoPvProspetto);
					}
				}
			}
		}

		Long idProvinciaProgressivo = 0L;

		// versamentoProvincia - versamentoPvEsonero
		if (datiServiziProdisSilp.getProvinceEsoneri() != null) {

			for (VersamentoProvincia versamentoProvincia : datiServiziProdisSilp.getProvinceEsoneri()) {
				if (versamentoProvincia.getSilapDProvincia() != null) {
					EsoTVersamentoProvincia esoTVersamentoProvincia = getVersamentoProvincia(esoTVersamento,
							versamentoProvincia.getSilapDProvincia().getId());
					// versamentoProvincia

					if (esoTVersamentoProvincia == null) {
						// inserimento versamento provincia
						esoTVersamentoProvincia = mappers.VERSAMENTO_PROVINCIA.toEntity(versamentoProvincia);

						// provincia
						SilapDProvincia silapDProvincia = new SilapDProvincia();
						silapDProvincia.setId(versamentoProvincia.getSilapDProvincia().getId());
						silapDProvincia
								.setDsSilapDProvincia(versamentoProvincia.getSilapDProvincia().getDsSilapDProvincia());
						esoTVersamentoProvincia.setSilapDProvincia(silapDProvincia);

						esoTVersamentoProvincia.setNumGgLavorativiSettimanali(5L);

						if (esoTVersamento.getEsoTVersamentoProvincias() == null)
							esoTVersamento.setEsoTVersamentoProvincias(new ArrayList<EsoTVersamentoProvincia>());

						esoTVersamentoProvincia.setIdEsoTVersamentoProvincia(idProvinciaProgressivo++);
						esoTVersamento.getEsoTVersamentoProvincias().add(esoTVersamentoProvincia);
					}

					// versamentoPvEsonero
					if (versamentoProvincia.getEsoTVersamentoPvEsoneros() != null) {

						for (VersamentoPvEsonero pvEsonero : versamentoProvincia.getEsoTVersamentoPvEsoneros()) {
							EsoTVersamentoPvEsonero esoTVersamentoPvEsonero = mappers.VERSAMENTO_PV_ESONERO
									.toEntity(pvEsonero);
							if (esoTVersamentoProvincia.getEsoTVersamentoPvEsoneros() == null)
								esoTVersamentoProvincia
										.setEsoTVersamentoPvEsoneros(new ArrayList<EsoTVersamentoPvEsonero>());
							esoTVersamentoProvincia.getEsoTVersamentoPvEsoneros().add(esoTVersamentoPvEsonero);
						}
					}
				}
			}
		}

		// versamentoProvincia - versamentoPvConvenzione
		if (datiServiziProdisSilp.getProvinceConvenzioni() != null) {
			for (VersamentoProvincia versamentoProvincia : datiServiziProdisSilp.getProvinceConvenzioni()) {
				if (versamentoProvincia.getSilapDProvincia() != null) {
					EsoTVersamentoProvincia esoTVersamentoProvincia = getVersamentoProvincia(esoTVersamento,
							versamentoProvincia.getSilapDProvincia().getId());
					// versamentoProvincia
					if (esoTVersamentoProvincia == null) {
						// inserimento versamento provincia
						esoTVersamentoProvincia = mappers.VERSAMENTO_PROVINCIA.toEntity(versamentoProvincia);

						// provincia
						SilapDProvincia silapDProvincia = new SilapDProvincia();
						silapDProvincia.setId(versamentoProvincia.getSilapDProvincia().getId());
						silapDProvincia
								.setDsSilapDProvincia(versamentoProvincia.getSilapDProvincia().getDsSilapDProvincia());
						esoTVersamentoProvincia.setSilapDProvincia(silapDProvincia);

						esoTVersamentoProvincia.setNumGgLavorativiSettimanali(5L);

						if (esoTVersamento.getEsoTVersamentoProvincias() == null)
							esoTVersamento.setEsoTVersamentoProvincias(new ArrayList<EsoTVersamentoProvincia>());
						esoTVersamentoProvincia.setIdEsoTVersamentoProvincia(idProvinciaProgressivo++);
						esoTVersamento.getEsoTVersamentoProvincias().add(esoTVersamentoProvincia);
					}

					// versamentoPvConvenzione
					if (versamentoProvincia.getEsoTVersamentoPvConvenziones() != null) {

						for (VersamentoPvConvenzione pvConvenzione : versamentoProvincia
								.getEsoTVersamentoPvConvenziones()) {
							EsoTVersamentoPvConvenzione esoTVersamentoPvConvenzione = mappers.VERSAMENTO_PV_CONVENZIONE
									.toEntity(pvConvenzione);

							// tipo convenzione
							if (pvConvenzione.getEsoDVersamentoTipoConvenzione() != null && pvConvenzione
									.getEsoDVersamentoTipoConvenzione().getIdSilL68TipoConvenzione() != null) {

								List<EsoDVersamentoTipoConvenzione> listTipi = EsoDVersamentoTipoConvenzione.list(
										"idSilL68TipoConvenzione",
										pvConvenzione.getEsoDVersamentoTipoConvenzione().getIdSilL68TipoConvenzione());

								if (listTipi != null && listTipi.size() > 0) {
									EsoDVersamentoTipoConvenzione esoDVersamentoTipoConvenzione = new EsoDVersamentoTipoConvenzione();
									esoDVersamentoTipoConvenzione.setIdEsoDVersamentoTipoConvenzione(
											listTipi.get(0).getIdEsoDVersamentoTipoConvenzione());
									esoTVersamentoPvConvenzione
											.setEsoDVersamentoTipoConvenzione(esoDVersamentoTipoConvenzione);
								}
							}

							if (esoTVersamentoProvincia.getEsoTVersamentoPvConvenziones() == null)
								esoTVersamentoProvincia
										.setEsoTVersamentoPvConvenziones(new ArrayList<EsoTVersamentoPvConvenzione>());
							esoTVersamentoProvincia.getEsoTVersamentoPvConvenziones().add(esoTVersamentoPvConvenzione);
						}
					}
				}
			}
		}
		
		// riconoscimento inabilita
		List<RiconoscimentiInabilita> riconoscimentiInabilitaSilp = silprestManager.findRiconoscimentiInabilita(
				esoTVersamento.getCodFiscale(), null, ""+annoRiferimento);
		if (riconoscimentiInabilitaSilp != null) {
			
			for (EsoTVersamentoProvincia esoTVersamentoProvincia : esoTVersamento.getEsoTVersamentoProvincias()) {
				for (RiconoscimentiInabilita riconoscimentiInabilita : riconoscimentiInabilitaSilp) {
					if (esoTVersamentoProvincia.getSilapDProvincia().getId().equals(riconoscimentiInabilita.getIdSilapDProvincia())) {
						
						if (esoTVersamentoProvincia.getEsoTVersamentoPvRicIns() == null)
							esoTVersamentoProvincia.setEsoTVersamentoPvRicIns(new ArrayList<EsoTVersamentoPvRicIn>());
						
						EsoTVersamentoPvRicIn esoTVersamentoPvRicIn = new EsoTVersamentoPvRicIn();
						esoTVersamentoPvRicIn.setDRiconoscimentoInvalidita(riconoscimentiInabilita.getDRiconoscInvalidita());
						esoTVersamentoPvRicIn.setDScadenza(riconoscimentiInabilita.getDScadenza());
						esoTVersamentoPvRicIn.setNumOreSettMed(riconoscimentiInabilita.getNumOreSettMed());
						
						esoTVersamentoProvincia.getEsoTVersamentoPvRicIns().add(esoTVersamentoPvRicIn);
				
					}
				}
			}			
		}
		
		

		return esoTVersamento;
	}

	private EsoTVersamentoProvincia getVersamentoProvincia(EsoTVersamento esoTVersamento, String idSilapDProvincia) {
		if (esoTVersamento.getEsoTVersamentoProvincias() != null) {
			for (EsoTVersamentoProvincia provincia : esoTVersamento.getEsoTVersamentoProvincias()) {
				if (provincia.getSilapDProvincia() != null && provincia.getSilapDProvincia().getId() != null
						&& provincia.getSilapDProvincia().getId().equalsIgnoreCase(idSilapDProvincia))
					return provincia;
			}
		}
		return null;
	}

	private EsoTBatchExec createLog(Date startDate) throws Exception {

		transactionManager.begin();
		EsoTBatchExec batchExec = new EsoTBatchExec();

		batchExec.setCodEsoTBatchExec(SilapConstants.BATCH_VERSAMENTO_PREVISIONE);
		batchExec.setDInizioExec(startDate);
		batchExec.setDFineExec(startDate);
		batchExec.setNumRecordDaElaborare(0L);
		batchExec.setNumRecordKo(0L);
		batchExec.setNumRecordOk(0L);
		batchExec.setCodUserInserim(utente.getCodFisc());
		batchExec.setCodUserAggiorn(utente.getCodFisc());
		batchExec.setDInserim(startDate);
		batchExec.setDAggiorn(startDate);
		batchExec.setNTimestamp(0L);
		batchExec.persist();
		transactionManager.commit();

		return batchExec;
	}

	@SuppressWarnings("deprecation")
	private ReportResponse creaXLS(Long annorifierimento, List<PrevisioneAzienda> previsioni) throws Exception {

		ReportTabellare reportTabellare = new ReportTabellare("Previsioni");
		reportTabellare.addNomiColonne("Azienda");
		reportTabellare.addNomiColonne("Anno rif.");
		reportTabellare.addNomiColonne("Provincia");
		reportTabellare.addNomiColonne("Importo previsione");

		for (PrevisioneAzienda previsione : previsioni) {
			for (PrevisioneAziendaProvincia prov : previsione.getProvincie()) {
				List<Object> cols = new ArrayList<Object>();
				cols.add(previsione.getCodiceFiscale());
				cols.add(previsione.getAnnoRiferimento());
				cols.add(prov.getProvincia());
				
				BigDecimal imp = new BigDecimal(prov.getImportoPrevisioneProvincia());
				imp.setScale(2, BigDecimal.ROUND_HALF_UP);
				imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);
				cols.add(imp);
				reportTabellare.addValori(cols);
			}
		}

		ReportResponse response = new ReportResponse();
		response.setFileNameTemplate("Previsioni" + annorifierimento);
		ReportUtilities.makeReportResponse(ReportUtilities.Format.EXCELL.getFormat(), response, reportTabellare);
		return response;
	}

	@Data
	private class PrevisioneAzienda {

		private String codiceFiscale;
		private Long annoRiferimento;
		private double importoPrevisioneAzienda;
		private List<PrevisioneAziendaProvincia> provincie;
		private boolean error;

		@SuppressWarnings("deprecation")
		@Override
		public String toString() {
			BigDecimal imp = new BigDecimal(importoPrevisioneAzienda);
			imp.setScale(2, BigDecimal.ROUND_HALF_UP);
			imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);
			return "AZIENDA: " + codiceFiscale + "; ANNO RIFERIMENTO: " + annoRiferimento + "; IMPORTO PREVISIONE: "
					+ imp + " {" + provincie.toString() + "}";
		}

	}

	@Data
	private class PrevisioneAziendaProvincia {

		private String provincia;
		private double importoPrevisioneProvincia;

		public PrevisioneAziendaProvincia(String provincia, double importoPrevisioneProvincia) {
			this.provincia = provincia;
			this.importoPrevisioneProvincia = importoPrevisioneProvincia;
		}

		@SuppressWarnings("deprecation")
		@Override
		public String toString() {
			BigDecimal imp = new BigDecimal(importoPrevisioneProvincia);
			imp.setScale(2, BigDecimal.ROUND_HALF_UP);
			imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);
			return "PROVINCIA: " + provincia + "; IMPORTO PREVISIONE: " + imp;
		}

	}

}
