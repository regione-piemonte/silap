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

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
public class VersamentoProspetto implements LogicalComparator<VersamentoProspetto> {

	@JsonProperty("idEsoTVersamentoProspetto")
	private Long idEsoTVersamentoProspetto;

	@JsonProperty("annoRiferimento")
	private Long annoRiferimento;

	@JsonProperty("baseComputoNazionale")
	private Long baseComputoNazionale;

	@JsonProperty("categoriaAzienda")
	private String categoriaAzienda;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("codiceRegionale")
	private String codiceRegionale;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("idEsoTVersamento")
	private Long idEsoTVersamento;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;
	
	@JsonProperty("esoTVersamentoPvProspettos")
	private List<VersamentoPvProspetto> esoTVersamentoPvProspettos;
	
	
	@Override
	public boolean logicalCompare(VersamentoProspetto other) {
		Log.info("VersamentoProspetto.logicaCompare");
		
		if (Objects.equals(codiceRegionale, other.getCodiceRegionale()) &&
				Objects.equals(annoRiferimento, other.getAnnoRiferimento()) &&
				Objects.equals(baseComputoNazionale, other.getBaseComputoNazionale())) {
			
			if ((esoTVersamentoPvProspettos ==  null || esoTVersamentoPvProspettos.size() == 0) 
				&& other.getEsoTVersamentoPvProspettos() == null)
				return true;
			
			if (esoTVersamentoPvProspettos ==  null && other.getEsoTVersamentoPvProspettos() != null)
				return false;
			
			if (esoTVersamentoPvProspettos != null && other.getEsoTVersamentoPvProspettos() == null)
				return false;
			
			if (esoTVersamentoPvProspettos.size() != other.getEsoTVersamentoPvProspettos().size())
				return false;
			
			
			for (VersamentoPvProspetto prospetto : esoTVersamentoPvProspettos) {
				
				boolean equalsFiglio = false;
				for (VersamentoPvProspetto otherProspetto : other.getEsoTVersamentoPvProspettos()) {
					if (prospetto.logicalCompare(otherProspetto)) {
						equalsFiglio = true;
						continue;
					}
				}
				
				if (!equalsFiglio)
					return false;
			}
			
			return true;
		}
		
		return false;
	}


}
