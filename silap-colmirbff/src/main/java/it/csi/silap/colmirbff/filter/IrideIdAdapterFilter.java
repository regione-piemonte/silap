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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.logging.Log;
import io.smallrye.config.SmallRyeConfig;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.exceptions.MalformedIdTokenException;
import it.csi.silap.colmirbff.api.dto.Utente;
import it.csi.silap.colmirbff.util.RequestUtils;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;

@Provider
public class IrideIdAdapterFilter implements ContainerRequestFilter {


	public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";
	
	
	@Context
    private HttpServletRequest request;
	
	@Context
	private UriInfo uriInfo;
	
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		Utente utente = getUserInfo(requestContext);
		
		if (utente == null) {
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
			return;
		}
		
		SilapThreadLocalContainer.CALLER_IP.set(RequestUtils.getIncomingIPAddress(request));
		SilapThreadLocalContainer.UTENTE_CONNESSO.set(utente);	
		
		SilapThreadLocalContainer.ID_RUOLO.set(getLong(requestContext.getHeaderString(SilapThreadLocalContainer.ID_RUOLO_KEY)));
		SilapThreadLocalContainer.ID_COD_FISC_SOGG_ABILITATO.set(requestContext.getHeaderString(SilapThreadLocalContainer.ID_COD_FISC_SOGG_ABILITATO_KEY));
	}
	
	
	private Utente getUserInfo(ContainerRequestContext requestContext) {
		String marker = getToken(requestContext);
		if (marker != null) {
			return initMarkerIride(marker, requestContext);

		} else if ("dev".equals(ConfigProvider.getConfig().unwrap(SmallRyeConfig.class).getProfiles().get(0))) {
			return initMarkerIride(null, requestContext);

		} else if (mustCheckPage(uriInfo.getRequestUri())) {
			Log.error("[IrideIdAdapterFilter::filter] Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
			return null;
		}
		return null;
	}
	
	
	private Utente initMarkerIride(String token, ContainerRequestContext containerRequest) {
		
		SilapThreadLocalContainer.TOKEN_SHIBBOLETH.set(token);

		
		Identita identita;
		try {
			if (token != null) {
				Log.debug("[IrideIdAdapterFilter::filter] token: " + token);
				identita = new Identita(token);
			}
			else {
				Log.debug("[IrideIdAdapterFilter::filter] token caricato da file");
				
				String pathHome = System.getProperty("user.home");
				String filenameToken = pathHome + "/token.txt";
				File fileToken = new File(filenameToken);
				String sToken = "abcdefghiABCDEFGHI1234509876=)(/&%$£zzTop)";
				if (fileToken.exists()) {
					FileReader fr = new FileReader(fileToken);
					BufferedReader br = new BufferedReader(fr);
					sToken = br.readLine();
					if (sToken != null) {
						sToken = sToken.trim();
					}
					br.close();
					fr.close();
					br = null;
					fr = null;
				}

				SilapThreadLocalContainer.TOKEN_SHIBBOLETH.set(sToken);
				identita = new Identita(sToken);
			}
			
		} catch (MalformedIdTokenException e) {
			Log.error("[IrideIdAdapterFilter::filter] Token non correttamente formattato. " + e.toString(), e);
			return null;

		} catch (Exception e) {
			Log.error("[IrideIdAdapterFilter::filter] Token non correttamente formattato. " + e.toString(), e);
			return null;
		}

		Log.trace("[IrideIdAdapterFilter::filter] Caricato marcatore IRIDE: " + identita);
		SilapThreadLocalContainer.IDENTITA.set(identita);
	
		
		Utente utente = new Utente();
		utente.setNome(identita.getNome());
		utente.setCognome(identita.getCognome());
		utente.setEnte("--");
		utente.setRuolo("--");
		utente.setCodFisc(identita.getCodFiscale());
		utente.setLivAuth(identita.getLivelloAutenticazione());
		utente.setCommunity(identita.getIdProvider());
		

		return utente;
	}
	
	
	private boolean mustCheckPage(URI requestURI) {
		return requestURI != null;
	}
	
	private Long getLong(String value) {
		if (value != null && value.trim().length()>0)
			return Long.parseLong(value);
		return null;
	}
	
	
	private String getToken(ContainerRequestContext httpreq) {
		String marker = (String) httpreq.getHeaderString(AUTH_ID_MARKER);
		try {
			String decodedMarker = new String(marker.getBytes("ISO-8859-1"), "UTF-8");
			return decodedMarker;
		} catch (java.io.UnsupportedEncodingException e) {
			return marker;
		} catch (NullPointerException npe) {
			return marker;
		}
	}
	
}
