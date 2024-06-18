/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.api.dto;

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
public class Parametro {

	@JsonProperty("idEsoTParametro")
	private Long idEsoTParametro;

	@JsonProperty("codParametro")
	private String codParametro;

	@JsonProperty("dFineValidita")
	private Date dFineValidita;

	@JsonProperty("dInizioValidita")
	private Date dInizioValidita;

	@JsonProperty("dsNote")
	private String dsNote;

	@JsonProperty("dsValore")
	private String dsValore;

	@JsonProperty("flgTipoValore")
	private String flgTipoValore;


}
