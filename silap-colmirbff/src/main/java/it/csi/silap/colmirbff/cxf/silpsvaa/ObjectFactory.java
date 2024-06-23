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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.silap.colmirbff.cxf.silpsvaa package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Fault_QNAME = new QName("urn:silpsvaa", "fault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.silap.colmirbff.cxf.silpsvaa
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ModificaSede }
     * 
     */
    public ModificaSede createModificaSede() {
        return new ModificaSede();
    }

    /**
     * Create an instance of {@link AnagraficaSediAziende }
     * 
     */
    public AnagraficaSediAziende createAnagraficaSediAziende() {
        return new AnagraficaSediAziende();
    }

    /**
     * Create an instance of {@link RicercaElencoAziendeResponse }
     * 
     */
    public RicercaElencoAziendeResponse createRicercaElencoAziendeResponse() {
        return new RicercaElencoAziendeResponse();
    }

    /**
     * Create an instance of {@link ElencoAziende }
     * 
     */
    public ElencoAziende createElencoAziende() {
        return new ElencoAziende();
    }

    /**
     * Create an instance of {@link VisualizzaDettaglioSedeAziendaResponse }
     * 
     */
    public VisualizzaDettaglioSedeAziendaResponse createVisualizzaDettaglioSedeAziendaResponse() {
        return new VisualizzaDettaglioSedeAziendaResponse();
    }

    /**
     * Create an instance of {@link ModificaAzienda }
     * 
     */
    public ModificaAzienda createModificaAzienda() {
        return new ModificaAzienda();
    }

    /**
     * Create an instance of {@link AnagraficaAziende }
     * 
     */
    public AnagraficaAziende createAnagraficaAziende() {
        return new AnagraficaAziende();
    }

    /**
     * Create an instance of {@link ServiziSilpException }
     * 
     */
    public ServiziSilpException createServiziSilpException() {
        return new ServiziSilpException();
    }

    /**
     * Create an instance of {@link RicercaSediAziendaResponse }
     * 
     */
    public RicercaSediAziendaResponse createRicercaSediAziendaResponse() {
        return new RicercaSediAziendaResponse();
    }

    /**
     * Create an instance of {@link ElencoSedi }
     * 
     */
    public ElencoSedi createElencoSedi() {
        return new ElencoSedi();
    }

    /**
     * Create an instance of {@link RicercaElencoAziende }
     * 
     */
    public RicercaElencoAziende createRicercaElencoAziende() {
        return new RicercaElencoAziende();
    }

    /**
     * Create an instance of {@link RicercaSediAzienda }
     * 
     */
    public RicercaSediAzienda createRicercaSediAzienda() {
        return new RicercaSediAzienda();
    }

    /**
     * Create an instance of {@link ModificaSedeResponse }
     * 
     */
    public ModificaSedeResponse createModificaSedeResponse() {
        return new ModificaSedeResponse();
    }

    /**
     * Create an instance of {@link VisualizzaDettaglioAzienda }
     * 
     */
    public VisualizzaDettaglioAzienda createVisualizzaDettaglioAzienda() {
        return new VisualizzaDettaglioAzienda();
    }

    /**
     * Create an instance of {@link VisualizzaDettaglioAziendaResponse }
     * 
     */
    public VisualizzaDettaglioAziendaResponse createVisualizzaDettaglioAziendaResponse() {
        return new VisualizzaDettaglioAziendaResponse();
    }

    /**
     * Create an instance of {@link VisualizzaDettaglioSedeAzienda }
     * 
     */
    public VisualizzaDettaglioSedeAzienda createVisualizzaDettaglioSedeAzienda() {
        return new VisualizzaDettaglioSedeAzienda();
    }

    /**
     * Create an instance of {@link ModificaAziendaResponse }
     * 
     */
    public ModificaAziendaResponse createModificaAziendaResponse() {
        return new ModificaAziendaResponse();
    }

    /**
     * Create an instance of {@link ArrayOfXsdString }
     * 
     */
    public ArrayOfXsdString createArrayOfXsdString() {
        return new ArrayOfXsdString();
    }

    /**
     * Create an instance of {@link UserException }
     * 
     */
    public UserException createUserException() {
        return new UserException();
    }

    /**
     * Create an instance of {@link CSIException }
     * 
     */
    public CSIException createCSIException() {
        return new CSIException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiziSilpException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:silpsvaa", name = "fault")
    public JAXBElement<ServiziSilpException> createFault(ServiziSilpException value) {
        return new JAXBElement<ServiziSilpException>(_Fault_QNAME, ServiziSilpException.class, null, value);
    }

}
