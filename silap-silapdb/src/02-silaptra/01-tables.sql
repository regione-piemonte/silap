CREATE TABLE IF NOT EXISTS silap_d_categoria_azienda
(
   id_silap_d_categoria_azienda  integer        NOT NULL,
   cod_categoria_azienda         varchar(1)     NOT NULL,
   ds_categoria_azienda          varchar(100)   NOT NULL,
   d_inizio                      date           NOT NULL,
   d_fine                        date
);

-- domain 'date': timestamp(0) without time zone;
-- domain 'date': timestamp(0) without time zone;

ALTER TABLE silap_d_categoria_azienda
   ADD CONSTRAINT pk_silap_d_categoria_azienda
   PRIMARY KEY (id_silap_d_categoria_azienda);

COMMENT ON COLUMN silap_d_categoria_azienda.cod_categoria_azienda IS 'Categoria azienda determinata in base alla base computo nazionale. Assume i valori A, B o C';
COMMENT ON COLUMN silap_d_categoria_azienda.ds_categoria_azienda IS 'Descrizione della categoria azienda determinata in base alla base computo nazionale';

GRANT REFERENCES, SELECT ON silap_d_categoria_azienda TO silap_eso;
GRANT SELECT, REFERENCES ON silap_d_categoria_azienda TO silap_eso_rw;
GRANT INSERT, SELECT, DELETE, UPDATE ON silap_d_categoria_azienda TO silap_tra_rw;

CREATE TABLE IF NOT EXISTS silap_d_ccnl
(
   id_silap_d_ccnl   varchar(5)      NOT NULL,
   ds_silap_d_ccnl   varchar(1500)   NOT NULL,
   num_ore           numeric(5,2),
   cod_silap_d_ccnl  varchar(5),
   ds_settore        varchar(100),
   d_inizio          timestamp       NOT NULL,
   d_fine            timestamp
);

ALTER TABLE silap_d_ccnl
   ADD CONSTRAINT pk_silap_d_ccnl
   PRIMARY KEY (id_silap_d_ccnl);

COMMENT ON TABLE silap_d_ccnl IS 'Contiene lelenco dei CCNL';
COMMENT ON COLUMN silap_d_ccnl.id_silap_d_ccnl IS 'Identificativo del CCNL';
COMMENT ON COLUMN silap_d_ccnl.ds_silap_d_ccnl IS 'Descrizione del CCNL';
COMMENT ON COLUMN silap_d_ccnl.num_ore IS 'Numero ore settimanali del contratto ';
COMMENT ON COLUMN silap_d_ccnl.cod_silap_d_ccnl IS 'Codifica ministeriale (fonte inps(21/06/2007)) ';
COMMENT ON COLUMN silap_d_ccnl.ds_settore IS 'Settore';
COMMENT ON COLUMN silap_d_ccnl.d_inizio IS 'Data inizio validita della codifica';
COMMENT ON COLUMN silap_d_ccnl.d_fine IS 'Data fine validita della codifica';

GRANT REFERENCES, SELECT ON silap_d_ccnl TO silap_eso;
GRANT SELECT ON silap_d_ccnl TO silap_eso_rw;
GRANT INSERT, SELECT, DELETE, UPDATE ON silap_d_ccnl TO silap_tra_rw;

CREATE TABLE IF NOT EXISTS silap_d_comune
(
   id_silap_d_comune     varchar(4)      NOT NULL,
   ds_silap_d_comune     varchar(1000)   NOT NULL,
   id_silap_d_provincia  varchar(3)      NOT NULL,
   d_inizio              timestamp       NOT NULL,
   d_fine                timestamp
);

ALTER TABLE silap_d_comune
   ADD CONSTRAINT pk_silap_d_comune
   PRIMARY KEY (id_silap_d_comune);

COMMENT ON TABLE silap_d_comune IS 'Contiene lelenco dei comuni';
COMMENT ON COLUMN silap_d_comune.id_silap_d_comune IS 'Identificativo del comune';
COMMENT ON COLUMN silap_d_comune.ds_silap_d_comune IS 'Descrizione del comune';
COMMENT ON COLUMN silap_d_comune.id_silap_d_provincia IS 'Identificativo della provincia';
COMMENT ON COLUMN silap_d_comune.d_inizio IS 'Data inizio validita della codifica';
COMMENT ON COLUMN silap_d_comune.d_fine IS 'Data fine validita della codifica';

GRANT REFERENCES, SELECT ON silap_d_comune TO silap_eso;
GRANT SELECT ON silap_d_comune TO silap_eso_rw;
GRANT INSERT, SELECT, DELETE, UPDATE ON silap_d_comune TO silap_tra_rw;

CREATE TABLE IF NOT EXISTS silap_d_messaggio
(
   id_silap_d_messaggio       numeric(7)      NOT NULL,
   id_silap_d_tipo_messaggio  varchar(2)      NOT NULL,
   ds_silap_d_messaggio       varchar(2000)   NOT NULL,
   cod_silap_d_messaggio      varchar(6)      NOT NULL,
   ds_titolo                  varchar(200),
   flg_attivo                 varchar(1)      NOT NULL
);

ALTER TABLE silap_d_messaggio
   ADD CONSTRAINT pk_silap_d_messaggio
   PRIMARY KEY (id_silap_d_messaggio);

COMMENT ON TABLE silap_d_messaggio IS 'Contiene lelenco dei messaggi';
COMMENT ON COLUMN silap_d_messaggio.id_silap_d_messaggio IS 'Identificativo del messaggio';
COMMENT ON COLUMN silap_d_messaggio.id_silap_d_tipo_messaggio IS 'Identificativo del tipo di messaggio';
COMMENT ON COLUMN silap_d_messaggio.ds_silap_d_messaggio IS 'Messaggio';
COMMENT ON COLUMN silap_d_messaggio.cod_silap_d_messaggio IS 'Codice del messaggio';
COMMENT ON COLUMN silap_d_messaggio.ds_titolo IS 'Descrizione del titolo';
COMMENT ON COLUMN silap_d_messaggio.flg_attivo IS 'Indica se e'' valido il messaggio. Default S';

GRANT REFERENCES, SELECT ON silap_d_messaggio TO silap_eso;
GRANT SELECT ON silap_d_messaggio TO silap_eso_rw;
GRANT INSERT, SELECT, DELETE, UPDATE ON silap_d_messaggio TO silap_tra_rw;

CREATE TABLE IF NOT EXISTS silap_d_provincia
(
   id_silap_d_provincia  varchar(3)    NOT NULL,
   ds_silap_d_provincia  varchar(30)   NOT NULL,
   ds_sigla_provincia    varchar(2)    NOT NULL,
   d_inizio              timestamp     NOT NULL,
   d_fine                timestamp
);

ALTER TABLE silap_d_provincia
   ADD CONSTRAINT pk_silap_d_provincia
   PRIMARY KEY (id_silap_d_provincia);

COMMENT ON TABLE silap_d_provincia IS 'Contiene lelenco delle province';
COMMENT ON COLUMN silap_d_provincia.id_silap_d_provincia IS 'Identificativo della provincia';
COMMENT ON COLUMN silap_d_provincia.ds_silap_d_provincia IS 'Descrizione provincia';
COMMENT ON COLUMN silap_d_provincia.ds_sigla_provincia IS 'Sigla provincia';
COMMENT ON COLUMN silap_d_provincia.d_inizio IS 'Data inizio validita della codifica';
COMMENT ON COLUMN silap_d_provincia.d_fine IS 'Data fine validita della codifica';

GRANT REFERENCES, SELECT ON silap_d_provincia TO silap_eso;
GRANT SELECT ON silap_d_provincia TO silap_eso_rw;
GRANT INSERT, SELECT, DELETE, UPDATE ON silap_d_provincia TO silap_tra_rw;

CREATE TABLE IF NOT EXISTS silap_d_tipo_messaggio
(
   id_silap_d_tipo_messaggio  varchar(2)    NOT NULL,
   ds_silap_d_tipo_messaggio  varchar(50)   NOT NULL
);

ALTER TABLE silap_d_tipo_messaggio
   ADD CONSTRAINT pk_silap_d_tipo_messaggio
   PRIMARY KEY (id_silap_d_tipo_messaggio);

GRANT REFERENCES, SELECT ON silap_d_tipo_messaggio TO silap_eso;
GRANT SELECT ON silap_d_tipo_messaggio TO silap_eso_rw;
GRANT INSERT, SELECT, DELETE, UPDATE ON silap_d_tipo_messaggio TO silap_tra_rw;


COMMIT;