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
package it.csi.silap.colmirsrv.util;

import java.util.List;

import lombok.Data;

@Data
public class SqlComplexResult<E> {
	int recordOfPage;
	int totalResult;
	int totalPage;
	List<E> list;
}
