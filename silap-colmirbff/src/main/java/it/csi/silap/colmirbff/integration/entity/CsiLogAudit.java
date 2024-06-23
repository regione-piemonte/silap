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
@Table(name="csi_log_audit")
public class CsiLogAudit extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_audit")
	private Integer idAudit;

	@Column(name="data_ora")
	private Date dataOra;

	@Column(name="ip_address")
	private String ipAddress;

	@Column(name="ip_app")
	private String ipApp;

	@Column(name="key_oper")
	private String keyOper;

	@Column(name="ogg_oper")
	private String oggOper;

	private String operazione;

	private String utente;

}
