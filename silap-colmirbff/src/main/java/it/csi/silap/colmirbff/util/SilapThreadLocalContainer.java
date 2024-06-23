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

import it.csi.iride2.policy.entity.Identita;
import it.csi.silap.colmirbff.api.dto.Utente;

/**
 * Thead local container
 */
public class SilapThreadLocalContainer {
	
	
	public static final String ID_COD_FISC_SOGG_ABILITATO_KEY = "x-cf-abilitato";
	public static final String ID_RUOLO_KEY = "x-ruolo";	
	
	
	/** Contains the connected user */
	public static final ThreadLocal<String> TOKEN_SHIBBOLETH = new ThreadLocal<>();
	public static final ThreadLocal<Utente> UTENTE_CONNESSO = new ThreadLocal<>();
	public static final ThreadLocal<Identita> IDENTITA = new ThreadLocal<>();
	public static final ThreadLocal<String> CALLER_IP = new ThreadLocal<>();
	
	
	public static final ThreadLocal<Long> ID_RUOLO = new ThreadLocal<>();
	public static final ThreadLocal<String> ID_COD_FISC_SOGG_ABILITATO = new ThreadLocal<>();
	
	
	
	/** Private constructor */
	private SilapThreadLocalContainer() {
		// Prevent instantiation
	}

	/**
	 * Cleanup of the thread locals
	 */
	public static void cleanup() {
		UTENTE_CONNESSO.remove();
		IDENTITA.remove();
		CALLER_IP.remove();
		ID_RUOLO.remove();
		ID_COD_FISC_SOGG_ABILITATO.remove();
		TOKEN_SHIBBOLETH.remove();
	}

}
