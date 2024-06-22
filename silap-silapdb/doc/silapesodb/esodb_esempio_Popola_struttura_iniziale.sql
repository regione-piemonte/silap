insert into ESO_D_VERSAMENTO_STATO values (1, 'Bozza', 'N', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_STATO values (2, 'Accettata', 'N', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_STATO values (3, 'Modificata', 'N', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_STATO values (4, 'Autorizzata', 'N', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_STATO values (5, 'Non autorizzata', 'N', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_STATO values (6, 'Annullata', 'S', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_STATO values (7, 'Eliminata', 'S', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_STATO values (8, 'Versata', 'S', to_date ('01/02/2024','DD/MM/YYYY'),null);

insert into ESO_D_VERSAMENTO_MOTIVO_SOSPENSIONE values (1, 'Licenziamento collettivo', 100, to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_MOTIVO_SOSPENSIONE values (2, 'Contratti di solidarieta o C.I.G.S.''', null, to_date ('01/05/2023','DD/MM/YYYY'),null);

insert into ESO_D_VERSAMENTO_TIPO_CONVENZIONE values (1, 'Programma', '1', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_TIPO_CONVENZIONE values (2, 'Individuale', '2', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_TIPO_CONVENZIONE values (3, 'Cooperativa', '3', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_TIPO_CONVENZIONE values (4, 'Art. 12 bis L.68/99', '4', to_date ('01/05/2023','DD/MM/YYYY'),null);
insert into ESO_D_VERSAMENTO_TIPO_CONVENZIONE values (5, 'Art. 14 D. Lgs 276/03', '5', to_date ('01/05/2023','DD/MM/YYYY'),null);

commit;
