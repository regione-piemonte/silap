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
@Table(name="eso_t_batch_log")
public class EsoTBatchLog extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name = "seq_eso_t_batch_log", sequenceName = "seq_eso_t_batch_log", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_batch_log")
	@Column(name="id_eso_t_batch_log")
	private Long idEsoTBatchLog;
	
	@Column(name="cod_fiscale")
	private String codFiscale;
	
	@Column(name="esito")
	private String esito;
	
	@Column(name="log_exec")
	private String logExec;
	
	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name="cod_user_inserim")
	private String codUserInserim;

	@Column(name="d_aggiorn")
	private Date dAggiorn;

	@Column(name="d_inserim")
	private Date dInserim;
	
	@Column(name="n_timestamp")
	private Long nTimestamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_eso_t_batch_exec")
	private EsoTBatchExec esoTBatchExec;
}





