/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { DOWNLOAD_FILE_NAME, RUOLO, STATO, STORAGE_KEY, WIZARD_MODE } from 'src/app/constants';
import { PaginationDataChange } from 'src/app/modules/colmircommon/models/pagination-data-change';
import { PromptModalService } from 'src/app/modules/colmircommon/services/prompt-modal.service';
import { ApiMessage, FormRicercaVersamentoEsoneri, Messaggio, RicercaVersamentoEsoneriResponse, Ruolo, Versamento, VersamentoEsoneriRidotto, VersamentoEsoneriService } from 'src/app/modules/silapapi';
import { CustomVersamentoEsoneriService } from 'src/app/services/custom-versamento-esoneri.service';
import { LogService } from 'src/app/services/log.service';
import { UserService } from 'src/app/services/user.service';
import { UtilitiesService } from 'src/app/services/utilities.service';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { MessageService } from 'src/app/services/message.service';
import { MESSAGGIO } from 'src/app/msg-constants';
import { Utils } from 'src/app/utils';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';


@Component({
  selector: 'colmirwcl-risultati-ricerca-dichiarazione-ver-eso',
  templateUrl: './risultati-ricerca-dichiarazione-ver-eso.component.html',
  styleUrls: ['./risultati-ricerca-dichiarazione-ver-eso.component.scss']
})
export class RisultatiRicercaDichiarazioneVerEsoComponent implements OnInit {

  @Input() currentPaginationData: PaginationDataChange;
  @Output() readonly changePaginationData = new EventEmitter<PaginationDataChange>();
  @Output() readonly eliminaDichiarazioneNotify = new EventEmitter<PaginationDataChange>();

  @Input() pagedResponse: RicercaVersamentoEsoneriResponse;
  @Input() formRicerca: FormRicercaVersamentoEsoneri;

  ruolo: Ruolo;
  mode: string;

  constructor(
    private logService: LogService,
    private spinner: NgxSpinnerService,
    private userService: UserService,
    private customVersamentoEsoneriService: CustomVersamentoEsoneriService,
    private utilitiesService: UtilitiesService,
    private translateService: TranslateService,
    private promptModalService: PromptModalService,
    private alertMessageService: AlertMessageService,
    private versamentoEsoneriService: VersamentoEsoneriService,
    private messageService: MessageService,
    private sessionStorageService: SessionStorageService,
    private router: Router,
    private readonly route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.logService.info(this.constructor.name, "ngOnInit");
    this.ruolo = this.userService.getRuoloSessione();
  }


  onChangePaginationData(event: PaginationDataChange) {
    this.currentPaginationData = event;
    this.changePaginationData.emit(event);
  }

  showModifica(el: VersamentoEsoneriRidotto): boolean {
    return el.idEsoDVersamentoStato === STATO.BOZZA.ID
  }
  showElimina(el: VersamentoEsoneriRidotto): boolean {
    return el.idEsoDVersamentoStato === STATO.BOZZA.ID && this.ruolo.idSilapDRuolo !== RUOLO.REGIONE.ID;
  }
  showAnnulla(el: VersamentoEsoneriRidotto): boolean {
    if (this.ruolo.idSilapDRuolo === RUOLO.REGIONE.ID) {
      return el.idEsoDVersamentoStato === STATO.AUTORIZZATA.ID;
    } else {
      return el.idEsoDVersamentoStato === STATO.MODIFICATA.ID || el.idEsoDVersamentoStato === STATO.ACCETTATA.ID;
    }
  }
  showStampa(el: VersamentoEsoneriRidotto): boolean {
    return el.idEsoDVersamentoStato !== STATO.ANNULLATA.ID && el.idEsoDVersamentoStato !== STATO.ELIMINATA.ID;
  }

  showVisualizzaAvvisoPagamento(el: VersamentoEsoneriRidotto): boolean{
    return el.idEsoDVersamentoStato === STATO.AVVISO_INVIATO.ID || el.idEsoDVersamentoStato === STATO.ACCONTO.ID || el.idEsoDVersamentoStato === STATO.SALDO.ID;
  }

  showVerificaStatoPagamento(el: VersamentoEsoneriRidotto): boolean{
    return el.idEsoDVersamentoStato === STATO.AVVISO_INVIATO.ID || el.idEsoDVersamentoStato === STATO.ACCONTO.ID;
  }

  onClickStampa(idEsoTVersamento: number) {
    this.spinner.show();
    this.customVersamentoEsoneriService.stampaVersamento(idEsoTVersamento, 'response').subscribe({
      next: (res: any) => {
        this.utilitiesService.downloadBlobFile(`${DOWNLOAD_FILE_NAME.DICHIARAZIONE_VERSAMENTO}.pdf`, res.body);
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore onClickEsportaFile: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }
  onClickEsportaFile(format: string) {
    this.spinner.show();
    this.customVersamentoEsoneriService.stampaRicercaVersamentoEsoneri(this.formRicerca, format, 'response').subscribe({
      next: (res: any) => {
        console.log(`res esporta file: ${JSON.stringify(res)}`)
        if (format === 'xls')
          format = 'xlsx';
        this.utilitiesService.downloadBlobFile(`${DOWNLOAD_FILE_NAME.RICERCA_VE}.${format}`, res.body);
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore onClickEsportaFile: ${JSON.stringify(error)}`);
        //const fileName = UtilsSilpLibWcl.extractFileNameFromContentDisposition(error.headers.get('Content-Disposition'));
        //this.utilitiesService.downloadBlobFile('prova', error.error.text);
        ////const url= window.URL.createObjectURL(error.error.text);
        //window.open(url);
        this.spinner.hide();
      }
    });
  }

  async onClickEliminaAnnulla(el: VersamentoEsoneriRidotto, flgElimina: boolean) {
    const title = flgElimina ? this.translate(marker('MODAL.TITLE.ELIMINA_DICH')) : this.translate(marker('MODAL.TITLE.ANNULLA_DICH'));
    const message = flgElimina ? this.getMessageByCodDecodificato(el, MESSAGGIO.COD_100022) : this.getMessageByCodDecodificato(el, MESSAGGIO.COD_100023);
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const userChoice = await this.promptModalService.openPrompt(title, message, pYes, pNo, 'warning');

    if (userChoice) {
      const idStato: number = flgElimina ? STATO.ELIMINATA.ID : STATO.ANNULLATA.ID;
      this.updateStato(el, idStato, flgElimina);
    }
  }

  translate(key: string) {
    return this.translateService.instant(key);
  }

  getMessageByCodDecodificato(el: VersamentoEsoneriRidotto, codMessaggio: string) {
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(codMessaggio);
    const mapParams: any = {
      0: el.annoRiferimento,
      1: el.dsDenominazioneAzienda,
      2: el.codFiscale
    };
    const message = Utils.replacePlaceHolder(messaggio.dsSilapDMessaggio, mapParams);
    return message;
  }



  updateStato(el: VersamentoEsoneriRidotto, idStato: number, flgElimina: boolean) {
    this.alertMessageService.emptyMessages();
    this.spinner.show();
    const message = flgElimina ? this.getMessageByCodDecodificato(el, MESSAGGIO.COD_100045) : this.getMessageByCodDecodificato(el, MESSAGGIO.COD_100044);
    this.versamentoEsoneriService.updateStatoVersamento(el.idEsoTVersamento, idStato).subscribe({
      next: (res: any) => {
        this.logService.info(this.constructor.name, `updateStatoVersamento res: ${JSON.stringify(res)}`);
        if (res.esitoPositivo) {
          if (flgElimina) {
            this.alertMessageService.setSingleSuccessMessage(message);
          } else {
            this.alertMessageService.setApiMessages(res.apiMessages);
          }
          this.eliminaDichiarazioneNotify.emit(this.currentPaginationData);
        } else {
          const messages: ApiMessage[] = res.apiMessages;
          this.alertMessageService.setApiMessages(messages);
        }
        this.spinner.hide();
      },
      error: (error) => {
        this.logService.error(this.constructor.name, `updateStatoVersamento error: ${JSON.stringify(error)}`)
        this.spinner.hide();
      }
    });
  }

  onClickGoToNavMain(idEsoTVersamento: number, mod: string) {
    this.alertMessageService.emptyMessages();
    this.spinner.show();
    const obj: any = {
      mode: mod
    }
    const request$: Observable<Versamento> = mod === WIZARD_MODE.VIEW ? this.versamentoEsoneriService.getVersamento(idEsoTVersamento) : this.versamentoEsoneriService.modificaVersamento(idEsoTVersamento);

    request$.subscribe({
      next: (res: any) => {
        console.log(`onClickModifica: ${JSON.stringify(res)}`);
        if (res.esitoPositivo) {
          this.sessionStorageService.setItem(STORAGE_KEY.SESSION.WIZARD_MODE, obj);
          const versamentoResponse: Versamento = res.versamento;
          this.sessionStorageService.setItem(STORAGE_KEY.SESSION.VERSAMENTO, versamentoResponse);
          this.router.navigate(['nav-main'],
            {
              relativeTo: this.route.parent
            });

        } else {
          this.alertMessageService.setApiMessages(res.apiMessages);
        }
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore onClickModifica: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }


  onClickModifica(idEsoDVersamentoStato: number) {
    this.versamentoEsoneriService.modificaVersamento(idEsoDVersamentoStato).subscribe({
      next: (res: any) => {
        console.log(`onClickModifica: ${JSON.stringify(res)}`)
        if (res.esitoPositivo) {
          const versamento: Versamento = res.versamento;
        } else {
          this.alertMessageService.setApiMessages(res.apiMessages);
        }
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore onClickModifica: ${JSON.stringify(error)}`);
        //const fileName = UtilsSilpLibWcl.extractFileNameFromContentDisposition(error.headers.get('Content-Disposition'));
        //this.utilitiesService.downloadBlobFile('prova', error.error.text);
        ////const url= window.URL.createObjectURL(error.error.text);
        //window.open(url);
        this.spinner.hide();
      }
    });
  }

  onClickStampaAvvisoPagamento(idEsoTVersamento: number){
    this.spinner.show();
    this.customVersamentoEsoneriService.getPdfAvvisoPagamento(String(idEsoTVersamento),'response').subscribe({
      next: (res: any) => {
        console.log(`res esporta file: ${JSON.stringify(res)}`)
        this.utilitiesService.downloadBlobFile(`${DOWNLOAD_FILE_NAME.AVVISO_PAGAMENTO}.pdf`, res.body);
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore onClickEsportaFile: ${JSON.stringify(error)}`);
        //const fileName = UtilsSilpLibWcl.extractFileNameFromContentDisposition(error.headers.get('Content-Disposition'));
        //this.utilitiesService.downloadBlobFile('prova', error.error.text);
        ////const url= window.URL.createObjectURL(error.error.text);
        //window.open(url);
        this.spinner.hide();
      }
    });

  }

  onClickVerificaStatoPagamento(idEsoTVersamento: number){
    this.spinner.show();
    this.versamentoEsoneriService.getPosizioneDebitoria(String(idEsoTVersamento)).subscribe({
      next: (res: any) => {
        if(res.esitoPositivo)
          this.openModalEsito(res.msg);
        else
          this.alertMessageService.setApiErrors(res.apiMessages);
      },
      error: (err)=>{
        this.spinner.hide();
        this.logService.error(this.constructor.name,'getPosizioneDebitoria',err);
      }
    });
  }

  openModalEsito(messaggio: Messaggio){
    this.spinner.hide();
    const title = messaggio.dsTitolo;
    const message = messaggio.dsSilapDMessaggio
    const pNo = this.translate(marker('APP.GENERICO.BTN.ANNULLA'));

    this.promptModalService.openPromptInfo(title, message, pNo, 'info');
  }


}
