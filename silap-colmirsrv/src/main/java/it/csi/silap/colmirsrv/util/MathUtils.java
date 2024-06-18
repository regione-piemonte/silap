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

public class MathUtils {

	public static int round(Double rounded) {
		double x = rounded - Math.floor(rounded);
		if (x>0.5) return ((int) rounded.doubleValue()) + 1;
		return (int) rounded.doubleValue();
	}
	
	
	public static int round(Long rounded) {
		double x = rounded - Math.floor(rounded);
		if (x>0.5) return ((int) rounded.doubleValue()) + 1;
		return (int) rounded.doubleValue();
	}
	
	public static int calcolaQuotaRiserva(Long baseComputo) {
		double rounded = (baseComputo.doubleValue()*7d) / 100d;
		double x = rounded - Math.floor(rounded);
		int quotaRiserva = 0;
		if (x>0.5)
			quotaRiserva = ((int) rounded) + 1;
		else quotaRiserva = ((int) rounded);
		return quotaRiserva;
	}
	
	
	public static double mathRound(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
}
