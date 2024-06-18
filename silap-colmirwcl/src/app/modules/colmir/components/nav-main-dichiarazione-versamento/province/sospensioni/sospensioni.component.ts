/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { DatePipe } from '@angular/common';
import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { NgbDatepicker } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { CONTROL_STATE, WIZARD_MODE } from 'src/app/constants';
import { PaginationDataChange } from 'src/app/modules/colmircommon/models/pagination-data-change';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { PromptModalService } from 'src/app/modules/colmircommon/services/prompt-modal.service';
import { Decodifica, DecodificaService, Messaggio, Versamento, VersamentoMotivoSospensione, VersamentoProvincia, VersamentoPvSospensione } from 'src/app/modules/silapapi';
import { MESSAGGIO } from 'src/app/msg-constants';
import { MessageService } from 'src/app/services/message.service';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-sospensioni',
  templateUrl: './sospensioni.component.html',
  styleUrls: ['./sospensioni.component.scss']
})
export class SospensioniComponent implements OnInit,AfterViewInit  {

  @Input() mode: string;
  @Input() initialVersamento: Versamento;

  @Input() currentPaginationData: PaginationDataChange;

  @Output() emitConferma = new EventEmitter<VersamentoPvSospensione[]>();

  versamentoCorrente: Versamento;

  @Input() versamenetoProvinciaSelezionataInitial: VersamentoProvincia;
  versamenetoProvinciaSelezionataCurrent: VersamentoProvincia;



  versamentoPvSospensione: VersamentoPvSospensione[] = [];
  motiviSospensioni: Decodifica[];



  sysDate: Date = new Date();
  error: boolean;



  @ViewChild("d_dataFine", { static: false }) d_dataFine: NgbDatepicker;
  @ViewChild("d_dataInizio", { static: false }) d_dataInizio: NgbDatepicker;


  get VIEW_MODE(): boolean {
    return this.mode === WIZARD_MODE.VIEW;
  }

  get f() {
    return this.form.controls as any;
  }

  get SOSPENSIONI_INVALID(): boolean {
    if (this.form.invalid) {
      return true;
    }
    return false;
  }

  get RANGE_SOSPENSIONI_ISVALID(): boolean {
    let isvalid: boolean = false;
    if(this.initialVersamento && this.versamentoPvSospensione && this.versamentoPvSospensione.length > 0){
      for(let i = 0; i < this.versamentoPvSospensione.length && !isvalid; i++){
        const annoInizioSospenzione: Date = new Date(this.versamentoPvSospensione[i].dInizioSospensione);
        const annoFineSospenzione: Date = new Date(this.versamentoPvSospensione[i].dFineSospensione);

        isvalid = this.initialVersamento.annoRiferimento >= annoInizioSospenzione.getFullYear() && this.initialVersamento.annoRiferimento <= annoFineSospenzione.getFullYear();
      }
    }
    return this.versamentoPvSospensione.length === 0 || isvalid;
  }

  form: FormGroup;
  constructor(
    private messageService: MessageService,
    private decodificaService: DecodificaService,
    private fb: FormBuilder,
    private promptModalService: PromptModalService,
    private alertMessageService: AlertMessageService,
    private translateService: TranslateService,
    private spinner: NgxSpinnerService,
    private datePipe: DatePipe
  ) { }

  ngAfterViewInit(): void {

  }

 //*********************LICENZIAMENTO COLLETTIVO VALE PER UNA SOLA PROVINCIA************************* */
  //il range del periodo delle sospensioni deve contenere l'anno di riferimento


  ngOnInit(): void {
    this.versamentoCorrente = Utils.clone(this.initialVersamento);
    this.versamenetoProvinciaSelezionataCurrent = Utils.clone(this.versamenetoProvinciaSelezionataInitial)
    if(this.versamenetoProvinciaSelezionataCurrent.esoTVersamentoPvSospensiones &&
      this.versamenetoProvinciaSelezionataCurrent.esoTVersamentoPvSospensiones.length > 0){
        this.versamentoPvSospensione =  Utils.clone(this.versamenetoProvinciaSelezionataCurrent.esoTVersamentoPvSospensiones);
    }
    this.loadDecodificaMotivoSospensioni();
    if(this.mode !== WIZARD_MODE.VIEW){
      this.initForm();
      this.setFormMode();
    }
    this.spinner.hide();
  }


  private initForm() {
    this.form = new FormGroup({
      dataInizio: new FormControl(null),
      dataFine: new FormControl(null),
      motSospensioneSelect: new FormControl(null),
      percentSospSelect: new FormControl(null,Validators.min(1)),
      numLavSospSelect: new FormControl(null)
    });
  }

  updateValueAndValidity(control: AbstractControl) {
    control.updateValueAndValidity();
  }

  private dataRangeValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    let invalid = false;
    const dataInizio = this.f['dataInizio'].value;
    const dataFine = this.f['dataFine' ].value;
    if (dataInizio && dataFine) {
       const dtInizio: Date = new Date(dataInizio);
       const dtFine: Date = new Date(dataFine);
       dtInizio.setHours(0,0,0,0);
       dtFine.setHours(0,0,0,0);
      invalid = dtInizio.valueOf() > dtFine.valueOf();
    }
    return invalid ? { invalidDataRange: { dataInizio, dataFine } } : null;
  };


  private motivoSospensioneValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    let invalid = false;
    const motivoSospensione = this.f['motSospensioneSelect'].value;
    if(motivoSospensione == 1){
      const versamentoProvince: VersamentoProvincia[] = this.versamentoCorrente.esoTVersamentoProvincias;
      for(let i = 0; i < versamentoProvince.length && !invalid; i++){

        if(versamentoProvince[i].idEsoTVersamentoProvincia !== this.versamenetoProvinciaSelezionataCurrent.idEsoTVersamentoProvincia){
          const sospensioni: VersamentoPvSospensione[] = versamentoProvince[i].esoTVersamentoPvSospensiones;
          for(let j = 0; j < sospensioni.length && !invalid; j++){
            invalid = sospensioni[j].esoDVersamentoMotivoSospensione.idEsoDVersamentoMotivoSospensione === 1;
          }
        }

      }

      if(!invalid){
        for(let i = 0; i < this.versamentoPvSospensione.length && !invalid; i++){
          invalid = motivoSospensione === this.versamentoPvSospensione[i].esoDVersamentoMotivoSospensione.idEsoDVersamentoMotivoSospensione;
        }
      }
    }
    return invalid ? { motivoSospensioneValidator: { motivoSospensione } } : null;
  };

  private toDayDaRangeValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    let invalid = false;
    const dataInizio = this.f['dataInizio'].value;

    if (dataInizio) {
      const dtInizio: Date = new Date(dataInizio);
      dtInizio.setHours(0,0,0,0);
      invalid = dtInizio.valueOf() > this.sysDate.valueOf();
    }
    return invalid ? { today: { dataInizio  } } : null;
  };


  setFormMode() {
    this.f['dataInizio'].setValidators([
      Validators.required,
      this.dataRangeValidator,
      this.dateAllowedValidator]);
    this.f['dataFine'].setValidators([
      Validators.required,
      this.dateAllowedValidator
    ]);
    this.f['dataInizio'].updateValueAndValidity();
    this.f['dataFine'].updateValueAndValidity();
    this.f['motSospensioneSelect'].setValidators([
      this.motivoSospensioneValidator,
      Validators.required]);
    this.f['motSospensioneSelect'].updateValueAndValidity();
    this.f['percentSospSelect'].setValidators([
      Validators.min(1),
      Validators.max(100)
    ]);
    this.f['percentSospSelect'].updateValueAndValidity();
    this.f['numLavSospSelect'].setValidators([
      Validators.min(1)
    ]);
    this.f['numLavSospSelect'].updateValueAndValidity();
  }


  onChangeMotivoSospensione(){
    const motivoSospensione: number = this.f['motSospensioneSelect'].value;
    this.f['percentSospSelect'][CONTROL_STATE.ENABLE]();
    this.f['numLavSospSelect'][CONTROL_STATE.ENABLE]();
    this.f['percentSospSelect'].removeValidators(Validators.required);
    this.f['numLavSospSelect'].removeValidators(Validators.required);
    this.f['percentSospSelect'].reset();
    this.f['numLavSospSelect'].reset();
    if(motivoSospensione && motivoSospensione === 1){
      this.f['percentSospSelect'].patchValue(100);
      this.f['percentSospSelect'][CONTROL_STATE.DISABLE]();
      this.f['numLavSospSelect'][CONTROL_STATE.DISABLE]();
    }else if(motivoSospensione && motivoSospensione === 2){
      this.f['percentSospSelect'].setValidators(Validators.required);
      this.f['numLavSospSelect'].setValidators(Validators.required);
    }
    this.f['percentSospSelect'].updateValueAndValidity();
    this.f['numLavSospSelect'].updateValueAndValidity();
  }


  setFormState() {
    const motSospensioni = this.f['motSospensioneSelect'].value;
    const percentSosp = this.f['percentSospSelect'].value;
    const numLavSosp = this.f['numLavSospSelect'].value;
    if (motSospensioni) {
      this.f['percentSospSelect'][CONTROL_STATE.ENABLE]();
      this.f['numLavSospSelect'][CONTROL_STATE.ENABLE]();
    } else {
      this.form.reset();
      this.f['percentSospSelect'][CONTROL_STATE.DISABLE]();
      this.f['numLavSospSelect'][CONTROL_STATE.DISABLE]();
    }

  }

  onChangePercentSosp(){
    const percent: number = this.f['percentSospSelect'].value;
    if(percent){
      this.f['numLavSospSelect'].reset();
      this.f['numLavSospSelect'][CONTROL_STATE.DISABLE]();
    }else{
      this.f['numLavSospSelect'][CONTROL_STATE.ENABLE]();
    }
  }

  onChangeNumLavSosp(){
    const numLav: number = this.f['numLavSospSelect'].value;
    const motSospensioni = this.f['motSospensioneSelect'].value;
    if(numLav && motSospensioni !== 1){
      this.f['percentSospSelect'].reset();
      this.f['percentSospSelect'][CONTROL_STATE.DISABLE]();
    }else{
      this.f['percentSospSelect'][CONTROL_STATE.ENABLE]();
    }
  }


  getMsgValidazioneByCode(code: string): string {
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code);
    return messaggio.dsSilapDMessaggio;
  }

  private loadDecodificaMotivoSospensioni() {
    this.decodificaService.findDVersamentoMotivoSospensione().subscribe({
      next: (res: any) => {
        console.log(res)
        if (res.esitoPositivo) {
          this.motiviSospensioni = res.list;
        }
      },
      error: (error) => {
        console.error(`findDVersamentoMotivoSospensione ${error}`);
      }
    });

  }


  onClickReset() {
    this.form.reset();
    this.f['percentSospSelect'][CONTROL_STATE.ENABLE]();
    this.f['numLavSospSelect'][CONTROL_STATE.ENABLE]();
    this.versamentoPvSospensione =  Utils.clone(this.versamenetoProvinciaSelezionataInitial.esoTVersamentoPvSospensiones);
  }


  async onClickConferma() {
    if (this.form.valid) {
      const idMotivoSospForm = this.f?.motSospensioneSelect?.value
      const esoDVersamentoMotivoSospensione: Decodifica = this.motiviSospensioni.find((decod: Decodifica) => {
        return decod.id === idMotivoSospForm;
      });
      let userChoice = true;
      if(esoDVersamentoMotivoSospensione.id === 1){

        const mapParams: any = {
          0: this.datePipe.transform(this.f.dataInizio.value, 'dd/MM/yyyy'),
          1: String(this.versamentoCorrente.annoRiferimento),
          2: this.versamentoCorrente.dsDenominazioneAzienda,
          3: this.versamentoCorrente.codFiscale,
          4: this.versamenetoProvinciaSelezionataCurrent.silapDProvincia.dsSilapDProvincia
        }
        const title = this.translate(marker('MODAL.TITLE.INSERIM_SOSP_LICENZIAMENTO_COLLETTIVO'));
        const message = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100089);
        const pYes = this.translate(marker('APP.YES'));
        const pNo = this.translate(marker('APP.NO'));

        const messageReplaced = Utils.replacePlaceHolder(message.dsSilapDMessaggio,mapParams);
        userChoice = await this.promptModalService.openPrompt(title, messageReplaced, pYes, pNo, 'warning');
      }

      if(!userChoice)
        return;

      const nuovoVersamentoPvSospensione: VersamentoPvSospensione = {
        dInizioSospensione: this.f.dataInizio.value,
        dFineSospensione: this.f.dataFine.value,
        esoDVersamentoMotivoSospensione: {
          idEsoDVersamentoMotivoSospensione: idMotivoSospForm,
          dsEsoDVersamentoMotivoSospensione: esoDVersamentoMotivoSospensione.descr
        },
        numLavoratoriSospesi: this.f.numLavSospSelect.value,
        percSospensione: this.f.percentSospSelect.value,
      };
      this.versamentoPvSospensione.push(nuovoVersamentoPvSospensione);
      this.form.reset();
    }
  }

  onClickConfermaProsegui() {

    this.emitConferma.emit(this.versamentoPvSospensione);

  }


  async onClickElimina(index: number) {
    const idEsoDVersamentoMotivoSospensione = this.versamentoPvSospensione[index].esoDVersamentoMotivoSospensione.idEsoDVersamentoMotivoSospensione;
    const idEsoTVersamentoPvSospensione = this.versamentoPvSospensione[index].idEsoTVersamentoPvSospensione;
    const title = this.translate(marker('MODAL.TITLE.ELIMINA_SOSP'));
    const message = idEsoDVersamentoMotivoSospensione === 1 ? this.messageService.getMessaggioByCod(MESSAGGIO.COD_100111) : this.messageService.getMessaggioByCod(MESSAGGIO.COD_100096);
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    let mapParams: any = {
      0: String(this.versamentoCorrente.annoRiferimento),
      1: this.versamentoCorrente.dsDenominazioneAzienda,
      2: this.versamentoCorrente.codFiscale
    }

    if(idEsoDVersamentoMotivoSospensione === 1){
      mapParams = {
        0: this.datePipe.transform(this.versamentoPvSospensione[index].dInizioSospensione, 'dd/MM/yyyy'),
        1: String(this.versamentoCorrente.annoRiferimento),
        2: this.versamentoCorrente.dsDenominazioneAzienda,
        3: this.versamentoCorrente.codFiscale
      }
    }

    const messageReplaced = Utils.replacePlaceHolder(message.dsSilapDMessaggio,mapParams);
    const userChoice = await this.promptModalService.openPrompt(title, messageReplaced, pYes, pNo, 'warning');

    if (userChoice) {
      this.error = false;
      this.versamentoPvSospensione.splice(index, 1);
      this.form.reset();
      this.f['percentSospSelect'][CONTROL_STATE.ENABLE]();
      this.f['numLavSospSelect'][CONTROL_STATE.ENABLE]();
      const message = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100097);
      const messageReplaced = Utils.replacePlaceHolder(message.dsSilapDMessaggio,mapParams);
      this.alertMessageService.setSingleSuccessMessage(messageReplaced);
    } else {
      console.log(`non eliminare${JSON.stringify(this.versamentoPvSospensione)}`);
    }
    this.spinner.hide();
  }


  translate(key: string) {
    return this.translateService.instant(key);
  }

  private dateAllowedValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    let invalidDataInizio = false;
    let invalidDataFine = false;
    let invalidGenerico = false;
    let dataInizio: Date = this.form.controls['dataInizio'].value;
    let dataFine: Date = this.form.controls['dataFine'].value;
    if(
      this.versamentoPvSospensione &&
      this.versamentoPvSospensione.length > 0){
        if(!Utils.isNullOrUndefinedOrEmptyField(dataInizio) && !Utils.isNullOrUndefinedOrEmptyField(dataFine)){
          dataInizio = new Date(dataInizio);
          dataInizio.setHours(0,0,0,0);
          dataFine = new Date(dataFine);
          dataFine.setHours(0,0,0,0);

          for(let i: number = 0; i < this.versamentoPvSospensione.length && !invalidGenerico; i++){
            let dInizioSospensione: Date = new Date(this.versamentoPvSospensione[i].dInizioSospensione);
            dInizioSospensione.setHours(0,0,0,0);
            let dFineSospensione: Date = new Date(this.versamentoPvSospensione[i].dFineSospensione);
            dFineSospensione.setHours(0,0,0,0);
            invalidGenerico = dataInizio.valueOf() > dFineSospensione.valueOf() || (dataInizio.valueOf() < dInizioSospensione.valueOf() && dataFine.valueOf() < dInizioSospensione.valueOf());
            invalidGenerico = !invalidGenerico;
          }
        }
    }
    return invalidGenerico ? { dateAllowed: { invalidGenerico } } : null;
  };

  getMsgByCode(code: string): string {
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code);
    return messaggio.dsSilapDMessaggio;
  }

}

