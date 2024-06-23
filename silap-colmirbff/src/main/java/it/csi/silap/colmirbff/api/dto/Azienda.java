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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Azienda {

	@JsonProperty("codFiscale")
	private String codFiscale;

	@JsonProperty("denomAzienda")
	private String denomAzienda;

	@JsonProperty("indirizzoSede")
	private String indirizzoSede;

	@JsonProperty("comuneSede")
	private Comune comuneSede;

	@JsonProperty("silapDCcnl")
	private Ccnl silapDCcnl;

	@JsonProperty("capSede")
	private String capSede;

	@JsonProperty("idSilAziAnagrafica")
	private Long idSilAziAnagrafica;
	
	@JsonProperty("idSilAziSede")
	private Long idSilAziSede;

	@JsonProperty("ragioneSocialeSede")
	private String ragioneSocialeSede;
	
	@JsonProperty("numCreditoResiduo")
	private BigDecimal numCreditoResiduo;
	
	@JsonProperty("idEsoTCreditoResiduo")
	private Long idEsoTCreditoResiduo;


}
