/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.silap.colmirsrv.api.dto.Decodifica;
import it.csi.silap.colmirsrv.api.dto.common.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecodificaResponse extends CommonResponse {

	@JsonProperty("decodifica")
	private Decodifica decodifica;

	@Setter
	@Accessors(chain = true)
	public static final class Builder {

		private Decodifica decodifica;

		public DecodificaResponse build() {
			DecodificaResponse result = new DecodificaResponse();
			result.setDecodifica(decodifica);
			return result;
		}

	}
}
