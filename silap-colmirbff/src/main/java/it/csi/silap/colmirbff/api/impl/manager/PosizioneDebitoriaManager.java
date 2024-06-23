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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.Parametro;
import it.csi.silap.colmirbff.api.dto.PosizioneDebitoria;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.integration.entity.EsoDVersamentoStato;
import it.csi.silap.colmirbff.integration.entity.EsoTPosizioneDebitoria;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamento;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProvincia;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvPeriodo;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoStato;
import it.csi.silap.colmirbff.util.Attachment;
import it.csi.silap.colmirbff.util.CommonUtils;
import it.csi.silap.colmirbff.util.MailUtils;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;
import it.csi.silap.colmirbff.util.mime.MimeTypeContainer.MimeType;

@Dependent
public class PosizioneDebitoriaManager extends BaseApiServiceImpl {

	@Inject
	private TransactionManager transactionManager;
	
	@Inject
	private EpayapiManager epayapiManager;

	@Inject
	private MailUtils mailUtils;

	@ConfigProperty(name = "file.system.home")
	private String fileSystemHome;

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	public String getIdentificativoPagamento(String cfAzienda, Long annoRiferimento) {
		return getIdentificativoPagamento(cfAzienda, annoRiferimento, 1);
	}

	public String getIdentificativoPagamento(String cfAzienda, Long annoRiferimento, long numRata) {
		return "cod_" + cfAzienda + "_" + annoRiferimento + "_0" + numRata;
	}
	

	public File getFilePdfAvvisoPagemento(Long idVersamento) throws BusinessException {
		EsoTVersamento versamento = EsoTVersamento.findById(idVersamento);		
		File dir = new File(new File(fileSystemHome), Long.toString(versamento.getAnnoRiferimento()));
		dir.mkdirs();
		return new File(dir,
				getIdentificativoPagamento(versamento.getCodFiscale(), versamento.getAnnoRiferimento()) + ".pdf");
	}


	@SuppressWarnings("deprecation")
	public void creaPosizioneDebitoria(Long idVersamento) throws BusinessException {

		EsoTVersamento versamento = EsoTVersamento.findById(idVersamento);

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
		

		PosizioneDebitoria posizioneDebitoriaPrimaRata = null;
		PosizioneDebitoria posizioneDebitoriaSecondaRata = null;
		

		if (versamento.getNumRate().intValue() == 1) {
			BigDecimal imp = new BigDecimal(importoTotale);
			imp.setScale(2, BigDecimal.ROUND_HALF_UP);
			imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);

			posizioneDebitoriaPrimaRata = epayapiManager.createDebtPositio(versamento.getCodFiscale(),
					versamento.getDsDenominazioneAzienda(), versamento.getDsEmailRiferimento(),
					getIdentificativoPagamento(versamento.getCodFiscale(), versamento.getAnnoRiferimento(), 1l), imp,
					versamento.getAnnoRiferimento(), versamento.getNumRate(), 1l);

			inserisciPosizioneDebitoria(versamento, posizioneDebitoriaPrimaRata);
		} else {

			// prima rata
			double primaRataImporto = importoTotale / 2d;
			BigDecimal imp = new BigDecimal(primaRataImporto);
			imp.setScale(2, BigDecimal.ROUND_HALF_UP);
			imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);

			posizioneDebitoriaPrimaRata = epayapiManager.createDebtPositio(versamento.getCodFiscale(),
					versamento.getDsDenominazioneAzienda(), versamento.getDsEmailRiferimento(),
					getIdentificativoPagamento(versamento.getCodFiscale(), versamento.getAnnoRiferimento(), 1l), imp,
					versamento.getAnnoRiferimento(), versamento.getNumRate(), 1l);

			inserisciPosizioneDebitoria(versamento, posizioneDebitoriaPrimaRata);

			// seconda rata
			double secondaRataImporto = importoTotale - imp.doubleValue();
			imp = new BigDecimal(secondaRataImporto);
			imp.setScale(2, BigDecimal.ROUND_HALF_UP);
			imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);

			posizioneDebitoriaSecondaRata = epayapiManager.createDebtPositio(versamento.getCodFiscale(),
					versamento.getDsDenominazioneAzienda(), versamento.getDsEmailRiferimento(),
					getIdentificativoPagamento(versamento.getCodFiscale(), versamento.getAnnoRiferimento(), 2l), imp,
					versamento.getAnnoRiferimento(), versamento.getNumRate(), 2l);

			inserisciPosizioneDebitoria(versamento, posizioneDebitoriaSecondaRata);

		}

		File pdf = salvaPdf(versamento, posizioneDebitoriaPrimaRata, posizioneDebitoriaSecondaRata);

		sendMailAvviso(versamento, pdf);
		
		
		updateStatoVersamento(idVersamento, SilapConstants.STATO_AVVISO_INVIATO);

	}
	
	private boolean hasStato(long idVersamento, long stato) {
		List<EsoTVersamentoStato> list = EsoTVersamentoStato.list("esoTVersamento.idEsoTVersamento = ?1 and esoDVersamentoStato.id = ?2 and flgCurrentRecord = 'S'",
				idVersamento, stato);
		if (list != null && list.size()>0)
			return true;
		return false;
	}

	@Transactional
	public Messaggio getPosizioneDebitoria(Long idVersamento) throws BusinessException {

		EsoTVersamento versamento = EsoTVersamento.findById(idVersamento);
	

		List<EsoTPosizioneDebitoria> list = versamento.getEsoTPosizioneDebitorias();
		EsoTPosizioneDebitoria esoTPosizioneDebitoriaPrimaRata = list.get(0);
		EsoTPosizioneDebitoria esoTPosizioneDebitoriaSecondaRata = null;
		if (list.size() > 1) {
			for (EsoTPosizioneDebitoria d : list) {
				if (d.getNumRata().intValue() == 1)
					esoTPosizioneDebitoriaPrimaRata = d;
				else if (d.getNumRata().intValue() == 2)
					esoTPosizioneDebitoriaSecondaRata = d;
			}
		}

		PosizioneDebitoria posizioneDebitoriaPrimaRata = epayapiManager
				.getDebtPositionData(esoTPosizioneDebitoriaPrimaRata.getCodIuv());
		
		if (posizioneDebitoriaPrimaRata.getCodIur() != null && posizioneDebitoriaPrimaRata.getCodIur().trim().length()>0) {
			aggiornaPosizioneDebitoria(versamento, posizioneDebitoriaPrimaRata, 1l);
		}

		PosizioneDebitoria posizioneDebitoriaSecondaRata = null;
		if (esoTPosizioneDebitoriaSecondaRata != null) {
			posizioneDebitoriaSecondaRata = epayapiManager.getDebtPositionData(esoTPosizioneDebitoriaSecondaRata.getCodIuv());
			
			if (posizioneDebitoriaSecondaRata.getCodIur() != null && posizioneDebitoriaSecondaRata.getCodIur().trim().length()>0) {
				aggiornaPosizioneDebitoria(versamento, posizioneDebitoriaSecondaRata, 2l);
				if (!hasStato(idVersamento, SilapConstants.STATO_SALDO))
					updateStatoVersamento(idVersamento, SilapConstants.STATO_SALDO);
				
			}
			else if (posizioneDebitoriaPrimaRata.getCodIur() != null && posizioneDebitoriaPrimaRata.getCodIur().trim().length()>0) {
				if (!hasStato(idVersamento, SilapConstants.STATO_ACCONTO))
					updateStatoVersamento(idVersamento, SilapConstants.STATO_ACCONTO);
			} 
		}
		else {
			if (posizioneDebitoriaPrimaRata.getCodIur() != null && 
					posizioneDebitoriaPrimaRata.getCodIur().trim().length()>0) {
				if (!hasStato(idVersamento, SilapConstants.STATO_SALDO))
					updateStatoVersamento(idVersamento, SilapConstants.STATO_SALDO);
			}
		}
		

		Messaggio msg = null;
		String risultaRata1 = "non risulta";
		if (posizioneDebitoriaPrimaRata != null && 
				posizioneDebitoriaPrimaRata.getCodIur() != null && 
				posizioneDebitoriaPrimaRata.getCodIur().trim().length()>0)
			risultaRata1 = "risulta";
		if (versamento.getNumRate()==2) {
			String risultaRata2 = "non risulta";
			if (posizioneDebitoriaSecondaRata != null && 
					posizioneDebitoriaSecondaRata.getCodIur() != null && 
							posizioneDebitoriaSecondaRata.getCodIur().trim().length()>0)
				risultaRata2 = "risulta";
			
			msg = getMsg(SilapConstants.MSG_PAGOPA_DUE_RATE, 
					versamento.getAnnoRiferimento().toString().trim(),
					versamento.getDsDenominazioneAzienda().trim(),
					versamento.getCodFiscale().trim(),
					risultaRata1, 
					posizioneDebitoriaPrimaRata.getCodAvviso(),
					risultaRata2, 
					(posizioneDebitoriaSecondaRata != null) ? posizioneDebitoriaSecondaRata.getCodAvviso(): "");
		}
		else {
			msg = getMsg(SilapConstants.MSG_PAGOPA_UNA_RATA, 
					versamento.getAnnoRiferimento().toString().trim(),
					versamento.getDsDenominazioneAzienda().trim(), 
					versamento.getCodFiscale().trim(),
					risultaRata1, 
					posizioneDebitoriaPrimaRata.getCodAvviso());
		}
		
		return msg;
	}

	@SuppressWarnings("deprecation")
	public void sendMailPromemoriaDiScadenza(Long idVersamento, Long numRataPreavviso) throws BusinessException {
		
		try {
			transactionManager.begin();
			
			
			EsoTVersamento versamento = EsoTVersamento.findById(idVersamento);
			
			Parametro subjectParam = getParametroByCod(SilapConstants.PARAM_COD_OAVPA);
			String subject = null;
			if (subjectParam != null && subjectParam.getDsValore() != null) {
				subject = subjectParam.getDsValore();
				if (subject != null) subject = subject.replaceAll("'", "''");

				String tipoScadenza = getParametroByCod(SilapConstants.PARAM_COD_OAVPB).getDsValore();
				if (numRataPreavviso.intValue() == 1)
					tipoScadenza = getParametroByCod(SilapConstants.PARAM_COD_OAVPC).getDsValore();
				else
					tipoScadenza = getParametroByCod(SilapConstants.PARAM_COD_OAVPD).getDsValore();
				
				Object[] valueSubject = new Object[] { tipoScadenza, versamento.getAnnoRiferimento().toString().trim(),
						versamento.getDsDenominazioneAzienda().trim(), versamento.getCodFiscale().trim() };

				subject = MessageFormat.format(subject, CommonUtils.formatTextMessage(valueSubject));

			} else
				subject = "Testo non presente base dati (cod:" + SilapConstants.PARAM_COD_OAVPA + ")";

			List<EsoTPosizioneDebitoria> listPosizioniDebitorie = versamento.getEsoTPosizioneDebitorias();
			EsoTPosizioneDebitoria esoTPosizioneDebitoria = listPosizioniDebitorie.get(0);
			if (listPosizioniDebitorie.size() > 1) {
				for (EsoTPosizioneDebitoria d : listPosizioniDebitorie) {
					if (d.getNumRata().intValue() == numRataPreavviso.intValue())
						esoTPosizioneDebitoria = d;
				}
			}

			Parametro textMessageParam = getParametroByCod(SilapConstants.PARAM_COD_MAVPA);
			String textMessage = null;
			if (textMessageParam != null && textMessageParam.getDsValore() != null) {

				textMessage = textMessageParam.getDsValore();
				if (textMessage != null) textMessage = textMessage.replaceAll("'", "''");

				String tipoImporto = getParametroByCod(SilapConstants.PARAM_COD_MAVPB).getDsValore();
				if (numRataPreavviso.intValue() == 1)
					tipoImporto = getParametroByCod(SilapConstants.PARAM_COD_MAVPC).getDsValore();
				else
					tipoImporto = getParametroByCod(SilapConstants.PARAM_COD_MAVPD).getDsValore();

				BigDecimal imp = new BigDecimal(esoTPosizioneDebitoria.getImportoAtteso().doubleValue());
				imp.setScale(2, BigDecimal.ROUND_HALF_UP);
				imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);

				Object[] valueMessage = new Object[] { dateFormatter.format(esoTPosizioneDebitoria.getDtScadenza()),
						versamento.getAnnoRiferimento().toString().trim(), versamento.getCodFiscale().trim(),
						versamento.getDsDenominazioneAzienda().trim(), tipoImporto, imp.toString() };

				textMessage = MessageFormat.format(textMessage, CommonUtils.formatTextMessage(valueMessage));

			} else
				textMessage = "Testo non presente base dati (cod:" + SilapConstants.PARAM_COD_MAVPA + ")";

			Collection<String> to = new ArrayList<String>();
			to.add(versamento.getDsEmailRiferimento());
			mailUtils.sendHtml(to, subject, textMessage, null);
			
			
			esoTPosizioneDebitoria.setDtInvioPromemoria(new Date());
			esoTPosizioneDebitoria.persist();

			transactionManager.commit();
		} catch (Exception e) {
			throw BusinessException.createError("Invio mail promemoria pagamento fallito");
		}
	}

	// =============================================================================
	// PRIVATE - METHODS

	private EsoTPosizioneDebitoria inserisciPosizioneDebitoria(EsoTVersamento versamento,
			PosizioneDebitoria posizioneDebitoria) {

		EsoTPosizioneDebitoria esoTPosizioneDebitoria = mappers.POSIZIONE_DEBITORIA.toEntity(posizioneDebitoria);

		EsoTVersamento esoTVersamentoKey = new EsoTVersamento();
		esoTVersamentoKey.setIdEsoTVersamento(versamento.getIdEsoTVersamento());
		esoTPosizioneDebitoria.setEsoTVersamento(esoTVersamentoKey);

		String userInserim = (SilapThreadLocalContainer.UTENTE_CONNESSO != null
				&& SilapThreadLocalContainer.UTENTE_CONNESSO.get() != null)
						? SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc()
						: "BATCH_USER";
		String userAggiorn = userInserim;
		Date now = new Date();
		
		
		Parametro paymentType = getParametroByCod(SilapConstants.PARAM_COD_TYPPA);
		esoTPosizioneDebitoria.setCodVersamento(paymentType.getDsValore());
		
		
		esoTPosizioneDebitoria.setCodUserAggiorn(userAggiorn);
		esoTPosizioneDebitoria.setCodUserInserim(userInserim);
		esoTPosizioneDebitoria.setDtInserim(now);
		esoTPosizioneDebitoria.setDtAggiorn(now);
		esoTPosizioneDebitoria.setNtimestamp(0l);

		esoTPosizioneDebitoria.persist();
		return esoTPosizioneDebitoria;
	}
	
	
	private EsoTPosizioneDebitoria aggiornaPosizioneDebitoria(EsoTVersamento versamento,
			PosizioneDebitoria posizioneDebitoria, Long numRata) {
		
		List<EsoTPosizioneDebitoria> list = versamento.getEsoTPosizioneDebitorias();
		EsoTPosizioneDebitoria esoTPosizioneDebitoria = list.get(0);
		if (list.size() > 1) {
			for (EsoTPosizioneDebitoria d : list) {
				if (d.getNumRata().intValue() == numRata.intValue())
					esoTPosizioneDebitoria = d;
			}
		}
		
		if (posizioneDebitoria != null && posizioneDebitoria.getImportoPagato() != null && 
				posizioneDebitoria.getImportoPagato().doubleValue()>0) {
			
			Long timestamp = esoTPosizioneDebitoria.getNtimestamp()+1;
				
			esoTPosizioneDebitoria.setCodIur(posizioneDebitoria.getCodIur());
			esoTPosizioneDebitoria.setDsInfoAggiuntive(posizioneDebitoria.getDsInfoAggiuntive());
			esoTPosizioneDebitoria.setDtEsito(posizioneDebitoria.getDtEsito());
			esoTPosizioneDebitoria.setDtOperazione(posizioneDebitoria.getDtOperazione());
			esoTPosizioneDebitoria.setImportoPagato(posizioneDebitoria.getImportoPagato());
			esoTPosizioneDebitoria.setCodAvviso(posizioneDebitoria.getCodAvviso());
			esoTPosizioneDebitoria.setCodTransazione(posizioneDebitoria.getCodTransazione());

			
	
			EsoTVersamento esoTVersamentoKey = new EsoTVersamento();
			esoTVersamentoKey.setIdEsoTVersamento(versamento.getIdEsoTVersamento());
			esoTPosizioneDebitoria.setEsoTVersamento(esoTVersamentoKey);
	
			String userInserim = (SilapThreadLocalContainer.UTENTE_CONNESSO != null
					&& SilapThreadLocalContainer.UTENTE_CONNESSO.get() != null)
							? SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc()
									: "BATCH_USER";
			String userAggiorn = userInserim;
			Date now = new Date();
			
			
			esoTPosizioneDebitoria.setCodUserAggiorn(userAggiorn);
			esoTPosizioneDebitoria.setCodUserInserim(userInserim);
			esoTPosizioneDebitoria.setDtInserim(now);
			esoTPosizioneDebitoria.setDtAggiorn(now);
			esoTPosizioneDebitoria.setNtimestamp(timestamp);
	
			esoTPosizioneDebitoria.persist();

		}
		
		return esoTPosizioneDebitoria;
	}

	private File salvaPdf(EsoTVersamento versamento, PosizioneDebitoria posizioneDebitoriaPrimaRata,
			PosizioneDebitoria posizioneDebitoriaSecondaRata) throws BusinessException {

		try {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException ex) {
			}

			File dir = new File(new File(fileSystemHome), Long.toString(versamento.getAnnoRiferimento()));
			dir.mkdirs();
			File targetFile = new File(dir,
					getIdentificativoPagamento(versamento.getCodFiscale(), versamento.getAnnoRiferimento()) + ".pdf");

			byte[] pdfPrimaRata = epayapiManager.getPaymentNotice(posizioneDebitoriaPrimaRata.getCodIuv());

			if (posizioneDebitoriaSecondaRata != null) {
				byte[] pdfSecondaata = epayapiManager.getPaymentNotice(posizioneDebitoriaSecondaRata.getCodIuv());
				PDFMergerUtility ut = new PDFMergerUtility();
				ut.addSource(new ByteArrayInputStream(pdfPrimaRata));
				ut.addSource(new ByteArrayInputStream(pdfSecondaata));
				ut.setDestinationFileName(targetFile.getAbsolutePath());
				ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			} else {
				try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
					outputStream.write(pdfPrimaRata);
				}
			}
			
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException ex) {
			}
			
			return targetFile;
		} catch (Exception e) {
			throw BusinessException.createError("Creazione pdf promemoria di pagamento fallito");
		}
	}
	
	
	public File ricercaESalvaPdf(EsoTVersamento versamento, String codIuv1, String codIuv2) throws BusinessException {
		try {
			File dir = new File(new File(fileSystemHome), Long.toString(versamento.getAnnoRiferimento()));
			dir.mkdirs();
			File targetFile = new File(dir,
					getIdentificativoPagamento(versamento.getCodFiscale(), versamento.getAnnoRiferimento()) + ".pdf");
			byte[] pdfPrimaRata = epayapiManager.getPaymentNotice(codIuv1);
			if (codIuv2 != null) {
				byte[] pdfSecondaata = epayapiManager.getPaymentNotice(codIuv2);
				PDFMergerUtility ut = new PDFMergerUtility();
				ut.addSource(new ByteArrayInputStream(pdfPrimaRata));
				ut.addSource(new ByteArrayInputStream(pdfSecondaata));
				ut.setDestinationFileName(targetFile.getAbsolutePath());
				ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			} else {
				try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
					outputStream.write(pdfPrimaRata);
				}
			}
			return targetFile;
		} catch (Exception e) {
			throw BusinessException.createError("Creazione pdf promemoria di pagamento fallito");
		}
	}
	

	private void sendMailAvviso(EsoTVersamento versamento, File pdf) {

		try {

			Parametro subjectParam = getParametroByCod(SilapConstants.PARAM_COD_OAPPA);
			String subject = null;
			if (subjectParam != null && subjectParam.getDsValore() != null) {
				subject = subjectParam.getDsValore();
				if (subject != null) subject = subject.replaceAll("'", "''");
				Object[] valueSubject = new Object[] { versamento.getAnnoRiferimento().toString().trim(),
						versamento.getDsDenominazioneAzienda().trim(), versamento.getCodFiscale().trim() };
				subject = MessageFormat.format(subject, CommonUtils.formatTextMessage(valueSubject));

			} else
				subject = "Testo non presente base dati (cod:" + SilapConstants.PARAM_COD_OAPPA + ")";

			Parametro textMessageParam = getParametroByCod(SilapConstants.PARAM_COD_MAPPA);
			String textMessage = null;
			if (textMessageParam != null && textMessageParam.getDsValore() != null) {

				textMessage = textMessageParam.getDsValore();
				if (textMessage != null) textMessage = textMessage.replaceAll("'", "''");

				Object[] valueMessage = new Object[] { versamento.getAnnoRiferimento().toString().trim(),
						versamento.getCodFiscale().trim(), versamento.getDsDenominazioneAzienda().trim() };

				textMessage = MessageFormat.format(textMessage, CommonUtils.formatTextMessage(valueMessage));

			} else
				textMessage = "Testo non presente base dati (cod:" + SilapConstants.PARAM_COD_MAPPA + ")";

			Collection<String> to = new ArrayList<String>();
			
			to.add(versamento.getDsEmailRiferimento());

			List<Attachment> attachments = null;
			if (pdf != null && pdf.exists()) {
				try {
					byte[] bytes = Files.readAllBytes(pdf.toPath());
					attachments = new ArrayList<Attachment>();
					attachments.add(new Attachment(bytes, MimeType.PDF, pdf.getName()));
				}
				catch (Exception err) {
					Log.error("[PosizioneDebitoriaManager::sendMailAvviso]", err);
				}
			}
			
			mailUtils.sendHtml(to, subject, textMessage, attachments);

		} catch (BusinessException e) {
			throw BusinessException.createError("Invio mail avviso pagamento fallito");
		}

	}
	
	
	private EsoTVersamentoStato updateStatoVersamento(Long idEsoTVersamento, Long idStato) {
		
		String codUtente = "BATCH_USER";
		
		
		Date now = new Date();

		EsoTVersamentoStato.update(
				"flgCurrentRecord=?1, nTimestamp = (nTimestamp + 1) where esoTVersamento.idEsoTVersamento=?2", null,
				idEsoTVersamento);

		EsoTVersamentoStato stato = new EsoTVersamentoStato();
		EsoTVersamento esoTVersamento = new EsoTVersamento();
		esoTVersamento.setIdEsoTVersamento(idEsoTVersamento);
		stato.setEsoTVersamento(esoTVersamento);

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
	
 
}
