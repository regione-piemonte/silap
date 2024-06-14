# Prodotto

SILAP : Sistema Informativo Lavoro Aziende Piemonte

# Descrizione del prodotto
Il prodotto SILAP implementa le funzionalità del Portale Esoneri - Versamento Esonero Parziale L.68/99.
Il "portale esoneri" fa parte degli applicativi della Regione Piemonte - Sistema Informativo Lavoro dedicati ai datori di lavoro, e si va ad affiancare, sul Portale Aziende, agli applicativi GECO - Comunicazioni obbligatorie (prodotto COMONL) e Prospetto Disabili L.68/99 (prodotto PRODIS).

Il servizio corrispondente è disponibile all'indirizzo https://servizi.regione.piemonte.it/catalogo/portale-pagamento-esoneri-contributivi-esonero-autorizzato

Il servizio si rivolge a imprese, liberi professionisti, intermediari (consulenti del lavoro).

L’esonero parziale è un istituto disciplinato dall’art. 5 comma 3 della Legge 12 marzo 1999 n. 68 “Norme per il diritto al lavoro dei disabili” e dal Decreto del Ministero del Lavoro 7 luglio 2000 n.357: “… i datori di lavoro privati e gli enti pubblici economici che, per le speciali condizioni della loro attività, non possono occupare l'intera percentuale dei disabili, possono, a domanda, essere parzialmente esonerati dall'obbligo dell'assunzione, alla condizione che versino al Fondo regionale per l'occupazione dei disabili di cui all'articolo 14 un contributo esonerativo per ciascuna unità non assunta...”.
Per "speciali condizioni di attività" si intende la presenza di almeno una delle seguenti condizioni:

- faticosità della prestazione lavorativa richiesta;
- pericolosità connaturata al tipo di attività, anche derivante da condizioni ambientali nelle quali si svolge l'attività stessa;
- particolare modalità di svolgimento dell'attività lavorativa.

L'esonero parziale può essere concesso fino alla misura percentuale massima del 60% della quota di riserva, in relazione alle caratteristiche dell'attività aziendale. L'autorizzazione all'esonero parziale è concessa a tempo determinato e impone il versamento di un contributo di 39,21 euro (Decreto Ministeriale del 30/09/2021) per ogni giorno lavorativo e per ogni persona disabile non occupata.
Da giugno 2017, la Regione Piemonte è l’amministrazione competente del procedimento di concessione dell’esonero parziale di cui all'art 5 comma 3 della Legge 12 marzo 1999 n. 68.

L’applicativo permette alle aziende o ai propri delegati di presentare la dichiarazione annuale ai fini del versamento del contributo di esonero art.5 L. 68/99.

Il prodotto "SILAP" è stato realizzato nel completo rispetto dei Modelli e Regole definiti dalle Istituzioni.

La progettazione del SILAP è coerente con la più ampia riprogettazione complessiva delle altre componenti applicative del Sistema Informativo Lavoro, nonché con le modalità di interazione (protocolli di comunicazione – REST API) già utilizzate.

Il sistema è integrato con il sistema SILP per il recupero delle informazioni relative alle richieste di esonero, con il sistema PRODIS per il recupero delle informazioni dal prospetto informativo, con il sistema GECO per il recupero delle informazioni relative alle Comunicazioni Obbligatorie e con il sistema di protocollazione (IUP).

Nell’ambito del Portale Esoneri si andranno ad inserire servizi applicativi per diversi “adempimenti”: il primo realizzato è quello relativo al “Collocamento Mirato (ColMir)”.

Complessivamente l’applicativo prevede moduli di front-end web che interagiscono, tramite API, con moduli di back-end dove risiede la logica di business e che accedono al DB.
Il prodotto segue quindi il paradigma “SPA – Single Page Application” : la componente di interfaccia Angular ha una corrispondente componente di “BackEnd”, realizzata nel linguaggio Java, e che espone API REST per la componente Angular; il back-end accede al DB.

Il prodotto è strutturato nelle seguenti componenti specifiche:
- [silapdb]( https://github.com/regione-piemonte/silap/tree/main/silap-silapdb ) : script per la creazione del DB (istanza DBMS Postgresql);
- [colmirwcl]( https://github.com/regione-piemonte/comonl/tree/main/comonl-comonlwcl ) : Client Web (Angular13), front-end applicativo;
- [colmirbff]( https://github.com/regione-piemonte/comonl/tree/main/comonl-comonlweb ) : Componente SPA con servizi REST per colmirwcl;					;
- [colmirsrv]( https://github.com/regione-piemonte/comonl/tree/main/comonl-comonlweb ) : Componente di esposizione servizi (REST API) verso altri applicativi del Sistema Informativo Regionale.				;


A ciascuna componente del prodotto elencata sopra corisponde una sotto-directory denominata silap-<nome_componente>.\
In ciascuna di queste cartelle di componente si trovano ulteriori informazioni specifiche, incluso il BOM della componente di prodotto.

Nella directory [csi-lib]( https://github.com/regione-piemonte/silap/tree/main/csi-lib ) si trovano le librerie sviluppate da CSI-Piemonte con licenza OSS, come indicato nei BOM delle singole componenti, ed usate trasversalmente nel prodotto.

## Architettura Tecnologica

Le tecnologie adottate sono conformi agli attuali standard adottati da CSI per lo sviluppo del Sistema infermativo di Regione Piemonte, ed in particolare sono orientate alla possibilità di installare il prodotto sw su infrastruttura “a container”, orientata alle moderne architetture a “mini/microservizi”, prediligendo sostanzialmente gli strumenti open-source consolidati a livello internazionale (Linux, Java, Apache…); nel dettaglio tali pile prevedono:

- AdoptOpenJDK 11
- Framework QUARKUS 2.16.2
- WS Apache 2.4
- DBMS Postgresql 12.4
- S.O. Linux CentOS 7
- Angular 13 (lato client web).

## Linguaggi di programmazione utilizzati

I principali linguaggi utilizzati sono:
•	Java v. 11
•	HTML5
•	Typescript/Javascript
•	XML/JSON
•	SQL

## DB di riferimento

A seguito di valutazione sull’utilizzo di DBMS open-source si è ritenuto che Postgresql garantisca adeguata robustezza e affidabilità tendo conto delle dimensioni previste per il DB e dei volumi annui gestiti.
La versione scelta è la 12.4, con possibilità di aggiornamento alla versione 15.
Per quanto riguarda la struttura del DB, si è optato per un “partizionamento” fra entità di carattere trasversale ed entità specifiche per ciascun adempimento. Tale strutturazione è coerente con la progettazione di piattaforme sw orientate ai "micro-servizi".

## Tecnologie framework e standard individuati
Le tecnologie individuate sono Open Source e lo "stack applicativo" utilizzato rispetta gli Standard CSI. Si basa quindi sull’utilizzo di:

- Quarkus 2.16.2.Final
- Angular 13.x
- Jasper report 6.2.x
- librerie sviluppate da CSI e mantenute trasversalmente per la cooperazione applicativa

Quarkus è un framework che aggrega svariate librerie, per assolvere alle differenti finalità. Ad esempio: 
•	Librerie per la realizzazione dei servizi REST viene utilizzata Resteasy
•	Librerie per la realizzazione della persistenza su DB relazionale viene utilizzato JPA e Hibernate col supporto della libreria Panache


# Prerequisiti di sistema

Una istanza DBMS Postgresql (consigliata la verione 15) con utenza avente privilegi per la creazione tabelle ed altri oggetti DB (tramite le istruzioni DDL messe a disposizione nella componente silapdb), ed una ulteriore utenza separata non proprietaria dello schama, per l'esecuzione di istruzioni DML di Create, Readd, Update e Delete sui dati.

Un ambiente (VM o Container) in cui poter installare AdoptOpenJDK 11: in tale ambiente girerà la JVM configurata con il framework Quarkus.\
Una istanza di web server, consigliato apache web server ( https://httpd.apache.org/ ).\
Per il build è previsto l'uso di Apache Maven ( https://maven.apache.org/ ).\
Per la compilazione/build delle componenti colmirbff e colmirsrv sono rese disponibili nella directory "csi-lib" una serie di librerie predisposte da CSI Piemonte per un uso trasversale nei prodotti realizzati, o per uso specifico in altri prodotti con cui SILAP si interfaccia. Indicazioni più specifiche sono disponibili nella documentazione di ciascuna componente.

Il prodotto SILAP è integrato nei servizi del sistema informativo di Regione Piemonte "Lavoro": alcune sue funzionalità sono quindi strettamente legate alla possibilità di accedere a servizi esposti da altre componenti dell'ecosistema "Lavoro" di Regione Piemonte.

Infine, anche per quanto concerne l'autenticazione e la profilazione degli utenti del sistema, SILAP è integrato con servizi trasversali del sistema informativo regionale ("Shibboleth", "IRIDE"), di conseguenza per un utilizzo in un altro contesto occorre avere a disposizione servizi analoghi o integrare moduli opportuni che svolgano analoghe funzionalità.
 

# Installazione

Creare lo schema del DB, tramite gli script della componente comonldb.
 
Configurare il datasource nel file application.properties , utilizzato in colmirbff e colmirsrv.

Configurare i web server e definire gli opportuni Virtual Host e "location" - per utilizzare il protocollo https occorre munirsi di adeguati certificati SSL.

Nel caso si vogliano sfruttare le funzionalità di invio mail, occorre anche configurare un mail-server.


# Deployment

Dopo aver seguito le indicazioni del paragrafo relativo all'installazione, si può procedere al build dei pacchetti ed al deploy sull'infrastruttura prescelta.


# Versioning
Per la gestione del codice sorgente viene utilizzato Git, ma non vi sono vincoli per l'utilizzo di altri strumenti analoghi.\
Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).


# Copyrights
© Copyright Regione Piemonte – 2024\
© Copyright CSI-Piemonte – 2024


# License

SPDX-License-Identifier: EUPL-1.2-or-later .\
Questo software è distribuito con licenza EUPL-1.2 .\
Consultare il file LICENSE.txt per i dettagli sulla licenza.
