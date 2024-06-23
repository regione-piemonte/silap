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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="silap_r_operatore_ruolo", schema= "silap_auth")
public class SilapROperatoreRuolo extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_silap_d_ruolo")
	private SilapDRuolo silapDRuolo;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_silap_d_operatore")
	private SilapDOperatore silapDOperatore;

	@Temporal(TemporalType.DATE)
	@Column(name = "d_fine")
	private Date dFine;

	@Temporal(TemporalType.DATE)
	@Column(name = "d_inizio")
	private Date dInizio;
	
}
