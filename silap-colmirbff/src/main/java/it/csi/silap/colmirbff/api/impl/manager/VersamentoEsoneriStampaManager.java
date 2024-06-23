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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;

import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.Versamento;
import it.csi.silap.colmirbff.api.dto.VersamentoProvincia;
import it.csi.silap.colmirbff.api.dto.VersamentoPvEsonero;
import it.csi.silap.colmirbff.api.dto.VersamentoPvGgExtraFestivi;
import it.csi.silap.colmirbff.api.dto.VersamentoPvPeriodo;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.util.report.ReportTabellare;
import it.csi.silap.colmirbff.util.report.ReportUtilities;
import it.csi.silap.colmirbff.util.report.ReportVersamento;
import it.csi.silap.colmirbff.util.report.ReportVersamentoSezione;

@Dependent
public class VersamentoEsoneriStampaManager extends BaseApiServiceImpl {

	BigDecimal totImportoProvince = new BigDecimal(0);

	public String stampaVersamentoEsoneri(Versamento versamento) {
		totImportoProvince = new BigDecimal(0);
		ReportVersamento report = new ReportVersamento(SilapConstants.TITOLO_STAMPA_VERSAMENTO);
		ReportVersamentoSezione sezioneDatiAziendali = new ReportVersamentoSezione("Dati aziendali");

		sezioneDatiAziendali.addValore("Denominazione azienda: ",
				versamento.getDsDenominazioneAzienda() != null ? versamento.getDsDenominazioneAzienda() : "-");
		sezioneDatiAziendali.addValore("Partita iva: ",
				versamento.getPartivaIva() != null ? versamento.getPartivaIva() : "-");
		sezioneDatiAziendali.addValore("Codice fiscale: ",
				versamento.getCodFiscale() != null ? versamento.getCodFiscale() : "-");

		sezioneDatiAziendali.addValore("Sede legale: "
				+ (versamento.getVersamentoSede().getComune().getDescr() != null
						? versamento.getVersamentoSede().getComune().getDescr()
						: "-")
				+ " ("
				+ (versamento.getVersamentoSede().getComune().getSilapDProvincia().getDescr() != null
						? versamento.getVersamentoSede().getComune().getSilapDProvincia().getDescr()
						: "-")
				+ ") "
				+ (versamento.getVersamentoSede().getDsIndirizzo() != null
						? versamento.getVersamentoSede().getDsIndirizzo()
						: "-"));
		sezioneDatiAziendali.addValore("CCNL: ",
				(versamento.getSilapDCcnl() != null && versamento.getSilapDCcnl().getDescr() != null) ? versamento.getSilapDCcnl().getDescr() : "-");
		sezioneDatiAziendali.addValore("Protocollo dichiarazione n. "
				+ (versamento.getNumProtocollo() != null ? versamento.getNumProtocollo().toString().trim() : "-")
				+ " del "
				+ (versamento.getDProtocollo() != null ? ReportUtilities.formatDate(versamento.getDProtocollo())
						: "-"));
		sezioneDatiAziendali.addValore("Credito residuo: " + (versamento.getNumCreditoResiduo() != null
				? versamento.getNumCreditoResiduo().toString().trim()
				: "-") + "€");

		report.addSezione(sezioneDatiAziendali);

		for (VersamentoProvincia provincia : versamento.getEsoTVersamentoProvincias()) {
			StringBuilder ggFestivi = new StringBuilder();
			for (VersamentoPvGgExtraFestivi giorniFestivi : provincia.getEsoTVersamentoPvGgExtraFestivis()) {
				if (giorniFestivi.getDGgFestivo() != null) {
					ggFestivi.append(ReportUtilities.formatDate(giorniFestivi.getDGgFestivo()));
					ggFestivi.append(" ");
				}
			}
			ReportVersamentoSezione sezioneProvincia = new ReportVersamentoSezione(
					"Provincia: " + (provincia.getSilapDProvincia().getDsSilapDProvincia() != null
							? provincia.getSilapDProvincia().getDsSilapDProvincia()
							: "-"));
			sezioneProvincia.addValore("Santo patrono: ",
					provincia.getDFestaPatronale() != null ? ReportUtilities.formatDate(provincia.getDFestaPatronale())
							: "-");
			sezioneProvincia.addValore("Giorni festivi da contratto: ", (ggFestivi != null && !"".equals(ggFestivi.toString())) ? ggFestivi.toString() : "-");
			sezioneProvincia.addValore("N. giorni lavorativi settimanali: ",
					provincia.getNumGgLavorativiSettimanali() != null
							? provincia.getNumGgLavorativiSettimanali().toString().trim()
							: "-");

			String esoneri = "";
			for (VersamentoPvEsonero esonero : provincia.getEsoTVersamentoPvEsoneros()) {
				String esoneroStringa = "Dal "
						+ (esonero.getDRichiesta() != null ? ReportUtilities.formatDate(esonero.getDRichiesta()) : "-")
						+ " al "
						+ (esonero.getDScadenza() != null ? ReportUtilities.formatDate(esonero.getDScadenza()) : "-")
						+ " ove la percentuale richiesta è del "
						+ (esonero.getPercRichiesta() != null ? esonero.getPercRichiesta().toString().trim() : "-")
						+ "%"
						+ (esonero.getPercConcessione() != null
								? " e la percentuale concessa è del " + esonero.getPercConcessione() + "%"
								: "")
						+ ".<br></br>Ultimo provvedimento: "
						+ (esonero.getDsTipoComunicazione() != null ? esonero.getDsTipoComunicazione() : "-") + " n. "
						+ (esonero.getNumClassificazione() != null ? esonero.getNumClassificazione().toString().trim()
								: "-")
						+ " del "
						+ (esonero.getDClassificazione() != null
								? ReportUtilities.formatDate(esonero.getDClassificazione())
								: "-")
						+ ". ";
				esoneri = esoneri.concat(esoneroStringa);
				esoneri = esoneri.concat("<br></br>");
				
				
			}
			sezioneProvincia.addValore("Esonero: ", esoneri != null ? esoneri : "-");
			System.out.println(esoneri);
			sezioneProvincia.addReportTabellare(getReportTabellareVersamento(provincia.getEsoTVersamentoPvPeriodos()));
			report.addSezione(sezioneProvincia);

		}
		ReportVersamentoSezione sezioneTotaleImporto = new ReportVersamentoSezione();
		DecimalFormat formatDecimal = new DecimalFormat("#,##0.00");
		totImportoProvince = totImportoProvince.subtract(versamento.getNumCreditoResiduo());
		String totaleImportoProvince = formatDecimal.format(totImportoProvince);
		sezioneTotaleImporto
				.addValore(totaleImportoProvince != null ? "TOTALE: " + totaleImportoProvince + "€" : "");
		report.addSezione(sezioneTotaleImporto);

		return ReportUtilities.createReportVersamento(report, "/img/logo_RegPiem.png");
	}

	private ReportTabellare getReportTabellareVersamento(List<VersamentoPvPeriodo> periodi) {
		ReportTabellare reportTabellare = new ReportTabellare();
		DecimalFormat formatDecimal = new DecimalFormat("#,##0.00");
		reportTabellare.addNomiColonne("PERIODO");
		reportTabellare.addNomiColonne("GG LAVORATIVI");
		reportTabellare.addNomiColonne("N. ESONERATI");
		reportTabellare.addNomiColonne("IMPORTO");
		List<Object> totali = new ArrayList<Object>();
		Long totNumGgLav = 0L;
		Long totNumLavEsonerati = 0L;
		BigDecimal totImporto = new BigDecimal(0);
		for (VersamentoPvPeriodo periodo : periodi) {
			
			if ("C".equalsIgnoreCase(periodo.getFlgTipo()))
				continue;
			
			List<Object> cols = new ArrayList<Object>();

			cols.add(ReportUtilities.formatDate(periodo.getDInizio()) + " - "
					+ ReportUtilities.formatDate(periodo.getDFine()));
			cols.add(periodo.getNumGgLavorativi());
			totNumGgLav += periodo.getNumGgLavorativi();
			cols.add(periodo.getNumLavoratoriEsonerati());
			totNumLavEsonerati += periodo.getNumLavoratoriEsonerati();
			cols.add(formatDecimal.format(periodo.getImporto()));
			totImporto = totImporto.add(periodo.getImporto());
			reportTabellare.addValori(cols);
		}

		String totaleImporto = formatDecimal.format(totImporto);
		totali.add("TOTALE");
		totali.add(totNumGgLav);
		totali.add(totNumLavEsonerati);
		totali.add(totaleImporto);
		totImportoProvince = totImportoProvince.add(totImporto);
		reportTabellare.addValori(totali);
		return reportTabellare;

	}
}
