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
 * <p>Classe Java per Outcome complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Outcome">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="eccezione" type="{urn:iupsv}CSIException"/>
 *         &lt;element name="messaggio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Outcome", propOrder = {
    "eccezione",
    "messaggio",
    "status"
})
public class Outcome {

    @XmlElement(required = true, nillable = true)
    protected CSIException eccezione;
    @XmlElement(required = true, nillable = true)
    protected String messaggio;
    protected int status;

    /**
     * Recupera il valore della proprietà eccezione.
     * 
     * @return
     *     possible object is
     *     {@link CSIException }
     *     
     */
    public CSIException getEccezione() {
        return eccezione;
    }

    /**
     * Imposta il valore della proprietà eccezione.
     * 
     * @param value
     *     allowed object is
     *     {@link CSIException }
     *     
     */
    public void setEccezione(CSIException value) {
        this.eccezione = value;
    }

    /**
     * Recupera il valore della proprietà messaggio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessaggio() {
        return messaggio;
    }

    /**
     * Imposta il valore della proprietà messaggio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessaggio(String value) {
        this.messaggio = value;
    }

    /**
     * Recupera il valore della proprietà status.
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * Imposta il valore della proprietà status.
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

}
