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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per ProtocolloDTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ProtocolloDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annullato" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataInvio" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="dataProtocollo" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="destinatariEsterni" type="{urn:iupsv}ArrayOf_xsd_anyType"/>
 *         &lt;element name="destinatarioInterno" type="{urn:iupsv}RiferimentoInternoDTO"/>
 *         &lt;element name="idProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mittenteEsterno" type="{urn:iupsv}SoggettoDTO"/>
 *         &lt;element name="mittenteInterno" type="{urn:iupsv}RiferimentoInternoDTO"/>
 *         &lt;element name="numeroAllegati" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numeroProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="oggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProtocolloDTO", propOrder = {
    "annullato",
    "dataInvio",
    "dataProtocollo",
    "destinatariEsterni",
    "destinatarioInterno",
    "idProtocollo",
    "mittenteEsterno",
    "mittenteInterno",
    "numeroAllegati",
    "numeroProtocollo",
    "oggetto",
    "tipoProtocollo"
})
public class ProtocolloDTO {

    protected boolean annullato;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataInvio;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataProtocollo;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfXsdAnyType destinatariEsterni;
    @XmlElement(required = true, nillable = true)
    protected RiferimentoInternoDTO destinatarioInterno;
    @XmlElement(required = true, nillable = true)
    protected String idProtocollo;
    @XmlElement(required = true, nillable = true)
    protected SoggettoDTO mittenteEsterno;
    @XmlElement(required = true, nillable = true)
    protected RiferimentoInternoDTO mittenteInterno;
    protected int numeroAllegati;
    @XmlElement(required = true, nillable = true)
    protected String numeroProtocollo;
    @XmlElement(required = true, nillable = true)
    protected String oggetto;
    @XmlElement(required = true, nillable = true)
    protected String tipoProtocollo;

    /**
     * Recupera il valore della proprietà annullato.
     * 
     */
    public boolean isAnnullato() {
        return annullato;
    }

    /**
     * Imposta il valore della proprietà annullato.
     * 
     */
    public void setAnnullato(boolean value) {
        this.annullato = value;
    }

    /**
     * Recupera il valore della proprietà dataInvio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInvio() {
        return dataInvio;
    }

    /**
     * Imposta il valore della proprietà dataInvio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInvio(XMLGregorianCalendar value) {
        this.dataInvio = value;
    }

    /**
     * Recupera il valore della proprietà dataProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataProtocollo() {
        return dataProtocollo;
    }

    /**
     * Imposta il valore della proprietà dataProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataProtocollo(XMLGregorianCalendar value) {
        this.dataProtocollo = value;
    }

    /**
     * Recupera il valore della proprietà destinatariEsterni.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfXsdAnyType }
     *     
     */
    public ArrayOfXsdAnyType getDestinatariEsterni() {
        return destinatariEsterni;
    }

    /**
     * Imposta il valore della proprietà destinatariEsterni.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfXsdAnyType }
     *     
     */
    public void setDestinatariEsterni(ArrayOfXsdAnyType value) {
        this.destinatariEsterni = value;
    }

    /**
     * Recupera il valore della proprietà destinatarioInterno.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentoInternoDTO }
     *     
     */
    public RiferimentoInternoDTO getDestinatarioInterno() {
        return destinatarioInterno;
    }

    /**
     * Imposta il valore della proprietà destinatarioInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentoInternoDTO }
     *     
     */
    public void setDestinatarioInterno(RiferimentoInternoDTO value) {
        this.destinatarioInterno = value;
    }

    /**
     * Recupera il valore della proprietà idProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProtocollo() {
        return idProtocollo;
    }

    /**
     * Imposta il valore della proprietà idProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProtocollo(String value) {
        this.idProtocollo = value;
    }

    /**
     * Recupera il valore della proprietà mittenteEsterno.
     * 
     * @return
     *     possible object is
     *     {@link SoggettoDTO }
     *     
     */
    public SoggettoDTO getMittenteEsterno() {
        return mittenteEsterno;
    }

    /**
     * Imposta il valore della proprietà mittenteEsterno.
     * 
     * @param value
     *     allowed object is
     *     {@link SoggettoDTO }
     *     
     */
    public void setMittenteEsterno(SoggettoDTO value) {
        this.mittenteEsterno = value;
    }

    /**
     * Recupera il valore della proprietà mittenteInterno.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentoInternoDTO }
     *     
     */
    public RiferimentoInternoDTO getMittenteInterno() {
        return mittenteInterno;
    }

    /**
     * Imposta il valore della proprietà mittenteInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentoInternoDTO }
     *     
     */
    public void setMittenteInterno(RiferimentoInternoDTO value) {
        this.mittenteInterno = value;
    }

    /**
     * Recupera il valore della proprietà numeroAllegati.
     * 
     */
    public int getNumeroAllegati() {
        return numeroAllegati;
    }

    /**
     * Imposta il valore della proprietà numeroAllegati.
     * 
     */
    public void setNumeroAllegati(int value) {
        this.numeroAllegati = value;
    }

    /**
     * Recupera il valore della proprietà numeroProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroProtocollo() {
        return numeroProtocollo;
    }

    /**
     * Imposta il valore della proprietà numeroProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroProtocollo(String value) {
        this.numeroProtocollo = value;
    }

    /**
     * Recupera il valore della proprietà oggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * Imposta il valore della proprietà oggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggetto(String value) {
        this.oggetto = value;
    }

    /**
     * Recupera il valore della proprietà tipoProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProtocollo() {
        return tipoProtocollo;
    }

    /**
     * Imposta il valore della proprietà tipoProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProtocollo(String value) {
        this.tipoProtocollo = value;
    }

}
