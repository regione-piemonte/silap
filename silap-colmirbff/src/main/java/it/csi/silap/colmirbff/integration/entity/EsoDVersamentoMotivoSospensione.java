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
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="eso_d_versamento_motivo_sospensione")
public class EsoDVersamentoMotivoSospensione extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_eso_d_versamento_motivo_sospensione")
	private Long id;

	@Column(name="d_fine")
	private Date dataFine;

	@Column(name="d_inizio")
	private Date dataInizio;

	@Column(name="ds_eso_d_versamento_motivo_sospensione")
	private String descr;

	private Long percentuale;

}
