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

import it.csi.silap.colmirbff.integration.entity.EsoTBatchExec;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchLog {

	@JsonProperty("idEsoTBatchLog")
	private Long idEsoTBatchLog;

	@JsonProperty("codFiscale")
	private String codFiscale;

	@JsonProperty("esito")
	private String esito;

	@JsonProperty("logExec")
	private String logExec;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("esoTBatchExec")
	private BatchExec esoTBatchExec;

}
