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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import it.csi.silap.colmirbff.api.MessaggioApi;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.response.MsgListResponse;
import it.csi.silap.colmirbff.api.dto.response.MsgResponse;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.integration.entity.SilapDMessaggio;
import it.csi.silap.colmirbff.interceptor.UserControl;



@Provider
public class MessaggioApiServiceImpl extends BaseApiServiceImpl implements MessaggioApi {

	
	@UserControl
	@Override
	public Response findByCod(String cod, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		MsgResponse result = new MsgResponse();
		result.setMsg(mappers.MESSAGGIO.toModel(SilapDMessaggio.find("codSilapDMessaggio", cod).firstResult()));
		return buildManagedResponseLogEnd(httpHeaders, result, "find");
	}
	

	@UserControl
	@Override
	public Response find(Long id, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		MsgResponse result = new MsgResponse();
		result.setMsg(mappers.MESSAGGIO.toModel(SilapDMessaggio.findById(id)));
		return buildManagedResponseLogEnd(httpHeaders, result, "find");
	}

	@UserControl
	@Override
	public Response findAll(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		MsgListResponse result = new MsgListResponse();
		
		List<Messaggio> msgs = mappers.MESSAGGIO.toModels(SilapDMessaggio.listAll());
		for (Messaggio msg : msgs)
			result.getMsgMap().put(msg.getCodSilapDMessaggio(), msg);
		return buildManagedResponseLogEnd(httpHeaders, result, "findAll");
	}
}
