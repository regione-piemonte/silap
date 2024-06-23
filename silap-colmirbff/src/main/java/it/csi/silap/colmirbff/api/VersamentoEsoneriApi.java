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

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.csi.silap.colmirbff.api.dto.ApiError;
import it.csi.silap.colmirbff.api.dto.Azienda;
import it.csi.silap.colmirbff.api.dto.Messaggio;
import it.csi.silap.colmirbff.api.dto.Parametro;
import it.csi.silap.colmirbff.api.dto.Versamento;
import it.csi.silap.colmirbff.api.dto.VersamentoProvincia;
import it.csi.silap.colmirbff.api.dto.VersamentoPvPeriodo;
import it.csi.silap.colmirbff.api.dto.VersamentoStato;
import it.csi.silap.colmirbff.api.dto.request.FormPrevisioniVersamentoEsoneri;
import it.csi.silap.colmirbff.api.dto.request.FormRicercaVersamentoEsoneri;
import it.csi.silap.colmirbff.api.dto.response.RicercaVersamentoEsoneriResponse;
import it.csi.silap.colmirbff.api.dto.response.VersamentoPvPeriodosResponse;
import it.csi.silap.colmirbff.util.mime.MimeTypeContainer.MimeType;


@Path("versamento-esoneri")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name="versamento esoneri", description = "Servizio versamento esoneri")
public interface VersamentoEsoneriApi {

	@POST
	@Path("ricerca")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce i versamenti esoneri",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = RicercaVersamentoEsoneriResponse.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response ricercaVersamentoEsoneri(FormRicercaVersamentoEsoneri formRicercaVersamentoEsoneri,@Min(0) @QueryParam("page")@NotNull int page,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("stampa-ricerca")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MimeType.PDF })
	@Operation(summary = "Restituisce la stampa dei versamenti esoneri",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = File.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response stampaRicercaVersamentoEsoneri(FormRicercaVersamentoEsoneri formRicercaVersamentoEsoneri,@QueryParam("format") String format,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	@GET
	@Path("stampa-versamento/{idEsoTVersamento}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MimeType.PDF })
	@Operation(summary = "Restituisce la stampa del versamento visualizzato",
    responses = { @ApiResponse(responseCode = "200", description = "Stampa versamento visualizzato",content = @Content(schema = @Schema(implementation = File.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response stampaVersamento(@PathParam("idEsoTVersamento") Long id,
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	
	@GET
	@Path("ricerca-azienda/{cf}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce l'azienda da un codice fiscale",
    responses = { @ApiResponse(responseCode = "200", description = "Azienda",content = @Content(schema = @Schema(implementation = Azienda.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response ricercaAzienda(@PathParam("cf") String cf,@QueryParam("idVersamento") Long idVeramaento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
		
	
	@POST
	@Path("salva-dati-aziendali")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamenti esoneri salvato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response salvaDatiAziendali(Versamento versamento ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	
	@POST
	@Path("controllo-modifica")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamenti esoneri salvato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response controlloModifica(Versamento versamento ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("modifica-dati-aziendali")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamenti esoneri salvato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response modificaDatiAziendali(Versamento versamento ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("salva-gg-lavorativi-provincia")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamenti esoneri salvato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response salvaGGLavorativiProvincia(VersamentoProvincia versamentoProvincia ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("modifica-gg-lavorativi-provincia")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamenti esoneri salvato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response modificaGGLavorativiProvincia(VersamentoProvincia versamentoProvincia ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	
	@POST
	@Path("salva-sospensioni-provincia")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamenti esoneri salvato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response salvaSospensioniProvincia(VersamentoProvincia versamentoProvincia ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("modifica-sospensioni-provincia/{ripristina}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamenti esoneri salvato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento esoneri",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response modificaSospensioniProvincia(VersamentoProvincia versamentoProvincia,@PathParam("ripristina") boolean ripristina ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("cancella-periodo-automatico-provincia/{idProvincia}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamento",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento periodi provincia",content = @Content(schema = @Schema(implementation = VersamentoPvPeriodosResponse.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response cancellaPeriodoAutomaticoProvincia(@PathParam("idProvincia") Long idProvincia, VersamentoPvPeriodo periodo ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	
	@POST
	@Path("cancella-periodi-provincia/{idProvincia}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamento",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento periodi provincia",content = @Content(schema = @Schema(implementation = VersamentoPvPeriodosResponse.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response cancellaPeriodiProvincia(@PathParam("idProvincia") Long idProvincia, List<VersamentoPvPeriodo> periodi ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	

	@POST
	@Path("inserisci-periodo-provincia/{idProvincia}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamento periodo",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento periodo",content = @Content(schema = @Schema(implementation = VersamentoPvPeriodo.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response inserisciPeriodoProvincia(@PathParam("idProvincia") Long idProvincia, VersamentoPvPeriodo periodo,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	@POST
	@Path("cancella-periodo-provincia")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamento periodo",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento periodo",content = @Content(schema = @Schema(implementation = VersamentoPvPeriodo.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response cancellaPeriodoProvincia(VersamentoPvPeriodo periodo ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	
	
	@POST
	@Path("conferma-versamento-provincie")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il versamento",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento periodo",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response confermaVersamentoProvincie(Long idVersamento,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	
	
	@POST
	@Path("conferma-invia")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Conferma e invia il versamento",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento periodo",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response confermaInviaVersamento(Long idVersamento,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("autorizza-versamento")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce il versamento con lo stato aggiornato",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento stato esoneri",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response autorizzaVersamento(VersamentoStato versamentoStato ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@GET
	@Path("nuovo-versamento")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce un VersamentoStato predisposto per l'inserimento",
    responses = { @ApiResponse(responseCode = "200", description = "VersamentoStato",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response getNuovoVersamento(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	

	@GET
	@Path("update-stato/{idEsoTVersamento}/{idDVersamentoStato}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce il nuovo stato versamento modificato",
    responses = { @ApiResponse(responseCode = "200", description = "VersamentoStato",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response updateStatoVersamento(@PathParam("idEsoTVersamento") Long idEsoTVersamento,@PathParam("idDVersamentoStato") Long idDVersamentoStato, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	
	@GET
	@Path("parametro/{codParametro}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce un Parametro dato il codice",
    responses = { @ApiResponse(responseCode = "200", description = "Parametro",content = @Content(schema = @Schema(implementation = Parametro.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response getParametroByCod(@PathParam("codParametro") String codParametro,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@GET
	@Path("get-versamento/{idEsoTVersamento}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce la dichiarazione versamento",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response getVersamento(@PathParam("idEsoTVersamento") Long idEsoTVersamento,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@GET
	@Path("modifica-versamento/{idEsoTVersamento}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@Operation(summary = "Restituisce la dichiarazione versamento da modificare",
    responses = { @ApiResponse(responseCode = "200", description = "Versamento",content = @Content(schema = @Schema(implementation = Versamento.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response modificaVersamento(@PathParam("idEsoTVersamento") Long idEsoTVersamento,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	
	
	@POST
	@Path("autorizza-dichiarazioni-batch")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Batch per l'autorizzazione delle dichiarazioni di esoneo",
    responses = { @ApiResponse(responseCode = "200", description = "Restituisce il messaggio del lancio batch",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response autorizzaDichiarazioniBatch(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@GET
	@Path("download-previsione-dichiarazioni-template")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MimeType.PDF })
	@Operation(summary = "Scarica il template per il batch previsione dichiarazioni",
    responses = { @ApiResponse(responseCode = "200", description = "Restituisce il file template",content = @Content(schema = @Schema(implementation = File.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response downloadPrevisioneDichiarazioniTemplate(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	

	@POST
	@Path("upload-previsione-dichiarazioni")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Operation(summary = "Upload il file xls per lanciare il batch previsione dichiarazioni",
    responses = { @ApiResponse(responseCode = "200", description = "Restituisce il messaggio del lancio batch",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@Parameter(name = "attachment", schema=@Schema(implementation = File.class))
	@Parameter(name = "anno", schema=@Schema(type="string"))
	@Parameter(name = "email", schema=@Schema(type="string"))
	Response uploadPrevisioneDichiarazioni(@MultipartForm FormPrevisioniVersamentoEsoneri formPrevisioniVersamentoEsoneri, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	
	@POST
	@Path("creazione-posizioni-debitorie-batch")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Batch per la creazione delle posizioni debitorie",
    responses = { @ApiResponse(responseCode = "200", description = "Restituisce il messaggio del lancio batch",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response creazionePosizioniDebitorieBatch(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("check-posizioni-debitorie-batch")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Batch per la creazione delle posizioni debitorie",
    responses = { @ApiResponse(responseCode = "200", description = "Restituisce il messaggio del lancio batch",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response checkPosizioniDebitorieBatch(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
	
	
	@GET
	@Path("get-pdf-avviso-pagamento/{idVersamento}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MimeType.PDF })
	@Operation(summary = "Restituisce il pdf del pagamento",
	responses = { @ApiResponse(responseCode = "200", description = "Pdf avviso pagamento",content = @Content(schema = @Schema(implementation = File.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response getPdfAvvisoPagamento(@PathParam("idVersamento") String idVersamento,@Context SecurityContext securityContext, 
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	

	
	@GET
	@Path("get-posizione-debitoria/{idVersamento}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Operation(summary = "Restituisce la posizione debitoria",
    responses = { @ApiResponse(responseCode = "200", description = "Messaggio posizione debitoria",content = @Content(schema = @Schema(implementation = Messaggio.class))),
                    @ApiResponse(responseCode = "0", description = "Errore sul sistema",content = @Content(schema = @Schema(implementation = ApiError.class))) })
	Response getPosizioneDebitoria(@PathParam("idVersamento") String idVersamento ,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);
	
}


