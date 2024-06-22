CREATE TABLE IF NOT EXISTS csi_log_audit
(
   id_audit    integer         NOT NULL,
   data_ora    timestamp       NOT NULL,
   ip_app      varchar(100)    NOT NULL,
   ip_address  varchar(40)     NOT NULL,
   utente      varchar(100)    NOT NULL,
   operazione  varchar(50)     NOT NULL,
   ogg_oper    varchar(4000)   NOT NULL,
   key_oper    varchar(2000)
);

ALTER TABLE csi_log_audit
   ADD CONSTRAINT pk_csi_log_audit
   PRIMARY KEY (id_audit);

COMMENT ON TABLE csi_log_audit IS 'Tabella audit CSI';
COMMENT ON COLUMN csi_log_audit.id_audit IS 'Identificativo audit';
COMMENT ON COLUMN csi_log_audit.data_ora IS 'Data e ora della chiamata';
COMMENT ON COLUMN csi_log_audit.ip_app IS 'Applicativi';
COMMENT ON COLUMN csi_log_audit.ip_address IS 'Indirizzo IP';
COMMENT ON COLUMN csi_log_audit.utente IS 'Utente';
COMMENT ON COLUMN csi_log_audit.operazione IS 'Nome delloperazione';
COMMENT ON COLUMN csi_log_audit.ogg_oper IS 'Parametri delloperazione';
COMMENT ON COLUMN csi_log_audit.key_oper IS 'Esito';

GRANT SELECT, INSERT, DELETE, UPDATE ON csi_log_audit TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_d_versamento_motivo_sospensione
(
   id_eso_d_versamento_motivo_sospensione  numeric(2)    NOT NULL,
   ds_eso_d_versamento_motivo_sospensione  varchar(50)   NOT NULL,
   percentuale                             numeric(3),
   d_inizio                                timestamp     NOT NULL,
   d_fine                                  timestamp
);

ALTER TABLE eso_d_versamento_motivo_sospensione
   ADD CONSTRAINT pk_eso_d_versamento_motivo_sospensione
   PRIMARY KEY (id_eso_d_versamento_motivo_sospensione);

COMMENT ON TABLE eso_d_versamento_motivo_sospensione IS 'Contiene i motivi di sospensione';
COMMENT ON COLUMN eso_d_versamento_motivo_sospensione.id_eso_d_versamento_motivo_sospensione IS 'Identificativo del motivo sospensione';
COMMENT ON COLUMN eso_d_versamento_motivo_sospensione.ds_eso_d_versamento_motivo_sospensione IS 'Descrizione del motivo sospensione';
COMMENT ON COLUMN eso_d_versamento_motivo_sospensione.percentuale IS 'Valore in percentuale di default';
COMMENT ON COLUMN eso_d_versamento_motivo_sospensione.d_inizio IS 'Data inizio validita della codifica';
COMMENT ON COLUMN eso_d_versamento_motivo_sospensione.d_fine IS 'Data fine validita della codifica';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_d_versamento_motivo_sospensione TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_d_versamento_stato
(
   id_eso_d_versamento_stato  numeric(2)    NOT NULL,
   ds_eso_d_versamento_stato  varchar(50)   NOT NULL,
   flg_stato_finale           varchar(1)    NOT NULL,
   d_inizio                   timestamp     NOT NULL,
   d_fine                     timestamp
);

ALTER TABLE eso_d_versamento_stato
   ADD CONSTRAINT pk_eso_d_versamento_stato
   PRIMARY KEY (id_eso_d_versamento_stato);

COMMENT ON TABLE eso_d_versamento_stato IS 'Contiene gli stati della dichiarazioni di versamento';
COMMENT ON COLUMN eso_d_versamento_stato.id_eso_d_versamento_stato IS 'Identificativo dello stato della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_d_versamento_stato.ds_eso_d_versamento_stato IS 'Descrzione dello stato della  dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_d_versamento_stato.flg_stato_finale IS 'Assume valore N se e'' uno stato inizale, S se e'' uno stato finale';
COMMENT ON COLUMN eso_d_versamento_stato.d_inizio IS 'Data inizio validita della codifica';
COMMENT ON COLUMN eso_d_versamento_stato.d_fine IS 'Data fine validita della codifica';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_d_versamento_stato TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_d_versamento_tipo_convenzione
(
   id_eso_d_versamento_tipo_convenzione  numeric(2)    NOT NULL,
   ds_eso_d_versamento_tipo_convenzione  varchar(50)   NOT NULL,
   id_sil_l68_tipo_convenzione           varchar(50)   NOT NULL,
   d_inizio                              timestamp     NOT NULL,
   d_fine                                timestamp
);

ALTER TABLE eso_d_versamento_tipo_convenzione
   ADD CONSTRAINT pk_eso_d_versamento_tipo_convenzione
   PRIMARY KEY (id_eso_d_versamento_tipo_convenzione);

COMMENT ON TABLE eso_d_versamento_tipo_convenzione IS 'Contiene i tipi di convenzione';
COMMENT ON COLUMN eso_d_versamento_tipo_convenzione.id_eso_d_versamento_tipo_convenzione IS 'Identificativo del tipo di convenzione';
COMMENT ON COLUMN eso_d_versamento_tipo_convenzione.ds_eso_d_versamento_tipo_convenzione IS 'Descrizione del tipo di convenzione';
COMMENT ON COLUMN eso_d_versamento_tipo_convenzione.id_sil_l68_tipo_convenzione IS 'Identificativo tipo di convenzione SILP (a scopo di transcodifica)';
COMMENT ON COLUMN eso_d_versamento_tipo_convenzione.d_inizio IS 'Data inizio validita della codifica';
COMMENT ON COLUMN eso_d_versamento_tipo_convenzione.d_fine IS 'Data fine validita della codifica';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_d_versamento_tipo_convenzione TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_batch_exec
(
   id_eso_t_batch_exec      integer        NOT NULL,
   cod_eso_t_batch_exec     varchar(5)     NOT NULL,
   d_inizio_exec            timestamp      NOT NULL,
   d_fine_exec              timestamp,
   num_record_da_elaborare  integer,
   num_record_ok            integer,
   num_record_ko            integer,
   note                     varchar(255),
   cod_user_inserim         varchar(16)    NOT NULL,
   d_inserim                timestamp      NOT NULL,
   cod_user_aggiorn         varchar(16)    NOT NULL,
   d_aggiorn                timestamp      NOT NULL,
   n_timestamp              integer        NOT NULL
);

ALTER TABLE eso_t_batch_exec
   ADD CONSTRAINT pk_eso_t_batch_exec
   PRIMARY KEY (id_eso_t_batch_exec);

COMMENT ON TABLE eso_t_batch_exec IS 'Contiene l''elenco delle aziende elaborate dal batch';
GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_batch_exec TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_batch_log
(
   id_eso_t_batch_log   integer        NOT NULL,
   id_eso_t_batch_exec  integer        NOT NULL,
   cod_fiscale          varchar(16)    NOT NULL,
   esito                varchar(2),
   log_exec             varchar(255),
   cod_user_inserim     varchar(16)    NOT NULL,
   d_inserim            timestamp      NOT NULL,
   cod_user_aggiorn     varchar(16)    NOT NULL,
   d_aggiorn            timestamp      NOT NULL,
   n_timestamp          integer        NOT NULL
);

ALTER TABLE eso_t_batch_log
   ADD CONSTRAINT pk_eso_t_batch_log
   PRIMARY KEY (id_eso_t_batch_log);

CREATE INDEX IF NOT EXISTS ie_eso_t_batch_log_01 ON silap_eso.eso_t_batch_log USING btree (id_eso_t_batch_exec);

CREATE INDEX IF NOT EXISTS ie_eso_t_batch_log_02 ON silap_eso.eso_t_batch_log USING btree (cod_fiscale);



COMMENT ON COLUMN eso_t_batch_log.esito IS 'Assume valori OK o KO';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_batch_log TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_credito_residuo
(
   id_eso_t_credito_residuo  integer        NOT NULL,
   cod_fiscale               varchar(16)    NOT NULL,
   num_valore                numeric(7,2)   NOT NULL
);

ALTER TABLE eso_t_credito_residuo
   ADD CONSTRAINT pk_eso_t_versamento_credito_residuo
   PRIMARY KEY (id_eso_t_credito_residuo);

COMMENT ON TABLE eso_t_credito_residuo IS 'Contiene i dati sul credito residuo di unazienda. Popolato con un trattamento dati. I dati sono forniti da Regione. Un valore solo per ogni azienda';
COMMENT ON COLUMN eso_t_credito_residuo.cod_fiscale IS 'Codice fiscale dellazienda';
COMMENT ON COLUMN eso_t_credito_residuo.num_valore IS 'Valore in euro del credito residuo';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_credito_residuo TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_notifica_pagamento_log
(
   id_eso_t_notifica_pagamento_log  integer        NOT NULL,
   cod_id_messaggio                 varchar(200)   NOT NULL,
   ds_messaggio                     varchar,
   cod_iuv                          varchar(18),
   esito                            varchar(2),
   ds_esito                         varchar(200),
   dt_inserim                       timestamp      NOT NULL,
   cod_user_inserim                 varchar(16)    NOT NULL,
   dt_aggiorn                       timestamp      NOT NULL,
   cod_user_aggiorn                 varchar(16)    NOT NULL,
   n_timestamp                      integer        NOT NULL
);

ALTER TABLE eso_t_notifica_pagamento_log
   ADD CONSTRAINT pk_eso_t_notifica_pagamento_log
   PRIMARY KEY (id_eso_t_notifica_pagamento_log);

COMMENT ON TABLE eso_t_notifica_pagamento_log IS 'log del servizio in ascolto per la ricezione delle notifiche di pagamento';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.id_eso_t_notifica_pagamento_log IS 'chiave primaria del record';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.cod_id_messaggio IS 'Id del messaggio ricevuto';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.ds_messaggio IS 'xml completo del messaggio riucevuto per le notifche di pagamento';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.cod_iuv IS 'IUV valorizzato solo per la singola notifica che presenta errori';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.esito IS 'Assume valori OK o KO';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.ds_esito IS 'Descrizione esito se la singola notifica estrapolata dal messaggio presenta errori';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.dt_inserim IS 'Data e ora inserimento del record';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.cod_user_inserim IS 'Servizio che ha inserito il record';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.dt_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.cod_user_aggiorn IS 'Ultimo servizio che ha aggiornato il record';
COMMENT ON COLUMN eso_t_notifica_pagamento_log.n_timestamp IS 'Numero di volte che è stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_notifica_pagamento_log TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_parametro
(
   id_eso_t_parametro  numeric(4)      NOT NULL,
   cod_parametro       varchar(5)      NOT NULL,
   flg_tipo_valore     varchar(2)      NOT NULL,
   ds_valore           varchar(1000)   NOT NULL,
   d_inizio_validita   timestamp       NOT NULL,
   d_fine_validita     timestamp,
   ds_note             varchar(50)
);

ALTER TABLE eso_t_parametro
   ADD CONSTRAINT pk_eso_t_parametro
   PRIMARY KEY (id_eso_t_parametro);

COMMENT ON TABLE eso_t_parametro IS 'Contiene un elenco di parametri utilizzati';
COMMENT ON COLUMN eso_t_parametro.id_eso_t_parametro IS 'Identificativo del parametero';
COMMENT ON COLUMN eso_t_parametro.cod_parametro IS 'Codice parametro';
COMMENT ON COLUMN eso_t_parametro.flg_tipo_valore IS 'Esprime il tipo di valore. Assume i valori; ST: stringa, DT: data, IN: Integer, FL: numero con la virgola, HH: ore; MM: minuti; SS: secondi, TI: Time';
COMMENT ON COLUMN eso_t_parametro.ds_valore IS 'Valore del parametro';
COMMENT ON COLUMN eso_t_parametro.d_inizio_validita IS 'Data inizio validita''';
COMMENT ON COLUMN eso_t_parametro.d_fine_validita IS 'Data fine validita''';
COMMENT ON COLUMN eso_t_parametro.ds_note IS 'Note sul parametro';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_parametro TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_posizione_debitoria
(
   id_eso_t_posizione_debitoria  integer         NOT NULL,
   id_eso_t_versamento           integer         NOT NULL,
   cod_iuv                       varchar(18)     NOT NULL,
   cod_avviso                    varchar(200)    NOT NULL,
   cod_id_pagamento              varchar(50)     NOT NULL,
   cod_versamento                varchar(4)      NOT NULL,
   dt_inizio_validita            date,
   dt_fine_validita              date,
   dt_scadenza                   date            NOT NULL,
   num_rata                      numeric(1)      NOT NULL,
   importo_atteso                numeric(13,2),
   ds_causale                    varchar(200),
   importo_pagato                numeric(13,2),
   ds_soggetto_versante          varchar(200),
   ds_debitore                   varchar(200),
   cf_debitore                   varchar(200),
   ds_psp                        varchar(200),
   cod_iur                       varchar(35),
   cod_transazione               varchar(50),
   dt_operazione                 date,
   dt_esito                      timestamp,
   ds_info_aggiuntive            varchar(200),
   ds_note                       varchar(200),
   dt_invio_promemoria           timestamp,
   dt_inserim                    timestamp       NOT NULL,
   cod_user_inserim              varchar(16)     NOT NULL,
   dt_aggiorn                    timestamp       NOT NULL,
   cod_user_aggiorn              varchar(16)     NOT NULL,
   n_timestamp                   integer         NOT NULL
);

-- domain 'date': timestamp(0) without time zone;
-- domain 'date': timestamp(0) without time zone;
-- domain 'date': timestamp(0) without time zone;
-- domain 'date': timestamp(0) without time zone;

ALTER TABLE eso_t_posizione_debitoria
   ADD CONSTRAINT pk_eso_t_posizione_debitoria
   PRIMARY KEY (id_eso_t_posizione_debitoria);

COMMENT ON TABLE eso_t_posizione_debitoria IS 'Contiene i dati degli avvisi di pagamento della dichiarazione';
COMMENT ON COLUMN eso_t_posizione_debitoria.id_eso_t_posizione_debitoria IS 'Identificativo della posizione debitoria';
COMMENT ON COLUMN eso_t_posizione_debitoria.id_eso_t_versamento IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_iuv IS 'Identificativo univoco di versamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_avviso IS 'Codice dell''avviso di pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_id_pagamento IS 'Identificativo univoco definito dal gestionale chiamante e richiesto dal motore dei pagamenti';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_versamento IS 'Codice del versamento definito dall''ente creditore';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_inizio_validita IS 'Data inizio validità della posizione debitoria';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_fine_validita IS 'Data fine validità della posizione debitoria';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_scadenza IS 'Data stampata sull''avviso di pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.num_rata IS 'Numero della rata (1 per prima rata o unico versamento, 2 per seconda rata)';
COMMENT ON COLUMN eso_t_posizione_debitoria.importo_atteso IS 'Importo atteso della posizioine debitoria';
COMMENT ON COLUMN eso_t_posizione_debitoria.ds_causale IS 'Causale del pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.importo_pagato IS 'Importo pagato';
COMMENT ON COLUMN eso_t_posizione_debitoria.ds_soggetto_versante IS 'Informazione del soggetto riportato nella transazione di pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.ds_debitore IS 'Nome e Cognome del cittadino oppure ragione sociale intestatario della posizione debitoria';
COMMENT ON COLUMN eso_t_posizione_debitoria.cf_debitore IS 'Codice fiscale o paritita iva intestatario della posizione debitoria';
COMMENT ON COLUMN eso_t_posizione_debitoria.ds_psp IS 'Descrizione del prestatore del servizio di pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_iur IS 'Identificativo univoco della riscossione';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_transazione IS 'nìNumero della transazione';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_operazione IS 'Data e ora dell''operazione di pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_esito IS 'Data e ora dell''esito del pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.ds_info_aggiuntive IS 'Infromazioni aggiuntive della transazione';
COMMENT ON COLUMN eso_t_posizione_debitoria.ds_note IS 'Note restituite nella notifica di pagamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_invio_promemoria IS 'Data e ora invio email automatica promemoria scadenza';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_user_inserim IS 'Servizio che ha inserito il record';
COMMENT ON COLUMN eso_t_posizione_debitoria.dt_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_posizione_debitoria.cod_user_aggiorn IS 'Ultimo servizio che ha aggiornato il record';
COMMENT ON COLUMN eso_t_posizione_debitoria.n_timestamp IS 'Numero di volte che è stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_posizione_debitoria TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_riconoscimenti_inabilita
(
   id_eso_t_riconoscimenti_inabilita  integer       NOT NULL,
   id_sil_azi_anagrafica              integer       NOT NULL,
   cod_fiscale                        varchar(16)   NOT NULL,
   partiva_iva                        varchar(11),
   anno_riferimento                   numeric(4)    NOT NULL,
   id_silap_d_provincia               varchar(3)    NOT NULL,
   d_riconosc_invalidita              date          NOT NULL,
   d_scadenza                         date,
   cod_fiscale_lav                    varchar(16),
   d_inizio_rapporto                  date,
   num_ore_sett_med                   numeric(2)
);

-- domain 'date': timestamp(0) without time zone;
-- domain 'date': timestamp(0) without time zone;
-- domain 'date': timestamp(0) without time zone;

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_riconoscimenti_inabilita TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento
(
   id_eso_t_versamento              integer        NOT NULL,
   id_sil_azi_anagrafica            integer        NOT NULL,
   cod_fiscale                      varchar(16)    NOT NULL,
   partiva_iva                      varchar(11),
   cod_fiscale_soggetto             varchar(16),
   anno_riferimento                 numeric(4)     NOT NULL,
   ds_denominazione_azienda         varchar(306)   NOT NULL,
   id_silap_d_ccnl                  varchar(5),
   ds_email_riferimento             varchar(300)   NOT NULL,
   num_rate                         numeric(1)     NOT NULL,
   num_credito_residuo              numeric(7,2)   NOT NULL,
   id_eso_t_versamento_sede_legale  integer        NOT NULL,
   anno_protocollo                  numeric(4),
   num_protocollo                   integer,
   d_protocollo                     timestamp,
   id_eso_t_credito_residuo         integer,
   cod_user_inserim                 varchar(16)    NOT NULL,
   d_inserim                        timestamp      NOT NULL,
   cod_user_aggiorn                 varchar(16)    NOT NULL,
   d_aggiorn                        timestamp      NOT NULL,
   n_timestamp                      integer        NOT NULL
);

ALTER TABLE eso_t_versamento
   ADD CONSTRAINT pk_eso_t_versamento
   PRIMARY KEY (id_eso_t_versamento);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_01 ON silap_eso.eso_t_versamento USING btree (id_sil_azi_anagrafica);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_02 ON silap_eso.eso_t_versamento USING btree (id_eso_t_versamento_sede_legale);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_03 ON silap_eso.eso_t_versamento USING btree (id_eso_t_credito_residuo);



COMMENT ON TABLE eso_t_versamento IS 'Contiene le dichiarazioni di versamento di una azienda';
COMMENT ON COLUMN eso_t_versamento.id_eso_t_versamento IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento.id_sil_azi_anagrafica IS 'Identificativo SILP dellazienda';
COMMENT ON COLUMN eso_t_versamento.cod_fiscale IS 'Codice fiscale dellazienda';
COMMENT ON COLUMN eso_t_versamento.partiva_iva IS 'Partita Iva dellazienda';
COMMENT ON COLUMN eso_t_versamento.cod_fiscale_soggetto IS 'Codice fiscale del soggetto abilitato, ovverro il codice fiscale dello studio di consulenza';
COMMENT ON COLUMN eso_t_versamento.anno_riferimento IS 'Anno di riferimento';
COMMENT ON COLUMN eso_t_versamento.ds_denominazione_azienda IS 'Denominazione azienda';
COMMENT ON COLUMN eso_t_versamento.id_silap_d_ccnl IS 'Identificativo del CCNL';
COMMENT ON COLUMN eso_t_versamento.ds_email_riferimento IS 'Email di riferimento';
COMMENT ON COLUMN eso_t_versamento.num_rate IS 'Indica il numero di rate per il versamento: in un''unica soluzione o in due rate';
COMMENT ON COLUMN eso_t_versamento.num_credito_residuo IS 'Credito residuo. Default  0';
COMMENT ON COLUMN eso_t_versamento.id_eso_t_versamento_sede_legale IS 'Identificativo della sede legale';
COMMENT ON COLUMN eso_t_versamento.anno_protocollo IS 'Anno di protocollo. Valorizzato se la dichiarazione è stata autorizzata';
COMMENT ON COLUMN eso_t_versamento.num_protocollo IS 'Numero protocollo. Valorizzato se la dichiarazione è stata autorizzata';
COMMENT ON COLUMN eso_t_versamento.d_protocollo IS 'Data protocollo della dichiarazione ai fini del versamento';
COMMENT ON COLUMN eso_t_versamento.id_eso_t_credito_residuo IS 'Riferimento al credito residuo eventualmente utilizzato';
COMMENT ON COLUMN eso_t_versamento.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento.n_timestamp IS 'Numero di volte che è stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_cob_lav
(
   id_eso_t_versamento_pv_cob_lav  integer        NOT NULL,
   id_eso_t_versamento_pv_cob      integer        NOT NULL,
   cod_fiscale                     varchar(16)    NOT NULL,
   ds_cognome                      varchar(50)    NOT NULL,
   ds_nome                         varchar(50)    NOT NULL,
   codice_comunicazione_reg        varchar(16)    NOT NULL,
   sigla_pv                        varchar(2)     NOT NULL,
   ds_tipo_comunicazione           varchar(50)    NOT NULL,
   ds_tipo_contratto               varchar(200)   NOT NULL,
   flg_fulltime                    varchar(1)     NOT NULL,
   num_ore_sett_med                numeric(2),
   flg_l68                         varchar(1)     NOT NULL
);

ALTER TABLE eso_t_versamento_cob_lav
   ADD CONSTRAINT pk_eso_t_versamento_cob_lav
   PRIMARY KEY (id_eso_t_versamento_pv_cob_lav);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_cob_lav_01 ON silap_eso.eso_t_versamento_cob_lav USING btree (id_eso_t_versamento_pv_cob);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_cob_lav_02 ON silap_eso.eso_t_versamento_cob_lav USING btree (codice_comunicazione_reg);



COMMENT ON TABLE eso_t_versamento_cob_lav IS 'Contiene i dati di variazione delle COB. I dati sono estratti da GECO';
COMMENT ON COLUMN eso_t_versamento_cob_lav.id_eso_t_versamento_pv_cob_lav IS 'Identificativo della tabella';
COMMENT ON COLUMN eso_t_versamento_cob_lav.id_eso_t_versamento_pv_cob IS 'Identificativo dei dati di variazione di una COB';
COMMENT ON COLUMN eso_t_versamento_cob_lav.cod_fiscale IS 'Codice fiscale del lavoratore';
COMMENT ON COLUMN eso_t_versamento_cob_lav.ds_cognome IS 'Cognome del lavoratore';
COMMENT ON COLUMN eso_t_versamento_cob_lav.ds_nome IS 'Nome del lavoratore';
COMMENT ON COLUMN eso_t_versamento_cob_lav.codice_comunicazione_reg IS 'Codice regionale della comunicazione. Corrisponde a COM_D_COMUNICAZIONE.CODICE_COMUNICAZIONE_REG';
COMMENT ON COLUMN eso_t_versamento_cob_lav.sigla_pv IS 'Sigla Provincia';
COMMENT ON COLUMN eso_t_versamento_cob_lav.ds_tipo_comunicazione IS 'Descrizione comunicazione: assunzione, cessazione, proroga, trasferimento,  trasformazione, â¦ Analoga alla COM_T_TIPO_COMUNICAZIONE o solo la descrizione';
COMMENT ON COLUMN eso_t_versamento_cob_lav.ds_tipo_contratto IS 'Descrizione del tipo contratto o metter la tabella dei contratti,â¦';
COMMENT ON COLUMN eso_t_versamento_cob_lav.flg_fulltime IS 'Assume i valori: S o N. Se S significa che il rapporto di lavoro e'' full-time';
COMMENT ON COLUMN eso_t_versamento_cob_lav.num_ore_sett_med IS 'Numero di ore parttime. Valorizzato se FLG_FULLTIME = N';
COMMENT ON COLUMN eso_t_versamento_cob_lav.flg_l68 IS 'Assume i valori: S o N. Se S significa che e'' un disabile';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_cob_lav TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_prospetto
(
   id_eso_t_versamento_prospetto  integer       NOT NULL,
   id_eso_t_versamento            integer       NOT NULL,
   codice_regionale               varchar(16)   NOT NULL,
   anno_riferimento               numeric(4)    NOT NULL,
   categoria_azienda              varchar(1),
   base_computo_nazionale         numeric(6)    NOT NULL,
   cod_user_inserim               varchar(16)   NOT NULL,
   d_inserim                      timestamp     NOT NULL,
   cod_user_aggiorn               varchar(16)   NOT NULL,
   d_aggiorn                      timestamp     NOT NULL,
   n_timestamp                    integer       NOT NULL
);

ALTER TABLE eso_t_versamento_prospetto
   ADD CONSTRAINT pk_eso_t_versamento_prospetto
   PRIMARY KEY (id_eso_t_versamento_prospetto);

CREATE UNIQUE INDEX ak_eso_t_versamento_prospetto_02 ON silap_eso.eso_t_versamento_prospetto USING btree (id_eso_t_versamento, anno_riferimento);



COMMENT ON TABLE eso_t_versamento_prospetto IS 'Contiene i dati dei prospetti di PRODIS';
COMMENT ON COLUMN eso_t_versamento_prospetto.id_eso_t_versamento_prospetto IS 'Identificativo del prospetto ';
COMMENT ON COLUMN eso_t_versamento_prospetto.id_eso_t_versamento IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_prospetto.codice_regionale IS 'Codice regionale del prospetto';
COMMENT ON COLUMN eso_t_versamento_prospetto.anno_riferimento IS 'Anno di riferimento del prospetto disabili';
COMMENT ON COLUMN eso_t_versamento_prospetto.categoria_azienda IS 'Categoria azienda. Assume i valori:  B, C';
COMMENT ON COLUMN eso_t_versamento_prospetto.base_computo_nazionale IS 'Base computo nazionale';
COMMENT ON COLUMN eso_t_versamento_prospetto.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_prospetto.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_prospetto.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_prospetto.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_prospetto.n_timestamp IS 'Numero di volte che e'' stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_prospetto TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_provincia
(
   id_eso_t_versamento_provincia  integer         NOT NULL,
   id_eso_t_versamento            integer         NOT NULL,
   id_silap_d_provincia           varchar(3)      NOT NULL,
   num_gg_lavorativi_settimanali  numeric(1)      NOT NULL,
   d_festa_patronale              timestamp,
   cod_user_inserim               varchar(16)     NOT NULL,
   d_inserim                      timestamp       NOT NULL,
   cod_user_aggiorn               varchar(16)     NOT NULL,
   d_aggiorn                      timestamp       NOT NULL,
   n_timestamp                    integer         NOT NULL,
   importo_automatico             numeric(13,2)
);

ALTER TABLE eso_t_versamento_provincia
   ADD CONSTRAINT pk_eso_t_versamento_provincia
   PRIMARY KEY (id_eso_t_versamento_provincia);

CREATE UNIQUE INDEX ak_eso_t_versamento_provincia_01 ON silap_eso.eso_t_versamento_provincia USING btree (id_eso_t_versamento, id_silap_d_provincia);



COMMENT ON TABLE eso_t_versamento_provincia IS 'Contiene le dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_provincia.id_eso_t_versamento_provincia IS 'Identificativo dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_provincia.id_eso_t_versamento IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_provincia.id_silap_d_provincia IS 'Identificativo della provincia';
COMMENT ON COLUMN eso_t_versamento_provincia.num_gg_lavorativi_settimanali IS 'Numero di giorni lavorativi settimanali. Assume valore 5 o 6';
COMMENT ON COLUMN eso_t_versamento_provincia.d_festa_patronale IS 'Data festa patronale';
COMMENT ON COLUMN eso_t_versamento_provincia.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_provincia.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_provincia.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_provincia.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_provincia.n_timestamp IS 'Numero di volte che e'' stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_provincia TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_cob
(
   id_eso_t_versamento_pv_cob     integer        NOT NULL,
   id_eso_t_versamento_provincia  integer        NOT NULL,
   d_cob                          timestamp      NOT NULL,
   num_lavoratori_disabili        numeric(7,2),
   num_lavoratori                 numeric(7,2)
);

ALTER TABLE eso_t_versamento_pv_cob
   ADD CONSTRAINT pk_eso_t_versamento_pv_cob
   PRIMARY KEY (id_eso_t_versamento_pv_cob);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_cob_01 ON silap_eso.eso_t_versamento_pv_cob USING btree (id_eso_t_versamento_provincia);



COMMENT ON TABLE eso_t_versamento_pv_cob IS 'Contiene i dati di variazione delle COB. I dati sono estratti da GECO';
COMMENT ON COLUMN eso_t_versamento_pv_cob.id_eso_t_versamento_pv_cob IS 'Identificativo dei dati di variazione di una COB';
COMMENT ON COLUMN eso_t_versamento_pv_cob.id_eso_t_versamento_provincia IS 'Identificativo dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_cob.d_cob IS 'Data della comunicazione';
COMMENT ON COLUMN eso_t_versamento_pv_cob.num_lavoratori_disabili IS 'N. variazione lavoratori disabili. Numero decimale con segno';
COMMENT ON COLUMN eso_t_versamento_pv_cob.num_lavoratori IS 'N. variazione lavoratori normodotati. Numero decimale con segno';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_cob TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_convenzione
(
   id_eso_t_versamento_pv_convenzione    integer       NOT NULL,
   id_eso_t_versamento_provincia         integer       NOT NULL,
   d_stipula                             timestamp     NOT NULL,
   num_posizioni_aperte                  numeric(6)    NOT NULL,
   d_scadenza                            timestamp,
   id_eso_d_versamento_tipo_convenzione  numeric(2)    NOT NULL,
   cod_user_inserim                      varchar(16)   NOT NULL,
   d_inserim                             timestamp     NOT NULL,
   cod_user_aggiorn                      varchar(16)   NOT NULL,
   d_aggiorn                             timestamp     NOT NULL,
   n_timestamp                           integer       NOT NULL
);

ALTER TABLE eso_t_versamento_pv_convenzione
   ADD CONSTRAINT pk_eso_t_versamento_pv_convenzione
   PRIMARY KEY (id_eso_t_versamento_pv_convenzione);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_convenzione_01 ON silap_eso.eso_t_versamento_pv_convenzione USING btree (id_eso_t_versamento_provincia);



COMMENT ON TABLE eso_t_versamento_pv_convenzione IS 'Contiene i dati della convenzione di SILP per la provincia indicata';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.id_eso_t_versamento_pv_convenzione IS 'Identificativo dei dati della convenzione di una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.id_eso_t_versamento_provincia IS 'Identificativo dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.d_stipula IS 'Data stipula';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.num_posizioni_aperte IS 'Numero di posizioni aperte al 31/12 dell''anno di riferimento';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.d_scadenza IS 'Data scadenza';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.id_eso_d_versamento_tipo_convenzione IS 'Identificativo del tipo di convenzione';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_pv_convenzione.n_timestamp IS 'Numero di volte che e'' stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_convenzione TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_esonero
(
   id_eso_t_versamento_pv_esonero  integer       NOT NULL,
   id_eso_t_versamento_provincia   integer       NOT NULL,
   d_richiesta                     timestamp     NOT NULL,
   perc_richiesta                  numeric(3)    NOT NULL,
   d_concessione                   timestamp,
   d_scadenza                      timestamp,
   perc_concessione                numeric(3),
   d_diniego                       timestamp,
   ds_tipo_comunicazione           varchar(50),
   d_classificazione               timestamp,
   num_classificazione             numeric(8),
   cod_user_inserim                varchar(16)   NOT NULL,
   d_inserim                       timestamp     NOT NULL,
   cod_user_aggiorn                varchar(16)   NOT NULL,
   d_aggiorn                       timestamp     NOT NULL,
   n_timestamp                     integer       NOT NULL
);

ALTER TABLE eso_t_versamento_pv_esonero
   ADD CONSTRAINT pk_eso_t_versamento_pv_esonero
   PRIMARY KEY (id_eso_t_versamento_pv_esonero);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_esonero_01 ON silap_eso.eso_t_versamento_pv_esonero USING btree (id_eso_t_versamento_provincia);



COMMENT ON TABLE eso_t_versamento_pv_esonero IS 'Contiene i dati dellesonero di SILP per la provincia indicata';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.id_eso_t_versamento_pv_esonero IS 'Identificativo dei dati esonero SILP per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.id_eso_t_versamento_provincia IS 'Identificativo dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.d_richiesta IS 'Data richiesta esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.perc_richiesta IS 'Percentuale della richiesta esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.d_concessione IS 'Data concessione esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.d_scadenza IS 'Data scadenza esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.perc_concessione IS 'Percentuale della concessione esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.d_diniego IS 'Data in cui è stata rifiutata la richiesta di esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.ds_tipo_comunicazione IS 'Tipo dell''ultima comunicazione dell''esonero: Richiesta esonero, Concessione proroga esonero, ...';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.d_classificazione IS 'Data della classificazione dell''ultima comunicazione dell''esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.num_classificazione IS 'Numero di classificazione dell''ultima comunicazione dell''esonero';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_pv_esonero.n_timestamp IS 'Numero di volte che è stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_esonero TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_gg_extra_festivi
(
   id_eso_t_versamento_pv_gg_extra_festivi  integer       NOT NULL,
   id_eso_t_versamento_provincia            integer       NOT NULL,
   d_gg_festivo                             timestamp     NOT NULL,
   cod_user_inserim                         varchar(16)   NOT NULL,
   d_inserim                                timestamp     NOT NULL,
   cod_user_aggiorn                         varchar(16)   NOT NULL,
   d_aggiorn                                timestamp     NOT NULL,
   n_timestamp                              integer       NOT NULL
);

ALTER TABLE eso_t_versamento_pv_gg_extra_festivi
   ADD CONSTRAINT pk_eso_t_versamento_pv_gg_extra_festivi
   PRIMARY KEY (id_eso_t_versamento_pv_gg_extra_festivi);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_gg_extra_festivi_01 ON silap_eso.eso_t_versamento_pv_gg_extra_festivi USING btree (id_eso_t_versamento_provincia);



COMMENT ON TABLE eso_t_versamento_pv_gg_extra_festivi IS 'Contiene le dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.id_eso_t_versamento_pv_gg_extra_festivi IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.id_eso_t_versamento_provincia IS 'Identificativo dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.d_gg_festivo IS 'Giorno festivo per la provincia';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_pv_gg_extra_festivi.n_timestamp IS 'Numero di volte che e'' stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_gg_extra_festivi TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_periodo
(
   id_eso_t_versamento_pv_periodo  integer         NOT NULL,
   id_eso_t_versamento_provincia   integer         NOT NULL,
   d_inizio                        timestamp       NOT NULL,
   d_fine                          timestamp       NOT NULL,
   flg_tipo                        varchar(1)      NOT NULL,
   base_computo                    numeric(6)      NOT NULL,
   num_disabili_in_forza           numeric(6)      NOT NULL,
   num_soggetti_compensati         numeric(6)      NOT NULL,
   quota_riserva                   numeric(6)      NOT NULL,
   num_gg_lavorativi               numeric(3)      NOT NULL,
   num_lavoratori_esonerati        numeric(6)      NOT NULL,
   importo                         numeric(13,2)   NOT NULL,
   num_esonerati_autocertificati   numeric(6),
   cod_user_inserim                varchar(16)     NOT NULL,
   d_inserim                       timestamp       NOT NULL,
   cod_user_aggiorn                varchar(16)     NOT NULL,
   d_aggiorn                       timestamp       NOT NULL,
   n_timestamp                     integer         NOT NULL,
   num_riallineamento_nazionale    integer         DEFAULT 0 NOT NULL,
   id_silap_d_categoria_azienda    integer
);

ALTER TABLE eso_t_versamento_pv_periodo
   ADD CONSTRAINT pk_eso_t_versamento_pv_periodo
   PRIMARY KEY (id_eso_t_versamento_pv_periodo);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_periodo_01 ON silap_eso.eso_t_versamento_pv_periodo USING btree (id_eso_t_versamento_provincia);



COMMENT ON TABLE eso_t_versamento_pv_periodo IS 'Contiene le dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.id_eso_t_versamento_pv_periodo IS 'Identificativo del periodo di una dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.id_eso_t_versamento_provincia IS 'Identificativo dichiarazioni di versamento di una azienda per una provincia';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.d_inizio IS 'Data inizio periodo';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.d_fine IS 'Data fine periodo';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.flg_tipo IS 'Indice se il periodo è stato calcolato automaticamente (A) o inserito da operatore (O) o cancellato da operatore (solo per quelli automatici) (C). Assume i valori: A,O,C';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.base_computo IS 'Base computo';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.num_disabili_in_forza IS 'Numero disabili in forza';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.num_soggetti_compensati IS 'Numero soggetti compensati (valore positivo o negativo)';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.quota_riserva IS 'Quota di riserva';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.num_gg_lavorativi IS 'Giorni lavorativi';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.num_lavoratori_esonerati IS 'Numero esonerati';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.importo IS 'Importo';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.num_esonerati_autocertificati IS 'Numero di posizioni esonerate autocertificate - dato inserito dall''operatore';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.n_timestamp IS 'Numero di volte che è stato aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_pv_periodo.num_riallineamento_nazionale IS 'Unità di riallineamento nazionale (obbligatorio,  numero intero che può essere negativo o positivo).';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_periodo TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_prospetto
(
   id_eso_t_versamento_pv_prospetto  integer       NOT NULL,
   id_eso_t_versamento_prospetto     integer       NOT NULL,
   id_silap_d_provincia              varchar(3)    NOT NULL,
   base_computo_provinciale          numeric(6)    NOT NULL,
   quota_riserva_disabili            numeric(6)    NOT NULL,
   num_soggetti_compensati_disabili  numeric(6),
   cat_compensazione_disabili        varchar(1),
   num_disabili_in_forza             numeric(6)    NOT NULL,
   num_esonerati_autocertificati     numeric(6),
   cod_user_inserim                  varchar(16)   NOT NULL,
   d_inserim                         timestamp     NOT NULL,
   cod_user_aggiorn                  varchar(16)   NOT NULL,
   d_aggiorn                         timestamp     NOT NULL,
   n_timestamp                       integer       NOT NULL
);

ALTER TABLE eso_t_versamento_pv_prospetto
   ADD CONSTRAINT pk_eso_t_versamento_pv_prospetto
   PRIMARY KEY (id_eso_t_versamento_pv_prospetto);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_prospetto_01 ON silap_eso.eso_t_versamento_pv_prospetto USING btree (id_eso_t_versamento_prospetto);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_prospetto_02 ON silap_eso.eso_t_versamento_pv_prospetto USING btree (id_silap_d_provincia);



COMMENT ON TABLE eso_t_versamento_pv_prospetto IS 'Contiene i dati dei prospetti di PRODIS per la provincia indicata';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.id_eso_t_versamento_pv_prospetto IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.id_eso_t_versamento_prospetto IS 'Identificativo del prospetto ';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.id_silap_d_provincia IS 'Identificativo della provincia';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.base_computo_provinciale IS 'Base computo disabili Art.3';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.quota_riserva_disabili IS 'Quota di  riserva disabili ';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.num_soggetti_compensati_disabili IS 'Numero soggetti compensati ';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.cat_compensazione_disabili IS 'Categoria compensazione. Assume il valore R o E';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.num_disabili_in_forza IS 'Numero disabili in forza';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.num_esonerati_autocertificati IS 'Numero di posizioni esonerate autocertificate - dato estratto dal prospetto disabili';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_pv_prospetto.n_timestamp IS 'Numero di volte che è stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_prospetto TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_ric_in
(
   id_eso_t_versamento_pv_ric_in  integer       NOT NULL,
   id_eso_t_versamento_provincia  integer       NOT NULL,
   d_riconoscimento_invalidita    timestamp     NOT NULL,
   d_scadenza                     timestamp,
   num_ore_sett_med               numeric(2),
   cod_user_inserim               varchar(16)   NOT NULL,
   d_inserim                      timestamp     NOT NULL,
   cod_user_aggiorn               varchar(16)   NOT NULL,
   d_aggiorn                      timestamp     NOT NULL,
   n_timestamp                    integer       NOT NULL
);

ALTER TABLE eso_t_versamento_pv_ric_in
   ADD CONSTRAINT pk_eso_t_versamento_pv_ric_in
   PRIMARY KEY (id_eso_t_versamento_pv_ric_in);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_ric_in ON silap_eso.eso_t_versamento_pv_ric_in USING btree (id_eso_t_versamento_provincia);



GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_ric_in TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_pv_sospensione
(
   id_eso_t_versamento_pv_sospensione      integer       NOT NULL,
   id_eso_t_versamento_provincia           integer       NOT NULL,
   id_eso_d_versamento_motivo_sospensione  numeric(2)    NOT NULL,
   d_inizio_sospensione                    timestamp     NOT NULL,
   d_fine_sospensione                      timestamp     NOT NULL,
   perc_sospensione                        numeric(3),
   num_lavoratori_sospesi                  numeric(6),
   cod_user_inserim                        varchar(16)   NOT NULL,
   d_inserim                               timestamp     NOT NULL,
   cod_user_aggiorn                        varchar(16)   NOT NULL,
   d_aggiorn                               timestamp     NOT NULL,
   n_timestamp                             integer       NOT NULL
);

ALTER TABLE eso_t_versamento_pv_sospensione
   ADD CONSTRAINT pk_eso_t_versamento_pv_sospensione
   PRIMARY KEY (id_eso_t_versamento_pv_sospensione);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_pv_sospensione_01 ON silap_eso.eso_t_versamento_pv_sospensione USING btree (id_eso_t_versamento_provincia);



COMMENT ON COLUMN eso_t_versamento_pv_sospensione.d_inizio_sospensione IS 'Data inizio sospensione';
COMMENT ON COLUMN eso_t_versamento_pv_sospensione.d_fine_sospensione IS 'Data fine sospensione';
COMMENT ON COLUMN eso_t_versamento_pv_sospensione.perc_sospensione IS 'Percentuale di lavoratori sospesi; nel caso di licenziamento collettivo, la percentuale deve valere 100';
COMMENT ON COLUMN eso_t_versamento_pv_sospensione.num_lavoratori_sospesi IS 'Numero di lavoratori sospesi';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_pv_sospensione TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_sede
(
   id_eso_t_versamento_sede  integer        NOT NULL,
   id_sil_azi_sede           integer        NOT NULL,
   ds_denominazione_sede     varchar(305)   NOT NULL,
   ds_indirizzo              varchar(150),
   cod_cap                   varchar(5),
   id_silap_d_comune         varchar(4)     NOT NULL,
   cod_user_inserim          varchar(16)    NOT NULL,
   d_inserim                 timestamp      NOT NULL,
   cod_user_aggiorn          varchar(16)    NOT NULL,
   d_aggiorn                 timestamp      NOT NULL,
   n_timestamp               integer        NOT NULL
);

ALTER TABLE eso_t_versamento_sede
   ADD CONSTRAINT pk_eso_t_versamento_sede
   PRIMARY KEY (id_eso_t_versamento_sede);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_sede_01 ON silap_eso.eso_t_versamento_sede USING btree (id_sil_azi_sede);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_sede_02 ON silap_eso.eso_t_versamento_sede USING btree (id_silap_d_comune);



COMMENT ON TABLE eso_t_versamento_sede IS 'Contiene la sede legale e sedi di riferimento per ogni provincia di una dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_sede.id_eso_t_versamento_sede IS 'Identiticativo della sede della dichiarazione di versamento';
COMMENT ON COLUMN eso_t_versamento_sede.id_sil_azi_sede IS 'Identificativo della sede SILP';
COMMENT ON COLUMN eso_t_versamento_sede.ds_denominazione_sede IS 'Denominazione azienda';
COMMENT ON COLUMN eso_t_versamento_sede.ds_indirizzo IS 'Indirizzo della sede. Da definire se suddividerlo in toponimo, indirizzo, n. civico.';
COMMENT ON COLUMN eso_t_versamento_sede.cod_cap IS 'CAP della sede';
COMMENT ON COLUMN eso_t_versamento_sede.id_silap_d_comune IS 'Identificativo del comune';
COMMENT ON COLUMN eso_t_versamento_sede.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_sede.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_sede.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_sede.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_sede.n_timestamp IS 'Numero di volte che e'' stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_sede TO silap_eso_rw;

CREATE TABLE IF NOT EXISTS eso_t_versamento_stato
(
   id_eso_t_versamento_stato  integer        NOT NULL,
   id_eso_t_versamento        integer        NOT NULL,
   id_eso_d_versamento_stato  numeric(2)     NOT NULL,
   flg_current_record         varchar(1),
   d_stato                    timestamp      NOT NULL,
   ds_note                    varchar(300),
   cod_user_inserim           varchar(16)    NOT NULL,
   d_inserim                  timestamp      NOT NULL,
   cod_user_aggiorn           varchar(16)    NOT NULL,
   d_aggiorn                  timestamp      NOT NULL,
   n_timestamp                integer        NOT NULL
);

ALTER TABLE eso_t_versamento_stato
   ADD CONSTRAINT pk_eso_t_versamento_stato
   PRIMARY KEY (id_eso_t_versamento_stato);

CREATE INDEX IF NOT EXISTS ie_eso_t_versamento_stato_01 ON silap_eso.eso_t_versamento_stato USING btree (id_eso_t_versamento);

CREATE UNIQUE INDEX ak_eso_t_versamento_stato_01 ON silap_eso.eso_t_versamento_stato USING btree (id_eso_t_versamento, flg_current_record);



COMMENT ON TABLE eso_t_versamento_stato IS 'Contiene le dichiarazioni di versamento di una azienda';
COMMENT ON COLUMN eso_t_versamento_stato.id_eso_t_versamento IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_stato.id_eso_d_versamento_stato IS 'Identificativo dello stato della dichiarazione di versamento esonero';
COMMENT ON COLUMN eso_t_versamento_stato.flg_current_record IS 'Indica il record corrente; può esserci un solo flg_current_record = S per ogni ID_ESO_T_VERSAMENTO';
COMMENT ON COLUMN eso_t_versamento_stato.d_stato IS 'Data dello stato';
COMMENT ON COLUMN eso_t_versamento_stato.ds_note IS 'Motivo autorizzazione o non autorizzazione';
COMMENT ON COLUMN eso_t_versamento_stato.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN eso_t_versamento_stato.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN eso_t_versamento_stato.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN eso_t_versamento_stato.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN eso_t_versamento_stato.n_timestamp IS 'Numero di volte che e'' stato aggiornato il record';

GRANT SELECT, INSERT, DELETE, UPDATE ON eso_t_versamento_stato TO silap_eso_rw;


COMMIT;