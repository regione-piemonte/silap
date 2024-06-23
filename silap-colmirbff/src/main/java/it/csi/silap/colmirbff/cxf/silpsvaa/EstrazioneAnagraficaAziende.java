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
package it.csi.silap.colmirbff.cxf.silpsvaa;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.2
 * 2023-07-13T10:02:05.207+02:00
 * Generated source version: 2.7.2
 * 
 */
@WebService(targetNamespace = "urn:silpsvaa", name = "EstrazioneAnagraficaAziende")
@XmlSeeAlso({ObjectFactory.class})
public interface EstrazioneAnagraficaAziende {

    @WebResult(name = "ricercaSediAziendaReturn", targetNamespace = "urn:silpsvaa")
    @RequestWrapper(localName = "ricercaSediAzienda", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.RicercaSediAzienda")
    @WebMethod
    @ResponseWrapper(localName = "ricercaSediAziendaResponse", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.RicercaSediAziendaResponse")
    public java.util.List<it.csi.silap.colmirbff.cxf.silpsvaa.ElencoSedi> ricercaSediAzienda(
        @WebParam(name = "in0", targetNamespace = "urn:silpsvaa")
        java.lang.String in0,
        @WebParam(name = "in1", targetNamespace = "urn:silpsvaa")
        it.csi.silap.colmirbff.cxf.silpsvaa.ElencoSedi in1
    ) throws ServiziSilpException_Exception;

    @WebResult(name = "ricercaElencoAziendeReturn", targetNamespace = "urn:silpsvaa")
    @RequestWrapper(localName = "ricercaElencoAziende", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.RicercaElencoAziende")
    @WebMethod
    @ResponseWrapper(localName = "ricercaElencoAziendeResponse", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.RicercaElencoAziendeResponse")
    public java.util.List<it.csi.silap.colmirbff.cxf.silpsvaa.ElencoAziende> ricercaElencoAziende(
        @WebParam(name = "in0", targetNamespace = "urn:silpsvaa")
        java.lang.String in0,
        @WebParam(name = "in1", targetNamespace = "urn:silpsvaa")
        it.csi.silap.colmirbff.cxf.silpsvaa.ElencoAziende in1
    ) throws ServiziSilpException_Exception;

    @WebResult(name = "visualizzaDettaglioAziendaReturn", targetNamespace = "urn:silpsvaa")
    @RequestWrapper(localName = "visualizzaDettaglioAzienda", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.VisualizzaDettaglioAzienda")
    @WebMethod
    @ResponseWrapper(localName = "visualizzaDettaglioAziendaResponse", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.VisualizzaDettaglioAziendaResponse")
    public it.csi.silap.colmirbff.cxf.silpsvaa.AnagraficaAziende visualizzaDettaglioAzienda(
        @WebParam(name = "in0", targetNamespace = "urn:silpsvaa")
        java.lang.String in0,
        @WebParam(name = "in1", targetNamespace = "urn:silpsvaa")
        java.lang.String in1
    ) throws ServiziSilpException_Exception;

    @WebResult(name = "visualizzaDettaglioSedeAziendaReturn", targetNamespace = "urn:silpsvaa")
    @RequestWrapper(localName = "visualizzaDettaglioSedeAzienda", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.VisualizzaDettaglioSedeAzienda")
    @WebMethod
    @ResponseWrapper(localName = "visualizzaDettaglioSedeAziendaResponse", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.VisualizzaDettaglioSedeAziendaResponse")
    public it.csi.silap.colmirbff.cxf.silpsvaa.AnagraficaSediAziende visualizzaDettaglioSedeAzienda(
        @WebParam(name = "in0", targetNamespace = "urn:silpsvaa")
        java.lang.String in0,
        @WebParam(name = "in1", targetNamespace = "urn:silpsvaa")
        java.lang.String in1
    ) throws ServiziSilpException_Exception;

    @WebResult(name = "modificaSedeReturn", targetNamespace = "urn:silpsvaa")
    @RequestWrapper(localName = "modificaSede", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.ModificaSede")
    @WebMethod
    @ResponseWrapper(localName = "modificaSedeResponse", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.ModificaSedeResponse")
    public it.csi.silap.colmirbff.cxf.silpsvaa.AnagraficaSediAziende modificaSede(
        @WebParam(name = "in0", targetNamespace = "urn:silpsvaa")
        java.lang.String in0,
        @WebParam(name = "in1", targetNamespace = "urn:silpsvaa")
        it.csi.silap.colmirbff.cxf.silpsvaa.AnagraficaSediAziende in1
    ) throws ServiziSilpException_Exception;

    @WebResult(name = "modificaAziendaReturn", targetNamespace = "urn:silpsvaa")
    @RequestWrapper(localName = "modificaAzienda", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.ModificaAzienda")
    @WebMethod
    @ResponseWrapper(localName = "modificaAziendaResponse", targetNamespace = "urn:silpsvaa", className = "it.csi.silap.colmirbff.cxf.silpsvaa.ModificaAziendaResponse")
    public it.csi.silap.colmirbff.cxf.silpsvaa.AnagraficaAziende modificaAzienda(
        @WebParam(name = "in0", targetNamespace = "urn:silpsvaa")
        java.lang.String in0,
        @WebParam(name = "in1", targetNamespace = "urn:silpsvaa")
        it.csi.silap.colmirbff.cxf.silpsvaa.AnagraficaAziende in1
    ) throws ServiziSilpException_Exception;
}
