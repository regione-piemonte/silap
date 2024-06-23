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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="silap_d_comune", schema= "silap_tra")
public class SilapDComune extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_silap_d_comune")
	private String id;

	@Column(name="d_fine")
	private Date dataFine;

	@Column(name="d_inizio")
	private Date dataInizio;

	@Column(name="ds_silap_d_comune")
	private String descr;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_silap_d_provincia")
	private SilapDProvincia silapDProvincia;

}
