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
 * <p>Classe Java per DatiLoginProtocolloDTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiLoginProtocolloDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codiceFiscaleUtente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="denominazioneAOO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAOO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idApplicativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idEnte" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="loginProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="passwordProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiLoginProtocolloDTO", propOrder = {
    "codiceFiscaleUtente",
    "denominazioneAOO",
    "idAOO",
    "idApplicativo",
    "idEnte",
    "idProvincia",
    "loginProtocollo",
    "passwordProtocollo"
})
public class DatiLoginProtocolloDTO {

    @XmlElement(required = true, nillable = true)
    protected String codiceFiscaleUtente;
    @XmlElement(required = true, nillable = true)
    protected String denominazioneAOO;
    @XmlElement(required = true, nillable = true)
    protected String idAOO;
    @XmlElement(required = true, nillable = true)
    protected String idApplicativo;
    @XmlElement(required = true, nillable = true)
    protected String idEnte;
    @XmlElement(required = true, nillable = true)
    protected String idProvincia;
    @XmlElement(required = true, nillable = true)
    protected String loginProtocollo;
    @XmlElement(required = true, nillable = true)
    protected String passwordProtocollo;

    /**
     * Recupera il valore della proprietà codiceFiscaleUtente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscaleUtente() {
        return codiceFiscaleUtente;
    }

    /**
     * Imposta il valore della proprietà codiceFiscaleUtente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscaleUtente(String value) {
        this.codiceFiscaleUtente = value;
    }

    /**
     * Recupera il valore della proprietà denominazioneAOO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazioneAOO() {
        return denominazioneAOO;
    }

    /**
     * Imposta il valore della proprietà denominazioneAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazioneAOO(String value) {
        this.denominazioneAOO = value;
    }

    /**
     * Recupera il valore della proprietà idAOO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAOO() {
        return idAOO;
    }

    /**
     * Imposta il valore della proprietà idAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAOO(String value) {
        this.idAOO = value;
    }

    /**
     * Recupera il valore della proprietà idApplicativo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdApplicativo() {
        return idApplicativo;
    }

    /**
     * Imposta il valore della proprietà idApplicativo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdApplicativo(String value) {
        this.idApplicativo = value;
    }

    /**
     * Recupera il valore della proprietà idEnte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEnte() {
        return idEnte;
    }

    /**
     * Imposta il valore della proprietà idEnte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEnte(String value) {
        this.idEnte = value;
    }

    /**
     * Recupera il valore della proprietà idProvincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProvincia() {
        return idProvincia;
    }

    /**
     * Imposta il valore della proprietà idProvincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProvincia(String value) {
        this.idProvincia = value;
    }

    /**
     * Recupera il valore della proprietà loginProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginProtocollo() {
        return loginProtocollo;
    }

    /**
     * Imposta il valore della proprietà loginProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginProtocollo(String value) {
        this.loginProtocollo = value;
    }

    /**
     * Recupera il valore della proprietà passwordProtocollo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordProtocollo() {
        return passwordProtocollo;
    }

    /**
     * Imposta il valore della proprietà passwordProtocollo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordProtocollo(String value) {
        this.passwordProtocollo = value;
    }

}
