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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.silap.colmirbff.cxf.iupsv package. 
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

    private final static QName _Fault2_QNAME = new QName("urn:iupsv", "fault2");
    private final static QName _Fault3_QNAME = new QName("urn:iupsv", "fault3");
    private final static QName _Fault_QNAME = new QName("urn:iupsv", "fault");
    private final static QName _Fault1_QNAME = new QName("urn:iupsv", "fault1");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.silap.colmirbff.cxf.iupsv
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Vector }
     * 
     */
    public Vector createVector() {
        return new Vector();
    }

    /**
     * Create an instance of {@link RicercaSoggettiResponse }
     * 
     */
    public RicercaSoggettiResponse createRicercaSoggettiResponse() {
        return new RicercaSoggettiResponse();
    }

    /**
     * Create an instance of {@link ArrayOfXsdAnyType }
     * 
     */
    public ArrayOfXsdAnyType createArrayOfXsdAnyType() {
        return new ArrayOfXsdAnyType();
    }

    /**
     * Create an instance of {@link RicercaProtocolli }
     * 
     */
    public RicercaProtocolli createRicercaProtocolli() {
        return new RicercaProtocolli();
    }

    /**
     * Create an instance of {@link UtenteProtocolloDTO }
     * 
     */
    public UtenteProtocolloDTO createUtenteProtocolloDTO() {
        return new UtenteProtocolloDTO();
    }

    /**
     * Create an instance of {@link FiltroRicercaProtocolliDTO }
     * 
     */
    public FiltroRicercaProtocolliDTO createFiltroRicercaProtocolliDTO() {
        return new FiltroRicercaProtocolliDTO();
    }

    /**
     * Create an instance of {@link GetInfoSistemaProtocollazioneResponse }
     * 
     */
    public GetInfoSistemaProtocollazioneResponse createGetInfoSistemaProtocollazioneResponse() {
        return new GetInfoSistemaProtocollazioneResponse();
    }

    /**
     * Create an instance of {@link InfoSistemaProtocollazioneDTO }
     * 
     */
    public InfoSistemaProtocollazioneDTO createInfoSistemaProtocollazioneDTO() {
        return new InfoSistemaProtocollazioneDTO();
    }

    /**
     * Create an instance of {@link TestResourcesResponse }
     * 
     */
    public TestResourcesResponse createTestResourcesResponse() {
        return new TestResourcesResponse();
    }

    /**
     * Create an instance of {@link SystemException }
     * 
     */
    public SystemException createSystemException() {
        return new SystemException();
    }

    /**
     * Create an instance of {@link UnrecoverableException }
     * 
     */
    public UnrecoverableException createUnrecoverableException() {
        return new UnrecoverableException();
    }

    /**
     * Create an instance of {@link Protocolla }
     * 
     */
    public Protocolla createProtocolla() {
        return new Protocolla();
    }

    /**
     * Create an instance of {@link DatiProtocollazioneDTO }
     * 
     */
    public DatiProtocollazioneDTO createDatiProtocollazioneDTO() {
        return new DatiProtocollazioneDTO();
    }

    /**
     * Create an instance of {@link IUPException }
     * 
     */
    public IUPException createIUPException() {
        return new IUPException();
    }

    /**
     * Create an instance of {@link HasSelfCheckResponse }
     * 
     */
    public HasSelfCheckResponse createHasSelfCheckResponse() {
        return new HasSelfCheckResponse();
    }

    /**
     * Create an instance of {@link TestResources }
     * 
     */
    public TestResources createTestResources() {
        return new TestResources();
    }

    /**
     * Create an instance of {@link HasSelfCheck }
     * 
     */
    public HasSelfCheck createHasSelfCheck() {
        return new HasSelfCheck();
    }

    /**
     * Create an instance of {@link ModificaProtocolloResponse }
     * 
     */
    public ModificaProtocolloResponse createModificaProtocolloResponse() {
        return new ModificaProtocolloResponse();
    }

    /**
     * Create an instance of {@link AnnullaProtocollo }
     * 
     */
    public AnnullaProtocollo createAnnullaProtocollo() {
        return new AnnullaProtocollo();
    }

    /**
     * Create an instance of {@link DatiAnnulloProtocolloDTO }
     * 
     */
    public DatiAnnulloProtocolloDTO createDatiAnnulloProtocolloDTO() {
        return new DatiAnnulloProtocolloDTO();
    }

    /**
     * Create an instance of {@link SelfCheck }
     * 
     */
    public SelfCheck createSelfCheck() {
        return new SelfCheck();
    }

    /**
     * Create an instance of {@link CalledResource }
     * 
     */
    public CalledResource createCalledResource() {
        return new CalledResource();
    }

    /**
     * Create an instance of {@link AnnullaProtocolloResponse }
     * 
     */
    public AnnullaProtocolloResponse createAnnullaProtocolloResponse() {
        return new AnnullaProtocolloResponse();
    }

    /**
     * Create an instance of {@link ModificaProtocollo }
     * 
     */
    public ModificaProtocollo createModificaProtocollo() {
        return new ModificaProtocollo();
    }

    /**
     * Create an instance of {@link DatiVariazioneProtocolloDTO }
     * 
     */
    public DatiVariazioneProtocolloDTO createDatiVariazioneProtocolloDTO() {
        return new DatiVariazioneProtocolloDTO();
    }

    /**
     * Create an instance of {@link CSIException }
     * 
     */
    public CSIException createCSIException() {
        return new CSIException();
    }

    /**
     * Create an instance of {@link RicercaSoggetti }
     * 
     */
    public RicercaSoggetti createRicercaSoggetti() {
        return new RicercaSoggetti();
    }

    /**
     * Create an instance of {@link FiltroRicercaSoggettiDTO }
     * 
     */
    public FiltroRicercaSoggettiDTO createFiltroRicercaSoggettiDTO() {
        return new FiltroRicercaSoggettiDTO();
    }

    /**
     * Create an instance of {@link CreaSoggetto }
     * 
     */
    public CreaSoggetto createCreaSoggetto() {
        return new CreaSoggetto();
    }

    /**
     * Create an instance of {@link SoggettoDTO }
     * 
     */
    public SoggettoDTO createSoggettoDTO() {
        return new SoggettoDTO();
    }

    /**
     * Create an instance of {@link CreaSoggettoResponse }
     * 
     */
    public CreaSoggettoResponse createCreaSoggettoResponse() {
        return new CreaSoggettoResponse();
    }

    /**
     * Create an instance of {@link RicercaProtocolliResponse }
     * 
     */
    public RicercaProtocolliResponse createRicercaProtocolliResponse() {
        return new RicercaProtocolliResponse();
    }

    /**
     * Create an instance of {@link GetInfoSistemaProtocollazione }
     * 
     */
    public GetInfoSistemaProtocollazione createGetInfoSistemaProtocollazione() {
        return new GetInfoSistemaProtocollazione();
    }

    /**
     * Create an instance of {@link GetDatiUtenteProtocolloResponse }
     * 
     */
    public GetDatiUtenteProtocolloResponse createGetDatiUtenteProtocolloResponse() {
        return new GetDatiUtenteProtocolloResponse();
    }

    /**
     * Create an instance of {@link SelfCheckResponse }
     * 
     */
    public SelfCheckResponse createSelfCheckResponse() {
        return new SelfCheckResponse();
    }

    /**
     * Create an instance of {@link InvocationNode }
     * 
     */
    public InvocationNode createInvocationNode() {
        return new InvocationNode();
    }

    /**
     * Create an instance of {@link ProtocollaResponse }
     * 
     */
    public ProtocollaResponse createProtocollaResponse() {
        return new ProtocollaResponse();
    }

    /**
     * Create an instance of {@link ProtocolloDTO }
     * 
     */
    public ProtocolloDTO createProtocolloDTO() {
        return new ProtocolloDTO();
    }

    /**
     * Create an instance of {@link GetDatiUtenteProtocollo }
     * 
     */
    public GetDatiUtenteProtocollo createGetDatiUtenteProtocollo() {
        return new GetDatiUtenteProtocollo();
    }

    /**
     * Create an instance of {@link DatiLoginProtocolloDTO }
     * 
     */
    public DatiLoginProtocolloDTO createDatiLoginProtocolloDTO() {
        return new DatiLoginProtocolloDTO();
    }

    /**
     * Create an instance of {@link RiferimentoInternoDTO }
     * 
     */
    public RiferimentoInternoDTO createRiferimentoInternoDTO() {
        return new RiferimentoInternoDTO();
    }

    /**
     * Create an instance of {@link ArrayOfInvocationNode }
     * 
     */
    public ArrayOfInvocationNode createArrayOfInvocationNode() {
        return new ArrayOfInvocationNode();
    }

    /**
     * Create an instance of {@link UserException }
     * 
     */
    public UserException createUserException() {
        return new UserException();
    }

    /**
     * Create an instance of {@link Outcome }
     * 
     */
    public Outcome createOutcome() {
        return new Outcome();
    }

    /**
     * Create an instance of {@link ArrayOfXsdString }
     * 
     */
    public ArrayOfXsdString createArrayOfXsdString() {
        return new ArrayOfXsdString();
    }

    /**
     * Create an instance of {@link ResourceType }
     * 
     */
    public ResourceType createResourceType() {
        return new ResourceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnrecoverableException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:iupsv", name = "fault2")
    public JAXBElement<UnrecoverableException> createFault2(UnrecoverableException value) {
        return new JAXBElement<UnrecoverableException>(_Fault2_QNAME, UnrecoverableException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IUPException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:iupsv", name = "fault3")
    public JAXBElement<IUPException> createFault3(IUPException value) {
        return new JAXBElement<IUPException>(_Fault3_QNAME, IUPException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CSIException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:iupsv", name = "fault")
    public JAXBElement<CSIException> createFault(CSIException value) {
        return new JAXBElement<CSIException>(_Fault_QNAME, CSIException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SystemException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:iupsv", name = "fault1")
    public JAXBElement<SystemException> createFault1(SystemException value) {
        return new JAXBElement<SystemException>(_Fault1_QNAME, SystemException.class, null, value);
    }

}
