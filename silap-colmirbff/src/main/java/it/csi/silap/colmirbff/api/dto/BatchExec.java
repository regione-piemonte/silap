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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.silap.colmirbff.integration.entity.EsoTBatchLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchExec {

	@JsonProperty("idEsoTBatchExec")
	private Long idEsoTBatchExec;

	@JsonProperty("codEsoTBatchExec")
	private String codEsoTBatchExec;

	@JsonProperty("dInizioExec")
	private Date dInizioExec;

	@JsonProperty("dFineExec")
	private Date dFineExec;

	@JsonProperty("numRecordDaElaborare")
	private Long numRecordDaElaborare;

	@JsonProperty("numRecordOk")
	private Long numRecordOk;

	@JsonProperty("note")
	private String note;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;
	
	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("esoTBatchLogs")
	private List<BatchLog> esoTBatchLogs;

}
