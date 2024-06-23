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
package it.csi.silap.colmirbff.api.dto.request;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

import it.csi.silap.colmirbff.api.dto.common.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormRicercaVersamentoEsoneri extends CommonRequest{
	
	@JsonProperty("codFisc")
	private String codFisc;
	
	@JsonProperty("denomAzienda")
	private String denomAzienda;
	
	@JsonProperty("dataInizio")
	private Date dataInizio;
	
	@JsonProperty("dataFine")
	private Date dataFine;
	
	@JsonProperty("statoDichiarazione")
	private List<Long> statoDichiarazione;
	
	@JsonProperty("annoProtocollo")
	private Integer annoProtocollo;
	
	@JsonProperty("numProtocollo")
	private Integer numProtocollo;
	
	@JsonProperty("flgUnicaSoluzione")
	private String flgUnicaSoluzione;
	
	@JsonProperty("flgDueRate")
	private String flgDueRate;
	

}
