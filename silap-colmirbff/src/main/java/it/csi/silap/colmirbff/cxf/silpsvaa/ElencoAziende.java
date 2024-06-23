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
 * <p>Classe Java per ElencoAziende complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ElencoAziende">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codFiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagTipoAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flgMaster" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "ElencoAziende", propOrder = {
    "codFiscale",
    "flagTipoAzienda",
    "flgMaster",
    "idAzienda",
    "partitaIva",
    "ragioneSociale"
})
public class ElencoAziende {

    @XmlElement(required = true, nillable = true)
    protected String codFiscale;
    @XmlElement(required = true, nillable = true)
    protected String flagTipoAzienda;
    @XmlElement(required = true, nillable = true)
    protected String flgMaster;
    @XmlElement(required = true, nillable = true)
    protected String idAzienda;
    @XmlElement(required = true, nillable = true)
    protected String partitaIva;
    @XmlElement(required = true, nillable = true)
    protected String ragioneSociale;

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
     * Recupera il valore della proprietà flgMaster.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlgMaster() {
        return flgMaster;
    }

    /**
     * Imposta il valore della proprietà flgMaster.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlgMaster(String value) {
        this.flgMaster = value;
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
