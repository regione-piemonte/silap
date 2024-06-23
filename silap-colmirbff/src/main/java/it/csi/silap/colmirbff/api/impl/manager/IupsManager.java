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

import java.util.Date;
import java.util.GregorianCalendar;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.icu.util.Calendar;

import io.quarkiverse.cxf.annotation.CXFClient;
import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.Versamento;
import it.csi.silap.colmirbff.cxf.iupsv.DatiProtocollazioneDTO;
import it.csi.silap.colmirbff.cxf.iupsv.IupsvSrv;
import it.csi.silap.colmirbff.cxf.iupsv.ProtocolloDTO;
import it.csi.silap.colmirbff.cxf.iupsv.SoggettoDTO;
import it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO;
import lombok.Data;

@Dependent
public class IupsManager {

	@Inject
	@CXFClient("iupsv")
	private IupsvSrv iupsv;

	public Protocollo protocolla(Versamento versamento) throws Exception {
		Protocollo protocollo = null;
		try {

			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);

			UtenteProtocolloDTO utente = new UtenteProtocolloDTO();
			utente.setIdApplicativo("SILAP");
			utente.setIdProvincia("rp");

			DatiProtocollazioneDTO dati = new DatiProtocollazioneDTO();
			dati.setDataInvio(toXMLGregorianCalendar(now));
			dati.setOggetto("Dichiarazione versamento ai fini dell’esonero " + versamento.getAnnoRiferimento() + " "
					+ versamento.getCodFiscale());
			SoggettoDTO soggettoDTO = new SoggettoDTO();
			soggettoDTO.setCodFiscale(versamento.getCodFiscale());
			soggettoDTO.setDenominazione(versamento.getDsDenominazioneAzienda());
			soggettoDTO.setTipoSoggetto("G");
			if (versamento.getVersamentoSede() != null && versamento.getVersamentoSede().getComune() != null) {
				soggettoDTO.setDescrComune(versamento.getVersamentoSede().getComune().getDescr());
				if (versamento.getVersamentoSede().getComune().getSilapDProvincia() != null)
					soggettoDTO.setSiglaProvincia(
							versamento.getVersamentoSede().getComune().getSilapDProvincia().getDescr());
				soggettoDTO.setIndirizzo(versamento.getVersamentoSede().getDsIndirizzo());
				soggettoDTO.setCapComune(versamento.getVersamentoSede().getCodCap());
			}
			dati.setMittenteEsterno(soggettoDTO);
			dati.setTipoDestinatarioInterno("1");
			dati.setTipoProtocollo("A");

			ProtocolloDTO protocolloIup = iupsv.protocolla(utente, dati);

			protocollo = new Protocollo();
			protocollo.setNumeroProtocollo(Long.parseLong(protocolloIup.getNumeroProtocollo().trim()));
			protocollo.setDataProtocollo(protocolloIup.getDataProtocollo().toGregorianCalendar().getTime());
		} catch (Exception err) {
			Log.error("[IupsManager::protocolla]", err);
			throw err;
		}
		return protocollo;
	}

	private static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(date);
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		} catch (DatatypeConfigurationException ex) {
			Log.error("[IupsManager::protocolla]", ex);
		}
		return xmlCalendar;
	}

	@Data
	public class Protocollo {
		private Long numeroProtocollo;
		private Date dataProtocollo;

		public Long getAnnoProtocollo() {
			if (dataProtocollo != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataProtocollo);
				return Long.valueOf(cal.get(Calendar.YEAR));
			}
			return null;
		}
	}

}
