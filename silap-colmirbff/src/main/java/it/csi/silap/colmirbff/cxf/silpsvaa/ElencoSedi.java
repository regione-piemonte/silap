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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ElencoSedi complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ElencoSedi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="capSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codPatInail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceFiscaleAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataFineAttivita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComuneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrIndirizzoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrLocalitaSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrNazioneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrNumCivicoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStatoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrToponimoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flgValida" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idComuneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idNazioneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idSedeAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idStatoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idToponimoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="motivoValidita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numAgenziaSomministrazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numPreferenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ragioneSocialeSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElencoSedi", propOrder = {
    "capSede",
    "codPatInail",
    "codiceFiscaleAzienda",
    "dataFineAttivita",
    "descrComuneSede",
    "descrIndirizzoSede",
    "descrLocalitaSede",
    "descrNazioneSede",
    "descrNumCivicoSede",
    "descrStatoSede",
    "descrToponimoSede",
    "email",
    "fax",
    "flgValida",
    "idAzienda",
    "idComuneSede",
    "idNazioneSede",
    "idSedeAzienda",
    "idStatoSede",
    "idToponimoSede",
    "motivoValidita",
    "numAgenziaSomministrazione",
    "numPreferenza",
    "ragioneSocialeSede",
    "telefono",
    "tipoSede"
})
public class ElencoSedi {

    @XmlElement(required = true, nillable = true)
    protected String capSede;
    @XmlElement(required = true, nillable = true)
    protected String codPatInail;
    @XmlElement(required = true, nillable = true)
    protected String codiceFiscaleAzienda;
    @XmlElement(required = true, nillable = true)
    protected String dataFineAttivita;
    @XmlElement(required = true, nillable = true)
    protected String descrComuneSede;
    @XmlElement(required = true, nillable = true)
    protected String descrIndirizzoSede;
    @XmlElement(required = true, nillable = true)
    protected String descrLocalitaSede;
    @XmlElement(required = true, nillable = true)
    protected String descrNazioneSede;
    @XmlElement(required = true, nillable = true)
    protected String descrNumCivicoSede;
    @XmlElement(required = true, nillable = true)
    protected String descrStatoSede;
    @XmlElement(required = true, nillable = true)
    protected String descrToponimoSede;
    @XmlElement(required = true, nillable = true)
    protected String email;
    @XmlElement(required = true, nillable = true)
    protected String fax;
    @XmlElement(required = true, nillable = true)
    protected String flgValida;
    @XmlElement(required = true, nillable = true)
    protected String idAzienda;
    @XmlElement(required = true, nillable = true)
    protected String idComuneSede;
    @XmlElement(required = true, nillable = true)
    protected String idNazioneSede;
    @XmlElement(required = true, nillable = true)
    protected String idSedeAzienda;
    @XmlElement(required = true, nillable = true)
    protected String idStatoSede;
    @XmlElement(required = true, nillable = true)
    protected String idToponimoSede;
    @XmlElement(required = true, nillable = true)
    protected String motivoValidita;
    @XmlElement(required = true, nillable = true)
    protected String numAgenziaSomministrazione;
    @XmlElement(required = true, nillable = true)
    protected String numPreferenza;
    @XmlElement(required = true, nillable = true)
    protected String ragioneSocialeSede;
    @XmlElement(required = true, nillable = true)
    protected String telefono;
    @XmlElement(required = true, nillable = true)
    protected String tipoSede;

    /**
     * Recupera il valore della proprietà capSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapSede() {
        return capSede;
    }

    /**
     * Imposta il valore della proprietà capSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapSede(String value) {
        this.capSede = value;
    }

    /**
     * Recupera il valore della proprietà codPatInail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPatInail() {
        return codPatInail;
    }

    /**
     * Imposta il valore della proprietà codPatInail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPatInail(String value) {
        this.codPatInail = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscaleAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscaleAzienda() {
        return codiceFiscaleAzienda;
    }

    /**
     * Imposta il valore della proprietà codiceFiscaleAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscaleAzienda(String value) {
        this.codiceFiscaleAzienda = value;
    }

    /**
     * Recupera il valore della proprietà dataFineAttivita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataFineAttivita() {
        return dataFineAttivita;
    }

    /**
     * Imposta il valore della proprietà dataFineAttivita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataFineAttivita(String value) {
        this.dataFineAttivita = value;
    }

    /**
     * Recupera il valore della proprietà descrComuneSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComuneSede() {
        return descrComuneSede;
    }

    /**
     * Imposta il valore della proprietà descrComuneSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComuneSede(String value) {
        this.descrComuneSede = value;
    }

    /**
     * Recupera il valore della proprietà descrIndirizzoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrIndirizzoSede() {
        return descrIndirizzoSede;
    }

    /**
     * Imposta il valore della proprietà descrIndirizzoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrIndirizzoSede(String value) {
        this.descrIndirizzoSede = value;
    }

    /**
     * Recupera il valore della proprietà descrLocalitaSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrLocalitaSede() {
        return descrLocalitaSede;
    }

    /**
     * Imposta il valore della proprietà descrLocalitaSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrLocalitaSede(String value) {
        this.descrLocalitaSede = value;
    }

    /**
     * Recupera il valore della proprietà descrNazioneSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrNazioneSede() {
        return descrNazioneSede;
    }

    /**
     * Imposta il valore della proprietà descrNazioneSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrNazioneSede(String value) {
        this.descrNazioneSede = value;
    }

    /**
     * Recupera il valore della proprietà descrNumCivicoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrNumCivicoSede() {
        return descrNumCivicoSede;
    }

    /**
     * Imposta il valore della proprietà descrNumCivicoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrNumCivicoSede(String value) {
        this.descrNumCivicoSede = value;
    }

    /**
     * Recupera il valore della proprietà descrStatoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrStatoSede() {
        return descrStatoSede;
    }

    /**
     * Imposta il valore della proprietà descrStatoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrStatoSede(String value) {
        this.descrStatoSede = value;
    }

    /**
     * Recupera il valore della proprietà descrToponimoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrToponimoSede() {
        return descrToponimoSede;
    }

    /**
     * Imposta il valore della proprietà descrToponimoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrToponimoSede(String value) {
        this.descrToponimoSede = value;
    }

    /**
     * Recupera il valore della proprietà email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta il valore della proprietà email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Recupera il valore della proprietà fax.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax() {
        return fax;
    }

    /**
     * Imposta il valore della proprietà fax.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax(String value) {
        this.fax = value;
    }

    /**
     * Recupera il valore della proprietà flgValida.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlgValida() {
        return flgValida;
    }

    /**
     * Imposta il valore della proprietà flgValida.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlgValida(String value) {
        this.flgValida = value;
    }

    /**
     * Recupera il valore della proprietà idAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAzienda() {
        return idAzienda;
    }

    /**
     * Imposta il valore della proprietà idAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAzienda(String value) {
        this.idAzienda = value;
    }

    /**
     * Recupera il valore della proprietà idComuneSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdComuneSede() {
        return idComuneSede;
    }

    /**
     * Imposta il valore della proprietà idComuneSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdComuneSede(String value) {
        this.idComuneSede = value;
    }

    /**
     * Recupera il valore della proprietà idNazioneSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdNazioneSede() {
        return idNazioneSede;
    }

    /**
     * Imposta il valore della proprietà idNazioneSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdNazioneSede(String value) {
        this.idNazioneSede = value;
    }

    /**
     * Recupera il valore della proprietà idSedeAzienda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSedeAzienda() {
        return idSedeAzienda;
    }

    /**
     * Imposta il valore della proprietà idSedeAzienda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSedeAzienda(String value) {
        this.idSedeAzienda = value;
    }

    /**
     * Recupera il valore della proprietà idStatoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdStatoSede() {
        return idStatoSede;
    }

    /**
     * Imposta il valore della proprietà idStatoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdStatoSede(String value) {
        this.idStatoSede = value;
    }

    /**
     * Recupera il valore della proprietà idToponimoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdToponimoSede() {
        return idToponimoSede;
    }

    /**
     * Imposta il valore della proprietà idToponimoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdToponimoSede(String value) {
        this.idToponimoSede = value;
    }

    /**
     * Recupera il valore della proprietà motivoValidita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivoValidita() {
        return motivoValidita;
    }

    /**
     * Imposta il valore della proprietà motivoValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivoValidita(String value) {
        this.motivoValidita = value;
    }

    /**
     * Recupera il valore della proprietà numAgenziaSomministrazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumAgenziaSomministrazione() {
        return numAgenziaSomministrazione;
    }

    /**
     * Imposta il valore della proprietà numAgenziaSomministrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumAgenziaSomministrazione(String value) {
        this.numAgenziaSomministrazione = value;
    }

    /**
     * Recupera il valore della proprietà numPreferenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumPreferenza() {
        return numPreferenza;
    }

    /**
     * Imposta il valore della proprietà numPreferenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumPreferenza(String value) {
        this.numPreferenza = value;
    }

    /**
     * Recupera il valore della proprietà ragioneSocialeSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRagioneSocialeSede() {
        return ragioneSocialeSede;
    }

    /**
     * Imposta il valore della proprietà ragioneSocialeSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRagioneSocialeSede(String value) {
        this.ragioneSocialeSede = value;
    }

    /**
     * Recupera il valore della proprietà telefono.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il valore della proprietà telefono.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Recupera il valore della proprietà tipoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoSede() {
        return tipoSede;
    }

    /**
     * Imposta il valore della proprietà tipoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoSede(String value) {
        this.tipoSede = value;
    }

}
