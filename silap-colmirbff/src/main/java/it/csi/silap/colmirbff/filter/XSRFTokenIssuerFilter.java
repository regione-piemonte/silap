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
package it.csi.silap.colmirbff.filter;

import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.Provider;

import io.quarkus.logging.Log;

@Provider
public class XSRFTokenIssuerFilter implements ContainerResponseFilter {

	public static final String COMPONENT_NAME = "colmirbff";

	private static final String XSRF_COOKIE_NAME = "XSRF-TOKEN";

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		Log.debug("[XSRFTokenIssuerFilter::filter] START");

		// Check if cookie already exists
		if (requestContext.getCookies().containsKey(XSRF_COOKIE_NAME)) {
			Log.debug("[XSRFTokenIssuerFilter::filter] no need to create a new token");
			Log.debug("[XSRFTokenIssuerFilter::filter] END");
			return;
		}

		Log.debug("[XSRFTokenIssuerFilter::filter] creating a new random token");
		String randomToken = UUID.randomUUID().toString();
		var tokenCookie = new NewCookie(XSRF_COOKIE_NAME, randomToken, "/", null, null, -1, true, false);
		responseContext.getHeaders().add("Set-Cookie", tokenCookie);
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		
		
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET");
		responseContext.getHeaders().add("Content-Type", "application/json");
		
		
		
	    	
		
		Log.debug("[XSRFTokenIssuerFilter::filter] END");
	}
}
