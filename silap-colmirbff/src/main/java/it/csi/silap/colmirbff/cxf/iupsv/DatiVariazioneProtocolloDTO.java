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
 * <p>Classe Java per DatiVariazioneProtocolloDTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiVariazioneProtocolloDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annoProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataInvio" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="mittenteEsterno" type="{urn:iupsv}SoggettoDTO"/>
 *         &lt;element name="numeroAllegati" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numeroProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="oggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoMittenteInterno" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "DatiVariazioneProtocolloDTO", propOrder = {
    "annoProtocollo",
    "dataInvio",
    "mittenteEsterno",
    "numeroAllegati",
    "numeroProtocollo",
    "oggetto",
    "tipoMittenteInterno",
    "tipoProtocollo"
})
public class DatiVariazioneProtocolloDTO {

    @XmlElement(required = true, nillable = true)
    protected String annoProtocollo;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataInvio;
    @XmlElement(required = true, nillable = true)
    protected SoggettoDTO mittenteEsterno;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer numeroAllegati;
    @XmlElement(required = true, nillable = true)
    protected String numeroProtocollo;
    @XmlElement(required = true, nillable = true)
    protected String oggetto;
    @XmlElement(required = true, nillable = true)
    protected String tipoMittenteInterno;
    @XmlElement(required = true, nillable = true)
    protected String tipoProtocollo;

    /**
     * Recupera il valore della proprietà annoProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnoProtocollo() {
        return annoProtocollo;
    }

    /**
     * Imposta il valore della proprietà annoProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnoProtocollo(String value) {
        this.annoProtocollo = value;
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
     * Recupera il valore della proprietà numeroAllegati.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroAllegati() {
        return numeroAllegati;
    }

    /**
     * Imposta il valore della proprietà numeroAllegati.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroAllegati(Integer value) {
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
     * Recupera il valore della proprietà tipoMittenteInterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMittenteInterno() {
        return tipoMittenteInterno;
    }

    /**
     * Imposta il valore della proprietà tipoMittenteInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMittenteInterno(String value) {
        this.tipoMittenteInterno = value;
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
