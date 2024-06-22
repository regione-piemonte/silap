ALTER TABLE eso_t_batch_log
  ADD CONSTRAINT fk_eso_t_batch_exec_01 FOREIGN KEY (id_eso_t_batch_exec)
  REFERENCES eso_t_batch_exec (id_eso_t_batch_exec)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_posizione_debitoria
  ADD CONSTRAINT fk_eso_t_versamento_01 FOREIGN KEY (id_eso_t_versamento)
  REFERENCES eso_t_versamento (id_eso_t_versamento)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento
  ADD CONSTRAINT fk_eso_t_credito_residuo_01 FOREIGN KEY (id_eso_t_credito_residuo)
  REFERENCES eso_t_credito_residuo (id_eso_t_credito_residuo)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento
  ADD CONSTRAINT fk_eso_t_versamento_sede_01 FOREIGN KEY (id_eso_t_versamento_sede_legale)
  REFERENCES eso_t_versamento_sede (id_eso_t_versamento_sede)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento
  ADD CONSTRAINT fk_silap_d_ccnl_02 FOREIGN KEY (id_silap_d_ccnl)
  REFERENCES silap_tra.silap_d_ccnl (id_silap_d_ccnl)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_cob_lav
  ADD CONSTRAINT fk_eso_t_versamento_pv_cob_01 FOREIGN KEY (id_eso_t_versamento_pv_cob)
  REFERENCES eso_t_versamento_pv_cob (id_eso_t_versamento_pv_cob)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_prospetto
  ADD CONSTRAINT fk_eso_t_versamento_04 FOREIGN KEY (id_eso_t_versamento)
  REFERENCES eso_t_versamento (id_eso_t_versamento)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_provincia
  ADD CONSTRAINT fk_eso_t_versamento_03 FOREIGN KEY (id_eso_t_versamento)
  REFERENCES eso_t_versamento (id_eso_t_versamento)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_provincia
  ADD CONSTRAINT fk_silap_d_provincia_02 FOREIGN KEY (id_silap_d_provincia)
  REFERENCES silap_tra.silap_d_provincia (id_silap_d_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_cob
  ADD CONSTRAINT fk_eso_t_versamento_provincia_06 FOREIGN KEY (id_eso_t_versamento_provincia)
  REFERENCES eso_t_versamento_provincia (id_eso_t_versamento_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_convenzione
  ADD CONSTRAINT fk_eso_d_versamento_tipo_convenzione_01 FOREIGN KEY (id_eso_d_versamento_tipo_convenzione)
  REFERENCES eso_d_versamento_tipo_convenzione (id_eso_d_versamento_tipo_convenzione)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_convenzione
  ADD CONSTRAINT fk_eso_t_versamento_provincia_05 FOREIGN KEY (id_eso_t_versamento_provincia)
  REFERENCES eso_t_versamento_provincia (id_eso_t_versamento_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_esonero
  ADD CONSTRAINT fk_eso_t_versamento_provincia_04 FOREIGN KEY (id_eso_t_versamento_provincia)
  REFERENCES eso_t_versamento_provincia (id_eso_t_versamento_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_gg_extra_festivi
  ADD CONSTRAINT fk_eso_t_versamento_provincia_03 FOREIGN KEY (id_eso_t_versamento_provincia)
  REFERENCES eso_t_versamento_provincia (id_eso_t_versamento_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_periodo
  ADD CONSTRAINT fk_eso_t_versamento_provincia_02 FOREIGN KEY (id_eso_t_versamento_provincia)
  REFERENCES eso_t_versamento_provincia (id_eso_t_versamento_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_periodo
  ADD CONSTRAINT fk_silap_d_categoria_azienda FOREIGN KEY (id_silap_d_categoria_azienda)
  REFERENCES silap_tra.silap_d_categoria_azienda (id_silap_d_categoria_azienda)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_prospetto
  ADD CONSTRAINT fk_eso_t_versamento_prospetto_01 FOREIGN KEY (id_eso_t_versamento_prospetto)
  REFERENCES eso_t_versamento_prospetto (id_eso_t_versamento_prospetto)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_prospetto
  ADD CONSTRAINT fk_silap_d_provincia_03 FOREIGN KEY (id_silap_d_provincia)
  REFERENCES silap_tra.silap_d_provincia (id_silap_d_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_ric_in
  ADD CONSTRAINT fk_eso_t_versamento_provincia_10 FOREIGN KEY (id_eso_t_versamento_provincia)
  REFERENCES eso_t_versamento_provincia (id_eso_t_versamento_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_sospensione
  ADD CONSTRAINT fk_eso_d_versamento_motivo_sospensione_01 FOREIGN KEY (id_eso_d_versamento_motivo_sospensione)
  REFERENCES eso_d_versamento_motivo_sospensione (id_eso_d_versamento_motivo_sospensione)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_pv_sospensione
  ADD CONSTRAINT fk_eso_t_versamento_provincia_01 FOREIGN KEY (id_eso_t_versamento_provincia)
  REFERENCES eso_t_versamento_provincia (id_eso_t_versamento_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_sede
  ADD CONSTRAINT fk_silap_d_comune_01 FOREIGN KEY (id_silap_d_comune)
  REFERENCES silap_tra.silap_d_comune (id_silap_d_comune)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_stato
  ADD CONSTRAINT fk_eso_d_versamento_stato_01 FOREIGN KEY (id_eso_d_versamento_stato)
  REFERENCES eso_d_versamento_stato (id_eso_d_versamento_stato)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE eso_t_versamento_stato
  ADD CONSTRAINT fk_eso_t_versamento_01 FOREIGN KEY (id_eso_t_versamento)
  REFERENCES eso_t_versamento (id_eso_t_versamento)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;


commit;
