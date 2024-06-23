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
 * <p>Classe Java per InvocationNode complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="InvocationNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="childs" type="{urn:iupsv}ArrayOfInvocationNode"/>
 *         &lt;element name="outcome" type="{urn:iupsv}Outcome"/>
 *         &lt;element name="resource" type="{urn:iupsv}CalledResource"/>
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="stopTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvocationNode", propOrder = {
    "childs",
    "outcome",
    "resource",
    "startTime",
    "stopTime"
})
public class InvocationNode {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfInvocationNode childs;
    @XmlElement(required = true, nillable = true)
    protected Outcome outcome;
    @XmlElement(required = true, nillable = true)
    protected CalledResource resource;
    protected long startTime;
    protected long stopTime;

    /**
     * Recupera il valore della proprietà childs.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInvocationNode }
     *     
     */
    public ArrayOfInvocationNode getChilds() {
        return childs;
    }

    /**
     * Imposta il valore della proprietà childs.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInvocationNode }
     *     
     */
    public void setChilds(ArrayOfInvocationNode value) {
        this.childs = value;
    }

    /**
     * Recupera il valore della proprietà outcome.
     * 
     * @return
     *     possible object is
     *     {@link Outcome }
     *     
     */
    public Outcome getOutcome() {
        return outcome;
    }

    /**
     * Imposta il valore della proprietà outcome.
     * 
     * @param value
     *     allowed object is
     *     {@link Outcome }
     *     
     */
    public void setOutcome(Outcome value) {
        this.outcome = value;
    }

    /**
     * Recupera il valore della proprietà resource.
     * 
     * @return
     *     possible object is
     *     {@link CalledResource }
     *     
     */
    public CalledResource getResource() {
        return resource;
    }

    /**
     * Imposta il valore della proprietà resource.
     * 
     * @param value
     *     allowed object is
     *     {@link CalledResource }
     *     
     */
    public void setResource(CalledResource value) {
        this.resource = value;
    }

    /**
     * Recupera il valore della proprietà startTime.
     * 
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Imposta il valore della proprietà startTime.
     * 
     */
    public void setStartTime(long value) {
        this.startTime = value;
    }

    /**
     * Recupera il valore della proprietà stopTime.
     * 
     */
    public long getStopTime() {
        return stopTime;
    }

    /**
     * Imposta il valore della proprietà stopTime.
     * 
     */
    public void setStopTime(long value) {
        this.stopTime = value;
    }

}
