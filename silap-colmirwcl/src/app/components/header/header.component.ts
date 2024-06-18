/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
import { DOCUMENT } from '@angular/common';
import { Component, EventEmitter, Inject, OnInit, Optional, Output } from '@angular/core';
import { Router } from '@angular/router';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { TranslateService } from '@ngx-translate/core';
import { ROUTE, RUOLO, STORAGE_KEY } from 'src/app/constants';
import { LANGUAGE_STORAGE, Language } from 'src/app/models';
import { SharedInfo } from 'src/app/models/shared-info';
import { ParametroService } from 'src/app/modules/colmircommon/services/parametro.service';
import { Utente } from 'src/app/modules/silapapi';
import { Ruolo } from 'src/app/modules/silapapi/model/ruolo';
import { PARAMETRO } from 'src/app/parametri-constants';
import { Config } from 'src/app/services/config';

import { MessageService } from 'src/app/services/message.service';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { UtilitiesService } from 'src/app/services/utilities.service';

@Component({
  selector: 'colmirwcl-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Output() readonly notifyStateSidebar = new EventEmitter<boolean>();

  lang: Language;
  languages: Language[] = [
    {langCode: 'it', locale: 'it-IT', iconCode: 'it', text: marker('LANGUAGE.ITALIAN')},
    {langCode: 'en', locale: 'en-GB', iconCode: 'gb', text: marker('LANGUAGE.ENGLISH')}
  ];

  ruolo: Ruolo;
  utente: Utente;
  stateSideBar: boolean = true;
  get CODICE_FISCALE(): string{
    if(this.ruolo){
      const idRuolo: number = this.ruolo.idSilapDRuolo;
      if(idRuolo === RUOLO.AMMINISTRATORE.ID || idRuolo === RUOLO.REGIONE.ID)
        return this.utente.codFisc;
      else
        return this.ruolo.codiceFiscaleSoggettoAbilitato;
    }
    return "";
  }

  constructor(
    private sessionStorageService: SessionStorageService,
    private localStorageService: SessionStorageService,
    private router: Router,
    private translateService: TranslateService,
    private messageService: MessageService,
    private utilitiesService: UtilitiesService,
    private parametroService: ParametroService,
    @Optional() @Inject(Config) private config: SharedInfo,
    @Inject(DOCUMENT) private document: Document
  ) { }

  ngOnInit(): void {
    this.messageService.loadMessages();
    this.parametroService.loadParametri([PARAMETRO.COD.MAILC]);
    this.utilitiesService.loadCurrentDate();
    this.translateService.setDefaultLang('it');
    this.translateService.use('it');
    this.sessionStorageService.storage$.subscribe((storage: Storage) => {
      this.ruolo = JSON.parse(storage.getItem(STORAGE_KEY.SESSION.RUOLO));
      this.utente = JSON.parse(storage.getItem(STORAGE_KEY.SESSION.UTENTE))
    });
  }

   private onChangedLanguage() {
    this.lang = this.sessionStorageService.getItem(LANGUAGE_STORAGE);
    this.translateService.use('it');
  }



  onClickGoToHomePage(){
    this.router.navigate([ROUTE.MODULE.COLMIR.ROOT,ROUTE.MODULE.COLMIR.HOME_PAGE]);
  }

  onClickGoToAccessoRuolo(){
    this.router.navigate([ROUTE.MODULE.APP.ACCESSO_RUOLO]);
  }

  onClickLogOut(){
    this.sessionStorageService.clearStorage();
    this.localStorageService.clearStorage();
    const logoutUrl = this.config.logOutUrl || '';
    this.document.location.href = logoutUrl;
  }

  openCloseSideBar() {
    this.stateSideBar = !this.stateSideBar;
    this.notifyStateSidebar.emit(this.stateSideBar);

  }

}
