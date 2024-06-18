/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
import { Component, OnInit, Input, Optional, Inject } from '@angular/core';
import { LeftSidebarService } from '../services/left-sidebar.service';
import { LeftSideBarTemplate } from '../models/left-side-bar-template';
import { Messaggio, Ruolo, RuoloFunzione, VersamentoEsoneriService } from '../modules/silapapi';
import { Utils } from '../utils';
import { AMBIENTE, FUNZIONI, STORAGE_KEY } from '../constants';
import { UserService } from '../services/user.service';
import { SessionStorageService } from '../services/storage/session-storage.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { AlertMessageService } from '../modules/colmircommon/services/alert-message.service';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { MessageService } from '../services/message.service';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { PromptModalService } from '../modules/colmircommon/services/prompt-modal.service';
import { Config } from '../services/config';
import { SharedInfo } from '../models/shared-info';





@Component({
  selector: 'colmirwcl-left-sidebar',
  templateUrl: './left-sidebar.component.html',
  styleUrls: ['./left-sidebar.component.scss']
})
export class LeftSidebarComponent implements OnInit {

  close: boolean = false;
  @Input() set state(state: boolean) {
    this.close = state;
    this.leftSideBarService.setCloseSideBar(this.close);
  }

  template: LeftSideBarTemplate[];

  ruoloFunzioni: RuoloFunzione[];
  ruolo: Ruolo;
  appName: string;

  constructor(
    private leftSideBarService: LeftSidebarService,
    private router: Router,
    private userService: UserService,
    private sessionStorageService: SessionStorageService,
    private versamentoEsnoreiService: VersamentoEsoneriService,
    private spinner: NgxSpinnerService,
    private alertMessageService: AlertMessageService,
    private messageService: MessageService,
    private translateService: TranslateService,
    private promptModalService: PromptModalService,
    @Optional() @Inject(Config) private config: SharedInfo
  ) {
  }


  ngAfterViewInit(): void {
    this.ruolo = this.userService.getRuoloSessione();
    this.ruoloFunzioni = this.ruolo ?  Utils.clone(this.ruolo.ruoloFunzioni) : undefined;
  }

  ngOnInit(): void {
    this.leftSideBarService.setCloseSideBar(this.close);
    this.sessionStorageService.storage$.subscribe((storage: Storage) => {
      this.ruolo = JSON.parse(storage.getItem(STORAGE_KEY.SESSION.RUOLO));
      this.ruoloFunzioni = this.ruolo ? Utils.clone(this.ruolo.ruoloFunzioni) : undefined;
    });
  }

  setClass() {
    this.close = !this.close;
    this.leftSideBarService.setCloseSideBar(this.close);
  }

  get DICHIARAZIONE_VERSAMENTO_ESONERI_ID(): number{
    return FUNZIONI.DICHIARAZIONE_VERSAMENTO_ESONERI.ID;
  }

  get AUTORIZZA_DICHIARAZIONE(): number{
    return FUNZIONI.AUTORIZZA_DICHIARAZIONI.ID;
  }

  get PREVISIONE_DICHIARAZIONE(): number{
    return FUNZIONI.PREVISIONE_DICHIARAZIONI.ID;
  }

  get GENERA_AVVISI_DI_PAGAMENTO_ID(): number{
    return FUNZIONI.GENERA_AVVISI_PAGAMENTO.ID;
  }

  onCLickLink(id: number){
    if(id === FUNZIONI.DICHIARAZIONE_VERSAMENTO_ESONERI.ID){
      this.router.navigate(['colmir','ricerca-versamento-esoneri']);
    } else if(id === FUNZIONI.AUTORIZZA_DICHIARAZIONI.ID){
      this.openModalConfermaAutorizza();
    }else if(id === FUNZIONI.PREVISIONE_DICHIARAZIONI.ID){
      this.router.navigate(['colmir','previsione-dichiarazione']);
    }
    if(id === FUNZIONI.GENERA_AVVISI_PAGAMENTO.ID){
      this.spinner.show();
      this.checkPosizioniDebitorieBatch();
    }
  }

  private checkPosizioniDebitorieBatch() {
    this.versamentoEsnoreiService.checkPosizioniDebitorieBatch().subscribe({
      next: (res: any) => {
        this.spinner.hide();
        if(res.esitoPositivo){
          this.openModalConfermaGenerazioneAvvisi(res.apiMessages[0].message);
        }
        else
          this.alertMessageService.setApiMessages(res.apiMessages);
      },
      error: (error) => {
        console.error(this.constructor.name, `errore autorizzaDichiarazioniBatch: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }


  async openModalConfermaAutorizza() {
    const title = this.translate(marker('AUTORIZZA.MODAL.TITLE'));
    const msg: Messaggio = this.messageService.getMessaggioByCod('100102');
    const message = msg.dsSilapDMessaggio;
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const userChoice = await this.promptModalService.openPrompt(title, message, pYes, pNo, 'warning');

    if (userChoice){
        this.autorizzaDichiarazione();
    }
  }

  async openModalConfermaGenerazioneAvvisi(msg: string) {
    const title = this.translate(marker('GENERA_AVVISO.MODAL.TITLE'));
    const message = msg;
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const userChoice = await this.promptModalService.openPrompt(title, message, pYes, pNo, 'warning');

    if (userChoice){
        this.creaPosizioneDebitoria();
    }
  }

  private creaPosizioneDebitoria() {
    this.versamentoEsnoreiService.creazionePosizioniDebitorieBatch().subscribe({
      next: (res: any) => {
        if(res.esitoPositivo)
          this.alertMessageService.setSingleSuccessMessage(res.msg.dsSilapDMessaggio);
        else
          this.alertMessageService.setApiMessages(res.apiMessages);
        this.spinner.hide();
      },
      error: (error) => {
        console.error(this.constructor.name, `errore autorizzaDichiarazioniBatch: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }



  private translate(key: string) {
    return this.translateService.instant(key);
  }


  private autorizzaDichiarazione() {
    this.spinner.show();
    this.versamentoEsnoreiService.autorizzaDichiarazioniBatch().subscribe({
      next: (res: any) => {
        if(res.esitoPositivo)
          this.alertMessageService.setSingleSuccessMessage(res.msg.dsSilapDMessaggio);
        else
          this.alertMessageService.setApiMessages(res.apiMessages);
        this.spinner.hide();
      },
      error: (error) => {
        console.error(this.constructor.name, `errore autorizzaDichiarazioniBatch: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }



}






