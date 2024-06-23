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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per AnagraficaAziende complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AnagraficaAziende">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="altreInformazioni" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codLavoroTemp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codMinAteco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrAteco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrCcnlAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrMinAteco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagArtigiana" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagMaster" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagPubblicaAmm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagTipoAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAteco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAziendaAccorpati" type="{urn:silpsvaa}ArrayOf_xsd_string"/>
 *         &lt;element name="idCcnlAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="partitaIva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ragioneSociale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnagraficaAziende", propOrder = {
    "altreInformazioni",
    "codLavoroTemp",
    "codMinAteco",
    "codUser",
    "codiceFiscale",
    "descrAteco",
    "descrCcnlAzienda",
    "descrMinAteco",
    "descrNaturaGiuridica",
    "descrTipoAzienda",
    "flagArtigiana",
    "flagMaster",
    "flagPubblicaAmm",
    "flagTipoAzienda",
    "idAteco",
    "idAzienda",
    "idAziendaAccorpati",
    "idCcnlAzienda",
    "idNaturaGiuridica",
    "partitaIva",
    "ragioneSociale"
})
public class AnagraficaAziende {

    @XmlElement(required = true, nillable = true)
    protected String altreInformazioni;
    @XmlElement(required = true, nillable = true)
    protected String codLavoroTemp;
    @XmlElement(required = true, nillable = true)
    protected String codMinAteco;
    @XmlElement(required = true, nillable = true)
    protected String codUser;
    @XmlElement(required = true, nillable = true)
    protected String codiceFiscale;
    @XmlElement(required = true, nillable = true)
    protected String descrAteco;
    @XmlElement(required = true, nillable = true)
    protected String descrCcnlAzienda;
    @XmlElement(required = true, nillable = true)
    protected String descrMinAteco;
    @XmlElement(required = true, nillable = true)
    protected String descrNaturaGiuridica;
    @XmlElement(required = true, nillable = true)
    protected String descrTipoAzienda;
    @XmlElement(required = true, nillable = true)
    protected String flagArtigiana;
    @XmlElement(required = true, nillable = true)
    protected String flagMaster;
    @XmlElement(required = true, nillable = true)
    protected String flagPubblicaAmm;
    @XmlElement(required = true, nillable = true)
    protected String flagTipoAzienda;
    @XmlElement(required = true, nillable = true)
    protected String idAteco;
    @XmlElement(required = true, nillable = true)
    protected String idAzienda;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfXsdString idAziendaAccorpati;
    @XmlElement(required = true, nillable = true)
    protected String idCcnlAzienda;
    @XmlElement(required = true, nillable = true)
    protected String idNaturaGiuridica;
    @XmlElement(required = true, nillable = true)
    protected String partitaIva;
    @XmlElement(required = true, nillable = true)
    protected String ragioneSociale;

    /**
     * Recupera il valore della proprietà altreInformazioni.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAltreInformazioni() {
        return altreInformazioni;
    }

    /**
     * Imposta il valore della proprietà altreInformazioni.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAltreInformazioni(String value) {
        this.altreInformazioni = value;
    }

    /**
     * Recupera il valore della proprietà codLavoroTemp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodLavoroTemp() {
        return codLavoroTemp;
    }

    /**
     * Imposta il valore della proprietà codLavoroTemp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodLavoroTemp(String value) {
        this.codLavoroTemp = value;
    }

    /**
     * Recupera il valore della proprietà codMinAteco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodMinAteco() {
        return codMinAteco;
    }

    /**
     * Imposta il valore della proprietà codMinAteco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodMinAteco(String value) {
        this.codMinAteco = value;
    }

    /**
     * Recupera il valore della proprietà codUser.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUser() {
        return codUser;
    }

    /**
     * Imposta il valore della proprietà codUser.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUser(String value) {
        this.codUser = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà descrAteco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrAteco() {
        return descrAteco;
    }

    /**
     * Imposta il valore della proprietà descrAteco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrAteco(String value) {
        this.descrAteco = value;
    }

    /**
     * Recupera il valore della proprietà descrCcnlAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrCcnlAzienda() {
        return descrCcnlAzienda;
    }

    /**
     * Imposta il valore della proprietà descrCcnlAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrCcnlAzienda(String value) {
        this.descrCcnlAzienda = value;
    }

    /**
     * Recupera il valore della proprietà descrMinAteco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrMinAteco() {
        return descrMinAteco;
    }

    /**
     * Imposta il valore della proprietà descrMinAteco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrMinAteco(String value) {
        this.descrMinAteco = value;
    }

    /**
     * Recupera il valore della proprietà descrNaturaGiuridica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrNaturaGiuridica() {
        return descrNaturaGiuridica;
    }

    /**
     * Imposta il valore della proprietà descrNaturaGiuridica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrNaturaGiuridica(String value) {
        this.descrNaturaGiuridica = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoAzienda() {
        return descrTipoAzienda;
    }

    /**
     * Imposta il valore della proprietà descrTipoAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoAzienda(String value) {
        this.descrTipoAzienda = value;
    }

    /**
     * Recupera il valore della proprietà flagArtigiana.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagArtigiana() {
        return flagArtigiana;
    }

    /**
     * Imposta il valore della proprietà flagArtigiana.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagArtigiana(String value) {
        this.flagArtigiana = value;
    }

    /**
     * Recupera il valore della proprietà flagMaster.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagMaster() {
        return flagMaster;
    }

    /**
     * Imposta il valore della proprietà flagMaster.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagMaster(String value) {
        this.flagMaster = value;
    }

    /**
     * Recupera il valore della proprietà flagPubblicaAmm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagPubblicaAmm() {
        return flagPubblicaAmm;
    }

    /**
     * Imposta il valore della proprietà flagPubblicaAmm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagPubblicaAmm(String value) {
        this.flagPubblicaAmm = value;
    }

    /**
     * Recupera il valore della proprietà flagTipoAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagTipoAzienda() {
        return flagTipoAzienda;
    }

    /**
     * Imposta il valore della proprietà flagTipoAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagTipoAzienda(String value) {
        this.flagTipoAzienda = value;
    }

    /**
     * Recupera il valore della proprietà idAteco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAteco() {
        return idAteco;
    }

    /**
     * Imposta il valore della proprietà idAteco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAteco(String value) {
        this.idAteco = value;
    }

    /**
     * Recupera il valore della proprietà idAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAzienda() {
        return idAzienda;
    }

    /**
     * Imposta il valore della proprietà idAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAzienda(String value) {
        this.idAzienda = value;
    }

    /**
     * Recupera il valore della proprietà idAziendaAccorpati.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public ArrayOfXsdString getIdAziendaAccorpati() {
        return idAziendaAccorpati;
    }

    /**
     * Imposta il valore della proprietà idAziendaAccorpati.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public void setIdAziendaAccorpati(ArrayOfXsdString value) {
        this.idAziendaAccorpati = value;
    }

    /**
     * Recupera il valore della proprietà idCcnlAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCcnlAzienda() {
        return idCcnlAzienda;
    }

    /**
     * Imposta il valore della proprietà idCcnlAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCcnlAzienda(String value) {
        this.idCcnlAzienda = value;
    }

    /**
     * Recupera il valore della proprietà idNaturaGiuridica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdNaturaGiuridica() {
        return idNaturaGiuridica;
    }

    /**
     * Imposta il valore della proprietà idNaturaGiuridica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdNaturaGiuridica(String value) {
        this.idNaturaGiuridica = value;
    }

    /**
     * Recupera il valore della proprietà partitaIva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartitaIva() {
        return partitaIva;
    }

    /**
     * Imposta il valore della proprietà partitaIva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartitaIva(String value) {
        this.partitaIva = value;
    }

    /**
     * Recupera il valore della proprietà ragioneSociale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRagioneSociale() {
        return ragioneSociale;
    }

    /**
     * Imposta il valore della proprietà ragioneSociale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRagioneSociale(String value) {
        this.ragioneSociale = value;
    }

}
