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
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.util.Format;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersamentoPvEsonero implements LogicalComparator<VersamentoPvEsonero> {

	@JsonProperty("idEsoTVersamentoPvEsonero")
	private Long idEsoTVersamentoPvEsonero;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dConcessione")
	private Date dConcessione;

	@JsonProperty("dDiniego")
	private Date dDiniego;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("dRichiesta")
	private Date dRichiesta;

	@JsonProperty("dScadenza")
	private Date dScadenza;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("percConcessione")
	private Long percConcessione;

	@JsonProperty("percRichiesta")
	private Long percRichiesta;

	@JsonProperty("dsTipoComunicazione")
	private String dsTipoComunicazione;
	
	@JsonProperty("dClassificazione")
	private Date dClassificazione;
	
	@JsonProperty("numClassificazione")
	private Long numClassificazione;
	
	
	@Override
	public boolean logicalCompare(VersamentoPvEsonero other) {
		Log.info("VersamentoPvEsonero.logicaCompare");
		return Format.isData1UgualeAData2(dClassificazione, other.dClassificazione)
				&& Format.isData1UgualeAData2(dConcessione, other.dConcessione) 
				&& Format.isData1UgualeAData2(dRichiesta, other.dRichiesta)
				&& Format.isData1UgualeAData2(dScadenza, other.dScadenza)
				&& Objects.equals(dsTipoComunicazione, other.dsTipoComunicazione)
				&& Objects.equals(numClassificazione, other.numClassificazione)
				&& Objects.equals(percConcessione, other.percConcessione)
				&& Objects.equals(percRichiesta, other.percRichiesta);
	}


	
	
	
	
	
	
	
}
