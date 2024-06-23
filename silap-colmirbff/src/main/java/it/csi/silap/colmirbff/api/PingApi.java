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


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.csi.silap.colmirbff.api.dto.ApiError;
import it.csi.silap.colmirbff.api.dto.Messaggio;



@Path("ping")
@Produces({ MediaType.APPLICATION_JSON })
@Tag(name="ping", description="Ping")
public interface PingApi  {
	
	
	@GET
	@Path("call")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce l'utente per l'operatore",
    responses = { @ApiResponse(responseCode = "200", description = "Risposta",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response call(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	
	@GET
	@Path("current-date")
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce la data corrente",
    responses = { @ApiResponse(responseCode = "200", description = "Risposta",content = @Content(schema = @Schema(implementation = Date.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response getCurrentDate(@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	
}
