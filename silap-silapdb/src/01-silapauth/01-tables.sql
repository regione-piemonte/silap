CREATE TABLE IF NOT EXISTS silap_d_funzione
(
   id_silap_d_funzione  numeric(4)      NOT NULL,
   ds_silap_d_funzione  varchar(100)    NOT NULL,
   ds_estesa            varchar(1000),
   d_inizio             timestamp       NOT NULL,
   d_fine               timestamp,
   note                 varchar(500),
   icona                varchar(100)
);

ALTER TABLE silap_d_funzione
   ADD CONSTRAINT pk_silap_d_funzione
   PRIMARY KEY (id_silap_d_funzione);

COMMENT ON TABLE silap_d_funzione IS 'Contiene elenco delle funzionalita''';
COMMENT ON COLUMN silap_d_funzione.id_silap_d_funzione IS 'Identificativo della funzionalita''';
COMMENT ON COLUMN silap_d_funzione.ds_silap_d_funzione IS 'Descrizione della funzionalita''';
COMMENT ON COLUMN silap_d_funzione.ds_estesa IS 'Descrizione estesa della funzionalita''';
COMMENT ON COLUMN silap_d_funzione.d_inizio IS 'Data inizio validita'' del ruolo';
COMMENT ON COLUMN silap_d_funzione.d_fine IS 'Data fine validita'' del ruolo';

GRANT REFERENCES, SELECT ON silap_d_funzione TO silap_eso;
GRANT SELECT ON silap_d_funzione TO silap_eso_rw;
GRANT DELETE, INSERT, SELECT, UPDATE ON silap_d_funzione TO silap_auth_rw;

CREATE TABLE IF NOT EXISTS silap_d_operatore
(
   id_silap_d_operatore  numeric(5)     NOT NULL,
   cod_fiscale           varchar(16)    NOT NULL,
   ds_cognome            varchar(100)   NOT NULL,
   ds_nome               varchar(100)   NOT NULL,
   cod_user_inserim      varchar(16)    NOT NULL,
   d_inserim             timestamp      NOT NULL,
   cod_user_aggiorn      varchar(16)    NOT NULL,
   d_aggiorn             timestamp      NOT NULL,
   n_timestamp           integer        NOT NULL
);

ALTER TABLE silap_d_operatore
   ADD CONSTRAINT pk_silap_d_operatore
   PRIMARY KEY (id_silap_d_operatore);

COMMENT ON TABLE silap_d_operatore IS 'Contiene elenco degli operatori Regionali e Amminstratori';
COMMENT ON COLUMN silap_d_operatore.id_silap_d_operatore IS 'Identificativo della dichiarazione di versamento esonero';
COMMENT ON COLUMN silap_d_operatore.cod_fiscale IS 'Codice fiscale delloperatore';
COMMENT ON COLUMN silap_d_operatore.ds_cognome IS 'Cognome delloperatore';
COMMENT ON COLUMN silap_d_operatore.ds_nome IS 'Nome delloperatore';
COMMENT ON COLUMN silap_d_operatore.cod_user_inserim IS 'Utente che ha inserito il record';
COMMENT ON COLUMN silap_d_operatore.d_inserim IS 'Data inserimento del record';
COMMENT ON COLUMN silap_d_operatore.cod_user_aggiorn IS 'Ultimo utente che ha aggiornato il record';
COMMENT ON COLUMN silap_d_operatore.d_aggiorn IS 'Data ultimo aggiornamento';
COMMENT ON COLUMN silap_d_operatore.n_timestamp IS 'Numero di volte che e'' stato aggiornato il record';

GRANT REFERENCES, SELECT ON silap_d_operatore TO silap_eso;
GRANT SELECT ON silap_d_operatore TO silap_eso_rw;
GRANT DELETE, INSERT, SELECT, UPDATE ON silap_d_operatore TO silap_auth_rw;

CREATE TABLE IF NOT EXISTS silap_d_ruolo
(
   id_silap_d_ruolo  numeric(3)    NOT NULL,
   ds_silap_d_ruolo  varchar(60)   NOT NULL,
   ds_descrizione    varchar(80)   NOT NULL
);

ALTER TABLE silap_d_ruolo
   ADD CONSTRAINT pk_silap_d_ruolo
   PRIMARY KEY (id_silap_d_ruolo);

COMMENT ON TABLE silap_d_ruolo IS 'Contiene elenco dei ruoli';
COMMENT ON COLUMN silap_d_ruolo.id_silap_d_ruolo IS 'Identificativo del ruolo';
COMMENT ON COLUMN silap_d_ruolo.ds_silap_d_ruolo IS 'Nome del ruolo';
COMMENT ON COLUMN silap_d_ruolo.ds_descrizione IS 'Descrizione del ruolo';

GRANT REFERENCES, SELECT ON silap_d_ruolo TO silap_eso;
GRANT SELECT ON silap_d_ruolo TO silap_eso_rw;
GRANT DELETE, INSERT, SELECT, UPDATE ON silap_d_ruolo TO silap_auth_rw;

CREATE TABLE IF NOT EXISTS silap_r_operatore_ruolo
(
   id_silap_d_operatore  numeric(5)   NOT NULL,
   id_silap_d_ruolo      numeric(3)   NOT NULL,
   d_inizio              timestamp    NOT NULL,
   d_fine                timestamp
);

ALTER TABLE silap_r_operatore_ruolo
   ADD CONSTRAINT pk_silap_r_operatore_ruolo
   PRIMARY KEY (id_silap_d_operatore, id_silap_d_ruolo);

GRANT REFERENCES, SELECT ON silap_r_operatore_ruolo TO silap_eso;
GRANT SELECT ON silap_r_operatore_ruolo TO silap_eso_rw;
GRANT DELETE, INSERT, SELECT, UPDATE ON silap_r_operatore_ruolo TO silap_auth_rw;

CREATE TABLE IF NOT EXISTS silap_r_ruolo_funzione
(
   id_silap_d_ruolo     numeric(3)   NOT NULL,
   id_silap_d_funzione  numeric(4)   NOT NULL,
   d_inizio             timestamp    NOT NULL,
   d_fine               timestamp
);

ALTER TABLE silap_r_ruolo_funzione
   ADD CONSTRAINT pk_silap_r_ruolo_funzione
   PRIMARY KEY (id_silap_d_ruolo, id_silap_d_funzione);

COMMENT ON TABLE silap_r_ruolo_funzione IS 'Indica per ogni ruolo quali sono le funzionalita'' associate';
COMMENT ON COLUMN silap_r_ruolo_funzione.id_silap_d_ruolo IS 'Identificativo del ruolo';
COMMENT ON COLUMN silap_r_ruolo_funzione.id_silap_d_funzione IS 'Identificativo della funzionalita''';
COMMENT ON COLUMN silap_r_ruolo_funzione.d_inizio IS 'Data inizio validita'' del ruolo';
COMMENT ON COLUMN silap_r_ruolo_funzione.d_fine IS 'Data fine validita'' del ruolo';

GRANT REFERENCES, SELECT ON silap_r_ruolo_funzione TO silap_eso;
GRANT SELECT ON silap_r_ruolo_funzione TO silap_eso_rw;
GRANT DELETE, INSERT, SELECT, UPDATE ON silap_r_ruolo_funzione TO silap_auth_rw;


COMMIT;