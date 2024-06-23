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
package it.csi.silap.colmirbff.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="eso_t_versamento_pv_periodo")
public class EsoTVersamentoPvPeriodo extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_versamento_pv_periodo", sequenceName = "seq_eso_t_versamento_pv_periodo", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_versamento_pv_periodo")
	@Column(name="id_eso_t_versamento_pv_periodo")
	private Long idEsoTVersamentoPvPeriodo;

	@Column(name="base_computo")
	private Long baseComputo;

	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name="cod_user_inserim")
	private String codUserInserim;

	@Column(name="d_aggiorn")
	private Date dAggiorn;

	@Column(name="d_fine")
	private Date dFine;

	@Column(name="d_inizio")
	private Date dInizio;

	@Column(name="d_inserim")
	private Date dInserim;

	@Column(name="flg_tipo")
	private String flgTipo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_eso_t_versamento_provincia")
	private EsoTVersamentoProvincia esoTVersamentoProvincia;

	@Column(name="importo")
	private BigDecimal importo;

	@Column(name="n_timestamp")
	private Long nTimestamp;

	@Column(name="num_disabili_in_forza")
	private Long numDisabiliInForza;

	@Column(name="num_esonerati_autocertificati")
	private Long numEsoneratiAutocertificati;

	@Column(name="num_gg_lavorativi")
	private Long numGgLavorativi;

	@Column(name="num_lavoratori_esonerati")
	private Long numLavoratoriEsonerati;

	@Column(name="num_soggetti_compensati")
	private Long numSoggettiCompensati;

	@Column(name="quota_riserva")
	private Long quotaRiserva;
	
	@Column(name="num_riallineamento_nazionale")
	private Long numRiallineamentoNazionale;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_silap_d_categoria_azienda")
	private SilapDCategoriaAzienda silapDCategoriaAzienda;
	
	
	
	// utili per il calcolo esonero automatico
	@Transient
	private EsoDVersamentoMotivoSospensione esoDVersamentoMotivoSospensione;
	@Transient
	private Long numLavoratoriSospesi;
	

}
