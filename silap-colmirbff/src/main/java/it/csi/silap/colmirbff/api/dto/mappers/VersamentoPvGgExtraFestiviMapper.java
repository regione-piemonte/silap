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

import it.csi.silap.colmirbff.api.dto.VersamentoPvGgExtraFestivi;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvGgExtraFestivi;

@ApplicationScoped
@Mapper(componentModel = "cdi")
public abstract class VersamentoPvGgExtraFestiviMapper implements BaseMapperInterface<VersamentoPvGgExtraFestivi,EsoTVersamentoPvGgExtraFestivi> {

	public abstract VersamentoPvGgExtraFestivi toModel(EsoTVersamentoPvGgExtraFestivi entity);

}
