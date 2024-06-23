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

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class VersamentoEsoneriRidotto {
	
	@Id
	private Long idEsoTVersamento;
	
	private String codFiscale;
	private Long annoRiferimento;
	private Date dtStato;
	private Long idEsoDVersamentoStato;
	private String descStato;
	private Long annoProtocollo;
	private Long numProtocollo;
	private BigDecimal importo;
	private String dsDenominazioneAzienda;

	private Long numRata;
	private BigDecimal importoPagato;
	private Date dtEsito;
}
