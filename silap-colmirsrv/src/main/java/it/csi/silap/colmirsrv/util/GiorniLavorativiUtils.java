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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GiorniLavorativiUtils {

	public static int getGiorniLavorativi(Date startDate, Date endDate, Long giorniLavorativiSettimana,
			Date festaPatronaleDate, List<Date> extraGiorniFestiviDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		int workDays = 0;
		
		Calendar pasquetta = Calendar.getInstance();
		pasquetta.setTime(getPasquetta(startCal.get(Calendar.YEAR)));

		Calendar festaPatronale = null;
		if (festaPatronaleDate != null) {
			festaPatronale = Calendar.getInstance();
			festaPatronale.setTime(festaPatronaleDate);
		}
		
		List<Calendar> extraGiorniFestivi = new ArrayList<Calendar>();
		if (extraGiorniFestiviDate != null) {
			for (Date date : extraGiorniFestiviDate) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				extraGiorniFestivi.add(cal);
			}
		} 

		// Return 0 if start and end are the same
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			int num = getSingoloGiorno(startCal, pasquetta, festaPatronale, extraGiorniFestivi);
			if (num == 1) {
				if (giorniLavorativiSettimana == 5) {
					if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
							&& startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						return 1;
				} else if (giorniLavorativiSettimana == 6) {
					if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
						return 1;
				}
				return 0;
			}
			return num;
		}

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(endDate);
			endCal.setTime(startDate);
		}

		boolean startDay = true;
		do {
			// excluding start date
			if (!startDay)
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			startDay = false;

			int num = getSingoloGiorno(startCal, pasquetta, festaPatronale, extraGiorniFestivi);
			if (num == 0)
				continue;
			
			// giorni extra festivi
			if (extraGiorniFestiviDate != null) {
				boolean scarta = false;
				for (Calendar cal : extraGiorniFestivi) {
					if (startCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
							&& startCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
							&& startCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
						scarta = true;
						break;
					}
				}
				if (scarta)
					continue;
			}

			if (giorniLavorativiSettimana == 5) {
				if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
						&& startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
					++workDays;
			} else if (giorniLavorativiSettimana == 6) {
				if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
					++workDays;
			}
		} while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

		
		if (workDays == 0) workDays = 1;
		return workDays;
	}
	
	
	
	
	private static int getSingoloGiorno(Calendar startCal, Calendar pasquetta, Calendar festaPatronale, List<Calendar> extraGiorniFestivi) {
		// 1 gennaio
		if (startCal.get(Calendar.MONTH) == 0 && startCal.get(Calendar.DAY_OF_MONTH) == 1)
			return 0;

		// 6 gennaio
		if (startCal.get(Calendar.MONTH) == 0 && startCal.get(Calendar.DAY_OF_MONTH) == 6)
			return 0;

		// 25 aprile
		if (startCal.get(Calendar.MONTH) == 3 && startCal.get(Calendar.DAY_OF_MONTH) == 25)
			return 0;

		// 1 maggio
		if (startCal.get(Calendar.MONTH) == 4 && startCal.get(Calendar.DAY_OF_MONTH) == 1)
			return 0;

		// 2 giugno
		if (startCal.get(Calendar.MONTH) == 5 && startCal.get(Calendar.DAY_OF_MONTH) == 2)
			return 0;

		// 15 agosto
		if (startCal.get(Calendar.MONTH) == 7 && startCal.get(Calendar.DAY_OF_MONTH) == 15)
			return 0;

		// 1 novembre
		if (startCal.get(Calendar.MONTH) == 10 && startCal.get(Calendar.DAY_OF_MONTH) == 1)
			return 0;

		// 8 dicembre
		if (startCal.get(Calendar.MONTH) == 11 && startCal.get(Calendar.DAY_OF_MONTH) == 8)
			return 0;

		// 25 dicembre
		if (startCal.get(Calendar.MONTH) == 11 && startCal.get(Calendar.DAY_OF_MONTH) == 25)
			return 0;

		// 26 dicembre
		if (startCal.get(Calendar.MONTH) == 11 && startCal.get(Calendar.DAY_OF_MONTH) == 26)
			return 0;
		
		
		// pasquetta
		if (startCal.get(Calendar.MONTH) == pasquetta.get(Calendar.MONTH)
				&& startCal.get(Calendar.DAY_OF_MONTH) == pasquetta.get(Calendar.DAY_OF_MONTH))
			return 0;

		// festa patronale
		if (festaPatronale != null) {
			if (startCal.get(Calendar.MONTH) == festaPatronale.get(Calendar.MONTH)
					&& startCal.get(Calendar.DAY_OF_MONTH) == festaPatronale.get(Calendar.DAY_OF_MONTH))
				return 0;
		}

		// giorni extra festivi
		boolean scarta = false;
		for (Calendar cal : extraGiorniFestivi) {
			if (startCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
					&& startCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
					&& startCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
				scarta = true;
				break;
			}
		}
		if (scarta)
			return 0;
				

		return 1;

	}

	private final static Date getPasquetta(int year) {

		int a = year % 19;
		int b = year % 4;
		int c = year % 7;

		int m = 0;
		int n = 0;

		if ((year >= 1583) && (year <= 1699)) {
			m = 22;
			n = 2;
		}
		if ((year >= 1700) && (year <= 1799)) {
			m = 23;
			n = 3;
		}
		if ((year >= 1800) && (year <= 1899)) {
			m = 23;
			n = 4;
		}
		if ((year >= 1900) && (year <= 2099)) {
			m = 24;
			n = 5;
		}
		if ((year >= 2100) && (year <= 2199)) {
			m = 24;
			n = 6;
		}
		if ((year >= 2200) && (year <= 2299)) {
			m = 25;
			n = 0;
		}
		if ((year >= 2300) && (year <= 2399)) {
			m = 26;
			n = 1;
		}
		if ((year >= 2400) && (year <= 2499)) {
			m = 25;
			n = 1;
		}

		int d = (19 * a + m) % 30;
		int e = (2 * b + 4 * c + 6 * d + n) % 7;

		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);

		if (d + e < 10) {
			calendar.set(Calendar.MONTH, Calendar.MARCH);
			calendar.set(Calendar.DAY_OF_MONTH, d + e + 22);
		} else {
			calendar.set(Calendar.MONTH, Calendar.APRIL);
			int day = d + e - 9;
			if (26 == day) {
				day = 19;
			}
			if ((25 == day) && (28 == d) && (e == 6) && (a > 10)) {
				day = 18;
			}
			calendar.set(Calendar.DAY_OF_MONTH, day);
		}

		calendar.add(Calendar.DAY_OF_MONTH, 1);

		return calendar.getTime();
	}

	public static void main(String[] args) {

		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.YEAR, 2023);
		startDate.set(Calendar.MONTH, 3);
		startDate.set(Calendar.DAY_OF_MONTH, 3);

		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.YEAR, 2023);
		endDate.set(Calendar.MONTH, 3);
		endDate.set(Calendar.DAY_OF_MONTH, 14);

		Calendar festaPatronale = Calendar.getInstance();
		festaPatronale.set(Calendar.YEAR, 2023);
		festaPatronale.set(Calendar.MONTH, 24);
		festaPatronale.set(Calendar.DAY_OF_MONTH, 5);
		
		String key = "20231231";
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,  Integer.parseInt(key.substring(0,4)));
		cal.set(Calendar.MONTH,  Integer.parseInt(key.substring(4,6))-1);
		cal.set(Calendar.DAY_OF_MONTH,  Integer.parseInt(key.substring(6)));
		

		//int gg = getGiorniLavorativi(startDate.getTime(), endDate.getTime(), 5L, festaPatronale.getTime(),null);
System.out.println(Integer.parseInt(key.substring(6)));
		System.out.println(cal.getTime());
	}

}
