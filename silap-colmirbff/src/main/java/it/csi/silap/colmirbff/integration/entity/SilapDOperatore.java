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
@Table(name="silap_d_operatore", schema= "silap_auth")
public class SilapDOperatore extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_silap_d_operatore")
	private Long idSilapDOperatore;

	@Column(name="cod_fiscale")
	private String codFiscale;

	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name="cod_user_inserim")
	private String codUserInserim;

	@Column(name="d_aggiorn")
	private Date dAggiorn;

	@Column(name="d_inserim")
	private Date dInserim;

	@Column(name="ds_cognome")
	private String dsCognome;

	@Column(name="ds_nome")
	private String dsNome;

	@Column(name="n_timestamp")
	private Integer nTimestamp;

	@OneToMany(mappedBy="silapDOperatore")
	@ToString.Exclude
	private List<SilapROperatoreRuolo> silapROperatoreRuolos;
}
