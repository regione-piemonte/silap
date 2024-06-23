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
package it.csi.silap.colmirbff.api.impl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import it.csi.silap.colmirbff.api.PingApi;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.response.CurrentDateResponse;
import it.csi.silap.colmirbff.api.dto.response.MsgResponse;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.interceptor.Audited;
import it.csi.silap.colmirbff.interceptor.UserControl;

@Provider
public class PingApiServiceImpl extends BaseApiServiceImpl implements PingApi {
	
	//@Inject private VersamentoEsoneriBatchManager versamentoEsoneriBatchManager;
	
	@UserControl
	@Audited
	@Override
	public Response call(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		
		final String methodName = "call";
		MsgResponse response = new MsgResponse();
		Messaggio msg = new Messaggio();
		msg.setDsSilapDMessaggio("PING colmirbff service");
		response.setEsitoPositivo(true);
		response.setMsg(msg);
		
		//versamentoEsoneriBatchManager.inviaPromemoriaPagamentoBatch();
		
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
		
	}
	
	
	@Override
	public Response getCurrentDate(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		
		Date currentDate = new Date();
		final String methodName = "getCurrentDate";
		CurrentDateResponse response = new CurrentDateResponse();
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(currentDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        
		response.setEsitoPositivo(true);
		response.setCurrentDate(calendar.getTime());
		return buildManagedResponseLogEnd(httpHeaders, response, methodName);
	}
	
	
	
}
