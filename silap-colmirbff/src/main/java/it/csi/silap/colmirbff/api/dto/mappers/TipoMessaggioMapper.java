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

import javax.enterprise.context.ApplicationScoped;

import org.mapstruct.Mapper;

import it.csi.silap.colmirbff.api.dto.TipoMessaggio;
import it.csi.silap.colmirbff.integration.entity.SilapDTipoMessaggio;

@ApplicationScoped
@Mapper(componentModel = "cdi")
public abstract class TipoMessaggioMapper implements BaseMapperInterface<TipoMessaggio,SilapDTipoMessaggio> {

	public abstract TipoMessaggio toModel(SilapDTipoMessaggio entity);

}
