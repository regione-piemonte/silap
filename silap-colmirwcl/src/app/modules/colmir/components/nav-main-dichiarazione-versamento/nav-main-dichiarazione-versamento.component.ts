/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, OnDestroy, OnInit } from '@angular/core';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { TranslateService } from '@ngx-translate/core';
import { STORAGE_KEY, WIZARD_MODE } from 'src/app/constants';
import { PromptModalService } from 'src/app/modules/colmircommon/services/prompt-modal.service';
import { Location } from '@angular/common';
import { Ccnl, Versamento, VersamentoEsoneriService, Messaggio, VersamentoProspetto, VersamentoProvincia, VersamentoSede, VersamentoStato } from 'src/app/modules/silapapi';
import { NgxSpinnerService } from 'ngx-spinner';
import { LogService } from 'src/app/services/log.service';
import { NgbNav } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { MessageService } from 'src/app/services/message.service';
import { Router } from '@angular/router';
import { MESSAGGIO } from 'src/app/msg-constants';

@Component({
  selector: 'colmirwcl-nav-main-dichiarazione-versamento',
  templateUrl: './nav-main-dichiarazione-versamento.component.html',
  styleUrls: ['./nav-main-dichiarazione-versamento.component.scss']
})
export class NavMainDichiarazioneVersamentoComponent implements OnInit, OnDestroy {

  get VIEW_MODE(){
    return this.mode === WIZARD_MODE.VIEW;
  }

  mode: string;
 // versamento: Versamento = NUOVO_VERSAMENTO;
  versamento: Versamento;
  idEsoTVersamento: number;
  active: number = 0;
  constructor(
    private translateService: TranslateService,
    private promptModalService: PromptModalService,
    private location: Location,
    private spinner: NgxSpinnerService,
    private logService: LogService,
    private sessionStorageService: SessionStorageService,
    private versamentoService: VersamentoEsoneriService,
    private alertMessageService: AlertMessageService,
    private messageService: MessageService,
    private router: Router
  ) {}


  ngOnDestroy(): void {
    this.sessionStorageService.clearItems(STORAGE_KEY.SESSION.VERSAMENTO);
    this.sessionStorageService.clearItems(STORAGE_KEY.SESSION.WIZARD_MODE);
    this.alertMessageService.emptyMessages();
  }

  ngOnInit(): void {
    this.spinner.show();
    const mod: any = this.sessionStorageService.getItem(STORAGE_KEY.SESSION.WIZARD_MODE);
    this.mode = mod.mode;
     this.sessionStorageService.storage$.subscribe((storage: Storage) => {
       this.versamento = JSON.parse(storage.getItem(STORAGE_KEY.SESSION.VERSAMENTO));
     });

    this.spinner.hide();
  }

  async onClickTornaElenco() {
    if (this.mode === WIZARD_MODE.VIEW) {
      this.location.back();
      return;
    }

    let messaggio: Messaggio = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100112);

    const title = this.translate(marker('NAV_MAIN_DICH_VER.MODAL.TITLE'));
    const message = messaggio.dsSilapDMessaggio;
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const userChoice = await this.promptModalService.openPrompt(title, message, pYes, pNo, 'warning');

    if (userChoice)
      this.location.back();
  }

  translate(key: string) {
    return this.translateService.instant(key);
  }

  conferma(event: any, nav: NgbNav) {
    if (this.active < 0)
      return;

    this.versamento = event;
    this.sessionStorageService.setItem(STORAGE_KEY.SESSION.VERSAMENTO,event);
    this.mode = WIZARD_MODE.EDIT;
    nav.select(this.active + 1);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  confermaInvio(event: Versamento){
    this.versamento = this.versamento;
    this.mode = WIZARD_MODE.VIEW;
    this.sessionStorageService.setItem(STORAGE_KEY.SESSION.WIZARD_MODE,{mode: this.mode});
    this.spinner.hide();
  }

  autorizzazione(versamantoStati: VersamentoStato[]){
    this.versamento.esoTVersamentoStatos = versamantoStati;
  }

}

const NUOVO_VERSAMENTO: Versamento = {
    annoProtocollo: undefined,
    annoRiferimento: undefined,
    codFiscale: undefined,
    codFiscaleSoggetto: undefined,
    dsDenominazioneAzienda: undefined,
    dsEmailRiferimento: undefined,
    esoTCreditoResiduo: {
      codFiscale: undefined,
      idEsoTCreditoResiduo: undefined,
      numValore: 0.00
    },
    idEsoTVersamento: undefined,
    versamentoSede: {} as VersamentoSede,
    idSilAziAnagrafica: undefined,
    nTimestamp: undefined,
    numCreditoResiduo: undefined,
    numProtocollo: undefined,
    numRate: 2,
    partivaIva: undefined,
    silapDCcnl: {} as Ccnl
}
