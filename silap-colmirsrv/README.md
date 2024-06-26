# Prodotto
SILAP componente COLMIRSRV (esposizione API di integrazione con altri servizi)


# Descrizione della componente
Questa cartella rappresenta una "unità di installazione" corrispondente alla componente "logica" di backend che espone servizi verso altri applicativi dei sistemi informativi regionali del lavoro e della formazione professionale; in particolare espone servizi utili all'integrazione con il sistema di pagamento ePay.

In generale, è prisposta per l'esposizione di API REST e web-service SOAP.
Si connette al DB (SILAPDB) per le operazioni CRUD correlate ai servizi esposti.
I servizi esposti sono fruiti tramite protocollo HTTP, e protetti tramite uno schema di "basic authentication".

Questa componente si basa su:
- Java 11 --> AdoptOpenJDK 11
- Framework QUARKUS 2.16.2
- Librerie per la realizzazione della persistenza su DB : JPA e Hibernate col supporto della libreria Panache

# Configurazioni iniziali
Fondamentale avere a disposzione un ambiente (Virtual Machine o "container") in cui è installato un JDK ver.11, preferibilmente Adopt OpenJDK.
Da un punto di vista generale, nella fase iniziale occorre adattare i file di properties alla propria configurazione.
Una delle cose principali da configurare è il datasource con i riferimenti del DB che si intende utilizzare (JNDI name).\
Per quanto riguarda le properties da configurare il punto "centrale" è costituito dal file:

 src/main/resources/application.properties

Entrando più in dettaglio, nel seguito si riportano le principali properties da configurare :

- quarkus.http.host=
- quarkus.http.port=
- quarkus.http.root-path=
- quarkus.cxf.path = 
- quarkus.application.name=colmirsrv

- quarkus.package.type=
- quarkus.datasource.db-kind=postgresql
- quarkus.datasource.jdbc.max-size=

- quarkus.datasource.username=
- quarkus.datasource.password=
- quarkus.datasource.jdbc.url=jdbc:postgresql://<host>:<porta>/<DB Name> ....

- quarkus.transaction-manager.default-transaction-timeout = <numero secondi>

- user epay
  - quarkus.security.users.embedded.users.epay=
  - quarkus.security.users.embedded.roles.epay=

- Basic auth cfg base
  - quarkus.http.auth.basic=tru
  - quarkus.security.users.embedded.enabled=true
  - quarkus.security.users.embedded.plain-text=true

- MAIL
  - mail.smtp.auth=< true | false >
  - mail.smtp.starttls.enable=< true | false >
  - mail.smtp.host=
  - mail.smtp.port=< di default 25>
  - mail.username=aaa@bbb.it
  - mail.password=
  - mail.from=ccc.ddd@eee.it
  - mail.from.name=
	

# Getting Started
Una volta prelevata e portata in locale dal repository la componente ("git clone"), procedere con la modifica dei file di configurazione in base al proprio ambiente di deploy e quindi procedere al build.

Le indicazioni principali riportate nel seguito sono quelle seguite in fase di realizzazione della componente.
## implementazoni di default e modalità di sviluppo ##

### sviluppo contract first ###
Il progetto è impostato per lo sviluppo secondo la modalità contract-first. per quanto riguarda l'esposizione di API REST:
* nella cartella ```src/main/resources/META-INF``` sono presenti i file derivati dallo scheletro base "openapi.yaml" basati sulla specifica openapi 3, contenenti le definizioni di base e nei quali è possibile aggiungere le definizioni delle risorse specfiche dell'applicativo
* il pom.xml prevede l'utilizzo del plugin swagger per la generazione delle sole interfacce JAX-RS a partire da tale file di specifiche. La generazione avviene in fase di compilazione (quindi può essere scatenata con il comando ```mvn clean compile```)
* il generatore non genera invece le classi di implementazione delle API, che devono essere definite manualmente ed implementare le interfacce JAX-RS (vedere esempio ```StatusApiServiceImpl```)

### altre implementazioni predefinite ###

Sono predefiniti:
* il filtro per il recepimento dell'header Shibboleth (se autenticazione prevista - di default è disattivato: se necessario scommentare la annotation ```@Provider```)
* i filtri per la protezione cross site request forgery (XSRF)

### api strumentali ###

Il progetto è impostato per esporre una implementazione minimale di default delle API strumentali per:
* health check:
  * liveness: ```/q/health/live```
  * readyness: ```/q/health/ready```
* metrics:  ```/q/metrics```

E' possibile estendere l'implementazione di default a seconda delle esigenze specifiche.

# Prerequisiti di sistema
Occorre per prima cosa predisporre il DB Schema utilizzato da questa componente, e popolarlo con i dati iniziali: si deve quindi prima aver completato installazione e configurazione della componente silapdb.

Nella directory "csi-lib" sono disponibili le librerie sviluppate da CSI e rese disponibili con le licenze indicate nel BOM.csv .

Occorre inoltre prevedere le opportune sostituzioni dei servizi esterni richiamati.

Per il "build" si è previsto di utilizzare Apache Maven. Nel seguito alcune indicazioni di dettaglio:

Compilazione: mvn clean package .

Nel caso si aggiunga un nuovo profile, esso deve essere referenziato nella sezione `<profiles>` del `pom.xml`.


# Installazione - Deployment

In linea di massima è possibile procedere al deploy secondo le modalità previste per le applicazioni basate su Quarkus.
Nel nostro caso si è scelto di effettuarlo come una "standard java application", ovvero un jar eseguibile con le librerie necessarie nel classpath della JVM.


# Esecuzione dei test

Questa componente è stata sottoposta a vulnerability assessment.

# Versioning

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

# Copyrights

© Copyright Regione Piemonte – 2024\

Questo stesso elenco dei titolari del software è anche riportato in Copyrights.txt .

# License
Il prodotto software è sottoposto alla licenza EUPL-1.2 o versioni successive.
SPDX-License-Identifier: EUPL-1.2-or-later

