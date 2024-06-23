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
package it.csi.silap.colmirbff.util.report;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReportVersamentoSezione {
	private String titolo;
	private List<Valore> valori;
	private List<ReportTabellare> reportTabellari;
	private boolean landscape;

	
	public ReportVersamentoSezione() {
	}
	public ReportVersamentoSezione(String titolo) {
		this.titolo = titolo;

	}
	public void addReportTabellare(ReportTabellare reportTabellareVersamento) {
		if (reportTabellari == null)
			this.reportTabellari = new ArrayList<>();
		this.reportTabellari.add(reportTabellareVersamento);
	}
	public void addValore(Valore valore) {
		if (valori == null)
			this.valori = new ArrayList<>();
		this.valori.add(valore);
	}

	public void addValore(String name) {
		if (valori == null)
			this.valori = new ArrayList<>();
		this.valori.add(new Valore(name, ""));
	}

	public void addValore(String name, String value) {
		if (valori == null)
			this.valori = new ArrayList<>();
		this.valori.add(new Valore(name, value));
	}

	public void addValore(String name, String value, String name2, String value2) {
		if (valori == null)
			this.valori = new ArrayList<>();
		this.valori.add(new Valore(name, value, name2, value2));
	}
}
