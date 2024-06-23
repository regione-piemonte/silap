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
package it.csi.silap.colmirbff.interceptor;

import java.io.Serializable;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.common.CommonResponse;
import it.csi.silap.colmirbff.api.impl.manager.UserControlManager;
import it.csi.silap.colmirbff.integration.entity.SilapDOperatore;

@UserControl
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class UserControlInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserControlManager userControlManager;

	@AroundInvoke
	public Object userControlExecution(InvocationContext invocationContext) throws Exception {
		
		// TODO INFRASTRUTTURA - controllo utente alle chiamate applicative
		/*
		if (!userControlManager.control(getOperatore())) {
			ApiMessage error = new ApiMessage.Builder().setCode("ERR_USER").setMessage("Utente non abilitato al sistema").build();
			CommonResponse common = new CommonResponse.Builder().setEsitoPositivo(false).addApiMessage(error).build();
			return Response.ok(common).build();
		}
		*/
		return (Response) invocationContext.proceed();
	}

	private SilapDOperatore getOperatore() {
		SilapDOperatore operatore = new SilapDOperatore();
		return operatore;
	}
}
