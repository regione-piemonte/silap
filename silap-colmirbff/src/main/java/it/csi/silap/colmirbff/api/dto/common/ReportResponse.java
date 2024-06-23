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
package it.csi.silap.colmirbff.api.dto.common;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.csi.silap.colmirbff.util.mime.MimeTypeContainer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportResponse extends BaseResponse {

	private byte[] bytes;
	private String fileNameTemplate;
	private MimeTypeContainer mimeTypeContainer;

	public ReportResponse() {
	}

	public ReportResponse(String fileNameTemplate) {
		this.fileNameTemplate = fileNameTemplate;
	}

	public Response composeResponse() {
		return Response.ok(bytes, getMimeTypeContainer().getMimeType())
				.header("Content-Disposition", "attachment; filename=\"" + getFileName() + "\"")
				.header("Content-Type", "application/pdf")
				.build();
	}

	private String getFileName() {
		return fileNameTemplate + "." + getMimeTypeContainer().getExtension();
	}

}
