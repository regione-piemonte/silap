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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersamentoSede {

	@JsonProperty("idEsoTVersamentoSede")
	private Long idEsoTVersamentoSede;

	@JsonProperty("codCap")
	private String codCap;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("dsDenominazioneSede")
	private String dsDenominazioneSede;

	@JsonProperty("dsIndirizzo")
	private String dsIndirizzo;

	@JsonProperty("idSilAziSede")
	private Long idSilAziSede;
	
	@JsonProperty("comune")
	private Comune comune;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;


}
