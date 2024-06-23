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
public class RiconoscimentiInabilita {
	
	
	@JsonProperty("idEsoTRiconoscimentiInabilita")
	private Long idEsoTRiconoscimentiInabilita;
	
	@JsonProperty("idSilAziAnagrafica")
	private Long idSilAziAnagrafica;
	
	@JsonProperty("codFiscale")
	private String codFiscale;
	
	@JsonProperty("partivaIva")
	private String partivaIva;
	
	@JsonProperty("annoRiferimento")
	private Long annoRiferimento;
	
	@JsonProperty("idSilapDProvincia")
	private String idSilapDProvincia;
	
	@JsonProperty("dRiconoscInvalidita")
	private Date dRiconoscInvalidita;
	
	@JsonProperty("dScadenza")
	private Date dScadenza;
	
	@JsonProperty("codFiscaleLav")
	private String codFiscaleLav;
	
	@JsonProperty("dInizioRapporto")
	private Date dInizioRapporto;
	
	@JsonProperty("numOreSettMed")
	private Long numOreSettMed;
	

}
