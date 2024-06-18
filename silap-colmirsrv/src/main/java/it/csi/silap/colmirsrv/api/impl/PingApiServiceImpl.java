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
package it.csi.silap.colmirsrv.api.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import it.csi.silap.colmirsrv.api.PingApi;
import it.csi.silap.colmirsrv.api.dto.Messaggio;
import it.csi.silap.colmirsrv.api.dto.response.CurrentDateResponse;
import it.csi.silap.colmirsrv.api.dto.response.MsgResponse;
import it.csi.silap.colmirsrv.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirsrv.interceptor.Audited;

@Provider
public class PingApiServiceImpl extends BaseApiServiceImpl implements PingApi {
	
	@Audited
	@Override
  @RolesAllowed("epay")
	public Response call(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		
		final String methodName = "call";
		MsgResponse response = new MsgResponse();
		Messaggio msg = new Messaggio();
		msg.setDsSilapDMessaggio("PING colmirsrv service");
		response.setEsitoPositivo(true);
		response.setMsg(msg);
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
	
	
	
	@Override
	public Response testChiamata(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		Date currentDate = new Date();
		final String methodName = "testChiamata";
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
