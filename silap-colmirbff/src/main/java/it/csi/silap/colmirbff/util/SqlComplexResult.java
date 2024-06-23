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
package it.csi.silap.colmirbff.util;

import java.util.List;

import lombok.Data;

@Data
public class SqlComplexResult<E> {
	int recordOfPage;
	int totalResult;
	int totalPage;
	List<E> list;
}
