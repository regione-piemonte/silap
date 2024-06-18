/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.integration.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "eso_t_notifica_pagamento_log")
public class EsoTNotificaPagamentoLog extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_notifica_pagamento_log", sequenceName = "seq_eso_t_notifica_pagamento_log", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_notifica_pagamento_log")
	@Column(name = "id_eso_t_notifica_pagamento_log")
	private Long idEsoTNotificaPagamentoLog;

	@Column(name = "cod_id_messaggio")
	private String codIdMessaggio;

	@Column(name = "ds_messaggio")
	private String dsMessaggio;

	@Column(name = "cod_iuv")
	private String codIuv;

	@Column(name = "esito")
	private String esito;

	@Column(name = "ds_esito")
	private String dsEsito;

	@Column(name = "dt_inserim")
	private Date dtInserim;

	@Column(name = "cod_user_inserim")
	private String codUserInserim;

	@Column(name = "dt_aggiorn")
	private Date dtAggiorn;

	@Column(name = "cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name = "n_timestamp")
	private Long ntimestamp;

}
