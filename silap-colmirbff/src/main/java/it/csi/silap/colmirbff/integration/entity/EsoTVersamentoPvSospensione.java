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

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="eso_t_versamento_pv_sospensione")
public class EsoTVersamentoPvSospensione extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_versamento_pv_sospensione", sequenceName = "seq_eso_t_versamento_pv_sospensione", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_versamento_pv_sospensione")
	@Column(name="id_eso_t_versamento_pv_sospensione")
	private Long idEsoTVersamentoPvSospensione;

	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name="cod_user_inserim")
	private String codUserInserim;

	@Column(name="d_aggiorn")
	private Date dAggiorn;

	@Column(name="d_fine_sospensione")
	private Date dFineSospensione;

	@Column(name="d_inizio_sospensione")
	private Date dInizioSospensione;

	@Column(name="d_inserim")
	private Date dInserim;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_eso_d_versamento_motivo_sospensione")
	private EsoDVersamentoMotivoSospensione esoDVersamentoMotivoSospensione;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_eso_t_versamento_provincia")
	private EsoTVersamentoProvincia esoTVersamentoProvincia;

	@Column(name="n_timestamp")
	private Long nTimestamp;

	@Column(name="num_lavoratori_sospesi")
	private Long numLavoratoriSospesi;

	@Column(name="perc_sospensione")
	private Long percSospensione;

}
