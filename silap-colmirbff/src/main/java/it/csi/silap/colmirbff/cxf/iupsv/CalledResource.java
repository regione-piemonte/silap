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
 * <p>Classe Java per CalledResource complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="CalledResource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codRisorsa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codSistema" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoRisorsa" type="{urn:iupsv}ResourceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CalledResource", propOrder = {
    "codRisorsa",
    "codSistema",
    "tipoRisorsa"
})
public class CalledResource {

    @XmlElement(required = true, nillable = true)
    protected String codRisorsa;
    @XmlElement(required = true, nillable = true)
    protected String codSistema;
    @XmlElement(required = true, nillable = true)
    protected ResourceType tipoRisorsa;

    /**
     * Recupera il valore della proprietà codRisorsa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRisorsa() {
        return codRisorsa;
    }

    /**
     * Imposta il valore della proprietà codRisorsa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodRisorsa(String value) {
        this.codRisorsa = value;
    }

    /**
     * Recupera il valore della proprietà codSistema.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSistema() {
        return codSistema;
    }

    /**
     * Imposta il valore della proprietà codSistema.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSistema(String value) {
        this.codSistema = value;
    }

    /**
     * Recupera il valore della proprietà tipoRisorsa.
     * 
     * @return
     *     possible object is
     *     {@link ResourceType }
     *     
     */
    public ResourceType getTipoRisorsa() {
        return tipoRisorsa;
    }

    /**
     * Imposta il valore della proprietà tipoRisorsa.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceType }
     *     
     */
    public void setTipoRisorsa(ResourceType value) {
        this.tipoRisorsa = value;
    }

}
