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
package it.csi.silap.colmirbff.cxf.iupsv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.2
 * 2023-07-13T10:01:43.620+02:00
 * Generated source version: 2.7.2
 * 
 */
@WebService(targetNamespace = "urn:iupsv", name = "IupsvSrv")
@XmlSeeAlso({ObjectFactory.class})
public interface IupsvSrv {

    @WebResult(name = "hasSelfCheckReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "hasSelfCheck", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.HasSelfCheck")
    @WebMethod
    @ResponseWrapper(localName = "hasSelfCheckResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.HasSelfCheckResponse")
    public boolean hasSelfCheck() throws CSIException_Exception;

    @WebResult(name = "testResourcesReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "testResources", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.TestResources")
    @WebMethod
    @ResponseWrapper(localName = "testResourcesResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.TestResourcesResponse")
    public boolean testResources() throws CSIException_Exception;

    @WebResult(name = "getInfoSistemaProtocollazioneReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "getInfoSistemaProtocollazione", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.GetInfoSistemaProtocollazione")
    @WebMethod
    @ResponseWrapper(localName = "getInfoSistemaProtocollazioneResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.GetInfoSistemaProtocollazioneResponse")
    public it.csi.silap.colmirbff.cxf.iupsv.InfoSistemaProtocollazioneDTO getInfoSistemaProtocollazione(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        java.lang.String in0
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;

    @WebResult(name = "protocollaReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "protocolla", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.Protocolla")
    @WebMethod
    @ResponseWrapper(localName = "protocollaResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.ProtocollaResponse")
    public it.csi.silap.colmirbff.cxf.iupsv.ProtocolloDTO protocolla(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO in0,
        @WebParam(name = "in1", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.DatiProtocollazioneDTO in1
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;

    @WebResult(name = "ricercaSoggettiReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "ricercaSoggetti", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.RicercaSoggetti")
    @WebMethod
    @ResponseWrapper(localName = "ricercaSoggettiResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.RicercaSoggettiResponse")
    public it.csi.silap.colmirbff.cxf.iupsv.ArrayOfXsdAnyType ricercaSoggetti(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO in0,
        @WebParam(name = "in1", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.FiltroRicercaSoggettiDTO in1
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;

    @RequestWrapper(localName = "modificaProtocollo", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.ModificaProtocollo")
    @WebMethod
    @ResponseWrapper(localName = "modificaProtocolloResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.ModificaProtocolloResponse")
    public void modificaProtocollo(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO in0,
        @WebParam(name = "in1", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.DatiVariazioneProtocolloDTO in1
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;

    @WebResult(name = "getDatiUtenteProtocolloReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "getDatiUtenteProtocollo", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.GetDatiUtenteProtocollo")
    @WebMethod
    @ResponseWrapper(localName = "getDatiUtenteProtocolloResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.GetDatiUtenteProtocolloResponse")
    public it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO getDatiUtenteProtocollo(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.DatiLoginProtocolloDTO in0
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;

    @WebResult(name = "ricercaProtocolliReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "ricercaProtocolli", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.RicercaProtocolli")
    @WebMethod
    @ResponseWrapper(localName = "ricercaProtocolliResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.RicercaProtocolliResponse")
    public it.csi.silap.colmirbff.cxf.iupsv.ArrayOfXsdAnyType ricercaProtocolli(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO in0,
        @WebParam(name = "in1", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.FiltroRicercaProtocolliDTO in1
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;

    @RequestWrapper(localName = "annullaProtocollo", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.AnnullaProtocollo")
    @WebMethod
    @ResponseWrapper(localName = "annullaProtocolloResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.AnnullaProtocolloResponse")
    public void annullaProtocollo(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO in0,
        @WebParam(name = "in1", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.DatiAnnulloProtocolloDTO in1
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;

    @WebResult(name = "selfCheckReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "selfCheck", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.SelfCheck")
    @WebMethod
    @ResponseWrapper(localName = "selfCheckResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.SelfCheckResponse")
    public it.csi.silap.colmirbff.cxf.iupsv.InvocationNode selfCheck(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        java.util.List<it.csi.silap.colmirbff.cxf.iupsv.CalledResource> in0
    ) throws CSIException_Exception;

    @WebResult(name = "creaSoggettoReturn", targetNamespace = "urn:iupsv")
    @RequestWrapper(localName = "creaSoggetto", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.CreaSoggetto")
    @WebMethod
    @ResponseWrapper(localName = "creaSoggettoResponse", targetNamespace = "urn:iupsv", className = "it.csi.silap.colmirbff.cxf.iupsv.CreaSoggettoResponse")
    public it.csi.silap.colmirbff.cxf.iupsv.SoggettoDTO creaSoggetto(
        @WebParam(name = "in0", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.UtenteProtocolloDTO in0,
        @WebParam(name = "in1", targetNamespace = "urn:iupsv")
        it.csi.silap.colmirbff.cxf.iupsv.SoggettoDTO in1
    ) throws IUPException_Exception, SystemException_Exception, UnrecoverableException_Exception, CSIException_Exception;
}