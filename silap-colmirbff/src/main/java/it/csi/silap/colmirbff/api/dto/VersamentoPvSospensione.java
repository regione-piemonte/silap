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
public class VersamentoPvSospensione {

	@JsonProperty("idEsoTVersamentoPvSospensione")
	private Long idEsoTVersamentoPvSospensione;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dFineSospensione")
	private Date dFineSospensione;

	@JsonProperty("dInizioSospensione")
	private Date dInizioSospensione;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("esoDVersamentoMotivoSospensione")
	private VersamentoMotivoSospensione esoDVersamentoMotivoSospensione;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("numLavoratoriSospesi")
	private Long numLavoratoriSospesi;

	@JsonProperty("percSospensione")
	private Long percSospensione;


}
