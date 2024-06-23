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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="eso_t_batch_exec")
public class EsoTBatchExec extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name = "seq_eso_t_batch_exec", sequenceName = "seq_eso_t_batch_exec", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_batch_exec")
	@Column(name="id_eso_t_batch_exec")
	private Long idEsoTBatchExec;
	
	@Column(name="cod_eso_t_batch_exec")
	private String codEsoTBatchExec;
	
	@Column(name="d_inizio_exec")
	private Date dInizioExec;

	@Column(name="d_fine_exec")
	private Date dFineExec;

	@Column(name="num_record_da_elaborare")
	private Long numRecordDaElaborare;
	
	@Column(name="num_record_ok")
	private Long numRecordOk;
	
	@Column(name="num_record_ko")
	private Long numRecordKo;
	
	@Column(name="note")
	private String note;

	@Column(name="cod_user_inserim")
	private String codUserInserim;
	
	@Column(name="d_inserim")
	private Date dInserim;
	
	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;
	
	@Column(name="d_aggiorn")
	private Date dAggiorn;
	
	@Column(name="n_timestamp")
	private Long nTimestamp;
	
	@OneToMany(mappedBy = "esoTBatchExec")
	private List<EsoTBatchLog> esoTBatchLogs;

}





