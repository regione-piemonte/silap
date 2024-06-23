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
public class VersamentoMotivoSospensione {

	@JsonProperty("idEsoDVersamentoMotivoSospensione")
	private Long id;

	@JsonProperty("dFine")
	private Date dataFine;

	@JsonProperty("dInizio")
	private Date dataInizio;

	@JsonProperty("dsEsoDVersamentoMotivoSospensione")
	private String descr;

	@JsonProperty("percentuale")
	private Long percentuale;


}
