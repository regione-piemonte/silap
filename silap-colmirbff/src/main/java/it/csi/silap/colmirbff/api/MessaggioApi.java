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
import it.csi.silap.colmirbff.api.dto.Messaggio;


@Path("Messaggio")
@Produces({ MediaType.APPLICATION_JSON })
@Tag(name="Messaggio", description="Messaggi applicativi")
public interface MessaggioApi  {
	
	
	@GET
	@Path("{cod}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce il messaggio",
    responses = { @ApiResponse(responseCode = "200", description = "Messaggio",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response findByCod(@PathParam("cod") String cod,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	
	@GET
	@Path("find/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce il messaggio",
    responses = { @ApiResponse(responseCode = "200", description = "Messaggio",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response find(@PathParam("id") Long id,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	

	//content = @Content(schema = @Schema(implementation = ApiError.class))
	@GET
	@Path("findAll")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce tutti i messaggi",
    responses = { @ApiResponse(responseCode = "200", description = "Messaggi registrati sul sistema",content = @Content(array = @ArraySchema(uniqueItems = false,schema = @Schema(implementation = Messaggio.class)))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "#/components/schemas/Map"))
                    )})
	Response findAll(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
   
}
