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

import javax.enterprise.context.ApplicationScoped;

import org.mapstruct.Mapper;

import it.csi.silap.colmirsrv.api.dto.PosizioneDebitoria;
import it.csi.silap.colmirsrv.integration.entity.EsoTPosizioneDebitoria;

@ApplicationScoped
@Mapper(componentModel = "cdi")
public abstract class PosizioneDebitoriaMapper
		implements BaseMapperInterface<PosizioneDebitoria, EsoTPosizioneDebitoria> {

	public abstract PosizioneDebitoria toModel(EsoTPosizioneDebitoria entity);

}
