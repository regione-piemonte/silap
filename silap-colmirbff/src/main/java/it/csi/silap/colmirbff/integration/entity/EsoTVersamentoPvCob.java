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
@Table(name="eso_t_versamento_pv_cob")
public class EsoTVersamentoPvCob extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_versamento_pv_cob", sequenceName = "seq_eso_t_versamento_pv_cob", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_versamento_pv_cob")
	@Column(name="id_eso_t_versamento_pv_cob")
	private Long idEsoTVersamentoPvCob;

	@Column(name="d_cob")
	private Date dCob;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_eso_t_versamento_provincia")
	private EsoTVersamentoProvincia esoTVersamentoProvincia;

	@Column(name="num_lavoratori")
	private Long numLavoratori;

	@Column(name="num_lavoratori_disabili")
	private Long numLavoratoriDisabili;
	
	@OneToMany(mappedBy="esoTVersamentoPvCob")
	private List<EsoTVersamentoCobLav> esoTVersamentoCobLavs;
	
	

}
