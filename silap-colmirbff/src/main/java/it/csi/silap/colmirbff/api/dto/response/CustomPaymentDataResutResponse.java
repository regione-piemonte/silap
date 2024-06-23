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
package it.csi.silap.colmirbff.api.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.csi.silap.colmirbff.api.dto.common.CommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomPaymentDataResutResponse extends CommonResponse {
	
  private BigDecimal importoPagatoTotale = null;
  private String enteBeneficiario = null;
  private String codiceFiscaleEnteBeneficiario = null;
  private String tipologiaVersamento = null;
  private BigDecimal importoPagato = null;
  private String nomeECognomeRagioneSociale = null;
  private String codiceFiscalePIva = null;
  private String codiceAvviso = null;
  private String identificativoUnicoVersamento = null;
  private String numeroTransazione = null;
  private String prestatoreDiServiziDiPagamento = null;
  private Date dataEOraOperazione = null;
  private Date dataEsitoPagamento = null;
  private String identificativoUnicoRiscossione = null;
  private String infoAggiuntive = null;
}
