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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "eso_t_versamento_provincia")
public class EsoTVersamentoProvincia extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_versamento_provincia", sequenceName = "seq_eso_t_versamento_provincia", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_versamento_provincia")
	@Column(name = "id_eso_t_versamento_provincia")
	private Long idEsoTVersamentoProvincia;

	@Column(name = "cod_user_aggiorn")
	private String codUserAggiorn;

	@Column(name = "cod_user_inserim")
	private String codUserInserim;

	@Column(name = "d_aggiorn")
	private Date dAggiorn;

	@Column(name = "d_festa_patronale")
	private Date dFestaPatronale;

	@Column(name = "d_inserim")
	private Date dInserim;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_eso_t_versamento")
	private EsoTVersamento esoTVersamento;

	@Column(name = "n_timestamp")
	private Long nTimestamp;

	@Column(name = "num_gg_lavorativi_settimanali")
	private Long numGgLavorativiSettimanali;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_silap_d_provincia")
	private SilapDProvincia silapDProvincia;

	@OneToMany(mappedBy = "esoTVersamentoProvincia")
	private List<EsoTVersamentoPvPeriodo> esoTVersamentoPvPeriodos;

	@OneToMany(mappedBy = "esoTVersamentoProvincia")
	private List<EsoTVersamentoPvGgExtraFestivi> esoTVersamentoPvGgExtraFestivis;

	@OneToMany(mappedBy = "esoTVersamentoProvincia")
	private List<EsoTVersamentoPvSospensione> esoTVersamentoPvSospensiones;

	@OneToMany(mappedBy = "esoTVersamentoProvincia")
	private List<EsoTVersamentoPvConvenzione> esoTVersamentoPvConvenziones;

	@OneToMany(mappedBy = "esoTVersamentoProvincia")
	private List<EsoTVersamentoPvEsonero> esoTVersamentoPvEsoneros;
	
	@OneToMany(mappedBy = "esoTVersamentoProvincia")
	private List<EsoTVersamentoPvCob> esoTVersamentoPvCobs;
	
	@OneToMany(mappedBy = "esoTVersamentoProvincia")
	private List<EsoTVersamentoPvRicIn> esoTVersamentoPvRicIns;
	
	@Column(name="importo_automatico")
	private BigDecimal importoAutomatico;
	

}
