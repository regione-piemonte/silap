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
@Table(name="silap_d_ccnl", schema= "silap_tra")
public class SilapDCcnl extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_silap_d_ccnl")
	private String id;

	@Column(name="cod_silap_d_ccnl")
	private String cod;

	@Column(name="d_fine")
	private Date dataFine;

	@Column(name="d_inizio")
	private Date dataInizio;

	@Column(name="ds_settore")
	private String dsSettore;

	@Column(name="ds_silap_d_ccnl")
	private String descr;

	@Column(name="num_ore")
	private Long numOre;
	
}
