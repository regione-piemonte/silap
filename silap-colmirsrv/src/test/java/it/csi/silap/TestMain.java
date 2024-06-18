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
package it.csi.silap;

import it.csi.silap.colmirsrv.util.MathUtils;

public class TestMain {
	
	public static void main(String[] args) {
		
		Long oreSettimanaliMedie = 21l;
		
		int ret = MathUtils.round(oreSettimanaliMedie/40d);

		
		System.out.println(ret);
		
	}

}
