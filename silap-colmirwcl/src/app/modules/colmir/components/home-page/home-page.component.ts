/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, Inject, OnDestroy, OnInit, Optional } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { AMBIENTE, FUNZIONI, ICONA_BASE, ROUTE } from 'src/app/constants';
import { SharedInfo } from 'src/app/models/shared-info';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { PromptModalService } from 'src/app/modules/colmircommon/services/prompt-modal.service';
import { Funzione, Messaggio, RuoloFunzione, VersamentoEsoneriService } from 'src/app/modules/silapapi';
import { Ruolo } from 'src/app/modules/silapapi/model/ruolo';
import { Config } from 'src/app/services/config';
import { LeftSidebarService } from 'src/app/services/left-sidebar.service';
import { MessageService } from 'src/app/services/message.service';
import { UserService } from 'src/app/services/user.service';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent implements OnInit,OnDestroy {


  ruolo: Ruolo;
  templateMenu: TemplateMenu;



  get DICHIARAZIONE_VERSAMENTO_ESONERI_ID(): number{
    return FUNZIONI.DICHIARAZIONE_VERSAMENTO_ESONERI.ID;
  }

  get AUTORIZZA_DICHIARAZIONI_ID(): number{
    return FUNZIONI.AUTORIZZA_DICHIARAZIONI.ID;
  }

  get PREVISIONE_DICHIARAZIONI_ID(): number{
    return FUNZIONI.PREVISIONE_DICHIARAZIONI.ID;
  }

  get GENERA_AVVISI_DI_PAGAMENTO_ID(): number{
    return FUNZIONI.GENERA_AVVISI_PAGAMENTO.ID;
  }

  constructor(
    private userService: UserService,
    private router: Router,
    private readonly route: ActivatedRoute,
    private leftSideBarService: LeftSidebarService,
    private versamentoEsnoreiService: VersamentoEsoneriService,
    private spinner: NgxSpinnerService,
    private alertMessageService: AlertMessageService,
    private messageService: MessageService,
    private promptModalService: PromptModalService,
    private translateService: TranslateService,
    @Optional() @Inject(Config) private config: SharedInfo
  ) {}


  ngOnDestroy(): void {
    this.alertMessageService.emptyMessages();
  }

  ngOnInit(): void {
    this.ruolo = this.userService.getRuoloSessione();
    this.leftSideBarService.showLeftSideBar();
    if(Utils.isNullOrUndefined(this.ruolo)){
      this.router.navigate([ this.router.navigate([ROUTE.MODULE.APP.ACCESSO_RUOLO])]);
    }

    this.buildMenu();
  }

  private buildMenu() {

    const ruoloFunzioi: RuoloFunzione[] = this.ruolo.ruoloFunzioni;
    let templateMenuTmp: TemplateMenu = [];
    ruoloFunzioi.forEach((rf: RuoloFunzione) => {
      let idFunzione = rf.funzione.idSilapDFunzione;
      if(idFunzione === FUNZIONI.DICHIARAZIONE_VERSAMENTO_ESONERI.ID){
        templateMenuTmp[idFunzione] = {...rf.funzione}
      }
      if(idFunzione === FUNZIONI.AUTORIZZA_DICHIARAZIONI.ID){
        templateMenuTmp[idFunzione] = {...rf.funzione}
      }
      if(idFunzione === FUNZIONI.PREVISIONE_DICHIARAZIONI.ID){
        templateMenuTmp[idFunzione] = {...rf.funzione}
      }if(idFunzione === FUNZIONI.GENERA_AVVISI_PAGAMENTO.ID){
        templateMenuTmp[idFunzione] = {...rf.funzione}
      }
    });
    this.templateMenu = {...templateMenuTmp};
  }

  getIconByIdFunzione(idFunzione: number): string{
    return this.templateMenu[idFunzione] ? this.templateMenu[idFunzione].iconaSilapDFunzione : ICONA_BASE;
  }

  goToFunzioneById(id: number){
    if(id === FUNZIONI.DICHIARAZIONE_VERSAMENTO_ESONERI.ID){
      this.router.navigate(['ricerca-versamento-esoneri'],
        {
          relativeTo: this.route.parent
        }
      );
    } else if(id === FUNZIONI.AUTORIZZA_DICHIARAZIONI.ID){
      this.openModalConfermaAutorizza();
    }else if(id === FUNZIONI.PREVISIONE_DICHIARAZIONI.ID){
      this.router.navigate(['previsione-dichiarazione'],
        {
          relativeTo: this.route.parent
        }
      );
    }else if(id === FUNZIONI.GENERA_AVVISI_PAGAMENTO.ID){
      this.spinner.show();
      this.checkPosizioniDebitorieBatch()
    }
  }


  private checkPosizioniDebitorieBatch() {
    this.alertMessageService.emptyMessages();
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


  

  private autorizzaDichiarazione() {
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

  private translate(key: string) {
    return this.translateService.instant(key);
  }

  getTitleByIdFunzione(idFunzione: number): string{
    if(this.templateMenu[idFunzione])
      return this.templateMenu[idFunzione].dsSilapDFunzione;
    return '';
  }

  getSubTitleByIdFunzione(idFunzione: number): string{
    if(this.templateMenu[idFunzione])
      return this.templateMenu[idFunzione].noteSilapDFunzione;
    return '';
  }

}

interface TemplateMenu {
  [key: number]: Funzione;
}
