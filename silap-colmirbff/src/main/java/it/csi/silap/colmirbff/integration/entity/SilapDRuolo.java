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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "silap_d_ruolo", schema = "silap_auth")
public class SilapDRuolo extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_silap_d_ruolo")
	private Long idSilapDRuolo;

	@Column(name = "ds_descrizione")
	private String dsDescrizione;

	@Column(name = "ds_silap_d_ruolo")
	private String dsSilapDRuolo;

	@OneToMany(mappedBy = "silapDRuolo")
	@ToString.Exclude
	private List<SilapROperatoreRuolo> silapROperatoreRuolos;

	@OneToMany(mappedBy = "silapDRuolo")
	@ToString.Exclude
	private List<SilapRRuoloFunzione> silapRRuoloFunziones;

}
