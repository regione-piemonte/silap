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
package it.csi.silap.colmirbff.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.logging.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Versamento implements LogicalComparator<Versamento> {

	@JsonProperty("idEsoTVersamento")
	private Long idEsoTVersamento;

	@JsonProperty("annoProtocollo")
	private Long annoProtocollo;

	@JsonProperty("annoRiferimento")
	private Long annoRiferimento;

	@JsonProperty("codFiscale")
	private String codFiscale;

	@JsonProperty("codFiscaleSoggetto")
	private String codFiscaleSoggetto;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("dsDenominazioneAzienda")
	private String dsDenominazioneAzienda;

	@JsonProperty("dsEmailRiferimento")
	private String dsEmailRiferimento;

	@JsonProperty("versamentoSede")
	private VersamentoSede versamentoSede;

	@JsonProperty("idSilAziAnagrafica")
	private Long idSilAziAnagrafica;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("numCreditoResiduo")
	private BigDecimal numCreditoResiduo;

	@JsonProperty("numProtocollo")
	private Long numProtocollo;
	
	@JsonProperty("dProtocollo")
	private Date dProtocollo;
	
	@JsonProperty("numRate")
	private Long numRate;

	@JsonProperty("partivaIva")
	private String partivaIva;

	@JsonProperty("esoTCreditoResiduo")
	private CreditoResiduo esoTCreditoResiduo;

	@JsonProperty("silapDCcnl")
	private Ccnl silapDCcnl;
	
	@JsonProperty("esoTVersamentoProspettos")
	private List<VersamentoProspetto> esoTVersamentoProspettos;
	
	@JsonProperty("esoTVersamentoStatos")
	private List<VersamentoStato> esoTVersamentoStatos;
	
	@JsonProperty("esoTVersamentoProvincias")
	private List<VersamentoProvincia> esoTVersamentoProvincias;
	
	@JsonProperty("esoTPosizioneDebitorias")
	private List<PosizioneDebitoria> esoTPosizioneDebitorias;
	
	@JsonProperty("printCalcoloAutomatico")
	private String printCalcoloAutomatico;
	
	@Override
	public boolean logicalCompare(Versamento other) {
		
		
		Log.info("DATABASE");
		Log.info(this.toString());
		Log.info("SERVIZIO");
		Log.info(other.toString());
		
		Log.info("Versamento.logicaCompare");
		boolean equals = false;

		// PROSPETTO
		if ((esoTVersamentoProspettos == null || esoTVersamentoProspettos.size() ==0) && 
				other.getEsoTVersamentoProspettos() == null)
			equals = true;
		
		if (!equals) {
			if (esoTVersamentoProspettos == null && other.getEsoTVersamentoProspettos() != null)
				return false;
			if (esoTVersamentoProspettos != null && other.getEsoTVersamentoProspettos() == null)
				return false;
			if (esoTVersamentoProspettos.size() != other.getEsoTVersamentoProspettos().size())
				return false;
		}
		
		for (VersamentoProspetto prospetto : esoTVersamentoProspettos) {
			boolean equalsFiglio = false;
			for (VersamentoProspetto otherProspetto : other.getEsoTVersamentoProspettos()) {
				if (prospetto.logicalCompare(otherProspetto)) {
					equalsFiglio = true;
					continue;
				}
			}
			
			if (!equalsFiglio)
				return false;
		}
		
		// PROVINCIA
		if ((esoTVersamentoProvincias == null || esoTVersamentoProvincias.size() ==0) 
				&& other.getEsoTVersamentoProvincias() == null)
			equals = true;
		
		if (!equals) {
			if (esoTVersamentoProvincias == null && other.getEsoTVersamentoProvincias() != null)
				return false;
			if (esoTVersamentoProvincias != null && other.getEsoTVersamentoProvincias() == null)
				return false;
			if (esoTVersamentoProvincias.size() != other.getEsoTVersamentoProvincias().size())
				return false;
		}
		
		
		for (VersamentoProvincia provincia : esoTVersamentoProvincias) {
			boolean equalsFiglio = false;
			for (VersamentoProvincia otherProvincia : other.getEsoTVersamentoProvincias()) {
				if (provincia.logicalCompare(otherProvincia)) {
					equalsFiglio = true;
					continue;
				}
			}
			
			if (!equalsFiglio)
				return false;
		}
				
		return true;
	}

}
