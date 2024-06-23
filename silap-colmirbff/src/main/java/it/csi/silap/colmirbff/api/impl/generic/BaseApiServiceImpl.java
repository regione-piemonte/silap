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
package it.csi.silap.colmirbff.api.impl.generic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import it.csi.silap.colmirbff.api.dto.ApiError;
import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.Decodifica;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.Parametro;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.TipoMessaggio;
import it.csi.silap.colmirbff.api.dto.common.CommonRequest;
import it.csi.silap.colmirbff.api.dto.common.CommonResponse;
import it.csi.silap.colmirbff.api.dto.mappers.SilapMappers;
import it.csi.silap.colmirbff.api.dto.response.DecodificaListResponse;
import it.csi.silap.colmirbff.api.dto.response.DecodificaResponse;
import it.csi.silap.colmirbff.exception.ServiceException;
import it.csi.silap.colmirbff.integration.dao.DAO;
import it.csi.silap.colmirbff.integration.entity.EsoTParametro;
import it.csi.silap.colmirbff.integration.entity.SilapDMessaggio;
import it.csi.silap.colmirbff.util.CommonUtils;
import it.csi.silap.colmirbff.util.QueryUtils;
import it.csi.silap.colmirbff.util.QueryUtils.Operatore;

/**
 * Classe base per tutte le implementazioni di servizi esposti. puo' contenere
 * alcune implementazioni comuni e alcuni metodi utili a tutti i servizi per
 * ridurre codice scritto
 * 
 * @author AB
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

	
	protected <R extends CommonResponse> Response buildManagedResponseLogEndNegativeApiError(Messaggio msg, HttpHeaders httpHeaders,String methodName) {
		ApiError error = new ApiError.Builder().setMessage(msg.getSilapDTipoMessaggio().getIdSilapDTipoMessaggio()).setCode(msg.getCodSilapDMessaggio()).setMessage(msg.getDsSilapDMessaggio()).build();
		return Response.status(Response.Status.SEE_OTHER).entity(error).build();
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
		Client client = getServiceClient();
		
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
			if (resp != null) {
				Object ret = resp.readEntity(entityResponseType);
				if (ret != null)
					Log.error("Errore " + resp.getStatus() + " nella chiamata servizio : " + urlService + "; parametri:" + ret.toString(), new ServiceException("Errore chiamata servizio: " + urlService));
				else Log.error("Errore " + resp.getStatus() + " nella chiamata servizio : " + urlService, new ServiceException("Errore chiamata servizio: " + urlService));	
			}
			if (entityErrorResponseType != null)
				throw new ServiceException("Errore " + resp.getStatus() + " nella chiamata servizio: " + urlService, resp.readEntity(entityErrorResponseType));
			return null;
		}
	}
	
	
	protected <T> T postServiceFullTollerance(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, Class<T> entityResponseType, String user, String password) {
		return postServiceFullTollerance(urlService,  queryParams,  headerParams,  request,  entityResponseType, user, password, null);
	}
	
	protected <T> T postServiceFullTollerance(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, Class<T> entityResponseType, String user, String password, GenericType<?> entityErrorResponseType) {

	    Client client = getServiceClient();
		
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
			T ret = resp.readEntity(entityResponseType);
			if (ret != null) {
				Log.error("Errore " + resp.getStatus() + " nella chiamata servizio : " + urlService + "; parametri:" + ret.toString(), new ServiceException("Errore chiamata servizio: " + urlService));
				return ret;
			}
			else Log.error("Errore " + resp.getStatus() + " nella chiamata servizio : " + urlService, new ServiceException("Errore chiamata servizio: " + urlService));
			return null;
		}
	}
	
	
	protected <T> T postService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, GenericType<T> entityResponseType, String user, String password) {
		return postService( urlService,  queryParams, headerParams, request, entityResponseType,  user,  password, null);
		
	}
	
	protected <T> T postService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Object request, GenericType<T> entityResponseType, String user, String password, GenericType<?> entityErrorResponseType) {
		Client client = getServiceClient();
		
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
		Client client = getServiceClient();
		
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
		
		Client client = getServiceClient();
		
		
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
	
	
	protected <T> T getService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, Class<T> entityResponseType,Class<?> entityErrorResponseType,  String user, String password) {
		
		Client client = getServiceClient();
		
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
		
		
		if (resp.getStatus() == 200) {
			return resp.readEntity(entityResponseType);
		}
		else {
			if (entityErrorResponseType != null)
				throw new ServiceException("Errore chiamata servizio: " + urlService, resp.readEntity(entityErrorResponseType));
			return null;
		}
	}
	
	protected <T> T getService(String urlService,  Map<String, Object> queryParams, Map<String, Object> headerParams, GenericType<T> entityResponseType, String user, String password) {
		
		Client client = getServiceClient();
		
		
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
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected DecodificaResponse findDecodificaById(Class<? extends PanacheEntityBase> entity, Object id) {
		try {			
			QueryUtils queryUtils = new QueryUtils();
			queryUtils.addParameterAnd("id", id);
			Method m = entity.getMethod("find", String.class, Map.class);			
			PanacheQuery query = (PanacheQuery<?>) m.invoke(null, queryUtils.getQuery() ,queryUtils.getParams()); 
			PanacheQuery<Decodifica> res = query.project(Decodifica.class);
			return new DecodificaResponse.Builder().setDecodifica(res.firstResult()).build();
		} catch (Exception e) {
			Log.error("[BaseApiServiceImpl::findDecodificaById]", e);
		}
		return new DecodificaResponse();
	}
	
	
	protected DecodificaListResponse findDecodifica(Class<? extends PanacheEntityBase> entity) {
		return findDecodifica(entity, null, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected DecodificaListResponse findDecodifica(Class<? extends PanacheEntityBase> entity, String dataInizioColumnName, String dataFineColumnName) {
		try {			
			QueryUtils queryUtils = new QueryUtils();
			if (dataInizioColumnName != null && dataFineColumnName != null)
				queryUtils.addValidRangeDateAnd(dataInizioColumnName, dataFineColumnName, new Date());
			Method m = entity.getMethod("find", String.class, Sort.class, Map.class);			
			PanacheQuery query = (PanacheQuery<?>) m.invoke(null, queryUtils.getQuery(), Sort.ascending("descr"),queryUtils.getParams()); 
			PanacheQuery<Decodifica> res = query.project(Decodifica.class);
			return new DecodificaListResponse.Builder().setList(res.list()).build();
		} catch (Exception e) {
			Log.error("[BaseApiServiceImpl::findDecodifica]", e);
		}
		return new DecodificaListResponse();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected DecodificaListResponse findDecodificaOrderByKey(Class<? extends PanacheEntityBase> entity, String dataInizioColumnName, String dataFineColumnName) {
		try {			
			QueryUtils queryUtils = new QueryUtils();
			if (dataInizioColumnName != null && dataFineColumnName != null)
				queryUtils.addValidRangeDateAnd(dataInizioColumnName, dataFineColumnName, new Date());
			Method m = entity.getMethod("find", String.class, Sort.class, Map.class);			
			PanacheQuery query = (PanacheQuery<?>) m.invoke(null, queryUtils.getQuery(), Sort.ascending("id"),queryUtils.getParams()); 
			PanacheQuery<Decodifica> res = query.project(Decodifica.class);
			return new DecodificaListResponse.Builder().setList(res.list()).build();
		} catch (Exception e) {
			Log.error("[BaseApiServiceImpl::findDecodificaOrderByKey]", e);
		}
		return new DecodificaListResponse();
	}
	
	
	protected DecodificaListResponse fillDecodifica(Class<? extends PanacheEntityBase> entity, String txt) {
		return fillDecodifica(entity, null, null, txt);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected DecodificaListResponse fillDecodifica(Class<? extends PanacheEntityBase> entity, String dataInizioColumnName, String dataFineColumnName, String txt) {
		try {			
			QueryUtils queryUtils = new QueryUtils();
			if (dataInizioColumnName != null && dataFineColumnName != null)
				queryUtils.addValidRangeDateAnd(dataInizioColumnName, dataFineColumnName, new Date());
			queryUtils.addParameterAnd("descr", txt, Operatore.LIKE_START);
			Method m = entity.getMethod("find", String.class, Sort.class, Map.class);			
			PanacheQuery query = (PanacheQuery<?>) m.invoke(null, queryUtils.getQuery(), Sort.ascending("descr"),queryUtils.getParams()); 
			PanacheQuery<Decodifica> res = query.page(Page.ofSize(SilapConstants.NUMERO_RECORD_COMMAD_COMPLETATION)).project(Decodifica.class);
			return new DecodificaListResponse.Builder().setList(res.list()).build();
		} catch (Exception e) {
			Log.error("[BaseApiServiceImpl::unchecked]", e);
		}
		return new DecodificaListResponse();
	}
	
	
	protected static class Authenticator implements ClientRequestFilter {

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

	
	private Client getServiceClient() {
		JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		ClientConfig configuration = new ClientConfig(jacksonJsonProvider);
		
		configuration.property("jersey.config.client.connectTimeout", 10000);
		configuration.property("jersey.config.client.readTimeout", 60000);
        
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.withConfig(configuration);
	    clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
	    clientBuilder.readTimeout(60, TimeUnit.SECONDS);
	    return clientBuilder.build();
	}
	
}
