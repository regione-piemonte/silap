# Prodotto - Componente
SILAP - SILAPDB

# Descrizione della componente
SILAPDB è la componente DB del prodotto SILAP.\
Il DBMS di riferimento è Postgresql 15.\
Tramite gli script messi a disposizione è possibile creare gli schemi dei dati del DB, usati dalle altre componenti che accedono ai dati per le operazioni CRUD. \
Per il popolamento iniziale delle tabelle possono essere forniti degli script "DML" su richiesta, con i valori utili per le tabelle di decodifica per esempio, nel caso di Regione Piemonte.\
Questa componente include quindi:
- script DDL per la creazione delle tabelle dello schema dati;
- script DDL per la definizione delle sequence e dei vincoli;
- script per la definizione delle viste (ove applicabile);

# Configurazioni iniziali
Definire un DB "silap" (per esempio) su una istanza DBMS Postgresql (consigliata versione 15 o superiore).
IL DB di SILAP prevede 3 diversi "schema" con relativi utenti correlati:
 - silap_auth : utenti silap_auth e silap_auth_rw ;
 - silap_eso : silap_eso e silap_eso_rw ;
 - silap_tra : silap_tra e silap_tra_rw ;
 
L'utente "silap_<suffisso>" è il proprietario dello schema corrispondente, e l'utente "sila_<suffisso>_rw" serve per accedere ai dati da applicativo ed effettuare le operazioni CRUD (questo utente non ha la possibilità di modificare lo schema dati).

# Getting Started
Una volta prelevato ("git clone") e portato in locale dal repository il progetto che include la componente DB, predisporsi per poter eseguire gli script nella sequenza indicata nel seguito.

# Prerequisiti di sistema
DBMS Postgresql versione 15; DB, schema ed utenti con permessi adeguati ad eseguire istruzioni di creazione tabelle.

# Installazione
Lanciare tutti gli script nella sequenza indicata dal prefisso del nome del file :

 - 01-silapauth :
   - 01-tables.sql
   - 02-foreign-keys.sql
   - nota: popolare le tabelle silap_d_funzione, silap_d_ruolo, silap_r_ruolo_funzione (possono essere forniti su richiesta i dati del contesto regione Piemonte)
    
 - 02-silaptra :
   - 01-tables.sql
   - 02-foreign-keys.sql
   - nota: popolare tutte le tabelle (possono essere forniti su richiesta i dati del contesto regione Piemonte)
    
 - 03-silapeso :
   - 01-sequences.sql
   - 02-tables.sql
   - 03-foreign-keys.sql
    - nota: popolare le tabelle di decodifica e dei parametri (le tabelle eso_d_* e la tabella eso_t_parametro)

Nella directory "doc" sono disponibili degli script di popolamento tabelle di esempio.

# Versioning
Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

# Authors
Fare riferimento a quanto riportato nel file AUTHORS.txt.

# Copyrights

© Copyright Regione Piemonte – 2024

Vedere anche il file Copyrights.txt .

# License
Il prodotto software è sottoposto alla licenza EUPL-1.2 o versioni successive.\
SPDX-License-Identifier: EUPL-1.2-or-later.\

Vedere il file EUPL v1_2 IT-LICENSE.txt per i dettagli.

