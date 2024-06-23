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
package it.csi.silap.colmirbff.api;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.csi.silap.colmirbff.api.dto.ApiError;
import it.csi.silap.colmirbff.api.dto.Decodifica;


@Path("decodifica")
@Produces({ MediaType.APPLICATION_JSON })
@Tag(name="decodifica", description="Servizio decodifiche")
public interface DecodificaApi  {
	
	//======================================================================
	// CATEGORIA AZIENDA
	@GET
	@Path("find-categoria-azienda/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce la categoria azienda",
    responses = { @ApiResponse(responseCode = "200", description = "Categorie azienda",content = @Content(schema = @Schema(implementation = Decodifica.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findCategoriaAziendaById(@PathParam("id") String id,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("find-categoria-azienda")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce tutte le categorie azienda",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche categorie azienda",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findCategoriaAzienda(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("fill-categoria-azienda/{txt}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce le categorie azienda autocomplete",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche categorie azienda autocomplete",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response fillCategoriaAzienda(@PathParam("txt") String txt,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	//======================================================================
	
	
	//======================================================================
	// COMUNE
	@GET
	@Path("find-comune/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce il comune",
    responses = { @ApiResponse(responseCode = "200", description = "Comune",content = @Content(schema = @Schema(implementation = Decodifica.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findComuneById(@PathParam("id") String id,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("find-comune")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce tutti i comuni",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche comune",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findComune(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("fill-comune/{txt}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce i comuni autocomplete",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche comune autocomplete",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response fillComune(@PathParam("txt") String txt,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	//======================================================================
	
	
	//======================================================================
	// PROVINCIA
	@GET
	@Path("find-provincia/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce la provincia",
    responses = { @ApiResponse(responseCode = "200", description = "Provincia",content = @Content(schema = @Schema(implementation = Decodifica.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findProvinciaById(@PathParam("id") String id,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("find-provincia")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce tutti le provincia",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche provincia",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findProvincia(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("fill-provincia/{txt}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce le provincie autocomplete",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche provincia autocomplete",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response fillProvincia(@PathParam("txt") String txt,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	//======================================================================
	
	
	//======================================================================
	// CCNL
	@GET
	@Path("find-ccnl/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce il ccnl",
    responses = { @ApiResponse(responseCode = "200", description = "Ccnl",content = @Content(schema = @Schema(implementation = Decodifica.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findCcnlById(@PathParam("id") String id,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("find-ccnl")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce tutti i ccnl",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche ccnl",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findCcnl(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("fill-ccnl/{txt}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce i ccnl autocomplete",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche ccnl autocomplete",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response fillCcnl(@PathParam("txt") String txt,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	//======================================================================
	
	
	//======================================================================
	// MOTIVO SOSPENSIONE
	@GET
	@Path("find-esoDVersamentoMotivoSospensione/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce il d_versamento_motivo_sospensione",
    responses = { @ApiResponse(responseCode = "200", description = "Motivo Sospensione",content = @Content(schema = @Schema(implementation = Decodifica.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findDVersamentoMotivoSospensioneById(@PathParam("id") String id,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("find-esoDVersamentoMotivoSospensione")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce tutti i d_versamento_motivo_sospensione",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche d_versamento_motivo_sospensione",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findDVersamentoMotivoSospensione(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("fill-esoDVersamentoMotivoSospensione/{txt}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce i d_versamento_motivo_sospensione autocomplete",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche d_versamento_motivo_sospensione autocomplete",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response fillDVersamentoMotivoSospensione(@PathParam("txt") String txt,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	//======================================================================
	
		
	//======================================================================
	// D_STATO
	@GET
	@Path("find-esoDStatoVersamento/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce il d_statoVersamento",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento",content = @Content(schema = @Schema(implementation = Decodifica.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findDStatoVersamentoById(@PathParam("id") String id,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("find-esoDStatoVersamento")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce tutti i d_statoVersamento",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche d_StatoVersamento",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findfindDStatoVersamento(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("fill-esoDStatoVersamento/{txt}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce i d_statoVersamento autocomplete",
    responses = { @ApiResponse(responseCode = "200", description = "Elenco decodifiche d_statoVersamento autocomplete",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Decodifica.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response fillDStatoVersamento(@PathParam("txt") String txt,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	//======================================================================
	 
}
