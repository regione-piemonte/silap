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

import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import javax.ws.rs.ext.Provider;

import it.csi.silap.colmirbff.integration.entity.SilapDOperatore;

@Provider
public class SilapDOperatoreManager {

	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void traceOperatore(@Observes SilapDOperatore operatore) {

		// TODO INFRASTRUTTURA - data ultimo accesso operatore
		
		/*
		operatore = SilapDOperatore.findById(SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc());
		operatore.setDataUltimoAccesso(new Date());
		operatore.persist();
		*/

	}

}
