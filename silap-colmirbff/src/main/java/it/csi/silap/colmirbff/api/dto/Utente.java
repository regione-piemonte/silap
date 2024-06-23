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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Utente {

	@JsonProperty("nome")
	private String nome = null;

	@JsonProperty("cognome")
	private String cognome = null;

	@JsonProperty("ente")
	private String ente = null;

	@JsonProperty("ruolo")
	private String ruolo = null;

	@JsonProperty("codFisc")
	private String codFisc = null;

	@JsonProperty("livAuth")
	private Integer livAuth = null;

	@JsonProperty("community")
	private String community = null;
	
	@JsonProperty("ruoli")
	private List<Ruolo> ruoli;

}
