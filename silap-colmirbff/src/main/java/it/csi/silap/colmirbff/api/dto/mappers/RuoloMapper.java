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
import org.mapstruct.Mapping;

import it.csi.silap.colmirbff.api.dto.Ruolo;
import it.csi.silap.colmirbff.integration.entity.SilapDRuolo;

@ApplicationScoped
@Mapper(componentModel = "cdi", uses = { RuoloFunzioneMapper.class })
public abstract class RuoloMapper implements BaseMapperInterface<Ruolo,SilapDRuolo> {

	@Mapping(source="idSilapDRuolo",target="idSilapDRuolo")
	@Mapping(source="dsDescrizione",target="dsDescrizione")
	@Mapping(source="dsSilapDRuolo",target="dsSilapDRuolo")
	@Mapping(source="silapRRuoloFunziones",target="ruoloFunzioni")
	public abstract Ruolo toModel(SilapDRuolo entity);

}
