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
package it.csi.silap.colmirsrv.interceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import it.csi.silap.colmirsrv.api.dto.SilapConstants;
import it.csi.silap.colmirsrv.api.dto.common.CommonResponse;
import it.csi.silap.colmirsrv.api.impl.manager.CsiLogAuditManager;
import it.csi.silap.colmirsrv.integration.entity.CsiLogAudit;
import it.csi.silap.colmirsrv.util.CommonUtils;
import it.csi.silap.colmirsrv.util.JsonUtils;
import it.csi.silap.colmirsrv.util.ResponseUtils;
import it.csi.silap.colmirsrv.util.SilapThreadLocalContainer;
import it.csi.util.performance.StopWatch;

public class AuditingInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ConfigProperty(name = "app.logger.name")
	String loggerName;
	
	@ConfigProperty(name = "app.component.name")
	String componentName;

	@Inject
	private CsiLogAuditManager csiLogAuditManager;

	
	
	@AroundInvoke
	public Object logExecution(InvocationContext invocationContext) throws Exception {
		
		
		System.out.println("AUDITED");
		

		boolean esito = true;
		String methodName = invocationContext.getMethod().getName();
		Response response = null;

		StopWatch watcher = startStopWatch();
		Log.info("[" + invocationContext.getMethod().getDeclaringClass().getName() + "::" + methodName + "] START");
		try {
			response = (Response) invocationContext.proceed();
			
		} catch (Throwable e) {

			esito = false;
			Log.error("[" + getClass().getSimpleName() + "::" + invocationContext.getMethod().getName()
					+ "] Exception of type " + e.getClass().getSimpleName(), e);
			e.printStackTrace();
			
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(),
							e.getClass().getSimpleName()))
					.build();
		
		} finally {

			if (Log.isDebugEnabled())
				Log.info("[" + invocationContext.getMethod().getDeclaringClass().getName() + "::" + methodName + "] END --> response=" + response.getEntity());
			else 
				Log.info("[" + invocationContext.getMethod().getDeclaringClass().getName() + "::" + methodName + "] END");
			
			
			
			
			stopStopWatch(watcher, invocationContext);
			CsiLogAudit log = insertCsiLogAudit(esito, methodName, invocationContext.getParameters(),
					esito ? response.getEntity() : null, SilapThreadLocalContainer.IDENTITA.get().getCodFiscale(),
					SilapThreadLocalContainer.CALLER_IP.get());

			csiLogAuditManager.log(log);
		}
		
		return response;
	}

	private StopWatch startStopWatch() {
		StopWatch watcher = new StopWatch(loggerName);

		watcher.start();
		return watcher;

	}

	private void stopStopWatch(StopWatch watcher, InvocationContext invocationContext) {
		if (watcher != null) {
			watcher.dumpElapsed(CommonUtils.truncClassName(invocationContext.getMethod().getDeclaringClass()),
					invocationContext.getMethod().getName() + "()",
					"invocazione API " + CommonUtils.truncClassName(invocationContext.getMethod().getDeclaringClass())
							+ "." + invocationContext.getMethod().getName(),
					"");
			watcher.stop();
		}
	}
	
	

	private CsiLogAudit insertCsiLogAudit(boolean esito, String operazione, Object[] oggOper,
			Object result, String codiceFiscale, String ipAddress) {
		try {
			Log.debug("[" + getClass().getSimpleName() + "::insertCsiLogAudit] BEGIN");

			CsiLogAudit log = new CsiLogAudit();
			log.setOperazione(operazione);
			String esitoStr = SilapConstants.OK;
			
			if (result == null)
				esitoStr = SilapConstants.KO + " - " + "Response nulla";
			else if (result instanceof CommonResponse) {
				if (!((CommonResponse)result).getEsitoPositivo())
					esitoStr = SilapConstants.KO + " - " + ResponseUtils.retrieveErrorsAsString((CommonResponse)result);
			}
			else esitoStr = result.toString();

			List<Object> parameter = new ArrayList<Object>();
			parameter.add(SilapThreadLocalContainer.ID_RUOLO.get());
			parameter.add(SilapThreadLocalContainer.ID_COD_FISC_SOGG_ABILITATO.get());
			
			if (oggOper != null && oggOper.length > 3)
				parameter.addAll(Arrays.asList(Arrays.copyOf(oggOper, oggOper.length - 3)));
			
			log.setOggOper(CommonUtils.troncaStringa(checkNvl(parameter), 3999));
			log.setDataOra(new Date());
			log.setIpApp(componentName);
			log.setUtente(codiceFiscale); // cod_fiscale_utente_iride
			log.setIpAddress(ipAddress);
			log.setKeyOper(CommonUtils.troncaStringa(esitoStr, 2000));

			return log;

		} finally {
			Log.debug("[" + getClass().getSimpleName() + "::insertCsiLogAudit] END");
		}
	}


	private String checkNvl(Object o) {
		return o == null ? "null" : JsonUtils.toJson(o);
	}

	

}
