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
 *         &lt;element name="getInfoSistemaProtocollazioneReturn" type="{urn:iupsv}InfoSistemaProtocollazioneDTO" minOccurs="0"/>
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
    "getInfoSistemaProtocollazioneReturn"
})
@XmlRootElement(name = "getInfoSistemaProtocollazioneResponse")
public class GetInfoSistemaProtocollazioneResponse {

    protected InfoSistemaProtocollazioneDTO getInfoSistemaProtocollazioneReturn;

    /**
     * Recupera il valore della proprietà getInfoSistemaProtocollazioneReturn.
     * 
     * @return
     *     possible object is
     *     {@link InfoSistemaProtocollazioneDTO }
     *     
     */
    public InfoSistemaProtocollazioneDTO getGetInfoSistemaProtocollazioneReturn() {
        return getInfoSistemaProtocollazioneReturn;
    }

    /**
     * Imposta il valore della proprietà getInfoSistemaProtocollazioneReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoSistemaProtocollazioneDTO }
     *     
     */
    public void setGetInfoSistemaProtocollazioneReturn(InfoSistemaProtocollazioneDTO value) {
        this.getInfoSistemaProtocollazioneReturn = value;
    }

}
