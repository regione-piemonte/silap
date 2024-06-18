/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { RUOLO, STATO, STORAGE_KEY, TYPE_ALERT, WIZARD_MODE } from 'src/app/constants';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { PromptModalService } from 'src/app/modules/colmircommon/services/prompt-modal.service';
import { Azienda, DVersamentoStato, Messaggio, PosizioneDebitoria, Ruolo, Versamento, VersamentoEsoneriService, VersamentoProvincia, VersamentoPvPeriodo, VersamentoStato } from 'src/app/modules/silapapi';
import { MESSAGGIO } from 'src/app/msg-constants';
import { LogService } from 'src/app/services/log.service';
import { MessageService } from 'src/app/services/message.service';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { UtilitiesService } from 'src/app/services/utilities.service';
import { Utils } from 'src/app/utils';


@Component({
  selector: 'colmirwcl-visualizza-dichiarazione-versamento',
  templateUrl: './visualizza-dichiarazione-versamento.component.html',
  styleUrls: ['./visualizza-dichiarazione-versamento.component.scss']
})
export class VisualizzaDichiarazioneVersamentoComponent implements OnInit {

  @Input() mode: string;
  @Input() initialVersamento: Versamento;

  @Output() emitConfermainvio = new EventEmitter<Versamento>();
  @Output() emitAutorizzazione = new EventEmitter<VersamentoStato[]>();

  form: FormGroup;
  aziendaCorrente: Azienda;
  versamentoCorrente: Versamento;

  ruolo: Ruolo;

  currentDate: Date;

  param: string;
  showLists: boolean;

  get VIEW_MODE(): boolean{
    return this.mode === WIZARD_MODE.VIEW;
  }

  get SHOW_DATI_PAGAMENTO(): boolean{
    const statoCOrrente = this.getStatoCorrente();
    return statoCOrrente && (statoCOrrente.esoDVersamentoStato.id === STATO.ACCONTO.ID || 
                               statoCOrrente.esoDVersamentoStato.id === STATO.SALDO.ID || 
                               statoCOrrente.esoDVersamentoStato.id === STATO.AVVISO_INVIATO.ID);
  }

  get SHOW_BTN_INVIO(): boolean{
    return this.mode !== WIZARD_MODE.VIEW && this.ruolo.idSilapDRuolo !== RUOLO.REGIONE.ID;
  }

  get f() {
    return this.form.controls as any;
  }

  get SEZIONE_AUTORIZZA(): boolean {
    let autorizza: boolean = false;
    if(this.mode === WIZARD_MODE.VIEW){

      if(
        this.ruolo.idSilapDRuolo === RUOLO.REGIONE.ID ||
        this.ruolo.idSilapDRuolo === RUOLO.AMMINISTRATORE.ID
      ){
        const versamantoStati: VersamentoStato[] = this.versamentoCorrente.esoTVersamentoStatos;
        for(let i = 0; i < versamantoStati.length && !autorizza; i++){
          autorizza = versamantoStati[i].flgCurrentRecord === 'S' && versamantoStati[i].esoDVersamentoStato.id === STATO.MODIFICATA.ID;
        }
      }
    }
    return autorizza;
  }

  messaggioAccordionInfo: Messaggio;
  dichiarazioneVersamenti: DichiarazioneVersamentoModel[];

  totaleImporto: string;

  esoTPosizioneDebitorias: PosizioneDebitoria[];


  constructor(
    private sessionStorageService: SessionStorageService,
    private translateService: TranslateService,
    private messageService: MessageService,
    private promptModalService: PromptModalService,
    private versamentoEsoneriService: VersamentoEsoneriService,
    private utilitiesService: UtilitiesService,
    private alertMessageService: AlertMessageService,
    private datePipe: DatePipe,
    private decimalPipe: DecimalPipe,
    private spinner: NgxSpinnerService,
    private logService: LogService
  ) { }

  ngOnInit(): void {
    this.messaggioAccordionInfo = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100001);
    this.ruolo = this.sessionStorageService.getItem(STORAGE_KEY.SESSION.RUOLO);
    this.versamentoCorrente = Utils.clone(this.initialVersamento);
    if(this.SEZIONE_AUTORIZZA)
      this.initForm();
    this.currentDate = new Date(this.utilitiesService.getCurrentDate());
    this.buildWrapperLists();
    if(this.versamentoCorrente.esoTPosizioneDebitorias && 
      this.versamentoCorrente.esoTPosizioneDebitorias.length > 1){
        this.versamentoCorrente.esoTPosizioneDebitorias.sort((pos1,pos2) =>{
        return pos1.numRata - pos2.numRata;
      });
    }
    window.scrollTo({ top: 0, behavior: 'smooth' });
    this.spinner.hide();
  }
  private buildWrapperLists() {
    const versamentoProvince: VersamentoProvincia[] = this.versamentoCorrente.esoTVersamentoProvincias;
    if(versamentoProvince && versamentoProvince.length > 0){
      this.dichiarazioneVersamenti = [];
      let totale: number = 0.00;
      versamentoProvince.forEach((verProv: VersamentoProvincia) => {
        const provincia: string = verProv.silapDProvincia.dsSilapDProvincia;
        let numGiorniLavorativi: number = 0;
        let numLavoratoriEsonerati: number = 0;
        let importo: number = 0.00;
        verProv.esoTVersamentoPvPeriodos.forEach((periodo: VersamentoPvPeriodo) => {
          if (periodo.flgTipo != 'C') {
            numGiorniLavorativi = numGiorniLavorativi + periodo.numGgLavorativi;
            numLavoratoriEsonerati = numLavoratoriEsonerati + periodo.numLavoratoriEsonerati;
            importo = importo + periodo.importo;
          }
        });
        this.dichiarazioneVersamenti.push({
          provincia: provincia,
          giorniLavorativi: numGiorniLavorativi,
          numLavEsonerati: numLavoratoriEsonerati,
          importo: importo
        });
        totale = totale + importo;
      });
      const creditoResiduoFormatted: string = this.decimalPipe.transform(this.versamentoCorrente.numCreditoResiduo,'0.2-2');
      const totaleMenoCreditoResiduo: number = totale - this.versamentoCorrente.numCreditoResiduo;
      this.totaleImporto = `${this.decimalPipe.transform(totale,'0.2-2')} - ${creditoResiduoFormatted} = ${ this.decimalPipe.transform((totaleMenoCreditoResiduo < 0 ? 0 : totaleMenoCreditoResiduo),'0.2-2') }`;
      this.versamentoCorrente.esoTVersamentoStatos.sort((s1: VersamentoStato,s2: VersamentoStato) => {
        let dataS1: Date = new Date(s1.dtStato);
        let dataS2: Date = new Date(s2.dtStato);
        dataS1.setHours(0,0,0,0);
        dataS2.setHours(0,0,0,0);
        return dataS2.getTime() - dataS1.getTime();
      });
      this.showLists = true
    }
  }

  private initForm() {
    this.form = new FormGroup({
      idDstato: new FormControl(STATO.AUTORIZZATA.ID,Validators.required),
      dsNote: new FormControl(null, Validators.required)
    });
  }

  async onClickAutorizza(){
    const idStato: number = this.f.idDstato.value;
    let messaggio: Messaggio = idStato === STATO.AUTORIZZATA.ID ? this.messageService.getMessaggioByCod(MESSAGGIO.COD_100029) : this.messageService.getMessaggioByCod(MESSAGGIO.COD_100030);

    const title = this.translate(marker('RIEPILOGO.MODAL.TITLE.AUTORIZZA'));

    let mapParams= {
      0: String(this.versamentoCorrente.annoRiferimento),
      1: this.versamentoCorrente.dsDenominazioneAzienda,
      2: this.versamentoCorrente.codFiscale,
      3: this.datePipe.transform(this.currentDate, 'dd/MM/yyyy')
    }

    const annoCorrente: number = this.currentDate.getFullYear();
    if(idStato === STATO.AUTORIZZATA.ID){
      //passo 9
      const differenzaAnni: number = annoCorrente - (this.initialVersamento.annoRiferimento + 1);
      if(differenzaAnni){
        messaggio = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100031);

        if(messaggio.silapDTipoMessaggio.idSilapDTipoMessaggio === TYPE_ALERT.ERROR){

          const messageReplaced = Utils.replacePlaceHolder(messaggio.dsSilapDMessaggio,mapParams);
          this.alertMessageService.setSingleErrorMessage(messageReplaced);
          return;
        }
      }
    }

    let message = Utils.replacePlaceHolder(messaggio.dsSilapDMessaggio,mapParams);


    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const userChoice = await this.promptModalService.openPrompt(title, message, pYes, pNo, 'warning');

    if (userChoice)
      this.aggiornaStato();
  }

  private async aggiornaStato() {
    this.alertMessageService.emptyMessages();

    const idStato: number = this.f.idDstato.value;

    let versamantoStato: VersamentoStato = {
      idEsoTVersamento: this.versamentoCorrente.idEsoTVersamento,
      esoDVersamentoStato: {
        id: idStato
      },
      dsNote: this.f.dsNote.value
    }

    this.spinner.show();
    this.versamentoEsoneriService.autorizzaVersamento(versamantoStato).subscribe({
      next: (res: any) => {
        if(res.esitoPositivo){
          versamantoStato.dtStato = new Date();
          versamantoStato.esoDVersamentoStato.descr = versamantoStato.esoDVersamentoStato.id === STATO.AUTORIZZATA.ID ? STATO.AUTORIZZATA.DS : STATO.NON_AUTORIZZATA.DS;
          this.versamentoCorrente.esoTVersamentoStatos.forEach((versamantoStato: VersamentoStato) => {
            versamantoStato.flgCurrentRecord = 'N';
          });
          versamantoStato.flgCurrentRecord = 'S';
          this.versamentoCorrente.esoTVersamentoStatos.push(versamantoStato);
          this.emitAutorizzazione.emit(this.versamentoCorrente.esoTVersamentoStatos);
          if(res.msg){
            this.alertMessageService.setSingleSuccessMessage(res.msg.dsSilapDMessaggio);
          } else if(res.apiMessages && (res.apiMessages.length > 0)){
            this.alertMessageService.setApiMessages(res.apiMessages);
          }
        }
        else
          this.alertMessageService.setApiMessages(res.apiMessages);
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore autorizzaVersamento: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }


  translate(key: string) {
    return this.translateService.instant(key);
  }

  onClickInvio(){
    this.spinner.show();
    this.versamentoEsoneriService.confermaInviaVersamento(this.versamentoCorrente.idEsoTVersamento).subscribe({
      next: (res: any) =>{

        if(res.esitoPositivo){
          const resVersamentoInvio: Versamento = res.versamento;
          this.alertMessageService.setApiMessages(res.apiMessages);
          this.emitConfermainvio.emit(resVersamentoInvio);
        }else{
          this.alertMessageService.setApiMessages(res.apiMessages);
          this.spinner.hide();
        }
      },
      error: (error: any) => {
        this.logService.error(this.constructor.name,`loadMessage: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }

  private getStatoCorrente(): VersamentoStato{
    let statoVersamento: VersamentoStato;
    if(this.versamentoCorrente){
      const statiVersamento = this.versamentoCorrente.esoTVersamentoStatos;
      statoVersamento = statiVersamento.find((stato: VersamentoStato) => {
        return stato.flgCurrentRecord === 'S';
      });
    }
    return statoVersamento;
  }

}

interface DichiarazioneVersamentoModel {
  provincia?: string,
  giorniLavorativi?: number,
  numLavEsonerati?: number,
  importo?: number
}
