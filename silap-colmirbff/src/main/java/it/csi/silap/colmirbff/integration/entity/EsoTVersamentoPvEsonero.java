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
@Table(name="eso_t_versamento_pv_esonero")
public class EsoTVersamentoPvEsonero extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_versamento_pv_esonero", sequenceName = "seq_eso_t_versamento_pv_esonero", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_versamento_pv_esonero")
	@Column(name="id_eso_t_versamento_pv_esonero")
	private Long idEsoTVersamentoPvEsonero;

	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name="cod_user_inserim")
	private String codUserInserim;

	@Column(name="d_aggiorn")
	private Date dAggiorn;

	@Column(name="d_concessione")
	private Date dConcessione;

	@Column(name="d_diniego")
	private Date dDiniego;

	@Column(name="d_inserim")
	private Date dInserim;

	@Column(name="d_richiesta")
	private Date dRichiesta;

	@Column(name="d_scadenza")
	private Date dScadenza;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_eso_t_versamento_provincia")
	private EsoTVersamentoProvincia esoTVersamentoProvincia;

	@Column(name="n_timestamp")
	private Long nTimestamp;

	@Column(name="perc_concessione")
	private Long percConcessione;

	@Column(name="perc_richiesta")
	private Long percRichiesta;

	@Column(name="ds_tipo_comunicazione")
	private String dsTipoComunicazione;
	
	@Column(name="d_classificazione")
	private Date dClassificazione;
	
	@Column(name="num_classificazione")
	private Long numClassificazione;

}
