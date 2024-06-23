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
package it.csi.silap.colmirbff.api.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.logging.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersamentoPvProspetto implements LogicalComparator<VersamentoPvProspetto> {

	@JsonProperty("idEsoTVersamentoPvProspetto")
	private Long idEsoTVersamentoPvProspetto;

	@JsonProperty("baseComputoProvinciale")
	private Long baseComputoProvinciale;

	@JsonProperty("catCompensazioneDisabili")
	private String catCompensazioneDisabili;

	@JsonProperty("codUserAggiorn")
	private String codUserAggiorn;

	@JsonProperty("codUserInserim")
	private String codUserInserim;

	@JsonProperty("dAggiorn")
	private Date dAggiorn;

	@JsonProperty("dInserim")
	private Date dInserim;

	@JsonProperty("silapDProvincia")
	private Provincia silapDProvincia;

	@JsonProperty("nTimestamp")
	private Long nTimestamp;

	@JsonProperty("numDisabiliInForza")
	private Long numDisabiliInForza;

	@JsonProperty("numEsoneratiAutocertificati")
	private Long numEsoneratiAutocertificati;

	@JsonProperty("numSoggettiCompensatiDisabili")
	private Long numSoggettiCompensatiDisabili;

	@JsonProperty("quotaRiservaDisabili")
	private Long quotaRiservaDisabili;

	@Override
	public boolean logicalCompare(VersamentoPvProspetto other) {
		Log.info("VersamentoPvProspetto.logicaCompare");
		if (other == null)
			return false;
		
		if (this.getSilapDProvincia() == null)
			return false;
		
		return Objects.equals(this.getSilapDProvincia().getId(), other.getSilapDProvincia().getId()) 
			    && Objects.equals(baseComputoProvinciale, other.baseComputoProvinciale)
				&& Objects.equals(catCompensazioneDisabili, other.catCompensazioneDisabili)
				&& Objects.equals(numDisabiliInForza, other.numDisabiliInForza)
				&& Objects.equals(numEsoneratiAutocertificati, other.numEsoneratiAutocertificati)
				&& Objects.equals(numSoggettiCompensatiDisabili, other.numSoggettiCompensatiDisabili)
				&& Objects.equals(quotaRiservaDisabili, other.quotaRiservaDisabili);
	}


}
