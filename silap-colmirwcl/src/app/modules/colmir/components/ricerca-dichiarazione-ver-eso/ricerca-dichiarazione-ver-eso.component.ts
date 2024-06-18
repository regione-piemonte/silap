/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { NgbAccordion } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { RUOLO, STATO, STORAGE_KEY, WIZARD_MODE } from 'src/app/constants';
import { PaginationDataChange } from 'src/app/modules/colmircommon/models/pagination-data-change';
import { SortEvent } from 'src/app/modules/colmircommon/models/sort-event';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { Decodifica, DecodificaService, FormRicercaVersamentoEsoneri, Messaggio, RicercaVersamentoEsoneriResponse, Versamento, VersamentoEsoneriService } from 'src/app/modules/silapapi';
import { Ruolo } from 'src/app/modules/silapapi/model/ruolo';
import { MESSAGGIO } from 'src/app/msg-constants';
import { LogService } from 'src/app/services/log.service';
import { MessageService } from 'src/app/services/message.service';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { UserService } from 'src/app/services/user.service';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-ricerca-dichiarazione-ver-eso',
  templateUrl: './ricerca-dichiarazione-ver-eso.component.html',
  styleUrls: ['./ricerca-dichiarazione-ver-eso.component.scss']
})
export class RicercaDichiarazioneVerEsoComponent implements OnInit, OnDestroy {

  @ViewChild('accordionRicerca', {static: false}) accordionRicerca: NgbAccordion;
  activeIds = ['panelRicerca'];

  ricercaEffettuata = false;
  currentPaginationData: PaginationDataChange;

  formRicerca: FormRicercaVersamentoEsoneri
  formPristine: FormRicercaVersamentoEsoneri;
  ricercaDaInviare: FormRicercaVersamentoEsoneri;
  pagedResponse: RicercaVersamentoEsoneriResponse;

  ruolo: Ruolo;

  stati: Decodifica[];

  messaggioAccordionInfo: Messaggio;

  get OPERATORE_REGIONALE(): number{
    return RUOLO.REGIONE.ID
  }

  get IS_OPERATORE_REGIONALE(): boolean{
    return this.ruolo.idSilapDRuolo === RUOLO.REGIONE.ID;
  }

  constructor(
    private userService: UserService,
    private router: Router,
    private spinner: NgxSpinnerService,
    private logService: LogService,
    private activatedRoute: ActivatedRoute,
    private messageService: MessageService,
    private versamentoEsoneriService: VersamentoEsoneriService,
    private alertMessageService: AlertMessageService,
    private readonly route: ActivatedRoute,
    private versamentoService: VersamentoEsoneriService,
    private sessionStorageService: SessionStorageService,
    private decodificaService: DecodificaService
  ) { }

  ngOnDestroy(): void {
    this.alertMessageService.emptyMessages();
  }

  ngOnInit(): void {
    this.spinner.show();
    this.loadStati();
    this.logService.info(this.constructor.name,'ngOnInit');
    this.ruolo = this.userService.getRuoloSessione();
    let codMessaggio = MESSAGGIO.COD_100007;
    if(this.ruolo.idSilapDRuolo === RUOLO.REGIONE.ID)
      codMessaggio = MESSAGGIO.COD_100106;
    this.messaggioAccordionInfo = this.messageService.getMessaggioByCod(codMessaggio);
    this.currentPaginationData = {
      limit: this.activatedRoute.snapshot.params['limit'] || 20,
      page: this.activatedRoute.snapshot.params['page'] || 0,
      offset: 0,
      sort: this.activatedRoute.snapshot.params['sort']
    };
    this.initForm();
    this.patchFormParams(this.activatedRoute.snapshot.params);
    this.spinner.hide();
  }

  private initForm(){
    const now: Date = new Date();
    let codFisc: string = null;
    let denom: string = null;
    if(
        this.ruolo.idSilapDRuolo === RUOLO.LEGALE_RAPPRESENTANTE.ID ||
        this.ruolo.idSilapDRuolo === RUOLO.DELEGATO.ID ||
        this.ruolo.idSilapDRuolo === RUOLO.PERSONA_AUTORIZZATA.ID
      ){
        codFisc = this.ruolo.codiceFiscaleSoggettoAbilitato;
        denom = this.ruolo.denominazioneSoggettoAbilitato;
      }
    now.setHours(0,0,0,0);
    this.formPristine = {
      codFisc: codFisc,
      denomAzienda: denom,
      dataInizio: new Date(now.setFullYear(now.getFullYear() - 1)),
      dataFine: new Date(),
      statoDichiarazione: [],
      annoProtocollo: null,
      numProtocollo: null,
      flgDueRate: null,
      flgUnicaSoluzione: null
    }
    this.formRicerca = this.formPristine;
  }

  private loadStati() {
    this.decodificaService.findfindDStatoVersamento().subscribe({
      next: (res: any) => {
        if(res.esitoPositivo){
          this.stati = res.list;
        }
      },
      error: (err) => {
        console.error(`loadStati: ${JSON.stringify(err)}`);
      }
    });
  }

  patchFormParams(params: any) {

    if (!params.goSearch) {
      return;
    }

    this.formRicerca = params;
  }

  onCerca(formRicerca: any){
    this.ricercaEffettuata = false;
    this.logService.info(this.constructor.name, 'onCerca', this.formRicerca);
    this.ricercaDaInviare = Utils.clone(formRicerca);
    this.effettuaRicerca(this.currentPaginationData.page, this.currentPaginationData.limit);
  }

  private effettuaRicerca(page: number, limit: number, sort?: SortEvent, sortMsg?: Boolean) {

    if (!sortMsg) {
      this.alertMessageService.emptyMessages();
    }
    this.spinner.show();
    this.logService.info(this.constructor.name,`effettuaRicerca: page:${page}, limit: ${limit}`);
    this.versamentoEsoneriService.ricercaVersamentoEsoneri(
      page,
      this.ricercaDaInviare
    ).subscribe({
      next: (res) =>{
         if(res && res.esitoPositivo){
           this.pagedResponse = res;
           if(this.pagedResponse.list.length > 0){
             this.setDisposition(page, limit);
           }else{
             this.alertMessageService.setSingleWarningMessage('Nessun risultato trovato');
           }
         }

         this.spinner.hide();
       },
       error: (error) =>{
         this.logService.error(this.constructor.name,`errore: ${error}`);
         this.spinner.hide();
       }
    });
    this.logService.info(this.constructor.name,`effettuaRicerca: response ${JSON.stringify(this.pagedResponse)}`);
  }

  onChangePaginationData(paginationData: PaginationDataChange) {
    this.effettuaRicerca(paginationData.page, paginationData.limit, paginationData.sort);
  }

  private setDisposition(page: number, limit: number){
    this.ricercaEffettuata = true;
    this.accordionRicerca.collapseAll();
    this.router.navigate(
      [
        this.clearObject({
          codFisc: this.ricercaDaInviare.codFisc,
          dataFine: this.ricercaDaInviare.dataFine.toISOString(),
          dataInizio: this.ricercaDaInviare.dataInizio.toISOString(),
          denomAzienda: this.ricercaDaInviare.denomAzienda,
          numProtocollo: this.ricercaDaInviare.numProtocollo,
          statoDichiarazione: JSON.stringify(this.ricercaDaInviare.statoDichiarazione),
          flgDueRate: this.ricercaDaInviare.flgDueRate,
          flgUnicaSoluzione: this.ricercaDaInviare.flgUnicaSoluzione,
          page,
          goSearch: true,
          limit
        })
      ],
      {
          relativeTo: this.activatedRoute,
          // NOTE: By using the replaceUrl option, we don't increase the Browser's
          // history depth with every filtering keystroke. This way, the List-View
          // remains a single item in the Browser's history, which allows the back
          // button to function much more naturally for the user.
          replaceUrl: true
      }
    );
  }

  onResetForm(){
    this.alertMessageService.emptyMessages();
    this.ricercaEffettuata = false;
    this.currentPaginationData = {
      limit: 20,
      page: 0,
      offset: 0,
      sort: this.activatedRoute.snapshot.params['sort']
    };
    this.router.navigate(
      [
        this.clearObject({})
      ],
      {
        relativeTo: this.activatedRoute,
        replaceUrl: true
      }
    );
  }

  private clearObject<T>(obj: T): T {
    const res = {} as T;
    Object.keys(obj)
      .filter(key => obj[key] !== null && obj[key] !== undefined)
      .forEach(key => res[key] = obj[key]);

    return res;
  }

  onClickGoToNavMain(){
    this.spinner.show();
    const obj: any = {
      mode: 'ins'
    }
    this.sessionStorageService.setItem(STORAGE_KEY.SESSION.WIZARD_MODE,obj);
    this.versamentoService.getNuovoVersamento().subscribe({
      next: (res: any) => {
        console.log(`onClickModifica: ${JSON.stringify(res)}`);
        if(res.esitoPositivo){
          const versamentoResponse: Versamento = res.versamento;
          this.sessionStorageService.setItem(STORAGE_KEY.SESSION.VERSAMENTO,versamentoResponse);
          this.router.navigate(['nav-main'],{relativeTo: this.route.parent});

        }else{
          this.alertMessageService.setApiMessages(res.apiMessages);
        }
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore onClickModifica: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
    this.router.navigate(['nav-main'],
      {
        relativeTo: this.route.parent,
      });

  }

  eliminaDichiarazione(paginationData: PaginationDataChange){
    if(
      this.pagedResponse && this.pagedResponse.totalResult === 1
    ){
      this.ricercaEffettuata = false;
    }
    this.effettuaRicerca(paginationData.page, paginationData.limit, null, true);
  }

}
