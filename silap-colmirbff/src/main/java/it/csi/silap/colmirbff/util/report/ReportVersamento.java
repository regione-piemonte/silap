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
@Data
public class ReportVersamento {

	private String titolo;
	
	private List<ReportVersamentoSezione> sezioni;
	private boolean landscape;
	
	public ReportVersamento(String titolo) {
		this.titolo = titolo;
	}
	
	public void addSezione(ReportVersamentoSezione sezione) {
		if (sezioni == null)
			this.sezioni = new ArrayList<>();
		this.sezioni.add(sezione);
	}
}
