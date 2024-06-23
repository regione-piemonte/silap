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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="eso_t_versamento_cob_lav")
public class EsoTVersamentoCobLav extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_versamento_pv_cob_lav", sequenceName = "seq_eso_t_versamento_pv_cob_lav", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_versamento_pv_cob_lav")
	@Column(name="id_eso_t_versamento_pv_cob_lav")
	private Long idEsoTVersamentoPvCobLav;

	@Column(name="cod_fiscale")
	private String codFiscale;

	@Column(name="codice_comunicazione_reg")
	private String codiceComunicazioneReg;

	@Column(name="ds_cognome")
	private String dsCognome;

	@Column(name="ds_nome")
	private String dsNome;

	@Column(name="ds_tipo_comunicazione")
	private String dsTipoComunicazione;

	@Column(name="ds_tipo_contratto")
	private String dsTipoContratto;

	@Column(name="flg_fulltime")
	private String flgFulltime;

	@Column(name="flg_l68")
	private String flgL68;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_eso_t_versamento_pv_cob")
	private EsoTVersamentoPvCob esoTVersamentoPvCob;

	@Column(name="num_ore_sett_med")
	private Long numOreSettMed;

	@Column(name="sigla_pv")
	private String siglaPv;

}
