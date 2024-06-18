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
package it.csi.silap.colmirsrv.api.impl.manager;

import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import javax.ws.rs.ext.Provider;

import it.csi.silap.colmirsrv.integration.entity.CsiLogAudit;

@Provider
public class CsiLogAuditManager {

	
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void log(@Observes CsiLogAudit csiLogAudit){
    	csiLogAudit.persist();
    }


}
