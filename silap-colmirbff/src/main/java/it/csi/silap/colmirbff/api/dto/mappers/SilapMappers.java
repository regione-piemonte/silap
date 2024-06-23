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
package it.csi.silap.colmirbff.api.dto.mappers;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Mappers for Silap
 */
@Dependent
public class SilapMappers {
	/*
	 * RICORDARSI DI RIFARE UNA COMPILE PER CREARE LE IMPLEMENTAZIONI DEI MAPPERS
	 * OGNI VOLTA CHE SI MODIFICA O CREA UN MAPPER
	 */

	@Inject
	public VersamentoPvGgExtraFestiviMapper VERSAMENTO_PV_GG_EXTRA_FESTIVI;
	@Inject
	public VersamentoMapper VERSAMENTO;
	@Inject
	public VersamentoPvEsoneroMapper VERSAMENTO_PV_ESONERO;
	@Inject
	public CcnlMapper CCNL;
	@Inject
	public VersamentoPvProspettoMapper VERSAMENTO_PV_PROSPETTO;
	@Inject
	public OperatoreMapper OPERATORE;
	@Inject
	public VersamentoStatoMapper VERSAMENTO_STATO;
	@Inject
	public RuoloMapper RUOLO;
	@Inject
	public MessaggioMapper MESSAGGIO;
	@Inject
	public FunzioneMapper FUNZIONE;
	@Inject
	public VersamentoPvSospensioneMapper VERSAMENTO_PV_SOSPENZIONE;
	@Inject
	public VersamentoProspettoMapper VERSAMENTO_PROSPETTO;
	@Inject
	public VersamentoCobLavMapper VERSAMENTO_COB_LAV;
	@Inject
	public TipoMessaggioMapper TIPO_MESSAGGIO;
	@Inject
	public VersamentoPvCobMapper VERSAMENTO_PV_COB;
	@Inject
	public VersamentoTipoConvenzioneMapper VERSAMENTO_TIPO_CONVENZIONE;
	@Inject
	public ProvinciaMapper PROVINCIA;
	@Inject
	public VersamentoMotivoSospensioneMapper VERSAMENTO_MOTIVO_SOSPENSIONE;
	@Inject
	public VersamentoSedeMapper VERSAMENTO_SEDE;
	@Inject
	public ParametroMapper PARAMETRO;
	@Inject
	public ComuneMapper COMUNE;
	@Inject
	public VersamentoPvConvenzioneMapper VERSAMENTO_PV_CONVENZIONE;
	@Inject
	public CreditoResiduoMapper CREDITO_RESIDUO;
	@Inject
	public VersamentoPvPeriodoMapper VERSAMENTO_PV_PERIODO;
	@Inject
	public VersamentoProvinciaMapper VERSAMENTO_PROVINCIA;

	@Inject
	public VersamentoEsoneriRidottoMapper VERSAMENTO_ESONERI_RIDOTTO;
	@Inject
	public RuoloFunzioneMapper RUOLO_FUNZIONE;

	@Inject
	public DVersamentoStatoMapper D_VERSAMENTO_STATO;

	@Inject
	public GecoComunicazioneObbligatoriaMapper COMUNICAZIONE_OBBLIGATORIA;

	@Inject
	public BatchExecMapper BATCH_EXEC;

	@Inject
	public BatchLogMapper BATCH_LOG;

	@Inject
	public PosizioneDebitoriaMapper POSIZIONE_DEBITORIA;
	
	@Inject
	public VersamentoPvRicInMapper VERSAMENTO_PV_RIC_IN;
	
	@Inject
	public RiconoscimentiInAbilitaMapper RICONOSCIMENTI_INABILITA;
	
	@Inject
	public CategoriaAziendaMapper CATEGORIA_AZIENDA;
	
	
}
