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
 * <p>Classe Java per DatiAnnulloProtocolloDTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiAnnulloProtocolloDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annoProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="motivoAnnullo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroProtocollo" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "DatiAnnulloProtocolloDTO", propOrder = {
    "annoProtocollo",
    "motivoAnnullo",
    "numeroProtocollo",
    "tipoProtocollo"
})
public class DatiAnnulloProtocolloDTO {

    @XmlElement(required = true, nillable = true)
    protected String annoProtocollo;
    @XmlElement(required = true, nillable = true)
    protected String motivoAnnullo;
    @XmlElement(required = true, nillable = true)
    protected String numeroProtocollo;
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
     * Recupera il valore della proprietà motivoAnnullo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivoAnnullo() {
        return motivoAnnullo;
    }

    /**
     * Imposta il valore della proprietà motivoAnnullo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivoAnnullo(String value) {
        this.motivoAnnullo = value;
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
