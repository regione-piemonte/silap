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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import it.csi.iride2.policy.entity.Identita;
import it.csi.silap.colmirbff.api.UtenteApi;
import it.csi.silap.colmirbff.api.dto.Utente;
import it.csi.silap.colmirbff.api.dto.response.UtenteResponse;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.api.impl.manager.RuoloManager;
import it.csi.silap.colmirbff.interceptor.Audited;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;

@Provider
public class UtenteApiServiceImpl extends BaseApiServiceImpl implements UtenteApi {
	
	@Inject
	private RuoloManager ruoloManager;

	@Audited
	@Override
	public Response self(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {

		final String methodName = "self";

		UtenteResponse result = new UtenteResponse();
		Utente utente = SilapThreadLocalContainer.UTENTE_CONNESSO.get();
		Identita identita = SilapThreadLocalContainer.IDENTITA.get();
		utente.setRuoli(ruoloManager.getRuoli(identita.getCodFiscale()));
		result.setUtente(utente);
		return buildManagedResponseLogEnd(httpHeaders, result, methodName);
	}
	
	
	
}
