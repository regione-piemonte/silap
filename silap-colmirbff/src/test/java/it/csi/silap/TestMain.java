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
package it.csi.silap;

import java.io.File;
import java.util.Date;

import com.ibm.icu.util.Calendar;

import it.csi.silap.colmirbff.util.MathUtils;

public class TestMain {
	
	public static void main(String[] args) {
		
		File file = new File("path/nome_file.pdf");
		
		System.out.println(file.getName());
		
		
		Long oreSettimanaliMedie = 21l;
		
		int ret = MathUtils.round(oreSettimanaliMedie/40d);

		
		System.out.println(ret);
		
	}

}
