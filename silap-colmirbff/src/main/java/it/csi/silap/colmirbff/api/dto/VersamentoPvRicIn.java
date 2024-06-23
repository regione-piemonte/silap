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
public class VersamentoPvRicIn implements LogicalComparator<VersamentoPvRicIn> {

	@JsonProperty("idEsoTVersamentoPvRicIn")
	private Long idEsoTVersamentoPvRicIn;

	@JsonProperty("dRiconoscimentoInvalidita")
	private Date dRiconoscimentoInvalidita;

	@JsonProperty("dScadenza")
	private Date dScadenza;

	@JsonProperty("numOreSettMed")
	private Long numOreSettMed;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@Override
	public boolean logicalCompare(VersamentoPvRicIn other) {
		Log.info("VersamentoPvConvenzione.logicaCompare");
		return Format.isData1UgualeAData2(dScadenza, other.dScadenza)
				&& Format.isData1UgualeAData2(dRiconoscimentoInvalidita, other.dRiconoscimentoInvalidita)
				&& Objects.equals(numOreSettMed, other.numOreSettMed);
	}

}
