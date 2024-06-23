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
 * <p>Classe Java per InfoSistemaProtocollazioneDTO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="InfoSistemaProtocollazioneDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="campiRicercaProtocolli" type="{urn:iupsv}ArrayOf_xsd_string"/>
 *         &lt;element name="consenteAnnulloProtocolli" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="consenteGestioneSoggetti" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="consenteModificaProtocolli" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="consenteRicercaProtocolli" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="provincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoSistemaProtocollazioneDTO", propOrder = {
    "campiRicercaProtocolli",
    "consenteAnnulloProtocolli",
    "consenteGestioneSoggetti",
    "consenteModificaProtocolli",
    "consenteRicercaProtocolli",
    "provincia"
})
public class InfoSistemaProtocollazioneDTO {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfXsdString campiRicercaProtocolli;
    protected boolean consenteAnnulloProtocolli;
    protected boolean consenteGestioneSoggetti;
    protected boolean consenteModificaProtocolli;
    protected boolean consenteRicercaProtocolli;
    @XmlElement(required = true, nillable = true)
    protected String provincia;

    /**
     * Recupera il valore della proprietà campiRicercaProtocolli.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public ArrayOfXsdString getCampiRicercaProtocolli() {
        return campiRicercaProtocolli;
    }

    /**
     * Imposta il valore della proprietà campiRicercaProtocolli.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public void setCampiRicercaProtocolli(ArrayOfXsdString value) {
        this.campiRicercaProtocolli = value;
    }

    /**
     * Recupera il valore della proprietà consenteAnnulloProtocolli.
     * 
     */
    public boolean isConsenteAnnulloProtocolli() {
        return consenteAnnulloProtocolli;
    }

    /**
     * Imposta il valore della proprietà consenteAnnulloProtocolli.
     * 
     */
    public void setConsenteAnnulloProtocolli(boolean value) {
        this.consenteAnnulloProtocolli = value;
    }

    /**
     * Recupera il valore della proprietà consenteGestioneSoggetti.
     * 
     */
    public boolean isConsenteGestioneSoggetti() {
        return consenteGestioneSoggetti;
    }

    /**
     * Imposta il valore della proprietà consenteGestioneSoggetti.
     * 
     */
    public void setConsenteGestioneSoggetti(boolean value) {
        this.consenteGestioneSoggetti = value;
    }

    /**
     * Recupera il valore della proprietà consenteModificaProtocolli.
     * 
     */
    public boolean isConsenteModificaProtocolli() {
        return consenteModificaProtocolli;
    }

    /**
     * Imposta il valore della proprietà consenteModificaProtocolli.
     * 
     */
    public void setConsenteModificaProtocolli(boolean value) {
        this.consenteModificaProtocolli = value;
    }

    /**
     * Recupera il valore della proprietà consenteRicercaProtocolli.
     * 
     */
    public boolean isConsenteRicercaProtocolli() {
        return consenteRicercaProtocolli;
    }

    /**
     * Imposta il valore della proprietà consenteRicercaProtocolli.
     * 
     */
    public void setConsenteRicercaProtocolli(boolean value) {
        this.consenteRicercaProtocolli = value;
    }

    /**
     * Recupera il valore della proprietà provincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Imposta il valore della proprietà provincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvincia(String value) {
        this.provincia = value;
    }

}
