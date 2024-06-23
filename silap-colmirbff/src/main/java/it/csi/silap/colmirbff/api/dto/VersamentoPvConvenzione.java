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
public class VersamentoPvConvenzione implements LogicalComparator<VersamentoPvConvenzione> {

	@JsonProperty("idEsoTVersamentoPvConvenzione")
	private Long idEsoTVersamentoPvConvenzione;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("dScadenza")
	private Date dScadenza;

	@JsonProperty("dStipula")
	private Date dStipula;

	@JsonProperty("esoDVersamentoTipoConvenzione")
	private VersamentoTipoConvenzione esoDVersamentoTipoConvenzione;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("numPosizioniAperte")
	private Long numPosizioniAperte;

	@Override
	public boolean logicalCompare(VersamentoPvConvenzione other) {
		Log.info("VersamentoPvConvenzione.logicaCompare");
		return Objects.equals(esoDVersamentoTipoConvenzione.getIdSilL68TipoConvenzione(),other.getEsoDVersamentoTipoConvenzione().getIdSilL68TipoConvenzione()) 
				&& Format.isData1UgualeAData2(dScadenza, other.dScadenza) 
				&& Format.isData1UgualeAData2(dStipula, other.dStipula)
				&& Objects.equals(numPosizioniAperte, other.numPosizioniAperte);
	}

	
	
}
