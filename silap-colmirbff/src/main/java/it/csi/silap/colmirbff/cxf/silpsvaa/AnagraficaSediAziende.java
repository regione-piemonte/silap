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
 * <p>Classe Java per AnagraficaSediAziende complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AnagraficaSediAziende">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EMail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="altreInformazioni" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="capSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codInail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codInps" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codMinisteroAteco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataFineAttivita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataInizioAttivita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrAtecoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComuneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrIndirizzoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrLocalitaSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrMotivo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrNazioneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrNumCivicoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrToponimoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagValida" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAtecoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idComuneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idNazioneSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idSedeAccorpati" type="{urn:silpsvaa}ArrayOf_xsd_string"/>
 *         &lt;element name="idSedeAzienda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idTipoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idToponimoSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numAgenziaSomm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numPreferenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ragioneSocialeSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="siglaProvinciaSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnagraficaSediAziende", propOrder = {
    "eMail",
    "altreInformazioni",
    "capSede",
    "codInail",
    "codInps",
    "codMinisteroAteco",
    "codUser",
    "dataFineAttivita",
    "dataInizioAttivita",
    "descrAtecoSede",
    "descrComuneSede",
    "descrIndirizzoSede",
    "descrLocalitaSede",
    "descrMotivo",
    "descrNazioneSede",
    "descrNumCivicoSede",
    "descrTipoSede",
    "descrToponimoSede",
    "fax",
    "flagValida",
    "idAtecoSede",
    "idAzienda",
    "idComuneSede",
    "idNazioneSede",
    "idSedeAccorpati",
    "idSedeAzienda",
    "idTipoSede",
    "idToponimoSede",
    "numAgenziaSomm",
    "numPreferenza",
    "ragioneSocialeSede",
    "siglaProvinciaSede",
    "telefono"
})
public class AnagraficaSediAziende {

    @XmlElement(name = "EMail", required = true, nillable = true)
    protected String eMail;
    @XmlElement(required = true, nillable = true)
    protected String altreInformazioni;
    @XmlElement(required = true, nillable = true)
    protected String capSede;
    @XmlElement(required = true, nillable = true)
    protected String codInail;
    @XmlElement(required = true, nillable = true)
    protected String codInps;
    @XmlElement(required = true, nillable = true)
    protected String codMinisteroAteco;
    @XmlElement(required = true, nillable = true)
    protected String codUser;
    @XmlElement(required = true, nillable = true)
    protected String dataFineAttivita;
    @XmlElement(required = true, nillable = true)
    protected String dataInizioAttivita;
    @XmlElement(required = true, nillable = true)
    protected String descrAtecoSede;
    @XmlElement(required = true, nillable = true)
    protected String descrComuneSede;
    @XmlElement(required = true, nillable = true)
    protected String descrIndirizzoSede;
    @XmlElement(required = true, nillable = true)
    protected String descrLocalitaSede;
    @XmlElement(required = true, nillable = true)
    protected String descrMotivo;
    @XmlElement(required = true, nillable = true)
    protected String descrNazioneSede;
    @XmlElement(required = true, nillable = true)
    protected String descrNumCivicoSede;
    @XmlElement(required = true, nillable = true)
    protected String descrTipoSede;
    @XmlElement(required = true, nillable = true)
    protected String descrToponimoSede;
    @XmlElement(required = true, nillable = true)
    protected String fax;
    @XmlElement(required = true, nillable = true)
    protected String flagValida;
    @XmlElement(required = true, nillable = true)
    protected String idAtecoSede;
    @XmlElement(required = true, nillable = true)
    protected String idAzienda;
    @XmlElement(required = true, nillable = true)
    protected String idComuneSede;
    @XmlElement(required = true, nillable = true)
    protected String idNazioneSede;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfXsdString idSedeAccorpati;
    @XmlElement(required = true, nillable = true)
    protected String idSedeAzienda;
    @XmlElement(required = true, nillable = true)
    protected String idTipoSede;
    @XmlElement(required = true, nillable = true)
    protected String idToponimoSede;
    @XmlElement(required = true, nillable = true)
    protected String numAgenziaSomm;
    @XmlElement(required = true, nillable = true)
    protected String numPreferenza;
    @XmlElement(required = true, nillable = true)
    protected String ragioneSocialeSede;
    @XmlElement(required = true, nillable = true)
    protected String siglaProvinciaSede;
    @XmlElement(required = true, nillable = true)
    protected String telefono;

    /**
     * Recupera il valore della proprietà eMail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMail() {
        return eMail;
    }

    /**
     * Imposta il valore della proprietà eMail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMail(String value) {
        this.eMail = value;
    }

    /**
     * Recupera il valore della proprietà altreInformazioni.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAltreInformazioni() {
        return altreInformazioni;
    }

    /**
     * Imposta il valore della proprietà altreInformazioni.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAltreInformazioni(String value) {
        this.altreInformazioni = value;
    }

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
     * Recupera il valore della proprietà codInail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodInail() {
        return codInail;
    }

    /**
     * Imposta il valore della proprietà codInail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodInail(String value) {
        this.codInail = value;
    }

    /**
     * Recupera il valore della proprietà codInps.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodInps() {
        return codInps;
    }

    /**
     * Imposta il valore della proprietà codInps.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodInps(String value) {
        this.codInps = value;
    }

    /**
     * Recupera il valore della proprietà codMinisteroAteco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodMinisteroAteco() {
        return codMinisteroAteco;
    }

    /**
     * Imposta il valore della proprietà codMinisteroAteco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodMinisteroAteco(String value) {
        this.codMinisteroAteco = value;
    }

    /**
     * Recupera il valore della proprietà codUser.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUser() {
        return codUser;
    }

    /**
     * Imposta il valore della proprietà codUser.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUser(String value) {
        this.codUser = value;
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
     * Recupera il valore della proprietà dataInizioAttivita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataInizioAttivita() {
        return dataInizioAttivita;
    }

    /**
     * Imposta il valore della proprietà dataInizioAttivita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataInizioAttivita(String value) {
        this.dataInizioAttivita = value;
    }

    /**
     * Recupera il valore della proprietà descrAtecoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrAtecoSede() {
        return descrAtecoSede;
    }

    /**
     * Imposta il valore della proprietà descrAtecoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrAtecoSede(String value) {
        this.descrAtecoSede = value;
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
     * Recupera il valore della proprietà descrMotivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrMotivo() {
        return descrMotivo;
    }

    /**
     * Imposta il valore della proprietà descrMotivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrMotivo(String value) {
        this.descrMotivo = value;
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
     * Recupera il valore della proprietà descrTipoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoSede() {
        return descrTipoSede;
    }

    /**
     * Imposta il valore della proprietà descrTipoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoSede(String value) {
        this.descrTipoSede = value;
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
     * Recupera il valore della proprietà flagValida.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagValida() {
        return flagValida;
    }

    /**
     * Imposta il valore della proprietà flagValida.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagValida(String value) {
        this.flagValida = value;
    }

    /**
     * Recupera il valore della proprietà idAtecoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAtecoSede() {
        return idAtecoSede;
    }

    /**
     * Imposta il valore della proprietà idAtecoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAtecoSede(String value) {
        this.idAtecoSede = value;
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
     * Recupera il valore della proprietà idSedeAccorpati.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public ArrayOfXsdString getIdSedeAccorpati() {
        return idSedeAccorpati;
    }

    /**
     * Imposta il valore della proprietà idSedeAccorpati.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public void setIdSedeAccorpati(ArrayOfXsdString value) {
        this.idSedeAccorpati = value;
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
     * Recupera il valore della proprietà idTipoSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoSede() {
        return idTipoSede;
    }

    /**
     * Imposta il valore della proprietà idTipoSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoSede(String value) {
        this.idTipoSede = value;
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
     * Recupera il valore della proprietà numAgenziaSomm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumAgenziaSomm() {
        return numAgenziaSomm;
    }

    /**
     * Imposta il valore della proprietà numAgenziaSomm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumAgenziaSomm(String value) {
        this.numAgenziaSomm = value;
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
     * Recupera il valore della proprietà siglaProvinciaSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaProvinciaSede() {
        return siglaProvinciaSede;
    }

    /**
     * Imposta il valore della proprietà siglaProvinciaSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaProvinciaSede(String value) {
        this.siglaProvinciaSede = value;
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

}
