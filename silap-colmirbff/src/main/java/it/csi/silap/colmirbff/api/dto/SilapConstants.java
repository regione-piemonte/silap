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
package it.csi.silap.colmirbff.api.dto;

public class SilapConstants {

	public static final String ID_REGIONE_PIEMONTE = "01";

	// codici ministeriali province piemontesi
	public static final String COD_PROV_TO = "001";
	public static final String COD_PROV_VC = "002";
	public static final String COD_PROV_NO = "003";
	public static final String COD_PROV_CN = "004";
	public static final String COD_PROV_AT = "005";
	public static final String COD_PROV_AL = "006";
	public static final String COD_PROV_BI = "096";
	public static final String COD_PROV_VB = "103";

	public static final String PRODIS_CALLER = "SILAP";
	public static final String COMONL_CALLER = "COMONL";

	public static final String SILPREST_CALLER = "SILAP";
	public static final String SILPREST_USER = "SILAPUSER";
	
	// codici comunicazioni obbligatorie geco
	public static final String COD_GECO_UNILAV = "UL";
	public static final String COD_GECO_UNISOM = "US";
	
	// RUOLO
	public static final Long RUOLO_AMMINISTRATORE = 1L;
	public static final Long RUOLO_REGIONE = 2L;
	public static final Long RUOLO_CONSULENTE = 3L;
	public static final Long RUOLO_DELEGATO = 4L;
	public static final Long RUOLO_PERSONA_AUTORIZZATA = 5L;
	public static final Long RUOLO_LEGALE_RAPPRESENTANTE = 6L;

	// DSTATO
	public static final Long STATO_BOZZA = 1L;
	public static final Long STATO_ACCETTATA = 2L;
	public static final Long STATO_MODIFICATA = 3L;
	public static final Long STATO_AUTORIZZATA = 4L;
	public static final Long STATO_NON_AUTORIZZATA = 5L;
	public static final Long STATO_ANNULLATA = 6L;
	public static final Long STATO_ELIMINATA = 7L;
	public static final Long STATO_AVVISO_INVIATO = 8L;
	public static final Long STATO_ACCONTO = 9L;
	public static final Long STATO_SALDO = 10L;
	
	
	
	public static final String STATO_BOZZA_DESCR = "Bozza";
	public static final String STATO_ACCETTATA_DESCR = "Accettata";
	public static final String STATO_MODIFICATA_DESCR = "Modificata";
	
	
	public static final String STATO_AVVISO_INVIATO_DESCR = "Avviso inviato";
	public static final String STATO_ACCONTO_DESCR = "Acconto";
	public static final String STATO_SALDO_DESCR = "Saldo";
	
	
	
	// eso_d_versamento_motivo_sospensione
	public static final Long MOTIVO_SOSPENSIONE_LICENZIAMENTO_COLLETTIVO = 1L;
	public static final Long MOTIVO_SOSPENSIONE_CIGS = 2L;

	// PARAMETRI
	public static final String PARAMETRO_NUM_MAX_RECORD = "numMaxRec";

	public static final int MINUTI_DURATA_SESSIONE = 30;
	public static final int NUMERO_RECORD_PER_PAGINA = 20;
	public static final int NUMERO_RECORD_PER_PAGINA_RIDOTTA = 10;

	public static final int NUMERO_RECORD_COMMAD_COMPLETATION = 10;

	public static final String S = "S";
	public static final String N = "N";
	public static final String OK = "OK";
	public static final String KO = "KO";
	
	public static final int MESE_GENNAIO = 0;
	public static final int MESE_FEBBRAIO = 1;
	public static final int MESE_MARZO = 2;
	public static final int MESE_APRILE = 3;
	public static final int MESE_MAGGIO = 4;
	public static final int MESE_GIUGNO = 5;
	public static final int MESE_LUGLIO = 6;
	public static final int MESE_AGOSTO = 7;
	public static final int MESE_SETTEMBRE = 8;
	public static final int MESE_OTTOBRE = 9;
	public static final int MESE_NOVEMBRE = 10;
	public static final int MESE_DICEMBRE = 11;
	

	public static final String MSG_ERRORE_STAMPA = "003";
	public static final String MSG_ERRORE__PROSPETTO_NON_TROVATO = "100013";
	public static final String MSG_COD_FISCALE_AZIENDA_NON_PRESENTE = "100020";
	public static final String MSG_ERRORE_CONDIZIONI_VARIATE = "100021";
	public static final String MSG_AZIENDES_CON_FLAG_S = "100024";
	public static final String MSG_SEDE_LEGALE_CESSATA = "100026";
	public static final String MSG_ERRORE_GENERAZIONE_PDF = "100034";
	public static final String MSG_ERRORE_INVIO_MAIL = "100036";
	public static final String MSG_ELIMINA_DICHIARAZIONE_NO_AUTH = "100041";
	public static final String MSG_ELIMINA_DICHIARAZIONE_NO_BOZZA = "100042";
	public static final String MSG_CONFERMA_STATO_ANNULLATA = "100044";
	public static final String MSG_ERRORE_SISTEMA_SEDE_LEGALE = "100054";
	public static final String MSG_ERRORE_ESTRAZIONE_SEDE_LEGALE = "100055";
	public static final String MSG_ERRORE_SISTEMA_DATI_ANAGRAFICI_AZ = "100056";
	public static final String MSG_ERRORE_SISTEMA_PROSPETTO = "100057";
	public static final String MSG_ERRORE_DATI_PROSPETTO = "100058";
	public static final String MSG_ANNO_BOZZA_VECCHIO = "100092";
	public static final String MSG_CONTROLLO_MODIFICA = "100093";
	public static final String MSG_SEDE_LEGALE_ESTERA = "100094";
	public static final String MSG_ERRORE_DATI_AZIENDA = "100095";
	public static final String MSG_ERRORE_GET_VERSAMENTO = "100098";
	
	public static final String MSG_CONTROLLO_PERIODI = "100100";
	public static final String MSG_CONTROLLO_PERIODI_SOVRAPPOSTI = "100103";
	public static final String MSG_CONTROLLO_PERIODI_SOVRAPPOSTI_MODIFICA = "100104";
	
	
	public static final String MSG_CONTROLLO_PERIODI_CONVENZIONE = "100107";
	public static final String MSG_CONTROLLO_PERIODI_SOSPENSIONE = "100108";
	
	public static final String MSG_ESONERO_MANCANTE = "100071";
	
	public static final String MSG_ESONERO_FUORI_TERMINE_FEBBRAIO = "100016";
	public static final String MSG_ESONERO_FUORI_TERMINE_GIUGNO = "100017";
	public static final String MSG_ESONERO_ERROR_STATO = "100032";
	public static final String MSG_ERRORE_PROTOCOLLAZIONE = "100076";
	public static final String MSG_SUCCESSO_INVIO = "100083";
	
	
	public static final String MSG_BATCH_CREAZIONE_DEBUTORIA_ATTIVO = "100115";
	public static final String MSG_BATCH_CREAZIONE_DEBUTORIA_NESSUNA_DICHIARAZIONE = "100116";
	public static final String MSG_BATCH_CREAZIONE_DEBUTORIA_CONFERMA = "100117";
	
	/*cancellaPeriodi*/
	public static final String MSG_CANCELLA_PERIODI_ESITO_POSITIVO = "100127";
	public static final String MSG_CANCELLA_PERIODI_ESITO_NEGATIVO = "100128";
	
	
	public static final String BATCH_VERSAMENTO_ACCETTAZIONE_AUTO = "COD_1";
	public static final String BATCH_VERSAMENTO_PREVISIONE = "COD_2";
	public static final String BATCH_CREAZIONE_POSIZIONI_DEBITORIE = "COD_3";
	public static final String BATCH_INVIA_AVVISI_PAGAMENTO = "COD_4";
	
	

	public static final String TIPO_SEDE_LEGALE_1 = "1";

	public static final String MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_BOZZA = "100008";
	public static final String MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_ACCETTATA = "100009";
	public static final String MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_AUTORIZZATA = "100010";
	
	
	public static final String MSG_DICHIARAZIONE_ESISTENTE_IN_STATO_PAGAMENTO = "100129";

	
	public static final String MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_AZ = "DBA00001";
	public static final String MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_SEDE_AZ_2 = "DBA00002";
	public static final String MSG_ERRORE_INTERNO_DATI_ANAGRAFICI_SEDE_AZ = "DBS00001";
	
	public static final String TITOLO_STAMPA_VERSAMENTO = "Riepilogo importi da versare (art.5 co.3 Legge 68/99)";
	
	
	
	
	public static final String PARAM_COD_ORGPA = "ORGPA";
	public static final String PARAM_COD_TYPPA = "TYPPA";
	public static final String PARAM_COD_TYPPN = "TYPPN";
	public static final String PARAM_COD_PDAT1 = "PDAT1";
	public static final String PARAM_COD_PDAT2 = "PDAT2";
	public static final String PARAM_COD_PROGG = "PROGG";
	public static final String PARAM_COD_FIIUV = "FIIUV";
	public static final String PARAM_COD_CAUPA = "CAUPA";
	public static final String PARAM_COD_OAVPA = "OAVPA";
	public static final String PARAM_COD_MAVPA = "MAVPA";
	
	
	public static final String PARAM_COD_OAVPB = "OAVPB";
	public static final String PARAM_COD_OAVPC = "OAVPC";
	public static final String PARAM_COD_OAVPD = "OAVPD";
	
	public static final String PARAM_COD_MAVPB = "MAVPB";
	public static final String PARAM_COD_MAVPC = "MAVPC";
	public static final String PARAM_COD_MAVPD = "MAVPD";
	
	public static final String PARAM_COD_OAPPA = "OAPPA";
	public static final String PARAM_COD_MAPPA = "MAPPA";
	
	
	public static final String MSG_PAGOPA_UNA_RATA = "100113";
	public static final String MSG_PAGOPA_DUE_RATE = "100114";
	
	
	
	public static final String MSG_PAGOPA_CREAZIONE_POSIZIONI = "100130";
	public static final String MSG_PAGOPA_CHECK_POSIZIONI = "100131";
	public static final String MSG_PAGOPA_PREVISIONE = "100132";
	public static final String MSG_PAGOPA_ERR_PREVISIONE = "100133";
	


}
