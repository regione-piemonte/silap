/*
esempio inserimento e aggornamento messaggi

*/


INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(129, 'E', 'Non e'' possibile inserire una nuova dichiarazione ai fini del versamento per l''azienda {0} ({1}) riferita all''anno {2} in quanto e'' gia'' presente una dichiarazione ai fini del versamento in stato {3}. Se si vuole inserirne una nuova si consiglia di contattare l''ufficio regionale competente.', '100129', 'Inserimento e modifica dichiarazione di fini del versamento', 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(130, 'I', 'Procedura di generazione avvisi di pagamento avviata con successo.', '100130', 'Genera avvisi di pagamento', 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(131, 'I', 'Check delle posizioni debitorie.', '100131', 'Genera avvisi di pagamento', 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(132, 'I', 'Previsione dichiarazione esoneri lanciata con successo.', '100132', 'Previsione dichiarazione', 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(133, 'E', 'Errore nel lancio previsione dichiarazione esoneri. ', '100133', 'Previsione dichiarazione', 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(134, 'V', 'Non è possibile inserire una data futura', '100134', NULL, 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(135, 'V', 'La data non rientra nell''anno di riferimento della dichiarazione.', '100135', NULL, 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(136, 'V', 'La data inizio non puo'' essere superiore all''anno di riferimento della dichiarazione.', '100136', NULL, 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(137, 'V', 'La data deve essere compresa nell''anno di riferimento della dichiarazione.', '100137', NULL, 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(138, 'V', 'Il periodo e'' sovrapposto ad altro periodo presente nella dichiarazione per la provincia. Occorre rimuovere tale incongruenza prima di procedere.', '100138', NULL, 'S');
INSERT INTO silap_tra.silap_d_messaggio (id_silap_d_messaggio, id_silap_d_tipo_messaggio, ds_silap_d_messaggio, cod_silap_d_messaggio, ds_titolo, flg_attivo) VALUES(139, 'V', 'La data da non puo'' essere superiore alla data a.', '100139', NULL, 'S');

UPDATE silap_tra.silap_d_messaggio SET ds_silap_d_messaggio='Inserire la data nel formato atteso: GG/MM/AAAA' WHERE id_silap_d_messaggio=4;

