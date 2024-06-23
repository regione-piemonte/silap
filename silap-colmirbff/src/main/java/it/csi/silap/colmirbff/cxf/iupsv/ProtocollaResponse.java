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
 *         &lt;element name="protocollaReturn" type="{urn:iupsv}ProtocolloDTO" minOccurs="0"/>
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
    "protocollaReturn"
})
@XmlRootElement(name = "protocollaResponse")
public class ProtocollaResponse {

    protected ProtocolloDTO protocollaReturn;

    /**
     * Recupera il valore della proprietà protocollaReturn.
     * 
     * @return
     *     possible object is
     *     {@link ProtocolloDTO }
     *     
     */
    public ProtocolloDTO getProtocollaReturn() {
        return protocollaReturn;
    }

    /**
     * Imposta il valore della proprietà protocollaReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtocolloDTO }
     *     
     */
    public void setProtocollaReturn(ProtocolloDTO value) {
        this.protocollaReturn = value;
    }

}
