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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import it.csi.silap.colmirbff.api.DecodificaApi;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.integration.entity.EsoDVersamentoMotivoSospensione;
import it.csi.silap.colmirbff.integration.entity.EsoDVersamentoStato;
import it.csi.silap.colmirbff.integration.entity.SilapDCategoriaAzienda;
import it.csi.silap.colmirbff.integration.entity.SilapDCcnl;
import it.csi.silap.colmirbff.integration.entity.SilapDComune;
import it.csi.silap.colmirbff.integration.entity.SilapDProvincia;
import it.csi.silap.colmirbff.interceptor.UserControl;


@Provider
public class DecodificaApiServiceImpl extends BaseApiServiceImpl implements DecodificaApi {
	
	/**
	 * Per implementare nuove decodifiche ricordarsi di cambiare nell' entity il campo id, descr, dataFine, dataInizio
	 */
	
	
	// ======================================================================
	// CATEGORIA AZIENDA
	@Override
	@UserControl
	public Response findCategoriaAziendaById(String id, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodificaById(SilapDCategoriaAzienda.class, id));
	}

	@Override
	@UserControl
	public Response findCategoriaAzienda(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodifica(SilapDCategoriaAzienda.class, "dataInizio", "dataFine"));
	}

	@Override
	@UserControl
	public Response fillCategoriaAzienda(String txt, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, fillDecodifica(SilapDCategoriaAzienda.class, "dataInizio", "dataFine", txt));
	}
	// ======================================================================
		
	
	// ======================================================================
	// COMUNE
	@Override
	@UserControl
	public Response findComuneById(String id, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodificaById(SilapDComune.class, id));
	}

	@Override
	@UserControl
	public Response findComune(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodifica(SilapDComune.class, "dataInizio", "dataFine"));
	}

	@Override
	@UserControl
	public Response fillComune(String txt, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, fillDecodifica(SilapDComune.class, "dataInizio", "dataFine", txt));
	}
	// ======================================================================
	
	
	// ======================================================================
	// PROVINCIA
	@Override
	@UserControl
	public Response findProvinciaById(String id, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodificaById(SilapDProvincia.class, id));
	}

	@Override
	@UserControl
	public Response findProvincia(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodifica(SilapDProvincia.class, "dataInizio", "dataFine"));
	}

	@Override
	@UserControl
	public Response fillProvincia(String txt, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, fillDecodifica(SilapDProvincia.class, "dataInizio", "dataFine", txt));
	}
	// ======================================================================
	
	
	
	// ======================================================================
	// CCNL
	@Override
	@UserControl
	public Response findCcnlById(String id, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodificaById(SilapDCcnl.class, id));
	}

	@Override
	@UserControl
	public Response findCcnl(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodifica(SilapDCcnl.class, "dataInizio", "dataFine"));
	}

	@Override
	@UserControl
	public Response fillCcnl(String txt, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, fillDecodifica(SilapDCcnl.class, "dataInizio", "dataFine", txt));
	}
	// ======================================================================
	
	
	
	// ======================================================================
	// D_STATO
	@Override
	@UserControl
	public Response findDStatoVersamentoById(String id, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodificaById(EsoDVersamentoStato.class, id));
	}

	@Override
	@UserControl
	public Response findfindDStatoVersamento(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodificaOrderByKey(EsoDVersamentoStato.class, "dataInizio", "dataFine"));
	}

	@Override
	@UserControl
	public Response fillDStatoVersamento(String txt, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, fillDecodifica(EsoDVersamentoStato.class, "dataInizio", "dataFine", txt));
	}
	// ======================================================================


	// ======================================================================
	// MOTIVO SOSPENSIONE
	@Override
	@UserControl
	public Response findDVersamentoMotivoSospensioneById(String id, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodificaById(EsoDVersamentoMotivoSospensione.class, id));
	}

	@Override
	@UserControl
	public Response findDVersamentoMotivoSospensione(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, findDecodifica(EsoDVersamentoMotivoSospensione.class, "dataInizio", "dataFine"));
	}

	@Override
	@UserControl
	public Response fillDVersamentoMotivoSospensione(String txt, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) {
		return buildManagedResponse(httpHeaders, fillDecodifica(EsoDVersamentoMotivoSospensione.class, "dataInizio", "dataFine", txt));
	}
	// ======================================================================
}
