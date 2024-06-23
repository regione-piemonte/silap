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
package it.csi.silap.colmirbff.api.impl.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;

import com.ibm.icu.util.Calendar;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.VersamentoPvPeriodo;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoCobLav;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProspetto;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoProvincia;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvCob;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvGgExtraFestivi;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvPeriodo;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvProspetto;
import it.csi.silap.colmirbff.integration.entity.EsoTVersamentoPvRicIn;
import it.csi.silap.colmirbff.integration.entity.SilapDCategoriaAzienda;
import it.csi.silap.colmirbff.util.GiorniLavorativiUtils;
import it.csi.silap.colmirbff.util.MathUtils;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;
import lombok.Data;

@Dependent
public class VersamentoEsoneriCalcoloManager {

	private static final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
	private static final ZoneId defaultZoneId = ZoneId.systemDefault();

	@Data
	private class Day {
		private LocalDate day;
		private String mark;
		private String markCob;
		private String markRicIn;
		private EsoTVersamentoPvPeriodo periodoEsonero;
		private EsoTVersamentoPvPeriodo periodoSospensione;
		private List<EsoTVersamentoPvPeriodo> periodoConvenzioni;
		private List<EsoTVersamentoPvRicIn> ricIn;
		private EsoTVersamentoPvCob cob;

	}

	public List<EsoTVersamentoPvPeriodo> calcoloAutomatico(double importoGiornalieroEsonero, 
			int annoRiferimento, EsoTVersamentoProvincia provincia,
			EsoTVersamentoProspetto prospetto, EsoTVersamentoPvProspetto prospettoProvincia,
			EsoTVersamentoProspetto prospettoPrec, EsoTVersamentoPvProspetto prospettoProvinciaPrec,
			List<EsoTVersamentoPvPeriodo> periodiEsoneri, List<EsoTVersamentoPvPeriodo> periodiSospensioni,
			List<EsoTVersamentoPvPeriodo> periodiConvenzioni,
			List<EsoTVersamentoPvCob> esoTVersamentoPvCobs,
			List<EsoTVersamentoPvRicIn> esoTVersamentoPvRicIns,
			StringBuffer printCalcoloAutomatico) {
		


		
		printCalcoloAutomatico.append("\nINIZIO CALCOLO");
		printPeriodoRiferimento(printCalcoloAutomatico,annoRiferimento);
		printProspetto(printCalcoloAutomatico,prospetto, prospettoProvincia, "PROSPETTO ANNO RIFERIMENTO E PROVINCIALE");
		printProspetto(printCalcoloAutomatico,prospettoPrec, prospettoProvinciaPrec, "PROSPETTO ANNO PRECEDENTE E PROVINCIALE");
		printPeriodi(printCalcoloAutomatico,periodiEsoneri, "ESONERI");
		printPeriodi(printCalcoloAutomatico,periodiSospensioni, "SOSPENSIONI");
		printPeriodi(printCalcoloAutomatico,periodiConvenzioni, "CONVENZIONI");
		printCobs(printCalcoloAutomatico,esoTVersamentoPvCobs);
		printRiconoscimentiInabilita(printCalcoloAutomatico,esoTVersamentoPvRicIns);
		
		
		List<EsoTVersamentoPvPeriodo> periodi = new ArrayList<EsoTVersamentoPvPeriodo>();
		
		
		if (periodiEsoneri == null || periodiEsoneri.size()<=0) {
			Log.info("[VersamentoEsoneriCalcoloManager::calcoloAutomatico] Esonero provincia mancante"); 
			printCalcoloAutomatico.append("\nFINE CALCOLO");
			Log.info(printCalcoloAutomatico.toString());
			return periodi;
		}
		
		
		if (prospettoProvincia == null) {
			Log.info("[VersamentoEsoneriCalcoloManager::calcoloAutomatico] Prospetto provincia mancante"); 
			printCalcoloAutomatico.append("\nFINE CALCOLO");
			Log.info(printCalcoloAutomatico.toString());
			return periodi;
		}
		
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		LocalDate startDate = LocalDate.of(annoRiferimento, Month.JANUARY, 1);
		LocalDate endDate = startDate.plusYears(1);

		EsoTVersamentoPvPeriodo periodo = null;
		Day prevDay = null;
		int baseComputo = prospettoProvincia.getBaseComputoProvinciale() != null ? 
				prospettoProvincia.getBaseComputoProvinciale().intValue() : 0;
		int disabiliInForza = prospettoProvincia.getNumDisabiliInForza() != null ? 
				prospettoProvincia.getNumDisabiliInForza().intValue() : 0;
		
		
		SilapDCategoriaAzienda categoriaAzienda = null;
		if (prospetto.getCategoriaAzienda() == null && prospetto.getBaseComputoNazionale() != null) {
			if (prospetto.getBaseComputoNazionale().intValue()<=35)
				categoriaAzienda = getCategoriaAzienda("C");
			else if (prospetto.getBaseComputoNazionale().intValue()<=50)
				categoriaAzienda = getCategoriaAzienda("B");
			else
				categoriaAzienda = getCategoriaAzienda("A");
				
		}
		else categoriaAzienda = getCategoriaAzienda(prospetto.getCategoriaAzienda());
		
		
		
		long numRiallineamentoNazionale = 0;
		if ("A".equalsIgnoreCase(prospetto.getCategoriaAzienda()))
			numRiallineamentoNazionale = prospettoProvincia.getQuotaRiservaDisabili() - MathUtils.calcolaQuotaRiserva((long)baseComputo);
		else if ("B".equalsIgnoreCase(prospetto.getCategoriaAzienda()))
			numRiallineamentoNazionale = prospettoProvincia.getQuotaRiservaDisabili() - 2l;
		else if ("C".equalsIgnoreCase(prospetto.getCategoriaAzienda()))
			numRiallineamentoNazionale = prospettoProvincia.getQuotaRiservaDisabili() - 1l;
		
		printCalcoloAutomatico.append("\nCategoria Azienda: " + (categoriaAzienda != null ? categoriaAzienda.getCodCategoriaAzienda() : "<nullo>") + 
				"; Num. Riallineamento Nazionale: " + numRiallineamentoNazionale + "\n\n");

		while (startDate.isBefore(endDate)) {

			Day currentDay = new Day();
			currentDay.setDay(startDate);
			
			/*
			if (startDate.getMonth() == Month.OCTOBER && startDate.getDayOfMonth() == 15) {
				System.out.print(true);
			}
			*/

			String mark = "";
			String markCob = null;
			String markRicIn = null;
			EsoTVersamentoPvPeriodo eso = inPeriodo(periodiEsoneri, startDate);
			if (eso != null) {
				currentDay.setPeriodoEsonero(eso);
				mark += " " + getMark("E", eso);
			}
			EsoTVersamentoPvPeriodo sosp = inPeriodo(periodiSospensioni, startDate);
			if (sosp != null) {
				currentDay.setPeriodoSospensione(sosp);
				mark += " " + getMark("S", sosp);
			}
			List<EsoTVersamentoPvPeriodo> convs = inPeriodoConvenzioni(periodiConvenzioni, startDate);
			if (convs != null && convs.size()>0) {
				currentDay.setPeriodoConvenzioni(convs);
				for (EsoTVersamentoPvPeriodo conv : convs)
					mark += " " + getMark("CONV", conv, "" + conv.getQuotaRiserva());
			}
			EsoTVersamentoPvCob cob = inPeriodoCobs(esoTVersamentoPvCobs, startDate);
			if (cob != null) {
				currentDay.setCob(cob);
				if (markCob == null) markCob = "";
				markCob += " " + "(C " + df.format(cob.getDCob()) + " - " + getCodiceRegionaliPerCob(cob) + ")";
			}
			List<EsoTVersamentoPvRicIn> ricIns = inPeriodoRicIn(esoTVersamentoPvRicIns, startDate);
			if (ricIns != null && ricIns.size()>0) {
				currentDay.setRicIn(ricIns);
				if (markRicIn == null) markRicIn = "";
				for (EsoTVersamentoPvRicIn ricIn : ricIns) {
					
					String dRiconoscimentoInvalidita = "<nulla>";
					if (ricIn.getDRiconoscimentoInvalidita() != null) 
						dRiconoscimentoInvalidita = df.format(ricIn.getDRiconoscimentoInvalidita());
					String dataScadenza = "<nulla>";
					if (ricIn.getDScadenza() != null)
						dataScadenza = df.format(ricIn.getDScadenza());
					
					markRicIn += " " + "(RICIN " + dRiconoscimentoInvalidita + " - " + dataScadenza + " - ore:" + ricIn.getNumOreSettMed() + ")";
				}
			}
			currentDay.setMark(mark);
			currentDay.setMarkCob(markCob);
			currentDay.setMarkRicIn(markRicIn);

			printCalcoloAutomatico.append(format.format(startDate) + " - " + 
					currentDay.getMark() + 
					(currentDay.getMarkCob() != null ? " - " + currentDay.getMarkCob() : "") +
					(currentDay.getMarkRicIn() != null ? " - " + currentDay.getMarkRicIn() : "") +
					"\n" );
			

			if (prevDay == null) {
				periodo = new EsoTVersamentoPvPeriodo();
				periodo.setSilapDCategoriaAzienda(categoriaAzienda);
				periodo.setDInizio(Date.from(startDate.atStartOfDay(defaultZoneId).toInstant()));
				
				
				if (currentDay.getCob() != null) {
					baseComputo = baseComputo + currentDay.getCob().getNumLavoratori().intValue();
					disabiliInForza = disabiliInForza + currentDay.getCob().getNumLavoratoriDisabili().intValue();
				}
				
				if (currentDay.getRicIn() != null) {
					int lavs = addLavoratoriRicIn(currentDay.getRicIn());
					baseComputo = baseComputo - lavs;
					disabiliInForza = disabiliInForza + lavs;
				}
				
				periodo.setBaseComputo(Long.valueOf(baseComputo));
				periodo.setNumDisabiliInForza(Long.valueOf(disabiliInForza));
				prevDay = currentDay;
			} else {
				if (!prevDay.getMark().equalsIgnoreCase(currentDay.getMark())) {
					periodo.setDFine(Date.from(startDate.minusDays(1).atStartOfDay(defaultZoneId).toInstant()));
					periodo = impostaPeriodo(importoGiornalieroEsonero, 
							provincia, periodo, prevDay, prospetto, prospettoProvincia, 
							prospettoPrec, prospettoProvinciaPrec,numRiallineamentoNazionale);
					
					if (prevDay.getPeriodoEsonero() != null)
						periodi.add(periodo);

					periodo = new EsoTVersamentoPvPeriodo();
					periodo.setSilapDCategoriaAzienda(categoriaAzienda);
					periodo.setDInizio(Date.from(startDate.atStartOfDay(defaultZoneId).toInstant()));
					
					if (currentDay.getCob() != null) {
						baseComputo = baseComputo + currentDay.getCob().getNumLavoratori().intValue();
						disabiliInForza = disabiliInForza + currentDay.getCob().getNumLavoratoriDisabili().intValue();
					}
					
					if (currentDay.getRicIn() != null) {
						int lavs = addLavoratoriRicIn(currentDay.getRicIn());
						baseComputo = baseComputo - lavs;
						disabiliInForza = disabiliInForza + lavs;
					}
					
					periodo.setBaseComputo(Long.valueOf(baseComputo));
					periodo.setNumDisabiliInForza(Long.valueOf(disabiliInForza));
					prevDay = currentDay;
				} else if (currentDay.getMarkCob() != null || currentDay.getMarkRicIn() != null) {

					periodo.setDFine(Date.from(startDate.minusDays(1).atStartOfDay(defaultZoneId).toInstant()));
					periodo = impostaPeriodo(importoGiornalieroEsonero, 
							provincia, periodo, prevDay, prospetto, prospettoProvincia, 
							prospettoPrec, prospettoProvinciaPrec, numRiallineamentoNazionale);
					
					if (prevDay.getPeriodoEsonero() != null)
						periodi.add(periodo);

					periodo = new EsoTVersamentoPvPeriodo();
					periodo.setSilapDCategoriaAzienda(categoriaAzienda);
					periodo.setDInizio(Date.from(startDate.atStartOfDay(defaultZoneId).toInstant()));
					
					if (currentDay.getCob() != null) {
						baseComputo = baseComputo + currentDay.getCob().getNumLavoratori().intValue();
						disabiliInForza = disabiliInForza + currentDay.getCob().getNumLavoratoriDisabili().intValue();
					}
					
					if (currentDay.getRicIn() != null) {
						int lavs = addLavoratoriRicIn(currentDay.getRicIn());
						baseComputo = baseComputo - lavs;
						disabiliInForza = disabiliInForza + lavs;
					}
					
					periodo.setBaseComputo(Long.valueOf(baseComputo));
					periodo.setNumDisabiliInForza(Long.valueOf(disabiliInForza));
					prevDay = currentDay;
				}

			}

			startDate = startDate.plusDays(1);
		}

		if (prevDay != null) {
			periodo.setDFine(Date.from(endDate.minusDays(1).atStartOfDay(defaultZoneId).toInstant()));
			periodo = impostaPeriodo(importoGiornalieroEsonero, provincia, periodo, 
					prevDay, prospetto, prospettoProvincia, prospettoPrec, 
					prospettoProvinciaPrec, numRiallineamentoNazionale);
			if (prevDay.getPeriodoEsonero() != null)
				periodi.add(periodo);
		}

		printCalcoloAutomatico.append("\nFINE CALCOLO");
		printPeriodi(printCalcoloAutomatico, periodi);
		// TODO PRINT IN PRODUZIONE
		Log.info(printCalcoloAutomatico.toString());
		
		return periodi;
	}

	private EsoTVersamentoPvPeriodo impostaPeriodo(double importoGiornalieroEsonero,
			EsoTVersamentoProvincia provincia, EsoTVersamentoPvPeriodo periodo, Day day,
			EsoTVersamentoProspetto prospetto, EsoTVersamentoPvProspetto prospettoProvincia,
			EsoTVersamentoProspetto prospettoPrec, EsoTVersamentoPvProspetto prospettoProvinciaPrec,
			long numRiallineamentoNazionale) {
		// giorni lavorativi
		List<Date> giorniExtraFestivi = null;
		List<EsoTVersamentoPvGgExtraFestivi> esoTVersamentoPvGgExtraFestivis = provincia
				.getEsoTVersamentoPvGgExtraFestivis();
		if (esoTVersamentoPvGgExtraFestivis != null && esoTVersamentoPvGgExtraFestivis.size() > 0) {
			for (EsoTVersamentoPvGgExtraFestivi esoTVersamentoPvGgExtraFestivi : esoTVersamentoPvGgExtraFestivis) {
				if (giorniExtraFestivi == null)
					giorniExtraFestivi = new ArrayList<Date>();
				giorniExtraFestivi.add(esoTVersamentoPvGgExtraFestivi.getDGgFestivo());
			}
		}
		int giorniLavorativi = GiorniLavorativiUtils.getGiorniLavorativi(periodo.getDInizio(), periodo.getDFine(),
				provincia.getNumGgLavorativiSettimanali(), provincia.getDFestaPatronale(), giorniExtraFestivi);
		periodo.setNumGgLavorativi(Long.valueOf(giorniLavorativi));

		int quotaRiserva = 0;
		if (prospetto != null && prospetto.getCategoriaAzienda() != null) {
			if ("A".equalsIgnoreCase(prospetto.getCategoriaAzienda()))
				quotaRiserva = MathUtils.calcolaQuotaRiserva(periodo.getBaseComputo());
			else if ("B".equalsIgnoreCase(prospetto.getCategoriaAzienda()))
				quotaRiserva = 2;
			else if ("C".equalsIgnoreCase(prospetto.getCategoriaAzienda()))
				quotaRiserva = 1;
		}
		
		
		// sospensioni
		if (day.getPeriodoSospensione() != null) {
			if (day.getPeriodoSospensione().getEsoDVersamentoMotivoSospensione() != null) {
				
				if (SilapConstants.MOTIVO_SOSPENSIONE_CIGS.intValue() == day.getPeriodoSospensione()
						.getEsoDVersamentoMotivoSospensione().getId().intValue()) {
					if (day.getPeriodoSospensione().getQuotaRiserva() != null) {
						double qs = quotaRiserva * ( 1d - day.getPeriodoSospensione().getQuotaRiserva().doubleValue()/100d);
						quotaRiserva = MathUtils.round(qs);					
					}
					else if (day.getPeriodoSospensione().getNumLavoratoriSospesi() != null) {
						int totDip = prospettoProvincia.getBaseComputoProvinciale().intValue();
						/*
						if (prospettoProvinciaPrec != null)
							totDip = prospettoProvinciaPrec.getBaseComputoProvinciale().intValue();
						*/
						double sq = ((totDip-day.getPeriodoSospensione().getNumLavoratoriSospesi())/(double)totDip);
						quotaRiserva = MathUtils.round(((double)quotaRiserva) *  sq);
					}
				}
				else if (SilapConstants.MOTIVO_SOSPENSIONE_LICENZIAMENTO_COLLETTIVO.intValue() == day.getPeriodoSospensione()
						.getEsoDVersamentoMotivoSospensione().getId().intValue()) {
					quotaRiserva = 0;
				}
			}
		}
		

		quotaRiserva = quotaRiserva + (int)numRiallineamentoNazionale;
		if (quotaRiserva < 0)
			quotaRiserva = 0;
		
		// convenzioni
		int numeroPosizioniAperte = 0;
		if (day.getPeriodoConvenzioni()!= null) {
			for (EsoTVersamentoPvPeriodo conv : day.getPeriodoConvenzioni())
				numeroPosizioniAperte += conv.getQuotaRiserva().intValue();
		}
		
		
		// esoneri
		int numCompensati = 0;
		int numEsoneriDaPagare = 0;
		int numLavoratoriEsonerati = 0;
		int numAutocertificati = 0;
		if (day.getPeriodoEsonero() != null) {
			numCompensati = prospettoProvincia.getNumSoggettiCompensatiDisabili() != null ? 
					prospettoProvincia.getNumSoggettiCompensatiDisabili().intValue() : 0;
			if (numCompensati >0 && "R".equalsIgnoreCase(prospettoProvincia.getCatCompensazioneDisabili()))
					numCompensati = numCompensati*-1;
			numAutocertificati = prospettoProvincia.getNumEsoneratiAutocertificati() != null ?
					prospettoProvincia.getNumEsoneratiAutocertificati().intValue() : 0;
			
			/*
			se categoria nazionale azienda = C (15-35 dip) ==> per QR=1 se azzerare il numero di esoneri da pagare ==> sarà l'unico caso che azzera in automatico le unità di esonero.
			Per le altre due casistiche di categoria nazionale azienda si applica regolarmente la formula generale:
			 dove la quota di esonero_max è 
			- se categoria nazionale azienda = B (36-50 dip) ==>  per QR  = 2  quindi  quota_esonero_max = (1 – n. esoneri_autocertificati)
			- se categoria nazionale azienda = A (>50 dip)     ==> per QR qualsiasi quindi quota_esonero_max = arrotonda ((quota riserva * esonero %) / 100) - n.esoneri_autocertificati,
			   e la percentuale d’esonero è quella della richiesta nel periodo tra la data richiesta e la data concessione -1 o tra la data richiesta e la data di diniego -1,
			   della concessione nel periodo tra la data di concessione alla data scadenza.
			 */
			int esoneroMax = 0;
			if (quotaRiserva<=1 && "C".equalsIgnoreCase(prospetto.getCategoriaAzienda())) 
				esoneroMax = 0;
			else if (quotaRiserva == 2 && "B".equalsIgnoreCase(prospetto.getCategoriaAzienda())) 
				esoneroMax = 1 - numAutocertificati;
			else if ("A".equalsIgnoreCase(prospetto.getCategoriaAzienda()))
				esoneroMax =  MathUtils.round((double)(quotaRiserva * day.getPeriodoEsonero().getQuotaRiserva())/100d - (double)numAutocertificati);
			
			if (esoneroMax<0)
				esoneroMax = 0;
			
			numLavoratoriEsonerati = (quotaRiserva - periodo.getNumDisabiliInForza().intValue() -
					numAutocertificati - numeroPosizioniAperte  + numCompensati);
			if (numLavoratoriEsonerati <0)
				numLavoratoriEsonerati = 0;

			numEsoneriDaPagare = Math.min(numLavoratoriEsonerati, esoneroMax);
		}
		
		periodo.setNumEsoneratiAutocertificati(Long.valueOf(numAutocertificati));
		periodo.setQuotaRiserva(Long.valueOf(quotaRiserva));
		periodo.setNumSoggettiCompensati(Long.valueOf(numCompensati));
		periodo.setNumLavoratoriEsonerati(Long.valueOf(numEsoneriDaPagare));
		periodo.setImporto(new BigDecimal(numEsoneriDaPagare*importoGiornalieroEsonero*periodo.getNumGgLavorativi()));
		periodo.setFlgTipo("A");
		periodo.setNumRiallineamentoNazionale(numRiallineamentoNazionale);
		
		
		String userInserim = (SilapThreadLocalContainer.UTENTE_CONNESSO != null && SilapThreadLocalContainer.UTENTE_CONNESSO.get() != null) ? SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc() : "";
		String userAggiorn = userInserim;
		Date now = new Date();

		periodo.setCodUserAggiorn(userAggiorn);
		periodo.setCodUserInserim(userInserim);
		periodo.setDInserim(now);
		periodo.setDAggiorn(now);
		periodo.setNTimestamp(0L);
		
		return periodo;
		
	}

	public EsoTVersamentoPvPeriodo inPeriodo(List<EsoTVersamentoPvPeriodo> periodi, LocalDate date) {
		boolean inPeriodo = false;
		for (EsoTVersamentoPvPeriodo periodo : periodi) {
			if (!"C".equals(periodo.getFlgTipo())) {
				LocalDate dataInizio = LocalDate.ofInstant(periodo.getDInizio().toInstant(), defaultZoneId);
				dataInizio = dataInizio.minusDays(1);
				LocalDate dataFine = LocalDate.ofInstant(periodo.getDFine().toInstant(), defaultZoneId);
				dataFine = dataFine.plusDays(1);
				inPeriodo = date.isAfter(dataInizio) && date.isBefore(dataFine);
				if (inPeriodo)
					return periodo;
			}
		}
		return null;
	}
	
	
	public List<EsoTVersamentoPvPeriodo> inPeriodoConvenzioni(List<EsoTVersamentoPvPeriodo> periodi, LocalDate date) {
		
		List<EsoTVersamentoPvPeriodo> ret = new ArrayList<EsoTVersamentoPvPeriodo>();
		
		boolean inPeriodo = false;
		for (EsoTVersamentoPvPeriodo periodo : periodi) {
			if (!"C".equals(periodo.getFlgTipo())) {
				LocalDate dataInizio = LocalDate.ofInstant(periodo.getDInizio().toInstant(), defaultZoneId);
				dataInizio = dataInizio.minusDays(1);
				LocalDate dataFine = LocalDate.ofInstant(periodo.getDFine().toInstant(), defaultZoneId);
				dataFine = dataFine.plusDays(1);
				inPeriodo = date.isAfter(dataInizio) && date.isBefore(dataFine);
				if (inPeriodo)
					ret.add(periodo);
			}
		}
		return ret;
	}
	
	
	public boolean inPeriodo(List<VersamentoPvPeriodo> periodi, VersamentoPvPeriodo periodo) {
		LocalDate startDate = LocalDate.ofInstant(periodo.getDInizio().toInstant(), defaultZoneId);
		LocalDate endDate = LocalDate.ofInstant(periodo.getDFine().toInstant(), defaultZoneId);
		
		boolean ret = false;
		for (VersamentoPvPeriodo per : periodi) {
			if (per.getIdEsoTVersamentoPvPeriodo().longValue() != periodo.getIdEsoTVersamentoPvPeriodo().longValue() && 
					!"C".equals(per.getFlgTipo())) {
				LocalDate dataInizio = LocalDate.ofInstant(per.getDInizio().toInstant(), defaultZoneId);
				dataInizio = dataInizio.minusDays(1);
				LocalDate dataFine = LocalDate.ofInstant(per.getDFine().toInstant(), defaultZoneId);
				dataFine = dataFine.plusDays(1);
				ret = (
						(startDate.isAfter(dataInizio) && startDate.isBefore(dataFine)) || 
						(endDate.isAfter(dataInizio) && endDate.isBefore(dataFine)));
			
				if (ret) 
					break;
			}
		}
		return ret;
	}
	
	
	public boolean esisteInPeriodo(List<EsoTVersamentoPvPeriodo> periodi, Date start, Date end) {
		
		LocalDate startDate = LocalDate.ofInstant(start.toInstant(), defaultZoneId);
		LocalDate endDate = LocalDate.ofInstant(end.toInstant(), defaultZoneId);
		
		boolean esiste = false;
		
		for (EsoTVersamentoPvPeriodo periodo : periodi) {
			if (!"C".equals(periodo.getFlgTipo())) {
				LocalDate dataInizio = LocalDate.ofInstant(periodo.getDInizio().toInstant(), defaultZoneId);
				dataInizio = dataInizio.minusDays(1);
				LocalDate dataFine = LocalDate.ofInstant(periodo.getDFine().toInstant(), defaultZoneId);
				dataFine = dataFine.plusDays(1);
				
				esiste = ((startDate.isBefore(dataInizio) && endDate.isBefore(dataInizio)) ||
						(startDate.isAfter(dataFine) && endDate.isAfter(dataFine)));
				
				if (esiste)
					return true;
			}
		}
		return false;
	}
	
	
	public boolean esisteInPeriodoCollocamentoMirato(List<EsoTVersamentoPvPeriodo> periodiCollocamentoMirato, Date start, Date end) {

		LocalDate dataInizioPeriodo = LocalDate.ofInstant(start.toInstant(), defaultZoneId);
		LocalDate dataFinePeriodo = LocalDate.ofInstant(end.toInstant(), defaultZoneId);

		for (EsoTVersamentoPvPeriodo periodoCollocamentoMirato : periodiCollocamentoMirato) {

			LocalDate dataInizioCollocamentoMirato = LocalDate.ofInstant(periodoCollocamentoMirato.getDInizio().toInstant(),
					defaultZoneId);
			dataInizioCollocamentoMirato = dataInizioCollocamentoMirato.minusDays(1);
			LocalDate dataFineCollocamentoMirato = LocalDate.ofInstant(periodoCollocamentoMirato.getDFine().toInstant(),
					defaultZoneId);
			dataFineCollocamentoMirato = dataFineCollocamentoMirato.plusDays(1);
			
			if (dataInizioPeriodo.isAfter(dataInizioCollocamentoMirato) && dataInizioPeriodo.isBefore(dataFineCollocamentoMirato) || dataFinePeriodo.isAfter(dataInizioCollocamentoMirato) && dataFinePeriodo.isBefore(dataFineCollocamentoMirato))
				return true;
			
		}
		return false;
	}
	
	
	
	
	public boolean inPeriodo(List<EsoTVersamentoPvPeriodo> periodi, Date start, Date end) {
		
		LocalDate startDate = LocalDate.ofInstant(start.toInstant(), defaultZoneId);
		LocalDate endDate = LocalDate.ofInstant(end.toInstant(), defaultZoneId);
		
		boolean inPeriodo = false;
		
		for (EsoTVersamentoPvPeriodo periodo : periodi) {
			if (!"C".equals(periodo.getFlgTipo())) {
				LocalDate dataInizio = LocalDate.ofInstant(periodo.getDInizio().toInstant(), defaultZoneId);
				dataInizio = dataInizio.minusDays(1);
				LocalDate dataFine = LocalDate.ofInstant(periodo.getDFine().toInstant(), defaultZoneId);
				dataFine = dataFine.plusDays(1);
				inPeriodo = (
						(startDate.isAfter(dataInizio) && startDate.isBefore(dataFine)) && 
						(endDate.isAfter(dataInizio) && endDate.isBefore(dataFine)));
				
				if (inPeriodo)
					return true;
			}
		}
		return false;
	}

	
	public EsoTVersamentoPvPeriodo getPeriodo(List<EsoTVersamentoPvPeriodo> periodi, Date start, Date end) {
		
		LocalDate startDate = LocalDate.ofInstant(start.toInstant(), defaultZoneId);
		LocalDate endDate = LocalDate.ofInstant(end.toInstant(), defaultZoneId);
		
		for (EsoTVersamentoPvPeriodo periodo : periodi) {
			if (!"C".equals(periodo.getFlgTipo())) {
				LocalDate dataInizio = LocalDate.ofInstant(periodo.getDInizio().toInstant(), defaultZoneId);
				dataInizio = dataInizio.minusDays(1);
				LocalDate dataFine = LocalDate.ofInstant(periodo.getDFine().toInstant(), defaultZoneId);
				dataFine = dataFine.plusDays(1);
				if (
						(startDate.isAfter(dataInizio) && startDate.isBefore(dataFine)) && 
						(endDate.isAfter(dataInizio) && endDate.isBefore(dataFine))) {
					return periodo;
				}
			}
		}
		return null;
	}

	
	public EsoTVersamentoPvPeriodo getPeriodoEsonero(List<EsoTVersamentoPvPeriodo> periodi, Date start, Date end) {
		
		LocalDate startDate = LocalDate.ofInstant(start.toInstant(), defaultZoneId);
		LocalDate endDate = LocalDate.ofInstant(end.toInstant(), defaultZoneId);
		
		for (EsoTVersamentoPvPeriodo periodo : periodi) {
			if (!"C".equals(periodo.getFlgTipo())) {
				LocalDate dataInizio = LocalDate.ofInstant(periodo.getDInizio().toInstant(), defaultZoneId);
				dataInizio = dataInizio.minusDays(1);
				LocalDate dataFine = LocalDate.ofInstant(periodo.getDFine().toInstant(), defaultZoneId);
				dataFine = dataFine.plusDays(1);
				if (
						(startDate.isAfter(dataInizio) && startDate.isBefore(dataFine)) && 
						(endDate.isAfter(dataInizio) && endDate.isBefore(dataFine))) {
					return periodo;
				}
			}
		}
		return null;
	}


	private EsoTVersamentoPvCob inPeriodoCobs(List<EsoTVersamentoPvCob> cobs, LocalDate date) {
		boolean inPeriodo = false;
		for (EsoTVersamentoPvCob cob : cobs) {
			LocalDate dataCob = LocalDate.ofInstant(cob.getDCob().toInstant(), defaultZoneId);
			inPeriodo = dataCob.getDayOfYear() == date.getDayOfYear();
			if (inPeriodo)
				return cob;
		}
		return null;
	}
	
	
	private List<EsoTVersamentoPvRicIn> inPeriodoRicIn(List<EsoTVersamentoPvRicIn> esoTVersamentoPvRicIns, LocalDate date) {
		List<EsoTVersamentoPvRicIn> ret = new ArrayList<EsoTVersamentoPvRicIn>();
		
		if (esoTVersamentoPvRicIns != null) {
			for (EsoTVersamentoPvRicIn ricIn : esoTVersamentoPvRicIns) {
				LocalDate dataRicIn = LocalDate.ofInstant(ricIn.getDRiconoscimentoInvalidita().toInstant(), defaultZoneId);
				if (dataRicIn.getDayOfYear() == date.getDayOfYear())
					ret.add(ricIn);
			}
		}
		return ret;
	}

	private String getMark(String mark, EsoTVersamentoPvPeriodo periodo) {
		return getMark(mark,periodo, null);
	}
	
	private String getMark(String mark, EsoTVersamentoPvPeriodo periodo, String desc) {
		String ret = "(" + mark + " " + df.format(periodo.getDInizio()) + " - " + df.format(periodo.getDFine());
		
		if (desc != null)
			ret += " [" + desc + "]";
		
		ret += ")";
		return ret;
	}

	@SuppressWarnings("deprecation")
	private void printPeriodi(StringBuffer print, List<EsoTVersamentoPvPeriodo> periodi) {
		print.append("\n\n");
		
		print.append("PERIODI CALCOLATI\n");
		if (periodi != null && periodi.size()>0) {
			for (EsoTVersamentoPvPeriodo periodo : periodi) {
				BigDecimal imp = new BigDecimal(periodo.getImporto().doubleValue());
				imp.setScale(2, BigDecimal.ROUND_HALF_UP); // this does change bd
				imp = imp.setScale(2, BigDecimal.ROUND_HALF_UP);
				print.append(df.format(periodo.getDInizio()) + " - " + df.format(periodo.getDFine()) 
						+ " - gg-lav: " + periodo.getNumGgLavorativi() 
						+ " - base computo: " + periodo.getBaseComputo()
						+ " - disabili in forza: " + periodo.getNumDisabiliInForza()
						+ " - num lav esonerati: " + periodo.getNumLavoratoriEsonerati()
						+ " - num lav esonerati autocert: " + periodo.getNumEsoneratiAutocertificati()
						+ " - importo: " + imp + "\n");
			}
		}
		else print.append("[nessun periodo calcolato]\n");
	}
	
	private void printProspetto(StringBuffer print, EsoTVersamentoProspetto prospetto, EsoTVersamentoPvProspetto prospettoProvincia,
			String label) {
		print.append("\n");
		print.append(label + "\n");
		if (prospetto != null) {
			print.append(prospetto.getAnnoRiferimento()+"\n");
			print.append("base computo nazionale:" + (prospetto != null ? prospetto.getBaseComputoNazionale() : "<nullo>")
					+ "\nbase computo provinciale:" + (prospettoProvincia != null ? prospettoProvincia.getBaseComputoProvinciale() : "<nullo>")
					+ "\nquota riserva disabili:" + (prospettoProvincia != null ? prospettoProvincia.getQuotaRiservaDisabili() : "<nullo>")
					+ "\ndisabili in forza:"
					+ (prospettoProvincia != null ? prospettoProvincia.getNumDisabiliInForza() : "<nullo>") + "\ncompensati:"
					+ (prospettoProvincia != null ? prospettoProvincia.getNumSoggettiCompensatiDisabili() : "<nullo>")
					+ "\nesonerati autocertificati:" + (prospettoProvincia != null ? prospettoProvincia.getNumEsoneratiAutocertificati() : "<nullo>") + "\n");
			
		}
		else print.append("[void]\n");
	}
	
	private void printPeriodoRiferimento(StringBuffer print, int annoRiferimento) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		Calendar cal = Calendar.getInstance();
		print.append("\n");
		print.append("PERIODO RIFERIMENTO\n");

		cal.set(Calendar.YEAR, annoRiferimento);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date dataInizio = cal.getTime();

		cal.set(Calendar.YEAR, annoRiferimento);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		Date dataFine = cal.getTime();

		print.append(df.format(dataInizio) + " - " + df.format(dataFine) + "\n");
	}
	
	private void printCobs(StringBuffer print, List<EsoTVersamentoPvCob> cobs) {
		
		print.append("\n");
		print.append("COBS PROVINCIA CALCOLATE\n");
		if (cobs != null && cobs.size()>0) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			for (EsoTVersamentoPvCob cob : cobs)
				print.append(df.format(cob.getDCob()) + " num. lavoratori:" + cob.getNumLavoratori() + " num lavoratori disabili:"
						+ cob.getNumLavoratoriDisabili() + "\n");
		}
		else print.append("[void]\n");
		
		print.append("\n");
	}
	
	private void printRiconoscimentiInabilita(StringBuffer print, List<EsoTVersamentoPvRicIn> ricIns) {
		
		print.append("\n");
		print.append("RICONOSCIMENTI INABILITA'\n");
		if (ricIns != null && ricIns.size()>0) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			for (EsoTVersamentoPvRicIn ricIn : ricIns) {
				String dRiconoscimentoInvalidita = "<nulla>";
				if (ricIn.getDRiconoscimentoInvalidita() != null) 
					dRiconoscimentoInvalidita = df.format(ricIn.getDRiconoscimentoInvalidita());
				String dataScadenza = "<nulla>";
				if (ricIn.getDScadenza() != null)
					dataScadenza = df.format(ricIn.getDScadenza());
				
				print.append("Data richiesta inabilita':" +dRiconoscimentoInvalidita + " Data scadenza:"
						+ dataScadenza + " Num. ore sett. medie:" + ricIn.getNumOreSettMed() + "\n");
			}
		}
		else print.append("[void]\n");
		
		print.append("\n");
		
	}

	private void printPeriodi(StringBuffer print, List<EsoTVersamentoPvPeriodo> periodi, String label) {
		
		print.append("\n");
		print.append(label + "\n");
		
		if (periodi != null && periodi.size()>0) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			
			for (EsoTVersamentoPvPeriodo periodo : periodi) {
				print.append(df.format(periodo.getDInizio()) + " - " + df.format(periodo.getDFine()));
				
				if (periodo.getQuotaRiserva() != null)
					print.append(" %:"+ periodo.getQuotaRiserva() + "\n");
				else if (periodo.getNumLavoratoriSospesi() != null && periodo.getNumLavoratoriSospesi().intValue()>0)
					print.append(" num lav sospesi:"+ periodo.getNumLavoratoriSospesi() + "\n");
			}
		}
		else print.append("[void]\n");
	}

	
	private String getCodiceRegionaliPerCob(EsoTVersamentoPvCob cob) {
		String str = "";
		if (cob != null) {
			for (EsoTVersamentoCobLav lav : cob.getEsoTVersamentoCobLavs()) {
				if (lav.getCodiceComunicazioneReg() != null)
					str += " - " + lav.getCodiceComunicazioneReg();
			}
		}
		return str;
	}
	
	
	private int addLavoratoriRicIn(List<EsoTVersamentoPvRicIn> ricIns) {
		int lav = 0;
		for (EsoTVersamentoPvRicIn ricIn : ricIns) {
			if (ricIn.getNumOreSettMed() == null)
				lav +=1;
			else lav += MathUtils.round(ricIn.getNumOreSettMed()/40d);
		}
		return lav;
	}
	
	
	private SilapDCategoriaAzienda getCategoriaAzienda(String cod) {
		List<SilapDCategoriaAzienda> cats = SilapDCategoriaAzienda.list("codCategoriaAzienda", cod);
		if (cats != null && cats.size()>0) {
			SilapDCategoriaAzienda ret = new SilapDCategoriaAzienda();
			ret.setId(cats.get(0).getId());
			return ret;
		}
		return null;
	}
		
}
