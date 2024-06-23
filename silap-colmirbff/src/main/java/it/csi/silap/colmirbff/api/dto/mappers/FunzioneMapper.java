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

import it.csi.silap.colmirbff.api.dto.Funzione;
import it.csi.silap.colmirbff.integration.entity.SilapDFunzione;

@ApplicationScoped
@Mapper(componentModel = "cdi")
public abstract class FunzioneMapper implements BaseMapperInterface<Funzione,SilapDFunzione> {

	public abstract Funzione toModel(SilapDFunzione entity);

}
