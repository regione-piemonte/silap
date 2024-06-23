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
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiMessage {

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("message")
	private String message = null;
	
	@JsonProperty("title")
	private String title = null;

	@JsonProperty("error")
	private Boolean error = null;
	
	@JsonProperty("tipo")
	private String tipo = null;
	
	public ApiMessage() {}
	
	public ApiMessage(Messaggio msg) {
		this.code = msg.getCodSilapDMessaggio();
		this.message = msg.getDsSilapDMessaggio();
		this.title = msg.getDsTitolo();
		this.error = ("E".equalsIgnoreCase(msg.getSilapDTipoMessaggio().getIdSilapDTipoMessaggio()));
		this.tipo = msg.getSilapDTipoMessaggio().getIdSilapDTipoMessaggio();
	}

	@Setter
	@Accessors(chain = true)
	public static final class Builder {

		private String code;
		private String message;
		private String title;
		private Boolean error;
		private String tipo;

		public ApiMessage build() {

			ApiMessage result = new ApiMessage();
			result.setCode(code);
			result.setMessage(message);
			result.setTitle(title);
			result.setError(error);
			result.setTipo(tipo);
			return result;
		}

	}

}
