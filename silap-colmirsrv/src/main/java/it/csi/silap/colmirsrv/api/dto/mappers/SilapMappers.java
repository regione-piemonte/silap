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
package it.csi.silap.colmirsrv.api.dto.mappers;

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
	public MessaggioMapper MESSAGGIO;

	@Inject
	public ParametroMapper PARAMETRO;
	
	@Inject
	public PosizioneDebitoriaMapper POSIZIONE_DEBITORIA;
	
	@Inject
	public VersamentoStatoMapper VERSAMENTO_STATO;
	
	
}
