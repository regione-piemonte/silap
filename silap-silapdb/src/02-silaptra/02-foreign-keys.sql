ALTER TABLE silap_d_comune
  ADD CONSTRAINT fk_silap_d_provincia_01 FOREIGN KEY (id_silap_d_provincia)
  REFERENCES silap_d_provincia (id_silap_d_provincia)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

ALTER TABLE silap_d_messaggio
  ADD CONSTRAINT fk_silap_d_tipo_messaggio_01 FOREIGN KEY (id_silap_d_tipo_messaggio)
  REFERENCES silap_d_tipo_messaggio (id_silap_d_tipo_messaggio)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;

commit;
