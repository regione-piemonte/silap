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
import java.util.ArrayList;
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
public class VersamentoProvincia implements LogicalComparator<VersamentoProvincia> {

	@JsonProperty("idEsoTVersamentoProvincia")
	private Long idEsoTVersamentoProvincia;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dFestaPatronale")
	private Date dFestaPatronale;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("idEsoTVersamento")
	private Long idEsoTVersamento;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("numGgLavorativiSettimanali")
	private Long numGgLavorativiSettimanali;
	
	@JsonProperty("silapDProvincia")
	private Provincia silapDProvincia;
	
	@JsonProperty("esoTVersamentoPvPeriodos")
	private List<VersamentoPvPeriodo> esoTVersamentoPvPeriodos;
	
	@JsonProperty("esoTVersamentoPvPeriodoMasters")
	private List<VersamentoPvPeriodoMaster> esoTVersamentoPvPeriodoMasters;
	
	@JsonProperty("esoTVersamentoPvGgExtraFestivis")
	private List<VersamentoPvGgExtraFestivi> esoTVersamentoPvGgExtraFestivis;
	
	@JsonProperty("esoTVersamentoPvSospensiones")
	private List<VersamentoPvSospensione> esoTVersamentoPvSospensiones;
	
	@JsonProperty("esoTVersamentoPvConvenziones")
	private List<VersamentoPvConvenzione> esoTVersamentoPvConvenziones;

	@JsonProperty("esoTVersamentoPvEsoneros")
	private List<VersamentoPvEsonero> esoTVersamentoPvEsoneros;
	
	@JsonProperty("esoTVersamentoPvCobs")
	private List<VersamentoPvCob> esoTVersamentoPvCobs;
	
	@JsonProperty("esoTVersamentoPvRicIns")
	private List<VersamentoPvRicIn> esoTVersamentoPvRicIns;
	
	@JsonProperty("importoAutomatico")
	private BigDecimal importoAutomatico;
	
	@Override
	public boolean logicalCompare(VersamentoProvincia other) {
		
		Log.info("VersamentoProvincia.logicaCompare");
		if (this.getSilapDProvincia() == null || other.getSilapDProvincia() == null)
			return false;
		
		
		boolean equals = false;

		// ESONERI
		if ((esoTVersamentoPvEsoneros == null || esoTVersamentoPvEsoneros.size()==0) && 
				other.getEsoTVersamentoPvEsoneros() == null)
			equals = true;
		
		if (!equals) {
			if (esoTVersamentoPvEsoneros == null && other.getEsoTVersamentoPvEsoneros() != null)
				return false;
			if (esoTVersamentoPvEsoneros != null && other.getEsoTVersamentoPvEsoneros() == null)
				return false;
			if (esoTVersamentoPvEsoneros.size() != other.getEsoTVersamentoPvEsoneros().size())
				return false;
		}
		
		for (VersamentoPvEsonero esonero : esoTVersamentoPvEsoneros) {
			boolean equalsFiglio = false;
			for (VersamentoPvEsonero otherEsonero : other.getEsoTVersamentoPvEsoneros()) {
				if (esonero.logicalCompare(otherEsonero)) {
					equalsFiglio = true;
					continue;
				}
			}
			
			if (!equalsFiglio)
				return false;
		}
		
		// CONVENZIONI
		List<VersamentoPvConvenzione> otherConv = new ArrayList<VersamentoPvConvenzione>();
		if (other.getEsoTVersamentoPvConvenziones() != null) {
			for (VersamentoPvConvenzione conv : other.getEsoTVersamentoPvConvenziones()) {
				if (conv.getDScadenza() != null)
					otherConv.add(conv);
			}
		}
		
		if (otherConv.size() == 0 && (esoTVersamentoPvConvenziones == null || esoTVersamentoPvConvenziones.size()==0))
			equals = true;
		
		
		if (!equals) {
			if (esoTVersamentoPvConvenziones == null && otherConv.size() == 0)
				return true;
			if (esoTVersamentoPvConvenziones.size() != other.getEsoTVersamentoPvConvenziones().size())
				return false;
		}
	
		
		for (VersamentoPvConvenzione convenzione : esoTVersamentoPvConvenziones) {
			boolean equalsFiglio = false;
			for (VersamentoPvConvenzione otherConvenzione : otherConv) {
				if (convenzione.logicalCompare(otherConvenzione)) {
					equalsFiglio = true;
					continue;
				}
			}
			
			if (!equalsFiglio)
				return false;
		}
		
		
		// RICONOSCIMENTI INABILITA
		equals = false;
		if ((esoTVersamentoPvRicIns == null || esoTVersamentoPvRicIns.size()==0) && 
				(other.getEsoTVersamentoPvRicIns() == null || other.getEsoTVersamentoPvRicIns().size()==0))
			equals = true;
		
		if (!equals) {
			if (esoTVersamentoPvRicIns == null && other.getEsoTVersamentoPvRicIns() != null)
				return false;
			if (esoTVersamentoPvRicIns != null && other.getEsoTVersamentoPvRicIns() == null)
				return false;
			if (esoTVersamentoPvRicIns.size() != other.getEsoTVersamentoPvRicIns().size())
				return false;
		}
		
		for (VersamentoPvRicIn ricIn : esoTVersamentoPvRicIns) {
			boolean equalsFiglio = false;
			for (VersamentoPvRicIn otherRicIn : other.getEsoTVersamentoPvRicIns()) {
				if (ricIn.logicalCompare(otherRicIn)) {
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
