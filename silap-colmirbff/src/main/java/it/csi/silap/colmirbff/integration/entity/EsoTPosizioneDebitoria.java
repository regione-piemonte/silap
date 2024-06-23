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
@Table(name="eso_t_posizione_debitoria")
public class EsoTPosizioneDebitoria extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_eso_t_posizione_debitoria", sequenceName = "seq_eso_t_posizione_debitoria", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eso_t_posizione_debitoria")
	@Column(name="id_eso_t_posizione_debitoria")
	private Long idEsoTPosizioneDebitoria;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_eso_t_versamento")
	private EsoTVersamento esoTVersamento;

	@Column(name="cod_iuv")
	private String codIuv;
	
	@Column(name="cod_avviso")
	private String codAvviso;
	
	@Column(name="cod_id_pagamento")
	private String codIdPagamento;
	
	@Column(name="cod_versamento")
	private String codVersamento;
	
	@Column(name="dt_inizio_validita")
	private Date dtInizioValidita;
	
	@Column(name="dt_fine_validita")
	private Date dtFineValidita;
	
	@Column(name="dt_scadenza")
	private Date dtScadenza;
	
	@Column(name="num_rata")
	private Long numRata;
	
	@Column(name="importo_atteso")
	private BigDecimal importoAtteso;
	
	@Column(name="ds_causale")
	private String dsCausale;
	
	@Column(name="importo_pagato")
	private BigDecimal importoPagato;
	
	@Column(name="ds_soggetto_versante")
	private String dsSoggettoVersante;
	
	@Column(name="ds_debitore")
	private String dsDebitore;
	
	@Column(name="cf_debitore")
	private String cfDebitore;
	
	@Column(name="cod_iur")
	private String codIur;
	
	@Column(name="cod_transazione")
	private String codTransazione;
	
	@Column(name="dt_operazione")
	private Date dtOperazione;
	
	@Column(name="dt_esito")
	private Date dtEsito;
	
	@Column(name="ds_info_aggiuntive")
	private String dsInfoAggiuntive;
	
	@Column(name="ds_note")
	private String dsNote;
	
	@Column(name="dt_inserim")
	private Date dtInserim;
	
	@Column(name="cod_user_inserim")
	private String codUserInserim;
	
	@Column(name="dt_aggiorn")
	private Date dtAggiorn;
	
	@Column(name="cod_user_aggiorn")
	private String codUserAggiorn;
	
	@Column(name="n_timestamp")
	private Long ntimestamp;
	
	@Column(name="dt_invio_promemoria")
	private Date dtInvioPromemoria;
	
	@Column(name="ds_psp")
	private String dsPsp;

}
