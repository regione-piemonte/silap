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
 *         &lt;element name="modificaSedeReturn" type="{urn:silpsvaa}AnagraficaSediAziende" minOccurs="0"/>
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
    "modificaSedeReturn"
})
@XmlRootElement(name = "modificaSedeResponse")
public class ModificaSedeResponse {

    protected AnagraficaSediAziende modificaSedeReturn;

    /**
     * Recupera il valore della proprietà modificaSedeReturn.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaSediAziende }
     *     
     */
    public AnagraficaSediAziende getModificaSedeReturn() {
        return modificaSedeReturn;
    }

    /**
     * Imposta il valore della proprietà modificaSedeReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaSediAziende }
     *     
     */
    public void setModificaSedeReturn(AnagraficaSediAziende value) {
        this.modificaSedeReturn = value;
    }

}
