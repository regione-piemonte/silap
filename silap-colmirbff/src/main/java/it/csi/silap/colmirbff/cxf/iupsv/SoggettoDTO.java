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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SoggettoDTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SoggettoDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="capComune" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceIstatComune" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="denominazioneLuogo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComune" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrNazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrRegione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idLuogo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idSoggettoGiuridico" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="partitaIVA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="siglaProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoggettoDTO", propOrder = {
    "capComune",
    "codFiscale",
    "codiceIstatComune",
    "cognome",
    "denominazione",
    "denominazioneLuogo",
    "descrComune",
    "descrNazione",
    "descrProvincia",
    "descrRegione",
    "idLuogo",
    "idSoggetto",
    "idSoggettoGiuridico",
    "indirizzo",
    "nome",
    "partitaIVA",
    "siglaProvincia",
    "tipoSoggetto"
})
public class SoggettoDTO {

    @XmlElement(required = true, nillable = true)
    protected String capComune;
    @XmlElement(required = true, nillable = true)
    protected String codFiscale;
    @XmlElement(required = true, nillable = true)
    protected String codiceIstatComune;
    @XmlElement(required = true, nillable = true)
    protected String cognome;
    @XmlElement(required = true, nillable = true)
    protected String denominazione;
    @XmlElement(required = true, nillable = true)
    protected String denominazioneLuogo;
    @XmlElement(required = true, nillable = true)
    protected String descrComune;
    @XmlElement(required = true, nillable = true)
    protected String descrNazione;
    @XmlElement(required = true, nillable = true)
    protected String descrProvincia;
    @XmlElement(required = true, nillable = true)
    protected String descrRegione;
    @XmlElement(required = true, nillable = true)
    protected String idLuogo;
    @XmlElement(required = true, nillable = true)
    protected String idSoggetto;
    @XmlElement(required = true, nillable = true)
    protected String idSoggettoGiuridico;
    @XmlElement(required = true, nillable = true)
    protected String indirizzo;
    @XmlElement(required = true, nillable = true)
    protected String nome;
    @XmlElement(required = true, nillable = true)
    protected String partitaIVA;
    @XmlElement(required = true, nillable = true)
    protected String siglaProvincia;
    @XmlElement(required = true, nillable = true)
    protected String tipoSoggetto;

    /**
     * Recupera il valore della proprietà capComune.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapComune() {
        return capComune;
    }

    /**
     * Imposta il valore della proprietà capComune.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapComune(String value) {
        this.capComune = value;
    }

    /**
     * Recupera il valore della proprietà codFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFiscale() {
        return codFiscale;
    }

    /**
     * Imposta il valore della proprietà codFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFiscale(String value) {
        this.codFiscale = value;
    }

    /**
     * Recupera il valore della proprietà codiceIstatComune.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceIstatComune() {
        return codiceIstatComune;
    }

    /**
     * Imposta il valore della proprietà codiceIstatComune.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceIstatComune(String value) {
        this.codiceIstatComune = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà denominazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * Imposta il valore della proprietà denominazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazione(String value) {
        this.denominazione = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneLuogo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneLuogo() {
        return denominazioneLuogo;
    }

    /**
     * Imposta il valore della proprietà denominazioneLuogo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneLuogo(String value) {
        this.denominazioneLuogo = value;
    }

    /**
     * Recupera il valore della proprietà descrComune.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComune() {
        return descrComune;
    }

    /**
     * Imposta il valore della proprietà descrComune.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComune(String value) {
        this.descrComune = value;
    }

    /**
     * Recupera il valore della proprietà descrNazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrNazione() {
        return descrNazione;
    }

    /**
     * Imposta il valore della proprietà descrNazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrNazione(String value) {
        this.descrNazione = value;
    }

    /**
     * Recupera il valore della proprietà descrProvincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrProvincia() {
        return descrProvincia;
    }

    /**
     * Imposta il valore della proprietà descrProvincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrProvincia(String value) {
        this.descrProvincia = value;
    }

    /**
     * Recupera il valore della proprietà descrRegione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrRegione() {
        return descrRegione;
    }

    /**
     * Imposta il valore della proprietà descrRegione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrRegione(String value) {
        this.descrRegione = value;
    }

    /**
     * Recupera il valore della proprietà idLuogo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLuogo() {
        return idLuogo;
    }

    /**
     * Imposta il valore della proprietà idLuogo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLuogo(String value) {
        this.idLuogo = value;
    }

    /**
     * Recupera il valore della proprietà idSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSoggetto() {
        return idSoggetto;
    }

    /**
     * Imposta il valore della proprietà idSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSoggetto(String value) {
        this.idSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà idSoggettoGiuridico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSoggettoGiuridico() {
        return idSoggettoGiuridico;
    }

    /**
     * Imposta il valore della proprietà idSoggettoGiuridico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSoggettoGiuridico(String value) {
        this.idSoggettoGiuridico = value;
    }

    /**
     * Recupera il valore della proprietà indirizzo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Imposta il valore della proprietà indirizzo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzo(String value) {
        this.indirizzo = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà partitaIVA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartitaIVA() {
        return partitaIVA;
    }

    /**
     * Imposta il valore della proprietà partitaIVA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartitaIVA(String value) {
        this.partitaIVA = value;
    }

    /**
     * Recupera il valore della proprietà siglaProvincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaProvincia() {
        return siglaProvincia;
    }

    /**
     * Imposta il valore della proprietà siglaProvincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaProvincia(String value) {
        this.siglaProvincia = value;
    }

    /**
     * Recupera il valore della proprietà tipoSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoSoggetto() {
        return tipoSoggetto;
    }

    /**
     * Imposta il valore della proprietà tipoSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoSoggetto(String value) {
        this.tipoSoggetto = value;
    }

}
