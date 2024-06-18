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
package it.csi.silap.colmirsrv.api.dto.common;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.silap.colmirsrv.api.dto.ApiMessage;
import it.csi.silap.colmirsrv.api.dto.Messaggio;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponse {

	@JsonProperty("esitoPositivo")
	private Boolean esitoPositivo = true;

	@JsonProperty("apiMessages")
	private List<ApiMessage> apiMessages = new ArrayList<ApiMessage>();
	
	
	public void addApiMessage(ApiMessage apiMessage) {
		if (apiMessages == null)
			apiMessages = new ArrayList<ApiMessage>();
		apiMessages.add(apiMessage);
	}
	
	public void addApiMessages(List<ApiMessage> apiMessages) {
		if (apiMessages == null)
			apiMessages = new ArrayList<ApiMessage>();
		apiMessages.addAll(apiMessages);
	}
	
	public void addApiMessage(Messaggio message) {
		if (apiMessages == null)
			apiMessages = new ArrayList<ApiMessage>();
		apiMessages.add(new ApiMessage(message));
	}
	
	@Setter
	@Accessors(chain = true)
	public static final class Builder {

		private Boolean esitoPositivo;
		private List<ApiMessage> apiMessages;

		public CommonResponse build() {

			CommonResponse result = new CommonResponse();
			result.setApiMessages(apiMessages);
			result.setEsitoPositivo(esitoPositivo);
			return result;
		}

		public Builder addApiMessage(ApiMessage apiMessage) {
			if (apiMessages == null)
				apiMessages = new ArrayList<ApiMessage>();
			apiMessages.add(apiMessage);
			return this;
		}
		
		public Builder addApiMessage(Messaggio message) {
			if (apiMessages == null)
				apiMessages = new ArrayList<ApiMessage>();
			apiMessages.add(new ApiMessage(message));
			return this;
		}

	}
}
