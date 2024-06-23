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

import it.csi.silap.colmirbff.api.dto.RuoloFunzione;
import it.csi.silap.colmirbff.integration.entity.SilapRRuoloFunzione;

@ApplicationScoped
@Mapper(componentModel = "cdi", uses = { FunzioneMapper.class })
public abstract class RuoloFunzioneMapper implements BaseMapperInterface<RuoloFunzione, SilapRRuoloFunzione> {


	@Mapping(source="silapDFunzione",target="funzione")
	@Mapping(source="DInizio",target="dataInizio")
	@Mapping(source="DFine",target="dataFine")
	public abstract RuoloFunzione toModel(SilapRRuoloFunzione entity);
	
	

}
