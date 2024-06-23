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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatiServiziProdisSilp {

	public DatiServiziProdisSilp() {
	}

	public DatiServiziProdisSilp(DatiServiziProdisSilp other) {
		this.setProspetti(other.getProspetti());
		this.setProvinceEsoneri(other.getProvinceEsoneri());
		this.setProvinceConvenzioni(other.getProvinceConvenzioni());
		this.setAzienda(other.getAzienda());
	}

	@JsonProperty("prospetti")
	@ToString.Exclude
	List<VersamentoProspetto> prospetti = new ArrayList<VersamentoProspetto>();
	
	@JsonProperty("provinceEsoneri")
	@ToString.Exclude
	List<VersamentoProvincia> provinceEsoneri = new ArrayList<VersamentoProvincia>();
	
	@JsonProperty("provinceConvenzioni")
	@ToString.Exclude
	List<VersamentoProvincia> provinceConvenzioni = new ArrayList<VersamentoProvincia>();
	
	@JsonProperty("messaggi")
	@ToString.Exclude
	List<ApiMessage > messaggi = new ArrayList<>();
	
	@JsonProperty("azienda")
	@ToString.Exclude
	Azienda azienda = null;
}
