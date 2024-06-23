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
@Table(name="eso_t_parametro")
public class EsoTParametro extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_eso_t_parametro")
	private Long idEsoTParametro;

	@Column(name="cod_parametro")
	private String codParametro;

	@Column(name="d_fine_validita")
	private Date dFineValidita;

	@Column(name="d_inizio_validita")
	private Date dInizioValidita;

	@Column(name="ds_note")
	private String dsNote;

	@Column(name="ds_valore")
	private String dsValore;

	@Column(name="flg_tipo_valore")
	private String flgTipoValore;

}
