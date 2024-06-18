/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="eso_t_versamento")
public class EsoTVersamento extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name="id_eso_t_versamento")
	private Long idEsoTVersamento;

	@Column(name="anno_protocollo")
	private Long annoProtocollo;

	@Column(name="anno_riferimento")
	private Long annoRiferimento;

	@Column(name="cod_fiscale")
	private String codFiscale;

	@Column(name="cod_fiscale_soggetto")
	private String codFiscaleSoggetto;

	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name="cod_user_inserim")
	private String codUserInserim;

	@Column(name="d_aggiorn")
	private Date dAggiorn;

	@Column(name="d_inserim")
	private Date dInserim;

	@Column(name="ds_denominazione_azienda")
	private String dsDenominazioneAzienda;

	@Column(name="ds_email_riferimento")
	private String dsEmailRiferimento;

	@Column(name="id_sil_azi_anagrafica")
	private Long idSilAziAnagrafica;

	@Column(name="n_timestamp")
	private Long nTimestamp;

	@Column(name="num_credito_residuo")
	private BigDecimal numCreditoResiduo;

	@Column(name="num_protocollo")
	private Long numProtocollo;

	@Column(name="d_protocollo")
	private Date dProtocollo;
	
	@Column(name="num_rate")
	private Long numRate;

	@Column(name="partiva_iva")
	private String partivaIva;
	

}
