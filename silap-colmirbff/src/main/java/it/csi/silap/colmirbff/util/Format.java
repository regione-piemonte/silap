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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
public class Format {
	private static final SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");

	public static int parseInt(String value) {
		int n = 0;
		try {
			n = Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return -1;
		}
		return n;
	}

	public static Long parseLong(String longValue) {
		try {
			return new Long(longValue);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String formatLong(Long longValue) {
		try {
			return longValue.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public static Double parseDouble(String doubleValue) {
		try {
			return new Double(doubleValue);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String formatDouble(Double doubleValue) {
		try {
			return doubleValue.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public static String formatDouble(Double doubleValue, int numeroDecimali) {
		String formato = "0.";
		for (int i = 0; i < numeroDecimali; i++) {
			formato += "0";
		}
		DecimalFormat fn = new DecimalFormat(formato, new DecimalFormatSymbols(Locale.ITALIAN));
		return fn.format(doubleValue);
	}

	/**
	 *
	 * @param data1
	 * @param data2
	 * @return 1 se date2>date1 0 se date2=date1 -1 se date2<date1
	 *
	 * @deprecated usate uno dei metodi isData1MinoreUgualeData2, isData1MinoreData2
	 *             ecc.ecc.
	 */
	public static int confrontaDate(Date data1, Date data2) {
		try {
			String sData1 = getStringDate(data1);
			String sData2 = getStringDate(data2);
			int dd1 = Integer.parseInt(sData1.substring(0, 2));
			int mm1 = Integer.parseInt(sData1.substring(3, 5));
			int yy1 = Integer.parseInt(sData1.substring(6, 10));
			int dd2 = Integer.parseInt(sData2.substring(0, 2));
			int mm2 = Integer.parseInt(sData2.substring(3, 5));
			int yy2 = Integer.parseInt(sData2.substring(6, 10));
			// se data2 e' maggiore di data1
			if ((yy2 > yy1) || (yy2 == yy1 && mm2 > mm1)) {
				return 1;
			}
			if (yy2 == yy1 && mm2 == mm1 && dd2 > dd1) {
				return 1;
			}
			// se data2 e' minore di data1
			if ((yy2 < yy1) || (yy2 == yy1 && mm2 < mm1)) {
				return -1;
			}
			if (yy2 == yy1 && mm2 == mm1 && dd2 < dd1) {
				return -1;
				// altrimenti sono uguali
			}
		} catch (Exception ex) {
			return 2;
		}
		return 0;
	}

	// confronto particolare fra due date: oltre all'anno, mese, giorno, anche ore,
	// minuti e secondi
	public static int confrontaDateOreMinutiSecondi(Date d1, Date d2) {
		int ris = 0;
		try {
			if (d1 != null && d2 == null) {
				return -1;
			}
			if (d1 == null && d2 == null) {
				return 0;
			}
			if (d1 == null && d2 != null) {
				return 1;
			}
			ris = 0;
			GregorianCalendar c1 = new GregorianCalendar();
			GregorianCalendar c2 = new GregorianCalendar();
			c1.setTime(d1);
			c2.setTime(d2);
			ris = confrontaDate(d1, d2);
			if (ris == 0) {
				// controllo ore, min, sec
				int ora1 = c1.get(Calendar.HOUR);
				int ora2 = c2.get(Calendar.HOUR);
				int min1 = c1.get(Calendar.MINUTE);
				int min2 = c2.get(Calendar.MINUTE);
				int sec1 = c1.get(Calendar.SECOND);
				int sec2 = c2.get(Calendar.SECOND);
				// controllo ore
				if (ora1 < ora2) {
					ris = 1;
					return ris;
				} else if (ora1 == ora2) {
					ris = 0;
				} else {
					ris = -1;
					return ris;
				}
				// controllo minuti
				if (ris == 0 && min1 < min2) {
					ris = 1;
					return ris;
				} else if (min1 == min2) {
					ris = 0;
				} else {
					ris = -1;
					return ris;
				}
				// controllo secondi
				if (ris == 0 && sec1 < sec2) {
					ris = 1;
					return ris;
				} else if (sec1 == sec2) {
					ris = 0;
				} else {
					ris = -1;
					return ris;
				}
			}
		} catch (Exception ex) {
			return 2;
		}
		return ris;
	}

	/** @todo CommonUTI */
	public static Date parseDate(String date) {
		return convertiStringaInData(date);
	}

	public static String formatDate(Date date) {
		return date.toString();
	}

	public static boolean verificaAnnoMinoreCorrente(String anno) {
		String sysdate = convertiDataInStringa(new Date());
		String annoCorrente = sysdate.substring(6);
		if (parseInt(anno) > parseInt(annoCorrente)) {
			return false;
		}
		return true;
	}

	/**
	 * Sostituisce la replaceFirst di String (in uso solo dalla 1.4)
	 */
	public static String replaceFirstString(final String input, final String exp, final String replacement) {
		final StringBuffer result = new StringBuffer();
		int startIdx = 0;
		int idxOld = 0;
		if ((idxOld = input.indexOf(exp, startIdx)) >= 0) {
			// SE l'exp che stiamo cercando esiste --> idxOld
			// conterra' l'indice dell'ultimo char della prima occorrenza
			// l'unica che ci interessa rimpiazzare
			// Per il resto tutto funziona come la replaceString (vedi sotto)
			result.append(input.substring(startIdx, idxOld));
			result.append(replacement);
			startIdx = idxOld + exp.length();
		}
		result.append(input.substring(startIdx));
		return result.toString();
	}

	public static String replaceString(final String aInput, final String aOldPattern, String aNewPattern) {

		// se non c'e' niente da sostituire me ne vado
		if (aInput == null || aInput.length() == 0 || aOldPattern == null || aOldPattern.length() == 0) {
			return aInput;
		}

		if (aNewPattern == null) {
			aNewPattern = ""; // se il pattern in ingresso e' nullo, lo imposto a vuoto
		}

		final StringBuffer result = new StringBuffer();
		// startIdx and idxOld delimit various chunks of aInput; these
		// chunks always end where aOldPattern begins
		int startIdx = 0;
		int idxOld = 0;
		while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
			// grab a part of aInput which does not include aOldPattern
			result.append(aInput.substring(startIdx, idxOld));
			// add aNewPattern to take place of aOldPattern
			result.append(aNewPattern);

			// reset the startIdx to just after the current match, to see
			// if there are any further matches
			startIdx = idxOld + aOldPattern.length();
		}
		// the final chunk will go to the end of aInput
		result.append(aInput.substring(startIdx));
		return result.toString();
	}



	public static ArrayList replaceStringInEveryElementOfInputCollection(Collection inputCollection, String oldString,
			String newString) {
		ArrayList result = new ArrayList();
		Iterator it = inputCollection.iterator();
		while (it.hasNext()) {
			String currElement = (String) it.next();
			currElement = Format.replaceString(currElement, oldString, newString);
			result.add(currElement);
		}
		return result;
	}

	public static java.util.Date getUtilDate(String data) {
		if (data == null || "".equals(data)) {
			return null;
		}
		String newDate = data.substring(0, 2) + "/" + data.substring(3, 5) + "/"
				+ (data.length() == 10 ? data.substring(6, 10) : data.substring(6, 8));
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		ParsePosition pos = new ParsePosition(0);
		return formatter.parse(newDate, pos);
	}

	public static String getStringDate(java.util.Date data) {
		if (data == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(data);
	}

	public static String getStringDateOraCorrente(java.util.Date data) {
		if (data == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(data);
	}

	public static java.util.Date convertiStringaInData(String data) {
		if (data == null || "".equals(data)) {
			return null;
		}
		java.util.Date date = null;
		String newDate = "";
		java.text.SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		java.text.ParsePosition pos = new ParsePosition(0);
		try {
			newDate = data.substring(0, 2) + "/" + data.substring(3, 5) + "/" + data.substring(6, 10);
			date = formatter.parse(newDate, pos);

			if (date == null) {

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				date = formatter.parse(formatter.format(df.parse(data)));
			}
		} catch (Exception ex) {

		}
		return date;
	}

	public static String convertiDataInStringa(Date date, String delim) {
		if (date == null || "".equals(date)) {
			return null;
		}
		String data = convertiDataInStringa(date);
		String newDate = "";
		try {
			newDate = data.substring(0, 2) + delim + data.substring(3, 5) + delim + data.substring(6, 10);
		} catch (Exception ex) {
			date = null;
		}
		return newDate;
	}

	// converte in data una stringa che rappresenta un adata e ora nel formato
	// dd/mm/yyyy hh:mm:ss anche con secondi opzionali
	public static java.util.Date convertiStringaConOreMinutiSecondiInData(String data) {
		if (data == null || "".equals(data.trim())) {
			return null;
		}
		GregorianCalendar date = null;
		String giorno = data.substring(0, 2);
		String mese = data.substring(3, 5);
		String anno = data.substring(6, 10);
		String ora = "00";
		String minuti = "00";
		String secondi = "00";
		String sottoStringaOreMinutiSecondi = null;
		int posPrimoSpazio = data.lastIndexOf(" ");
		sottoStringaOreMinutiSecondi = data.substring(posPrimoSpazio + 1);
		ora = sottoStringaOreMinutiSecondi.substring(0, 2);
		minuti = sottoStringaOreMinutiSecondi.substring(3, 5);
		// se la parte contenente ore minuti secondi ha piu' di 5 caratteri vuol dire
		// che ha anche i secondi
		if (sottoStringaOreMinutiSecondi.length() > 5) {
			secondi = sottoStringaOreMinutiSecondi.substring(6, 8);
		}

		try {
			date = new GregorianCalendar(Integer.parseInt(anno), Integer.parseInt(mese) - 1, Integer.parseInt(giorno),
					Integer.parseInt(ora), Integer.parseInt(minuti), Integer.parseInt(secondi));
		} catch (Exception ex) {
			return null;
		}
		return date.getTime();
	}

	public static GregorianCalendar convertiStringaInGragorianCalendar(String data) {
		if (data == null || "".equals(data.trim())) {
			return null;
		}
		GregorianCalendar date = null;
		try {
			date = new GregorianCalendar(Integer.parseInt(data.substring(6, 10)),
					Integer.parseInt(data.substring(3, 5)) - 1, Integer.parseInt(data.substring(0, 2)));
		} catch (NumberFormatException n) {
			SimpleDateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dfOut = new SimpleDateFormat("dd/MM/yyyy");

			date = new GregorianCalendar();
			try {
				date.setTime(dfOut.parse(dfOut.format(dfIn.parse(data))));
			} catch (ParseException e) {
				return null;
			}

		} catch (Exception ex) {
			return null;
		}
		return date;
	}

	public static String convertiGragorianCalendarInStringa(GregorianCalendar data) {
		if (data == null) {
			return null;
		}
		String year = "";
		String day = "" + data.get(Calendar.DAY_OF_MONTH);
		if (day.length() == 1) {
			day = "0" + day;
		}
		String month = "";
		month = "" + (data.get(Calendar.MONTH) + 1);
		if ("0".equalsIgnoreCase(month)) {
			month = "12";
			year = "" + (data.get(Calendar.YEAR));
		} else {
			year = "" + data.get(Calendar.YEAR);
		}
		if (month.length() == 1) {
			month = "0" + month;
		}
		return day + "/" + month + "/" + year;
	}

	public static Date convertGragorianCalendarToDate(GregorianCalendar data) {
		if (data == null) {
			return null;
		}
		return data.getTime();
	}

	public static String convertiDataInStringa(Date data) {
		if (data == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(data);
	}

	public static String convertiDataEOraInStringa(java.util.Date data) {
		if (data == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
		return formatter.format(data);
	}

	public static String convertiDataEOreMinutiInStringa(java.util.Date data) {
		if (data == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
		return formatter.format(data);
	}

	/**
	 * AB metodo per togliere eventuali ore minuti secondi da una Date. La uso
	 * per evitare di inserire su DB date complete quando non serve
	 *
	 * @param data
	 * @return
	 */
	public static Date truncDate(Date data) {
		return datePrivaDiOreMinutiSecondi(data);
	}
	
	
	
	public static final boolean isDateSoloGiornoUguali(Date date1, Date date2) {
		return datePrivaDiOreMinutiSecondi(date1).equals(datePrivaDiOreMinutiSecondi(date2));
	}
	

	/**
	 * restituisce la DIFFERENZA fra due date in giorni ai fini del calcolo della
	 * durata di un contratto (si somma 1 al risultato della diffDays nornali) in
	 * tal modo un contratto che inizia e finisce lo stesso giorno ha durata 1
	 * giorno e non 0
	 */
	public static int dateDiffDaysPerContratto(java.util.Date dataInizio, java.util.Date dataFine) {
		if (Format.isData1MinoreData2(dataFine, dataInizio)) {
			return 0;
		}
		return Format.dateDiffDays(dataInizio, dataFine) + 1; // 1 giorno e' la durata minima di un contratto !!!
	}

	/**
	 * restituisce la DIFFERENZA fra due date in giorni
	 */
	public static int dateDiffDays(java.util.Date d1, java.util.Date d2) {
		int result;
		if (d2.after(d1)) {
			result = (int) ((d2.getTime() / 86400000) - (d1.getTime() / 86400000));
		} else {
			result = (int) ((d1.getTime() / 86400000) - (d2.getTime() / 86400000));
		}
		return result;
	}

	// aggiunge giorni a una data e restituisce la data
	public static Date dateAddDays(java.util.Date date, int days) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	// aggiunge mesi a una data e restituisce la data
	// mettendo un valore "month" con - davanti toglie invece
	// di aggiungere
	public static Date dateAddMonth(java.util.Date date, int month) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}

	public static Date dateAddMonthMenoUnGiorno(java.util.Date date, int month) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		Date data = Format.dateAddDays(cal.getTime(), -1);
		return data;
	}

	// aggiunge anni a una data e restituisce la data
	public static Date dateAddYear(java.util.Date date, int year) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}

	public static Date dateAddYearMenoUnGiorno(java.util.Date date, int month) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.YEAR, month);
		Date data = Format.dateAddDays(cal.getTime(), -1);
		return data;
	}

	/*******
	 * [SG]:Aggiunge ad una data le HH:MM:SS di today
	 *
	 * @param date
	 * @return
	 */
	public static Date dateAddTime(java.util.Date data, Calendar today) {
		if (data == null) {
			return null;
		}
		Calendar dataResult = Calendar.getInstance();
		dataResult.setTime(data);
		dataResult.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY));
		dataResult.set(Calendar.MINUTE, today.get(Calendar.MINUTE));
		dataResult.set(Calendar.SECOND, today.get(Calendar.SECOND));
		return dataResult.getTime();
	}

	/**
	 * Questo metodo aggiunge o sottrae i giorni specificati all data in input
	 *
	 * se i giorni passati in input sono > 0 vengono sommati altrimenti se < 0
	 * vengono sottrratti
	 *
	 * @param date  Date
	 * @param month int
	 * @return Date
	 */
	public static Date datePiuOMenoGiorniSpecificati(java.util.Date date, int giorniDaSommareOSottrarre) {

		if (giorniDaSommareOSottrarre == 0) {
			return date;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, giorniDaSommareOSottrarre);
		return cal.getTime();
	}

	/**
	 * Versione del metodo convertiDTOBooleanInboolean che restituisce una stringa
	 *
	 * @param value
	 * @return 'true' o 'false'
	 */
	// public static String convertiDTOBooleanInStringaBoolean(String value){
	// return new Boolean(convertiDTOBooleanInStringaBoolean(value)).toString();
	// }

	/*
	 * public static boolean convertiStringInBoolean(String value) { if (value !=
	 * null) { return Boolean.valueOf(value).booleanValue(); } else { return false;
	 * } }
	 */

	public static boolean verificaCodiceFiscale(String codice) {
		if (codice == null) {
			codice = "";
		}
		codice = codice.trim();
		// LUNGHEZZA ERRATA
		if (codice.length() != 16) {
			return false;
		}
		// LUNGHEZZA CORRETTA
		else if (codice.length() == 16) {
			String uno = codice.substring(0, 1);
			char unoc = uno.charAt(0);
			String due = codice.substring(1, 2);
			char duec = due.charAt(0);
			String tre = codice.substring(2, 3);
			char trec = tre.charAt(0);
			String quattro = codice.substring(3, 4);
			char quattroc = quattro.charAt(0);
			String cinque = codice.substring(4, 5);
			char cinquec = cinque.charAt(0);
			String sei = codice.substring(5, 6);
			char seic = sei.charAt(0);
			// CONTROLLO FORMALE
			if (Character.isDigit(unoc) || Character.isDigit(duec) || Character.isDigit(trec)
					|| Character.isDigit(quattroc) || Character.isDigit(cinquec) || Character.isDigit(seic)
					|| Character.isLetter(codice.substring(6, 7).charAt(0))
					|| Character.isLetter(codice.substring(7, 8).charAt(0))
					|| Character.isDigit(codice.substring(8, 9).charAt(0))
					|| Character.isLetter(codice.substring(9, 10).charAt(0))
					|| Character.isLetter(codice.substring(10, 11).charAt(0))
					|| Character.isDigit(codice.substring(11, 12).charAt(0))
					|| Character.isLetter(codice.substring(12, 13).charAt(0))
					|| Character.isLetter(codice.substring(13, 14).charAt(0))
					|| Character.isLetter(codice.substring(14, 15).charAt(0))
					|| Character.isDigit(codice.substring(15, 16).charAt(0))) {
				return false;
			}
		}
		return true;
	}

	public static String generaCodiceFiscale(String cognome, String nome, String dataNascita, String sesso,
			String codComune, String codStatoEstero) {

		String tmpCodiceFiscale = "";
		if (codComune != null && !"".equals(codComune)) {
			tmpCodiceFiscale = (calcolaCognome(cognome)) + (calcolaNome(nome)) + (calcolaDataNasc(dataNascita, sesso))
					+ codComune;
		} else if (codStatoEstero != null && !"".equals(codStatoEstero)) {
			tmpCodiceFiscale = (calcolaCognome(cognome)) + (calcolaNome(nome)) + (calcolaDataNasc(dataNascita, sesso))
					+ codStatoEstero;
		}
		// calcolo del CRT
		tmpCodiceFiscale = tmpCodiceFiscale + (calcolaControlCrt(tmpCodiceFiscale));
		return tmpCodiceFiscale;
	}

	/**
	 * Calcola il carattere di controllo: La stringa di ritorno sara' costituita da
	 * una lettera dell'alfabeto ottenuta dai controlli che devono essere effettuati
	 * sul codice fiscale che viene passato. Il calcolo e' realizzato secondo una
	 * mappa associativa propria dell'algoritmo di calcolo.
	 *
	 * @param codFisc Codice Fiscale calcolato con cognome,nome,data di nascita e
	 *                codice comune
	 * @return stringa con il carattere di controllo del CF
	 */
	private static char calcolaControlCrt(String codFisc) {
		int counter = 0; // serve per accumulare il codice numerico
		int offset = 0;
		// I primi 8 caratteri dispari...
		for (int i = 0; i < 8; i++, offset += 2) {
			char tmpChar = codFisc.charAt(offset);
			if (tmpChar == 'A' || tmpChar == '0') {
				counter += 1;
			} else if (tmpChar == 'B' || tmpChar == '1') {
				counter += 0;
			} else if (tmpChar == 'C' || tmpChar == '2') {
				counter += 5;
			} else if (tmpChar == 'D' || tmpChar == '3') {
				counter += 7;
			} else if (tmpChar == 'E' || tmpChar == '4') {
				counter += 9;
			} else if (tmpChar == 'F' || tmpChar == '5') {
				counter += 13;
			} else if (tmpChar == 'G' || tmpChar == '6') {
				counter += 15;
			} else if (tmpChar == 'H' || tmpChar == '7') {
				counter += 17;
			} else if (tmpChar == 'I' || tmpChar == '8') {
				counter += 19;
			} else if (tmpChar == 'J' || tmpChar == '9') {
				counter += 21;
			} else if (tmpChar == 'K') {
				counter += 2;
			} else if (tmpChar == 'L') {
				counter += 4;
			} else if (tmpChar == 'M') {
				counter += 18;
			} else if (tmpChar == 'N') {
				counter += 20;
			} else if (tmpChar == 'O') {
				counter += 11;
			} else if (tmpChar == 'P') {
				counter += 3;
			} else if (tmpChar == 'Q') {
				counter += 6;
			} else if (tmpChar == 'R') {
				counter += 8;
			} else if (tmpChar == 'S') {
				counter += 12;
			} else if (tmpChar == 'T') {
				counter += 14;
			} else if (tmpChar == 'U') {
				counter += 16;
			} else if (tmpChar == 'V') {
				counter += 10;
			} else if (tmpChar == 'W') {
				counter += 22;
			} else if (tmpChar == 'X') {
				counter += 25;
			} else if (tmpChar == 'Y') {
				counter += 24;
			} else if (tmpChar == 'Z') {
				counter += 23;
			}
		}

		// I primi 7 caratteri pari
		offset = 1;
		for (int j = 0; j < 7; j++, offset += 2) {
			char tmpChar = codFisc.charAt(offset);
			if (Character.isDigit(tmpChar)) {
				counter += tmpChar - '0';
			} else if (tmpChar == 'A') {
				counter += 0;
			} else if (tmpChar == 'B') {
				counter += 1;
			} else if (tmpChar == 'C') {
				counter += 2;
			} else if (tmpChar == 'D') {
				counter += 3;
			} else if (tmpChar == 'E') {
				counter += 4;
			} else if (tmpChar == 'F') {
				counter += 5;
			} else if (tmpChar == 'G') {
				counter += 6;
			} else if (tmpChar == 'H') {
				counter += 7;
			} else if (tmpChar == 'I') {
				counter += 8;
			} else if (tmpChar == 'J') {
				counter += 9;
			} else if (tmpChar == 'K') {
				counter += 10;
			} else if (tmpChar == 'L') {
				counter += 11;
			} else if (tmpChar == 'M') {
				counter += 12;
			} else if (tmpChar == 'N') {
				counter += 13;
			} else if (tmpChar == 'O') {
				counter += 14;
			} else if (tmpChar == 'P') {
				counter += 15;
			} else if (tmpChar == 'Q') {
				counter += 16;
			} else if (tmpChar == 'R') {
				counter += 17;
			} else if (tmpChar == 'S') {
				counter += 18;
			} else if (tmpChar == 'T') {
				counter += 19;
			} else if (tmpChar == 'U') {
				counter += 20;
			} else if (tmpChar == 'V') {
				counter += 21;
			} else if (tmpChar == 'W') {
				counter += 22;
			} else if (tmpChar == 'X') {
				counter += 23;
			} else if (tmpChar == 'Y') {
				counter += 24;
			} else if (tmpChar == 'Z') {
				counter += 25;
			}
		}
		// il codiceCrt e' un numero tra 0 e 25
		int codiceCrt = counter % 26;

		// il codice risultante e' il (codiceCrt+1)-esimo carattere dell'alfabeto
		return (char) ('A' + codiceCrt);
	}

	/**
	 * Calcola i caratteri derivanti dal nome: - tutte le vocali presenti nel nome
	 * vengono memorizzate in una variabile - tutte le consonanti presenti nel nome
	 * vengono memorizzate in una variabile La stringa di ritorno sara' costituita
	 * da:
	 * <ul>
	 * <li>TRE CONSONANTI (la prima,la terza e la quarta): nel caso in cui le
	 * consonanti sono piu' di 3
	 * <li>TRE CONSONANTI (la prima,la seconda,la terza) nel caso in cui le
	 * consonanti sono 3
	 * <li>DUE CONSONANTI E UNA VOCALE : nel caso in cui le consonanti sono 2 ed e'
	 * stata trovata almeno 1 vocale
	 * <li>DUE CONSONANTI E X : nel caso in cui le consonanti sono 2 e non sono
	 * state trovate vocali
	 * <li>UNA CONSUNANTE E DUE VOCALI : nel caso in cui c'e' 1 sola consonante e le
	 * vocali sono almeno 2
	 * <li>UNA CONSONANTE,UNA VOCALE E X: nel caso un cui c'e' 1 sola consonante e 1
	 * sola vocale
	 * <li>DUE VOCALI E X nel caso in cui non ci sono consonanti
	 * </ul>
	 *
	 * @param nome Nome da cui devono derivare i secondi 3 caratteri del CF
	 * @return stringa con i secondi 3 caratteri del codice fiscale
	 */

	private static String calcolaNome(String nome) {
		String tmpText = new String();
		String tmpConsonanti = new String();
		String tmpVocali = new String();
		String nomeU = nome.toUpperCase();
		int lungh = nomeU.length();
		char currChar;
		for (int i = 0; i < lungh; i++) {
			currChar = nomeU.charAt(i);
			if (currChar == 'A' || currChar == '�' || currChar == '�' || currChar == 'E' || currChar == '�'
					|| currChar == '�' || currChar == 'I' || currChar == '�' || currChar == '�' || currChar == 'O'
					|| currChar == '�' || currChar == '�' || currChar == 'U' || currChar == '�' || currChar == '�') {
				tmpVocali = tmpVocali + (currChar);

			} else if ((currChar != ' ') && currChar != '\'') {
				tmpConsonanti = tmpConsonanti + (currChar);

			}
		}
		if (tmpConsonanti.length() > 3) {
			tmpText = tmpText + tmpConsonanti.charAt(0) + tmpConsonanti.charAt(2) + tmpConsonanti.charAt(3);
		} else if (tmpConsonanti.length() == 3) {
			tmpText = tmpText + tmpConsonanti.charAt(0) + tmpConsonanti.charAt(1) + tmpConsonanti.charAt(2);
		} else if (tmpConsonanti.length() == 2) {
			tmpText = tmpText + tmpConsonanti.charAt(0) + tmpConsonanti.charAt(1);
			// nel caso di nome con due sole consonanti non calcolo esattamente il codice
			// fiscale
			if (tmpVocali.length() > 1 || tmpVocali.length() == 1) {
				tmpText = tmpText + tmpVocali.charAt(0);
			} else {
				tmpText = tmpText + 'X';
			}
		} else if (tmpConsonanti.length() == 1) {
			tmpText = tmpText + tmpConsonanti.charAt(0);
			if (tmpVocali.length() > 1) {
				tmpText = tmpText + tmpVocali.charAt(0) + tmpVocali.charAt(1);
			} else {
				tmpText = tmpText + tmpVocali.charAt(0);
				tmpText = tmpText + 'X';
			}
		} else {
			tmpText = tmpText + tmpVocali.charAt(0) + tmpVocali.charAt(1);
			tmpText = tmpText + 'X';
		}
		return tmpText;
	}

	/**
	 * Calcola i caratteri derivanti dalla data di nascita e sesso: La stringa di
	 * ritorno sara' costituita da:
	 * <ul>
	 * <li>ANNO : dato dall'ultimo e dal penultimo carattere della data di nascita
	 * <li>MESE : si memorizza in una variabile il quarto e il quinto carattere
	 * della data di nascita, a seconda del numero trovato si avra' una lettera
	 * dell'alfabeto corrispondente
	 * <li>GIORNO: nel caso in cui il sesso e' 'M' e' dato dal primo e secondo
	 * carattere della data di nascita nel caso in cui il sesso e' 'F' e' dato dal
	 * numero prima ricavato + 40
	 * </ul>
	 *
	 * @param dataNascita Data da cui devono derivare i caratteri 7 - 12 del CF
	 * @return stringa con i caratteri 7 - 12 del codice fiscale
	 */

	private static final char[] mese = new char[] { 'A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T' };

	private static String calcolaDataNasc(String dataNascita, String sesso) {
		String tmpMese = new String();
		String tmpGiorno = new String();
		String tmpTextGG = new String();
		String tmpText = new String();
		int length = dataNascita.length();

		// calcolo della porzione di codice che descrive la data di nascita a partire
		// da una data nel formato dd-mm-aaaa:
		// Anno: gli ultimi due caratteri
		tmpText = tmpText + dataNascita.charAt(length - 2) + dataNascita.charAt(length - 1);

		// Mese: il terzo e il quarto carattere
		tmpMese = tmpMese + dataNascita.charAt(3) + dataNascita.charAt(4);
		tmpText += mese[((Integer.parseInt(tmpMese)) - 1)];

		// Giorno: i primi due caratteri (il numero deve essere aumentato di 40
		// se il sesso e' femminile
		tmpGiorno = tmpGiorno + dataNascita.charAt(0) + dataNascita.charAt(1);
		if (sesso.charAt(0) == 'F') {
			tmpTextGG += ((Integer.parseInt(tmpGiorno)) + 40);
		} else {
			tmpTextGG += tmpGiorno;
		}

		// riempio se necessario con uno zero a sinistra
		if (tmpTextGG.length() == 1) {
			tmpText += '0';
		}

		tmpText += tmpTextGG;
		return tmpText;
	}

	/**
	 * Calcola i caratteri derivanti dal cognome: - tutte le vocali presenti nel
	 * cognome vengono memorizzate in una variabile - tutte le consonanti presenti
	 * nel cognome vengono memorizzate in una variabile La stringa di ritorno sara'
	 * costituita da:
	 * <ul>
	 * <li>TRE CONSONANTI (le prime tre): nel caso in cui le consonanti sono almeno
	 * 3
	 * <li>DUE CONSONANTI E UNA VOCALE : nel caso in cui le consonanti sono 2 ed e'
	 * stata trovata almeno 1 vocale
	 * <li>DUE CONSONANTI E X : nel caso in cui le consonanti sono 2 e non sono
	 * state trovate vocali
	 * <li>UNA CONSUNANTE E DUE VOCALI : nel caso in cui c'e' 1 sola consonante e le
	 * vocali sono almeno 2
	 * <li>UNA CONSONANTE,UNA VOCALE E X: nel caso un cui c'e' 1 sola consonante e 1
	 * sola vocale
	 * <li>DUE VOCALI E X nel caso in cui non ci sono consonanti
	 * </ul>
	 *
	 * @param cognome Cognome da cui devono derivare i primi 3 caratteri del CF
	 * @return stringa con i primi 3 caratteri del codice fiscale
	 */

	private static String calcolaCognome(String cognome) {
		String tmpText = new String();
		String tmpConsonanti = new String();
		String tmpVocali = new String();

		String cognomeU = cognome.toUpperCase();
		char currChar;
		int lungh = cognomeU.length();
		for (int i = 0; i < lungh; i++) {
			currChar = cognomeU.charAt(i);
			if (currChar == 'A' || currChar == '�' || currChar == '�' || currChar == 'E' || currChar == '�'
					|| currChar == '�' || currChar == 'I' || currChar == '�' || currChar == '�' || currChar == 'O'
					|| currChar == '�' || currChar == '�' || currChar == 'U' || currChar == '�' || currChar == '�') {
				tmpVocali = tmpVocali + (currChar);

			} else if ((currChar != ' ') && currChar != '\'') {
				// else if((currChar != ' ' ) )
				tmpConsonanti = tmpConsonanti + (currChar);

			}

		}
		if (tmpConsonanti.length() > 2) {
			tmpText = tmpText + tmpConsonanti.charAt(0) + tmpConsonanti.charAt(1) + tmpConsonanti.charAt(2);
		} else if (tmpConsonanti.length() == 2) {
			tmpText = tmpText + tmpConsonanti.charAt(0) + tmpConsonanti.charAt(1);

			// nel caso di cognome con sole due consonanti non calcolo esattamente il codice
			// fiscale
			if (tmpVocali.length() > 1 || tmpVocali.length() == 1) {
				tmpText = tmpText + tmpVocali.charAt(0);
			} else {
				tmpText = tmpText + 'X';
			}
		} else if (tmpConsonanti.length() == 1) {
			tmpText = tmpText + tmpConsonanti.charAt(0);
			if (tmpVocali.length() > 1) {
				tmpText = tmpText + tmpVocali.charAt(0) + tmpVocali.charAt(1);
			} else {
				tmpText = tmpText + tmpVocali.charAt(0);
				tmpText = tmpText + 'X';
			}
		} else {
			tmpText = tmpText + tmpVocali.charAt(0) + tmpVocali.charAt(1);
			tmpText = tmpText + 'X';
		}

		return tmpText;
	}

	/**
	 * Funzione che dato in input l'anno ritorna il giorno di Pasqua.
	 *
	 * @param N Anno
	 * @return
	 */
	public static Date calcolaPasqua(int N) {
		Date pasqua = null;
		String gg;
		String mm;
		String aaaa = N + "";
		int aaaaLunghezza = aaaa.length();
		for (int i = aaaaLunghezza; i < 4; i++) {
			aaaa = "0" + aaaa;
		}

		long mese;
		long giorno;
		int G = N % 19;
		long C = N / 100;
		long H = (C - C / 4 - (8 * C + 13) / 25 + 19 * G + 15) % 30;
		long I = H - (H / 28) * (1 - (H / 28) * (29 / (H + 1)) * ((21 - G) / 11));
		long J = (N + N / 4 + I + 2 - C + C / 4) % 7;
		long L = I - J;

		long mL = 3 + (L + 40) / 44;
		long mI = Math.round(3 + (L + 40) / 44);
		if (mL < mI) {
			mese = mI - 1;
		} else {
			mese = mI;
		}
		mm = mese + "";
		int mmLunghezza = mm.length();
		for (int i = mmLunghezza; i < 2; i++) {
			mm = "0" + mm;
		}
		long gL = L + 28 - 31 * (mese / 4);
		long gI = Math.round(L + 28 - 31 * (mese / 4));
		if (gL < gI) {
			giorno = gI - 1;
		} else {
			giorno = gI;
		}
		gg = giorno + "";
		int ggLunghezza = gg.length();
		for (int i = ggLunghezza; i < 2; i++) {
			gg = "0" + gg;
		}

		if (giorno == 0 || mese == 0) {
			return null;
		}
		java.text.SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		java.text.ParsePosition pos = new ParsePosition(0);
		pasqua = formatter.parse(gg + "/" + mm + "/" + aaaa, pos);
		return pasqua;
	}

	/**
	 * Funzione che dato in input l'anno ritorna la data del giorno di pasquetta
	 *
	 * @param N Anno
	 * @return
	 */
	public static Date calcolaPasquetta(int N) {
		Date pasqua = calcolaPasqua(N);
		if (pasqua != null) {
			return dateAddDays(calcolaPasqua(N), 1);
		} else {
			return null;
		}
	}

	/**
	 * Funzione che converte una stringa rappresentante un orario dal formato HH:MM
	 * in minuti dalla mezzanotte
	 *
	 * @param ora Stringa in formato HH:MM
	 * @return minuti dalla mezzanotte. Restituisce null se l'input e' null o se le
	 *         ore sono piu' di 23 ossia non valide come orario giornaliero
	 */
	public static Long convertiOraInMinuti(String ora) {
		return convertOreFormatoStringaInMinuti(ora, 23);
	}

	/**
	 * Converte un numero di ore e minuti nel formato HH:MM in minuti complessivi
	 *
	 * @param numOre rappresenta un ammontare di tempo fino a 9999 ore e 59 minuti
	 * @return i minuti che rappresentano lo stesso ammontare di tempo
	 */
	public static Long convertiNumOreFormatoStringaInMinuti(String numOre) {
		return convertOreFormatoStringaInMinuti(numOre, 99999);
	}

	
	private static Long convertOreFormatoStringaInMinuti(String ora, int maxOreConsentite) {
		if (ora == null) {
			return null;
		}
		try {
			StringTokenizer st = new StringTokenizer(ora, ":");

			String[] arr = new String[2];
			arr[0] = st.nextToken();
			arr[1] = st.nextToken();

			int hh = Integer.parseInt(arr[0]);
			int mm = Integer.parseInt(arr[1]);
			if ((mm < 0) || (mm > 59) || (hh < 0) || (hh > maxOreConsentite)) {
				return null;
			}
			int minutes = hh * 60;
			minutes += mm;
			return new Long(minutes);

		}
		// In caso di anomalia ritorno null
		catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Restituisce true se l'ora e' minore o uguale a max e maggiore o uguale a min
	 *
	 * @param ora
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean oraCompresaTra(String ora, String min, String max) {
		Long oraInt = Format.convertiOraInMinuti(ora);
		Long minInt = new Long(min);
		Long maxInt = new Long(max);
		if (minInt.intValue() <= oraInt.intValue() && maxInt.intValue() >= oraInt.intValue()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Calcola la differenza tra due date in anni
	 */
	public static int dateDiffInAnni(Date dInizio, Date dFine) {

		Calendar dataIniziale = new GregorianCalendar();
		dataIniziale.setTime(dInizio);

		Calendar dataFinale = new GregorianCalendar();
		dataFinale.setTime(dFine);

		int annoIniziale = dataIniziale.get(Calendar.YEAR);
		int annoFinale = dataFinale.get(Calendar.YEAR);

		return annoIniziale - annoFinale;
	}

	private static int elapsed(GregorianCalendar g1, GregorianCalendar g2, int type) {
		GregorianCalendar gc1, gc2;
		int elapsed = 0;
		// Create copies since we will be clearing/adding
		if (g2.after(g1)) {
			gc2 = (GregorianCalendar) g2.clone();
			gc1 = (GregorianCalendar) g1.clone();
		} else {
			gc2 = (GregorianCalendar) g1.clone();
			gc1 = (GregorianCalendar) g2.clone();
		}
		if (type == Calendar.MONTH || type == Calendar.YEAR) {
			gc1.clear(Calendar.DATE);
			gc2.clear(Calendar.DATE);
		}
		if (type == Calendar.YEAR) {
			gc1.clear(Calendar.MONTH);
			gc2.clear(Calendar.MONTH);
		}
		// normalizzo i giorni del mese
		gc1.set(Calendar.DAY_OF_MONTH, 1);
		gc2.set(Calendar.DAY_OF_MONTH, 1);

		while (gc1.before(gc2)) {
			gc1.add(type, 1);
			elapsed++;
		}

		return elapsed;
	}

	public static int getElapsedMonths(String d1Str, String d2Str) {
		java.util.Date d1 = convertiStringaInData(d1Str);
		java.util.Date d2 = convertiStringaInData(d2Str);

		GregorianCalendar data1 = new GregorianCalendar();
		data1.setTime(d1);
		GregorianCalendar data2 = new GregorianCalendar();
		data2.setTime(d2);

		return elapsed(data1, data2, Calendar.MONTH);
	}

	public static String calendarToString(Calendar c) {
		return c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DATE);
	}

	/**
	 * Calcola la differenza tra la data di fine e la data inizio restituendo valori
	 * negativi nel caso in cui la data fine sia minore della data inizio dataFine
	 * dataInizio
	 *
	 * restituisce (dataFine - dataInizio)
	 *
	 */
	static public int dateDiffDaysConSegno(Date dataFine, Date dataInizio) {
		int result;
		result = (int) ((dataFine.getTime() / 86400000) - (dataInizio.getTime() / 86400000));
		return result;
	}

	/**
	 * Restituisce un intero contenente l'eta' calcolata ad una certa data di
	 * riferimento (per l'eta' al momento corrente si passa la sysdate) Non lancia
	 * eccezioni: se il calcolo fallisce per qualche motivo l'eta' restituita e' un
	 * insensato -1
	 *
	 * @param dataNascita
	 * @param dataRiferimento
	 * @return
	 */
	public static int calcolaEtaAData(Date dataNascita, Date dataRiferimento) {
		int etaCalcolata = 0;
		try {
			String sNascita = convertiDataInStringa(dataNascita);
			String sRif = convertiDataInStringa(dataRiferimento);

			int annoN = Integer.parseInt(sNascita.substring(6, 10));
			int annoR = Integer.parseInt(sRif.substring(6, 10));
			int meseN = Integer.parseInt(sNascita.substring(3, 5));
			int meseR = Integer.parseInt(sRif.substring(3, 5));
			int giornoN = Integer.parseInt(sNascita.substring(0, 2));
			int giornoR = Integer.parseInt(sRif.substring(0, 2));
			if (annoR >= annoN) {
				if (meseR > meseN) {
					etaCalcolata = annoR - annoN;
				} else if (meseR == meseN) {
					if (giornoR >= giornoN) {
						etaCalcolata = annoR - annoN;
					} else {
						etaCalcolata = (annoR - annoN) - 1;
					}
				} else if (meseR < meseN) {
					etaCalcolata = (annoR - annoN) - 1;
				}
			}
		} catch (Exception e) {
			etaCalcolata = -1;
		}
		return etaCalcolata;
	}

	public static Date getPrimoGennaioAnnoCorrente() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		return cal.getTime();
	}

	public static Vector convertiMapPerTransitatore(Map mappa) {
		Vector v = new Vector();
		if (mappa == null) {
			return null;
		} else {
			Iterator iter = mappa.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				Object value = mappa.get(key);
				v.add(key);
				v.add(value);
			}
			return v;
		}
	}

	public static HashMap convertiTransitatoreInMap(Vector v) {
		HashMap m = new HashMap();
		Iterator iter = v.iterator();
		while (iter.hasNext()) {
			Object chiave = iter.next();
			Object valore = iter.next();
			m.put(chiave, valore);
		}
		return m;
	}

	/**
	 * Restituisce una data corrispondente al giorno N del mese successivo alla data
	 * passata in input
	 */
	public static Date dammiIlGiornoXDelMeseSuccessivoAllaDataDiInput(int x, Date data) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, x);
		return cal.getTime();
	}

	public static Date dammiIlGiornoXDelMeseDellaDataDiInput(int x, Date data) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, x);
		return cal.getTime();
	}

	public static int dammiLAnnoDellaDataPassataInInput(Date dataDaCuiEstrarreLAnno) {
		Calendar c = new GregorianCalendar();
		c.setTime(dataDaCuiEstrarreLAnno);
		return c.get(Calendar.YEAR);
	}

	/**
	 * restituisce il giorno della settimana di una data 1 lunedi, 2 martedi,3
	 * martedi ... 7 domenica
	 *
	 * @param dataInput
	 * @return
	 */
	public static int dammiIlGiornoDellaSettimanaDellaDataInInput(Date dataInput) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dataInput);
		// mi butta fuori un giorno in piu' sottraggo uno
		int numGiorno = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return numGiorno == 0 ? 7 : numGiorno;
	}

	/**
	 * restituisce true se le due date passate in input sono uguali
	 *
	 * @return
	 */
	public static boolean isData1UgualeAData2(Date data1, Date data2) {
		
		if (data1 == null && data2 == null)
			return true;
		
		if (data1 != null && data2 == null)
			return false;
		
		if (data1 == null && data2 != null)
			return false;
		
		return convertiDataInStringa(data1).equals(convertiDataInStringa(data2));
		
		//return datePrivaDiOreMinutiSecondi(data1).getTime() == datePrivaDiOreMinutiSecondi(data2).getTime();
	}

	/**
	 * restituisce true se la data passata come primo parametro e' maggiore della
	 * data passata come secondo parametro
	 *
	 * @return
	 */
	public static boolean isData1MaggioreData2(Date data1, Date data2) {
		return datePrivaDiOreMinutiSecondi(data1).getTime() > datePrivaDiOreMinutiSecondi(data2).getTime();
	}

	/**
	 * restituisce la data maggiore fra le due in input oppure l'unica valorizzata
	 * se una delle due non lo e'
	 */
	public static Date getDataMaggioreOUnicaValorizzata(Date data1, Date data2) {
		if (data1 != null && data2 == null) {
			return data1;
		}
		if (data1 == null && data2 != null) {
			return data2;
		}

		if (isData1MaggioreData2(data1, data2)) {
			return data1;
		} else {
			return data2;
		}
	}

	/**
	 * restituisce true se la data passata come primo parametro e' minore della data
	 * passata come secondo parametro
	 *
	 * @return
	 */
	public static boolean isData1MinoreData2(Date data1, Date data2) {
		return datePrivaDiOreMinutiSecondi(data1).getTime() < datePrivaDiOreMinutiSecondi(data2).getTime();
	}

	/**
	 * restituisce true se la data passata come primo parametro e' maggiore della
	 * data passata come secondo parametro
	 *
	 * @return
	 */
	public static boolean isData1MaggioreUgualeData2(Date data1, Date data2) {
		return datePrivaDiOreMinutiSecondi(data1).getTime() >= datePrivaDiOreMinutiSecondi(data2).getTime();
	}

	/**
	 * restituisce true se la data passata come primo parametro e' minore della data
	 * passata come secondo parametro
	 *
	 * @return
	 */
	public static boolean isData1MinoreUgualeData2(Date data1, Date data2) {
		return datePrivaDiOreMinutiSecondi(data1).getTime() <= datePrivaDiOreMinutiSecondi(data2).getTime();
	}

	/**
	 * Rimuove ore minuti e secondi dalla data di input
	 *
	 * @param date
	 * @return
	 */
	private static Date datePrivaDiOreMinutiSecondi(Date date) {
		return Format.convertiStringaInData(Format.convertiDataInStringa(date));
	}

	/**
	 * Ritorna true se e' vera una delle seguenti condizioni: - l'oggetto e' nullo -
	 * l'oggetto e' una stringa vuota dopo essere stata trimmata
	 *
	 * @param oIn
	 * @return
	 */
	// public static boolean isStringVoid(String oIn){
	// return (oIn == null || oIn.trim().length() == 0);
	// }
	//
	// public static boolean isStringNotVoid(String oIn){
	// return ! isStringVoid(oIn);
	// }

	public static boolean equalsIgnoreCaseNotNull(String oIn, String valoreDiConfronto) {
		return (oIn != null && oIn.equalsIgnoreCase(valoreDiConfronto));
	}

	/**
	 * Ritorna l'indice della prima occorrenza di una stringa in un'altra ignorando
	 * il case. Se una delle stringhe e' nulla ritorna -1 anche nel caso la stringa
	 * non sia trovata
	 *
	 * @param strContenente
	 * @param strContenuta
	 * @return
	 */
	public static int indexOfIgnoreCase(String strContenente, String strContenuta) {
		if (strContenente == null || strContenuta == null) {
			return -1;
		}

		return (strContenente.toLowerCase().indexOf(strContenuta.toLowerCase()));
	}

	public static boolean isStringaNumerica(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean equalsNullValueAware(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (obj1 != null && obj2 != null && obj1.equals(obj2)) {
			return true;
		}
		return false;
	}

	public static int confrontaDateInFormatoDateOStringa(Object d1, Object d2) {
		Date d1Date = null;
		Date d2Date = null;
		if (d1 instanceof String) {
			d1Date = convertiStringaInData((String) d1);
		} else if (d1 instanceof Date) {
			d1Date = (Date) d1;
		}

		if (d2 instanceof String) {
			d2Date = convertiStringaInData((String) d2);
		} else if (d2 instanceof Date) {
			d1Date = (Date) d2;
		}
		return confrontaDateOreMinutiSecondi(d1Date, d2Date);
	}

	/**
	 * Ritorna true se la data una data e' compresa tra altre due date , estremi
	 * compresi
	 *
	 * @param dataDaConfrontare   data per il confronto
	 * @param dataLimiteInferiore
	 * @param dataLimiteSuperiore
	 * @return
	 */
	public static boolean isDataCompresaTra(Date dataDaConfrontare, Date dataLimiteInferiore,
			Date dataLimiteSuperiore) {

		if (confrontaDateOreMinutiSecondi(dataDaConfrontare, dataLimiteInferiore) <= 0
				&& confrontaDateOreMinutiSecondi(dataDaConfrontare, dataLimiteSuperiore) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Estrae lo stack trace di un errore e lo restituisce sottoforma di stringa
	 *
	 * @param t
	 * @return
	 */
	public static String dump(Throwable t) {
		StringWriter out = new StringWriter();
		t.printStackTrace(new PrintWriter(out));
		return out.toString();
	}

	public static int dateDiffInMesi(Date dInizio, Date dFine) {
		double days = dateDiffDays(dInizio, dFine);
		return (int) Math.round(days / 30.);
	}

	public static boolean isPartitaIVA(String s) {
		return (s.length() == 11) && isStringaNumerica(s);
	}

	public static boolean isCodiceFiscaleOPartitaIVA(String s) {
		return verificaCodiceFiscale(s) || isPartitaIVA(s);
	}

	public static boolean isIndirizzoEmailCorretto(String email) {
		int posChiocciola = -1;
		int posPunto = -1;
		int lunghezzaStringa = -1;
		// posizione della chiocciola e del punto
		lunghezzaStringa = email.length();
		posChiocciola = email.lastIndexOf("@");
		posPunto = email.lastIndexOf(".");

		if (posChiocciola > 0 && // ci sia la chiocchiola e non sia il primo carattere
				posPunto > 0 && // ci sia almeno un punto
				posChiocciola + 1 < posPunto && // il punto sia dopo un carattere dopo la chiocciola
				posPunto < lunghezzaStringa - 1 // il punto non sia l'ultimo carattere
		) {
			return true;
		} else {
			return false;
		}
	}

	public static String dumpString(Object o) {
		if (o == null) {
			return "#NULL#";
		}
		if (!(o instanceof String)) {
			return "#" + o.getClass().getName() + "#";
		}
		String s = (String) o;
		StringBuffer r = new StringBuffer();
		for (int c1 = 0; c1 < s.length(); c1++) {
			if (c1 > 0) {
				r.append(",");
			}
			r.append((int) s.charAt(c1));
		}
		return r.toString();
	}

	public static String decodeHexString(String s) {
		if (s == null) {
			return null;
		}
		if (!s.startsWith("0x")) {
			return s;
		}
		StringBuffer r = new StringBuffer(s.length() / 2);
		for (int c1 = 2; c1 < s.length(); c1 += 2) {
			r.append((char) Integer.parseInt(s.substring(c1, c1 + 2), 16));
		}
		return r.toString();
	}

	public static String replaceFirstString(final String input, final String exp, final Object replacement) {

		final StringBuffer result = new StringBuffer();
		final String placeholder = "{" + exp + "}";

		int startIdx = 0;
		int idxOld = 0;
		if ((idxOld = input.indexOf(placeholder, startIdx)) >= 0) {
			// SE l'exp che stiamo cercando esiste --> idxOld
			// conterra' l'indice dell'ultimo char della prima occorrenza
			// l'unica che ci interessa rimpiazzare
			// Per il resto tutto funziona come la replaceString (vedi
			// sotto)
			result.append(input.substring(startIdx, idxOld));
			String repacementStr = null;

			if (replacement == null) {
				repacementStr = "null";
			} else if (replacement instanceof String) {
				repacementStr = (String) replacement;
			} else if (replacement instanceof Date) {
				repacementStr = dataFormat.format((Date) replacement);
			} else {
				repacementStr = replacement.toString();
			}

			result.append(repacementStr);
			startIdx = idxOld + placeholder.length();
			// } else {
			// throw new UnresolvedPlaceholderException(input, placeholder);
		}
		result.append(input.substring(startIdx));
		return result.toString();
	}

	public static String getMessaggioFormatted(String messaggio, Map variabili) {
		if (messaggio == null) {
			return null;
		}
		if (variabili != null) {

			Iterator i = variabili.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				String key = (String) e.getKey();
				if (key != null && e.getValue() != null) {
					messaggio = Format.replaceFirstString(messaggio, key, e.getValue());
				}
			}
		}
		return messaggio;
	}

}
