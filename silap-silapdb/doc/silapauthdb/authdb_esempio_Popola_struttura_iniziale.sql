insert into silap_d_ruolo (id_silap_d_ruolo,ds_silap_d_ruolo,ds_descrizione) 
values
  (1,'Amministratore','Amministratore del sistema'),
  (2,'Regione','Operatore Regionale'),
  (3,'Consulente responsabile','Consulente responsabile - C'),
  (4,'Delegato','Delegato - D'),
  (5,'Persona autorizzata','Persona autorizzata - E'),
  (6,'Legale rappresentante','Legale rappresentante o persona con carica (da AAEP)');


insert into silap_d_funzione (id_silap_d_funzione,ds_silap_d_funzione,ds_estesa,d_inizio,d_fine,note,icona) 
values
  (1,'Dichiarazione versamento esoneri',null,to_timestamp('01/05/2023', 'dd/mm/yyyy'),null,'Gestione dichiarazione annuale ai fini del versamento esoneri','fas fa-project-diagram'),
  (2,'Elimina dichiarazione versamento',null,to_timestamp('01/05/2023', 'dd/mm/yyyy'),null,null,null),
  (3,'Autorizza / Non autorizza dichiarazione versamento',null,to_timestamp('01/05/2023', 'dd/mm/yyyy'),null,null,null),
  (4,'Annulla dichiarazione versamento',null,to_timestamp('01/05/2023', 'dd/mm/yyyy'),null,null,null),
  (5,'Inserisce dichiarazione versamento',null,to_timestamp('01/05/2023', 'dd/mm/yyyy'),null,null,null),
  (6,'Modifica dichiarazione versamento',null,to_timestamp('01/05/2023', 'dd/mm/yyyy'),null,null,null),
  (7,'Autorizza dichiarazioni ',null,to_timestamp('26/09/2023', 'dd/mm/yyyy'),null,'Autorizzazione automatica delle richieste accettate','fas fa-space-shuttle'),
  (8,'Previsione dichiarazione',null,to_timestamp('03/10/2023', 'dd/mm/yyyy'),null,'Previsione dichiarazioni indicate nel file di upload','fas fa-rocket');


INSERT INTO silap_r_ruolo_funzione (id_silap_d_ruolo,id_silap_d_funzione,d_inizio,d_fine) 
VALUES
  (1,1,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (1,2,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (1,3,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (1,4,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (1,5,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (1,6,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (1,7,to_timestamp('26/09/2023', 'dd/mm/yyyy'),NULL),
  (1,8,to_timestamp('03/10/2023', 'dd/mm/yyyy'),NULL),
  (2,1,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (2,3,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (2,4,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (2,6,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (3,1,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (3,2,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (3,4,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (3,5,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (3,6,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (4,1,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (4,2,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (4,4,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (4,6,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (4,5,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (5,1,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (5,2,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (5,4,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (5,6,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (5,5,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (6,1,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (6,2,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (6,4,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (6,5,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL),
  (6,6,to_timestamp('01/05/2023', 'dd/mm/yyyy'),NULL);

