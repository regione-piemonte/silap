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
public class PosizioneDebitoria {

	@JsonProperty("idEsoTPosizioneDebitoria")
	private Long idEsoTPosizioneDebitoria;

	@JsonProperty("codIuv")
	private String codIuv;

	@JsonProperty("codAvviso")
	private String codAvviso;

	@JsonProperty("codIdPagamento")
	private String codIdPagamento;

	@JsonProperty("codVersamento")
	private String codVersamento;

	@JsonProperty("dtInizioValidita")
	private Date dtInizioValidita;

	@JsonProperty("dtFineValidita")
	private Date dtFineValidita;

	@JsonProperty("dtScadenza")
	private Date dtScadenza;

	@JsonProperty("numRata")
	private Long numRata;

	@JsonProperty("importoAtteso")
	private BigDecimal importoAtteso;

	@JsonProperty("dsCausale")
	private String dsCausale;

	@JsonProperty("importoPagato")
	private BigDecimal importoPagato;

	@JsonProperty("dsSoggettoVersante")
	private String dsSoggettoVersante;

	@JsonProperty("dsDebitore")
	private String dsDebitore;

	@JsonProperty("cfDebitore")
	private String cfDebitore;

	@JsonProperty("codIur")
	private String codIur;

	@JsonProperty("codTransazione")
	private String codTransazione;

	@JsonProperty("dtOperazione")
	private Date dtOperazione;

	@JsonProperty("dtEsito")
	private Date dtEsito;

	@JsonProperty("dsInfoAggiuntive")
	private String dsInfoAggiuntive;

	@JsonProperty("dsNote")
	private String dsNote;

	@JsonProperty("dtInserim")
	private Date dtInserim;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dtAggiorn")
	private Date dtAggiorn;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("ntimestamp")
	private Long ntimestamp;
	
	@JsonProperty("dtInvioPromemoria")
	private Date dtInvioPromemoria;
	
	@JsonProperty("dsPsp")
	private String dsPsp;

}
