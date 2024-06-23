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
 * <p>Classe Java per UtenteProtocolloDTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="UtenteProtocolloDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codiceFiscaleUtente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="denominazioneAOO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrizioneStruttura" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAOO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idApplicativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idEnte" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idNodo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idRuolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idStruttura" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="loginProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nomeRuolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="passwordProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="targetFolderKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UtenteProtocolloDTO", propOrder = {
    "codiceFiscaleUtente",
    "denominazioneAOO",
    "descrizioneStruttura",
    "idAOO",
    "idApplicativo",
    "idEnte",
    "idNodo",
    "idProvincia",
    "idRuolo",
    "idStruttura",
    "loginProtocollo",
    "nomeRuolo",
    "passwordProtocollo",
    "targetFolderKey"
})
public class UtenteProtocolloDTO {

    @XmlElement(required = true, nillable = true)
    protected String codiceFiscaleUtente;
    @XmlElement(required = true, nillable = true)
    protected String denominazioneAOO;
    @XmlElement(required = true, nillable = true)
    protected String descrizioneStruttura;
    @XmlElement(required = true, nillable = true)
    protected String idAOO;
    @XmlElement(required = true, nillable = true)
    protected String idApplicativo;
    @XmlElement(required = true, nillable = true)
    protected String idEnte;
    @XmlElement(required = true, nillable = true)
    protected String idNodo;
    @XmlElement(required = true, nillable = true)
    protected String idProvincia;
    @XmlElement(required = true, nillable = true)
    protected String idRuolo;
    @XmlElement(required = true, nillable = true)
    protected String idStruttura;
    @XmlElement(required = true, nillable = true)
    protected String loginProtocollo;
    @XmlElement(required = true, nillable = true)
    protected String nomeRuolo;
    @XmlElement(required = true, nillable = true)
    protected String passwordProtocollo;
    @XmlElement(required = true, nillable = true)
    protected String targetFolderKey;

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
     * Recupera il valore della proprietà descrizioneStruttura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneStruttura() {
        return descrizioneStruttura;
    }

    /**
     * Imposta il valore della proprietà descrizioneStruttura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneStruttura(String value) {
        this.descrizioneStruttura = value;
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
     * Recupera il valore della proprietà idNodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdNodo() {
        return idNodo;
    }

    /**
     * Imposta il valore della proprietà idNodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdNodo(String value) {
        this.idNodo = value;
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
     * Recupera il valore della proprietà idRuolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdRuolo() {
        return idRuolo;
    }

    /**
     * Imposta il valore della proprietà idRuolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdRuolo(String value) {
        this.idRuolo = value;
    }

    /**
     * Recupera il valore della proprietà idStruttura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdStruttura() {
        return idStruttura;
    }

    /**
     * Imposta il valore della proprietà idStruttura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdStruttura(String value) {
        this.idStruttura = value;
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
     * Recupera il valore della proprietà nomeRuolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeRuolo() {
        return nomeRuolo;
    }

    /**
     * Imposta il valore della proprietà nomeRuolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeRuolo(String value) {
        this.nomeRuolo = value;
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

    /**
     * Recupera il valore della proprietà targetFolderKey.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetFolderKey() {
        return targetFolderKey;
    }

    /**
     * Imposta il valore della proprietà targetFolderKey.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetFolderKey(String value) {
        this.targetFolderKey = value;
    }

}
