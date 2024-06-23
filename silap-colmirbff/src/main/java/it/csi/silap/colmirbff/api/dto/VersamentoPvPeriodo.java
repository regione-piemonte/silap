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

import java.math.BigDecimal;
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
public class VersamentoPvPeriodo {

	@JsonProperty("idEsoTVersamentoPvPeriodo")
	private Long idEsoTVersamentoPvPeriodo;

	@JsonProperty("baseComputo")
	private Long baseComputo;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dFine")
	private Date dFine;

	@JsonProperty("dInizio")
	private Date dInizio;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("flgTipo")
	private String flgTipo;

	@JsonProperty("importo")
	private BigDecimal importo;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("numDisabiliInForza")
	private Long numDisabiliInForza;

	@JsonProperty("numEsoneratiAutocertificati")
	private Long numEsoneratiAutocertificati;

	@JsonProperty("numGgLavorativi")
	private Long numGgLavorativi;

	@JsonProperty("numLavoratoriEsonerati")
	private Long numLavoratoriEsonerati;

	@JsonProperty("numSoggettiCompensati")
	private Long numSoggettiCompensati;

	@JsonProperty("quotaRiserva")
	private Long quotaRiserva;

	@JsonProperty("numRiallineamentoNazionale")
	private Long numRiallineamentoNazionale;
	
	@JsonProperty("sovrapposto")
	private boolean sovrapposto;
	
	@JsonProperty("silapDCategoriaAzienda")
	private CategoriaAzienda silapDCategoriaAzienda;
	
	private Long idEsonero;

}
