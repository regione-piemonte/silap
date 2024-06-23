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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.microprofile.context.ManagedExecutor;

import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.api.impl.runnable.AutorizzaDichiarazioniRunnable;
import it.csi.silap.colmirbff.api.impl.runnable.CreazionePosizioniDebitorieRunnable;
import it.csi.silap.colmirbff.api.impl.runnable.InviaPromemoriaPagamentoRunnable;
import it.csi.silap.colmirbff.api.impl.runnable.PrevisioneDichiarazioniRunnable;
import it.csi.silap.colmirbff.exception.BusinessException;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;

@Dependent
public class VersamentoEsoneriBatchManager extends BaseApiServiceImpl {

	@Inject
	private ManagedExecutor managedExecutor;
	
	@Inject
	private AutorizzaDichiarazioniRunnable autorizzaDichiarazioniRunnable;
	
	@Inject
	private PrevisioneDichiarazioniRunnable previsioneDichiarazioniRunnable;
	
	@Inject
	private CreazionePosizioniDebitorieRunnable creazionePosizioniDebitorieRunnable;
	
	@Inject 
	private InviaPromemoriaPagamentoRunnable inviaPromemoriaPagamentoRunnable;
	
	
	public void autorizzaDichiarazioniBatch() {
		autorizzaDichiarazioniRunnable.setUtente(SilapThreadLocalContainer.UTENTE_CONNESSO.get());
		managedExecutor.execute(() -> autorizzaDichiarazioniRunnable.run());
	}

	public void creazionePosizioniDebitorieBatch() {
		creazionePosizioniDebitorieRunnable.setUtente(SilapThreadLocalContainer.UTENTE_CONNESSO.get());
		managedExecutor.execute(() -> creazionePosizioniDebitorieRunnable.run());
	}
	
	public void previsioneDichiarazioniBatch(String anno,String email, byte[] attachement) {
			
		List<String> cfAziende = new ArrayList<String>();
		
		if (attachement != null && attachement.length>0)
		try {
			InputStream is = new ByteArrayInputStream(attachement);
			Workbook wb = WorkbookFactory.create(is);

			Sheet sheet = wb.getSheetAt(0);
			Row row = null;
			boolean firstLine = true;
			DataFormatter formatter = new DataFormatter();
			
			for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum()+1; i++) {
				if (!firstLine) {
					row = sheet.getRow(i);
					cfAziende.add(formatter.formatCellValue(row.getCell(0)));
				}
				firstLine = false;
			}
		}
		catch (Exception err) {
			err.printStackTrace();
			throw new BusinessException("Errore nella lettura del file");
		}
		
		if (anno == null)
			throw new BusinessException("Anno non specificato");
		
		if (email == null)
			throw new BusinessException("Email non specificata");
		
		if (cfAziende == null || cfAziende.size()<=0)
			throw new BusinessException("Nessun codice fiscale nel file di upload");
		
		previsioneDichiarazioniRunnable.setAnnoRiferimento(Long.valueOf(anno));
		previsioneDichiarazioniRunnable.setEmail(email);
		previsioneDichiarazioniRunnable.setCfAziende(cfAziende);
		previsioneDichiarazioniRunnable.setUtente(SilapThreadLocalContainer.UTENTE_CONNESSO.get());
		managedExecutor.execute(() -> previsioneDichiarazioniRunnable.run());
	}
	
	
	
	public void inviaPromemoriaPagamentoBatch() {
		managedExecutor.execute(() -> inviaPromemoriaPagamentoRunnable.run());
	}

}
