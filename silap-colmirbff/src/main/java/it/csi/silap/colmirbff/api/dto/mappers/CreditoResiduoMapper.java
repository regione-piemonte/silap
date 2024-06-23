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

import it.csi.silap.colmirbff.api.dto.CreditoResiduo;
import it.csi.silap.colmirbff.integration.entity.EsoTCreditoResiduo;

@ApplicationScoped
@Mapper(componentModel = "cdi")
public abstract class CreditoResiduoMapper implements BaseMapperInterface<CreditoResiduo,EsoTCreditoResiduo> {

	public abstract CreditoResiduo toModel(EsoTCreditoResiduo entity);

}
