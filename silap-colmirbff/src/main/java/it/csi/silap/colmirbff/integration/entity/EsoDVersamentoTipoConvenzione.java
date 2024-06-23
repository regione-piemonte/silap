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
@Table(name="eso_d_versamento_tipo_convenzione")
public class EsoDVersamentoTipoConvenzione extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_eso_d_versamento_tipo_convenzione")
	private Long idEsoDVersamentoTipoConvenzione;

	@Column(name="d_fine")
	private Date dFine;

	@Column(name="d_inizio")
	private Date dInizio;

	@Column(name="ds_eso_d_versamento_tipo_convenzione")
	private String dsEsoDVersamentoTipoConvenzione;

	@Column(name="id_sil_l68_tipo_convenzione")
	private String idSilL68TipoConvenzione;

}
