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
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ruolo {

	public Ruolo() {
	}

	public Ruolo(Ruolo other) {
		this.setIdSilapDRuolo(other.getIdSilapDRuolo());
		this.setDsDescrizione(other.getDsDescrizione());
		this.setDsSilapDRuolo(other.getDsSilapDRuolo());
		this.setRuoloFunzioni(other.getRuoloFunzioni());

		this.setDenominazioneSoggettoAbilitato(other.getDenominazioneSoggettoAbilitato());
		this.setCodiceFiscaleSoggettoAbilitato(other.getCodiceFiscaleSoggettoAbilitato());
	}

	@JsonProperty("idSilapDRuolo")
	private Long idSilapDRuolo;

	@JsonProperty("dsDescrizione")
	private String dsDescrizione;

	@JsonProperty("dsSilapDRuolo")
	private String dsSilapDRuolo;

	@JsonProperty("ruoloFunzioni")
	@ToString.Exclude
	private List<RuoloFunzione> ruoloFunzioni;

	@JsonProperty("operatore")
	private Operatore operatore;

	@JsonProperty("denominazioneSoggettoAbilitato")
	private String denominazioneSoggettoAbilitato;

	@JsonProperty("codiceFiscaleSoggettoAbilitato")
	private String codiceFiscaleSoggettoAbilitato;

}
