/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Messaggio, Versamento, VersamentoProvincia, VersamentoPvGgExtraFestivi } from 'src/app/modules/silapapi';
import { Utils } from 'src/app/utils';
import { CONTROL_STATE, STORAGE_KEY, WIZARD_MODE } from 'src/app/constants';
import { MessageService } from 'src/app/services/message.service';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { MESSAGGIO } from 'src/app/msg-constants';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'colmirwcl-giorni-lavorativi',
  templateUrl: './giorni-lavorativi.component.html',
  styleUrls: ['./giorni-lavorativi.component.scss']
})
export class GiorniLavorativiComponent implements OnInit {

  @Output() emitConferma = new EventEmitter<VersamentoProvincia>();

  @Input() initialVersamento: Versamento;
  @Input() mode: string;
  versamenetoProvinciaSelezionataInitial: VersamentoProvincia;
  versamenetoProvinciaSelezionataCurrent: VersamentoProvincia;

  @Input() set versamenetoProvinciaSelezionata(versamenetoProvinciaSelezionata: VersamentoProvincia){
    this.versamenetoProvinciaSelezionataInitial = versamenetoProvinciaSelezionata;
    this.versamenetoProvinciaSelezionataCurrent = Utils.clone(this.versamenetoProvinciaSelezionataInitial);
    this.patchValueInForm();
  }

  form: FormGroup = new FormGroup({
    giorniLav: new FormControl(null, Validators.required),
    dataFestaPatronale: new FormControl(null),
    dataGiorniFestivi: new FormControl(null),
    giorniLavContratto: new FormControl(null)
  });
  versamentoCorrente: Versamento;
  elencoGiorniFestivi: VersamentoPvGgExtraFestivi[];
  error: boolean;
  errorFestaPatronale: boolean;



  get VIEW_MODE(): boolean{
    return this.mode === WIZARD_MODE.VIEW;
  }

  get f() {
    return this.form.controls as any;
  }

  get GIORNI_LAVORATIVI_INVALID(): boolean{
    return !this.form?.valid || this.error || this.errorFestaPatronale;
  }

  constructor(
    private messageService: MessageService,
    private alertMessageService: AlertMessageService,
    private spinner: NgxSpinnerService
  ) { }


  ngOnInit(): void {
    this.versamentoCorrente = Utils.clone(this.initialVersamento);
    if(this.mode === WIZARD_MODE.VIEW)
      this.form[CONTROL_STATE.DISABLE]();
    this.spinner.hide();
  }

  private patchValueInForm() {
    this.f.dataGiorniFestivi.reset();
    this.f.giorniLav.patchValue(this.versamenetoProvinciaSelezionataCurrent.numGgLavorativiSettimanali);
    if(this.versamenetoProvinciaSelezionataCurrent.dFestaPatronale)
      this.f.dataFestaPatronale.patchValue(new Date(this.versamenetoProvinciaSelezionataCurrent.dFestaPatronale));
    this.elencoGiorniFestivi = [];
    if(this.versamenetoProvinciaSelezionataCurrent.esoTVersamentoPvGgExtraFestivis)
      this.elencoGiorniFestivi = Utils.clone(this.versamenetoProvinciaSelezionataCurrent.esoTVersamentoPvGgExtraFestivis);
  }



  onChnageDataFestaPatronale(){
    const control: AbstractControl = this.f.dataFestaPatronale;
    if(!control.valid || Utils.isNullOrUndefinedOrEmptyField(control.value)){
      control.reset();
      this.errorFestaPatronale=false;
      return;
    }
    let dataFestaPatronale: Date = new Date(control.value);
    dataFestaPatronale.setHours(0,0,0,0);
    const isInvalid: boolean = this.elencoGiorniFestivi.some((giornoFestivo: VersamentoPvGgExtraFestivi) => {
      let dGgFestivo: Date = new Date(giornoFestivo.dGgFestivo);
      dGgFestivo.setHours(0,0,0,0);
      return dGgFestivo.getTime() === dataFestaPatronale.getTime();
    });
    if(isInvalid)
      this.errorFestaPatronale = true;
    else
      this.errorFestaPatronale = false;
  }

  onChnageDate(){
    const control: AbstractControl = this.f.dataGiorniFestivi;
    if(!control.valid || Utils.isNullOrUndefinedOrEmptyField(control.value)){
      control.reset();
      this.error = false;
      this.errorFestaPatronale = false;
      return;
    }
    const data: Date = new Date(control.value);
    this.addRemoveGiornofestivo(data);
  }

  onDateSelection(date: NgbDateStruct) {

    let selectedDate = new Date(date.year, date.month - 1, date.day);
    this.addRemoveGiornofestivo(selectedDate);

  }


  private addRemoveGiornofestivo(selectedDate: Date) {
    selectedDate.setHours(0,0,0,0);
    const isDuplicate: boolean = this.elencoGiorniFestivi.some((giorno: VersamentoPvGgExtraFestivi) => {
      let dGgFestivo: Date = new Date(giorno.dGgFestivo);
      dGgFestivo.setHours(0, 0, 0, 0);
      return dGgFestivo.getTime() === selectedDate.getTime();
    });
    const dataFestaPatronaleControl: AbstractControl = this.f.dataFestaPatronale;
    let timeFestaPatronale: number;
    if(dataFestaPatronaleControl.valid){
      const dataFestaPatronale = new Date(dataFestaPatronaleControl.value);
      dataFestaPatronale.setHours(0,0,0,0);
      timeFestaPatronale = dataFestaPatronale.getTime();
    }
    const isEqualToFestaPatronale: boolean = selectedDate.getTime() === timeFestaPatronale;
    if (isDuplicate){
      this.error = true;
    }
    if(isEqualToFestaPatronale){
      this.errorFestaPatronale = true;
    }
    if(!this.error && !this.errorFestaPatronale){
      const giornoFestivo: VersamentoPvGgExtraFestivi = {
        dGgFestivo: selectedDate
      };
      this.elencoGiorniFestivi.push(giornoFestivo);
      this.error = false;
      this.errorFestaPatronale = false;
    }

  }

  onClickReset(){
    this.alertMessageService.emptyMessages();
    this.form.reset();
    this.error = false;
    this.errorFestaPatronale = false;
    this.versamentoCorrente = Utils.clone(this.initialVersamento);
    if(!Utils.isNullOrUndefinedOrEmptyField(this.versamenetoProvinciaSelezionataInitial.dFestaPatronale)){
      const dFestaPatronale: Date = new Date(this.versamenetoProvinciaSelezionataInitial.dFestaPatronale);
      this.f['dataFestaPatronale'].patchValue(dFestaPatronale);
    }
    this.f['giorniLav'].setValue(this.versamenetoProvinciaSelezionataInitial.numGgLavorativiSettimanali);
    this.elencoGiorniFestivi = Utils.clone(this.versamenetoProvinciaSelezionataInitial.esoTVersamentoPvGgExtraFestivis);
  }

  onClickConfermaProsegui(){
    this.versamenetoProvinciaSelezionataCurrent.numGgLavorativiSettimanali = this.f.giorniLav.value;
    this.versamenetoProvinciaSelezionataCurrent.dFestaPatronale = this.f.dataFestaPatronale.value;
    this.versamenetoProvinciaSelezionataCurrent.esoTVersamentoPvGgExtraFestivis = this.elencoGiorniFestivi;
    this.emitConferma.emit(this.versamenetoProvinciaSelezionataCurrent);
  }

  getMsgValidazioneByCode(code: string): string{
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code) || {};
    return messaggio.dsSilapDMessaggio;
  }



  getMessageByCodDecodificato(){
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100053);
    const mapParams= {
      0: this.f['dataGiorniFestivi'].value.toLocaleDateString(),
      1: this.versamentoCorrente.dsDenominazioneAzienda,
      2: this.versamentoCorrente.codFiscale
    }
    let message = Utils.replacePlaceHolder(messaggio.dsSilapDMessaggio,mapParams);
    return message;
  }



  onClickElimina(index: number){
    this.error = false;
    this.errorFestaPatronale = false;
    this.elencoGiorniFestivi.splice(index,1);
    this.f.dataGiorniFestivi.reset();
  }
}




