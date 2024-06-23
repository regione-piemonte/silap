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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersamentoCobLav {

	@JsonProperty("idEsoTVersamentoPvCobLav")
	private Long idEsoTVersamentoPvCobLav;

	@JsonProperty("codFiscale")
	private String codFiscale;

	@JsonProperty("codiceComunicazioneReg")
	private String codiceComunicazioneReg;

	@JsonProperty("dsCognome")
	private String dsCognome;

	@JsonProperty("dsNome")
	private String dsNome;

	@JsonProperty("dsTipoComunicazione")
	private String dsTipoComunicazione;

	@JsonProperty("dsTipoContratto")
	private String dsTipoContratto;

	@JsonProperty("flgFulltime")
	private String flgFulltime;

	@JsonProperty("flgL68")
	private String flgL68;

	@JsonProperty("numOreSettMed")
	private Long numOreSettMed;

	@JsonProperty("siglaPv")
	private String siglaPv;


}
