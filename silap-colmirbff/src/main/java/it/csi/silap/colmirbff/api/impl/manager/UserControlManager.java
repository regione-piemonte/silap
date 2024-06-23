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

import java.util.List;

import javax.enterprise.event.Observes;
import javax.ws.rs.ext.Provider;

import it.csi.silap.colmirbff.integration.entity.SilapDOperatore;
import it.csi.silap.colmirbff.util.SilapThreadLocalContainer;

@Provider
public class UserControlManager {

	public boolean control(@Observes SilapDOperatore silOperatore){
		List<SilapDOperatore> listaOperatori = SilapDOperatore.list("codFiscale", SilapThreadLocalContainer.UTENTE_CONNESSO.get().getCodFisc());
		if (listaOperatori != null && listaOperatori.size()>0)  
			silOperatore = listaOperatori.get(0);
   		return silOperatore != null && silOperatore.getIdSilapDOperatore() != null;
    }
}
