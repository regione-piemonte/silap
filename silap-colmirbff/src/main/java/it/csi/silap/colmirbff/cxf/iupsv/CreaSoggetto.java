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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="in0" type="{urn:iupsv}UtenteProtocolloDTO" minOccurs="0"/>
 *         &lt;element name="in1" type="{urn:iupsv}SoggettoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "in0",
    "in1"
})
@XmlRootElement(name = "creaSoggetto")
public class CreaSoggetto {

    protected UtenteProtocolloDTO in0;
    protected SoggettoDTO in1;

    /**
     * Recupera il valore della proprietà in0.
     * 
     * @return
     *     possible object is
     *     {@link UtenteProtocolloDTO }
     *     
     */
    public UtenteProtocolloDTO getIn0() {
        return in0;
    }

    /**
     * Imposta il valore della proprietà in0.
     * 
     * @param value
     *     allowed object is
     *     {@link UtenteProtocolloDTO }
     *     
     */
    public void setIn0(UtenteProtocolloDTO value) {
        this.in0 = value;
    }

    /**
     * Recupera il valore della proprietà in1.
     * 
     * @return
     *     possible object is
     *     {@link SoggettoDTO }
     *     
     */
    public SoggettoDTO getIn1() {
        return in1;
    }

    /**
     * Imposta il valore della proprietà in1.
     * 
     * @param value
     *     allowed object is
     *     {@link SoggettoDTO }
     *     
     */
    public void setIn1(SoggettoDTO value) {
        this.in1 = value;
    }

}
