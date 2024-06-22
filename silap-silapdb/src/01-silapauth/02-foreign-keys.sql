alter table silap_r_operatore_ruolo
  add constraint fk_silap_d_operatore_01 foreign key (id_silap_d_operatore)
  references silap_d_operatore (id_silap_d_operatore)
  on update no action
  on delete no action;

alter table silap_r_operatore_ruolo
  add constraint fk_silap_d_ruolo_01 foreign key (id_silap_d_ruolo)
  references silap_d_ruolo (id_silap_d_ruolo)
  on update no action
  on delete no action;

alter table silap_r_ruolo_funzione
  add constraint fk_silap_d_funzione_01 foreign key (id_silap_d_funzione)
  references silap_d_funzione (id_silap_d_funzione)
  on update no action
  on delete no action;

alter table silap_r_ruolo_funzione
  add constraint fk_silap_d_ruolo_02 foreign key (id_silap_d_ruolo)
  references silap_d_ruolo (id_silap_d_ruolo)
  on update no action
  on delete no action;


commit;
