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
package it.csi.silap.colmirsrv.api.impl.generic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import it.csi.silap.colmirsrv.api.dto.ApiMessage;
import it.csi.silap.colmirsrv.api.dto.Messaggio;
import it.csi.silap.colmirsrv.api.dto.Parametro;
import it.csi.silap.colmirsrv.api.dto.TipoMessaggio;
import it.csi.silap.colmirsrv.api.dto.common.CommonRequest;
import it.csi.silap.colmirsrv.api.dto.common.CommonResponse;
import it.csi.silap.colmirsrv.api.dto.mappers.SilapMappers;
import it.csi.silap.colmirsrv.exception.ServiceException;
import it.csi.silap.colmirsrv.integration.dao.DAO;
import it.csi.silap.colmirsrv.integration.entity.EsoTParametro;
import it.csi.silap.colmirsrv.integration.entity.SilapDMessaggio;
import it.csi.silap.colmirsrv.util.CommonUtils;

/**
 * Classe base per tutte le implementazioni di servizi esposti. puo' contenere
 * alcune implementazioni comuni e alcuni metodi utili a tutti i servizi per
 * ridurre codice scritto
 * 
 * @author 1871
 *
 */

public class BaseApiServiceImpl {
	
	@Inject
	protected DAO dao;

	@Inject
	protected SilapMappers mappers;
	

	
	protected Messaggio getMsg(String code, Object... values) {	
		List<SilapDMessaggio> list = SilapDMessaggio.list("codSilapDMessaggio", code);
		if (list != null && list.size()>0) {
			
			Messaggio msg = mappers.MESSAGGIO.toModel(list.get(0));
			if (values != null && values.length>0) {
				try {
					//messaggio.replaceAll per consentire alla String.format di formattare i messaggi con apici 
					String messaggio = msg.getDsSilapDMessaggio();
					if (messaggio != null) {
						messaggio = messaggio.replaceAll("'", "''");
					}
					msg.setDsSilapDMessaggio(MessageFormat.format(messaggio, CommonUtils.formatTextMessage(values)));
				}
				catch (Exception err) {
					msg = new Messaggio();
					
					TipoMessaggio tipo = new TipoMessaggio();
					tipo.setIdSilapDTipoMessaggio("E");
					msg.setSilapDTipoMessaggio(tipo);
					msg.setCodSilapDMessaggio("E000");
					msg.setDsSilapDMessaggio("Attenzione codice messaggio " + code + ", placeholders errati");
					return msg;
				}
			}
			
			return msg;
		}
		
		Messaggio msg = new Messaggio();
		TipoMessaggio tipo = new TipoMessaggio();
		tipo.setIdSilapDTipoMessaggio("E");
		msg.setSilapDTipoMessaggio(tipo);
		msg.setCodSilapDMessaggio("E000");
		msg.setDsSilapDMessaggio("Attenzione codice messaggio " + code + " non mappato nella tabella SILAP_D_MESSAGGIO");
		return msg;
	}
	
	
	protected String getDescrMsg(String code, Object... values) {
		List<SilapDMessaggio> list = SilapDMessaggio.list("codSilapDMessaggio", code);
		if (list != null && list.size()>0 && list.get(0).getDsSilapDMessaggio() != null) {
			
			if (values != null && values.length>0) {
				try {					//messaggio.replaceAll per consentire alla String.format di formattare i messaggi con apici 
					String messaggio = list.get(0).getDsSilapDMessaggio();
					if (messaggio != null) {
						messaggio = messaggio.replaceAll("'", "''");
					}
					return MessageFormat.format(messaggio, CommonUtils.formatTextMessage(values));
				}
				catch (Exception err) {
					return "Attenzione codice messaggio " + code + ", placeholders errati";			
				}
			}
			return list.get(0).getDsSilapDMessaggio();
		}
		return "Attenzione codice messaggio " + code + " non mappato nella tabella SILAP_D_MESSAGGIO";
	}
	
	
	protected Parametro getParametroByCod(String codParametro) {
		
		Optional<EsoTParametro> parametroOpt = EsoTParametro.find("codParametro", codParametro).singleResultOptional();
		if(!parametroOpt.isPresent()) {
			throw new NotFoundException("getParametroByCod: "+codParametro);
		}
		return mappers.PARAMETRO.toModel(parametroOpt.get());
	}
	
	
	protected <R extends CommonResponse> Response buildManagedResponseLogEndNegative(Messaggio msg, HttpHeaders httpHeaders) {
		ApiMessage error = new ApiMessage.Builder().setCode(msg.getCodSilapDMessaggio()).setMessage(msg.getDsSilapDMessaggio()).build();
		CommonResponse common = new CommonResponse.Builder().setEsitoPositivo(false).addApiMessage(error).build();
		return Response.ok(common).build();
	}
	
	protected <R extends CommonResponse> Response buildManagedResponseLogEndNegative(Messaggio msg, HttpHeaders httpHeaders,String methodName) {
		ApiMessage error = new ApiMessage.Builder().setTipo(msg.getSilapDTipoMessaggio().getIdSilapDTipoMessaggio()).setCode(msg.getCodSilapDMessaggio()).setMessage(msg.getDsSilapDMessaggio()).build();
		CommonResponse common = new CommonResponse.Builder().setEsitoPositivo(false).addApiMessage(error).build();
		return Response.ok(common).build();
	}


	protected <R extends CommonResponse> Response buildManagedResponseLogEnd(HttpHeaders httpHeaders, R responseContent,String methodName) {
		return Response.ok(responseContent).build();
	}

	
	protected <R extends CommonResponse> Response buildManagedResponse(HttpHeaders httpHeaders, R responseContent) {
		return Response.ok(responseContent).build();
	}
	
	protected <T> T postService(String urlService, Object request, Class<T> entityResponseType) {		
		return postService(urlService, null, null, request, entityResponseType, null, null);
	}
	
	protected <T> T postService(String urlService, Map<String, Object> headerParams, Object request, Class<T> entityResponseType) {		
		return postService(urlService, null, headerParams, request, entityResponseType, null, null);
}
	
	
	protected <T> T postService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, Class<T> entityResponseType, String user, String password) {
		return postService(urlService,  queryParams,  headerParams,  request,  entityResponseType, user, password, null);
	}
	
	
	protected <T> T postService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, Class<T> entityResponseType, String user, String password, GenericType<?> entityErrorResponseType) {
		JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Client client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
		
		if (user != null && password != null) {
			Authenticator authenticator = new Authenticator(user, password);
			client.register(authenticator);
		}
		
		WebTarget target = client.target(urlService);
		if (queryParams != null) {
			for (Map.Entry<String, Object> entry : queryParams.entrySet())
				target = target.queryParam(entry.getKey(), entry.getValue());
		}
		
		if (headerParams == null)
			headerParams = new HashMap<String, Object>();
		
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		for (Map.Entry<String, Object> entry : headerParams.entrySet())
			invocationBuilder.header(entry.getKey(), entry.getValue());
		
		Response resp = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
		if (resp.getStatus() == 200)
			return resp.readEntity(entityResponseType);
		else {
			if (entityErrorResponseType != null)
				throw new ServiceException("Errore chiamata servizio: " + urlService, resp.readEntity(entityErrorResponseType));
			return null;
		}
	}
	
	
	protected <T> T postService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, GenericType<T> entityResponseType, String user, String password) {
		return postService( urlService,  queryParams, headerParams, request, entityResponseType,  user,  password, null);
		
	}
	
	protected <T> T postService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, GenericType<T> entityResponseType, String user, String password, GenericType<?> entityErrorResponseType) {
		JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Client client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
		
		if (user != null && password != null) {
			Authenticator authenticator = new Authenticator(user, password);
			client.register(authenticator);
		}
		
		WebTarget target = client.target(urlService);
		if (queryParams != null) {
			for (Map.Entry<String, Object> entry : queryParams.entrySet())
				target = target.queryParam(entry.getKey(), entry.getValue());
		}
		
		if (headerParams == null)
			headerParams = new HashMap<String, Object>();
		
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		for (Map.Entry<String, Object> entry : headerParams.entrySet())
			invocationBuilder.header(entry.getKey(), entry.getValue());
		
		Response resp = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
		
		if (resp.getStatus() == 200)
			return resp.readEntity(entityResponseType);
		else {
			if (entityErrorResponseType != null)
				throw new ServiceException("Errore chiamata servizio: " + urlService, resp.readEntity(entityErrorResponseType));
			return null;
		}
	}
	
	
	protected <T> T putService(String urlService, CommonRequest request, Class<T> entityResponseType) {		
		JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Client client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
		
		WebTarget target = client.target(urlService);
		Map<String, Object> headerParams = new HashMap<String, Object>();
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		
	
		for (Map.Entry<String, Object> entry : headerParams.entrySet())
			invocationBuilder.header(entry.getKey(), entry.getValue());
		

		Response r = invocationBuilder.put(Entity.entity(request, MediaType.APPLICATION_JSON));
		return r.readEntity(entityResponseType);
	}
	
	protected <T> T getService(String urlService, Class<T> entityResponseType) {
		return getService(urlService,null, entityResponseType);
	}
	
	protected <T> T getService(String urlService,  Map<String, Object> queryParams, Class<T> entityResponseType) {
		return getService( urlService, queryParams, null, entityResponseType, null, null);
	}
	
	protected <T> T getService(String urlService,  Map<String, Object> queryParams, Class<T> entityResponseType, String user, String password) {
		return getService( urlService, queryParams, null, entityResponseType, user,password);
	}
	
	protected <T> T getService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Class<T> entityResponseType) {
		return getService(urlService, queryParams, headerParams, entityResponseType, null,null);
	}
	
	
	
	protected <T> T getService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Class<T> entityResponseType, String user, String password) {
		
		JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Client client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
		
		
		if (user != null && password != null) {
			Authenticator authenticator = new Authenticator(user, password);
			client.register(authenticator);
		}
		
		WebTarget target = client.target(urlService);
		if (queryParams != null) {
			for (Map.Entry<String, Object> entry : queryParams.entrySet())
				target = target.queryParam(entry.getKey(), entry.getValue());
		}
		
		if (headerParams == null)
			headerParams = new HashMap<String, Object>();
	
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		for (Map.Entry<String, Object> entry : headerParams.entrySet())
			invocationBuilder.header(entry.getKey(), entry.getValue());
		
				
		Response resp = invocationBuilder.get();
		if (resp.getStatus() == 200)
			return resp.readEntity(entityResponseType);
		else return null;
	}
	
	
	protected <T> T getService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, GenericType<T> entityResponseType, String user, String password) {
		
		JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Client client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
		
		
		if (user != null && password != null) {
			Authenticator authenticator = new Authenticator(user, password);
			client.register(authenticator);
		}
		
		WebTarget target = client.target(urlService);
		if (queryParams != null) {
			for (Map.Entry<String, Object> entry : queryParams.entrySet())
				target = target.queryParam(entry.getKey(), entry.getValue());
		}
		
		if (headerParams == null)
			headerParams = new HashMap<String, Object>();
		
			
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		for (Map.Entry<String, Object> entry : headerParams.entrySet())
			invocationBuilder.header(entry.getKey(), entry.getValue());
		
				
		Response resp = invocationBuilder.get();
		if (resp.getStatus() == 200)
			return resp.readEntity(entityResponseType);
		else return null;
	}
	

	
	private static class Authenticator implements ClientRequestFilter {

		private final String user;
		private final String password;

		public Authenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		public void filter(ClientRequestContext requestContext) throws IOException {
			MultivaluedMap<String, Object> headers = requestContext.getHeaders();
			final String basicAuthentication = getBasicAuthentication();
			headers.add("Authorization", basicAuthentication);

		}

		private String getBasicAuthentication() {
			String token = this.user + ":" + this.password;
			try {
				return "BASIC " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				throw new IllegalStateException("Cannot encode with UTF-8", ex);
			}
		}
	}

	
	
}
