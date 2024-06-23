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
 *         &lt;element name="modificaAziendaReturn" type="{urn:silpsvaa}AnagraficaAziende" minOccurs="0"/>
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
    "modificaAziendaReturn"
})
@XmlRootElement(name = "modificaAziendaResponse")
public class ModificaAziendaResponse {

    protected AnagraficaAziende modificaAziendaReturn;

    /**
     * Recupera il valore della proprietà modificaAziendaReturn.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaAziende }
     *     
     */
    public AnagraficaAziende getModificaAziendaReturn() {
        return modificaAziendaReturn;
    }

    /**
     * Imposta il valore della proprietà modificaAziendaReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaAziende }
     *     
     */
    public void setModificaAziendaReturn(AnagraficaAziende value) {
        this.modificaAziendaReturn = value;
    }

}
