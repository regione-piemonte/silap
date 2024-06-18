/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { CONTROL_STATE, RUOLO, STATO } from 'src/app/constants';
import { Decodifica, FormRicercaVersamentoEsoneri, Messaggio, Ruolo } from 'src/app/modules/silapapi';
import { LogService } from 'src/app/services/log.service';
import { MessageService } from 'src/app/services/message.service';
import { UserService } from 'src/app/services/user.service';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-form-ricerca-dichiarazione-ver-eso',
  templateUrl: './form-ricerca-dichiarazione-ver-eso.component.html',
  styleUrls: ['./form-ricerca-dichiarazione-ver-eso.component.scss']
})
export class FormRicercaDichiarazioneVerEsoComponent implements OnInit {

  sysDate: Date = new Date();

  @ViewChild("d_dataFine",{static: false}) d_dataFine: any;
  @ViewChild("d_dataInizio",{static: false}) d_dataInizio: any;

  @Input() formPristine: any;
  @Input() formRicerca: any;

  @Output() readonly datiRicerca = new EventEmitter<any>();
  @Output() readonly resetForm = new EventEmitter();

  @Input() stati: Decodifica[];
  statiSelezionati: number[] = [];
  showForm: boolean;

  get f() {
    return this.form.controls as any;
  }
  get statoControls() {
    return this.form.controls['statoDichiarazione'] as FormArray;
  }

  ruolo: Ruolo;

  STATI_DEFAULT = [
    STATO.ACCETTATA.ID,
    STATO.AUTORIZZATA.ID,
    STATO.BOZZA.ID,
    STATO.MODIFICATA.ID,
    STATO.NON_AUTORIZZATA.ID];

  form: FormGroup;
  constructor(
    private fb: FormBuilder,
    private logService: LogService,
    private userService: UserService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.logService.info(this.constructor.name,"ngOnInit");
    this.sysDate.setHours(0,0,0,0);
    this.ruolo = this.userService.getRuoloSessione();
    this.initForm();
    this.setFormMode();
    if(!this.formRicerca.goSearch){
      this.statiSelezionati = [
        STATO.ACCETTATA.ID,
        STATO.AUTORIZZATA.ID,
        STATO.BOZZA.ID,
        STATO.MODIFICATA.ID,
        STATO.NON_AUTORIZZATA.ID];;
      this.patchValueinForms(this.formRicerca);
    }
    else{
      this.statiSelezionati = Utils.clone(this.formRicerca.statoDichiarazione);
      this.patchParamsInForm(this.formRicerca);
    }
    this.setFormState();
    this.showForm = true;
  }


  private patchValueinForms(form: FormRicercaVersamentoEsoneri){
    this.form.patchValue(form);
    let formArray: FormArray = this.statoControls;
    this.stati.forEach((stato: Decodifica,index) => {
      if( STATO.ACCETTATA.ID === stato.id || 
          STATO.AUTORIZZATA.ID === stato.id ||
          STATO.BOZZA.ID === stato.id ||
          STATO.MODIFICATA.ID === stato.id ||
          STATO.NON_AUTORIZZATA.ID === stato.id
        ){
          formArray.controls[index].patchValue(true);
        }else{
          formArray.controls[index].patchValue(false);
        }

    });
  }

  private initForm() {
    this.form = this.fb.group({
      codFisc: this.fb.control(null),
      denomAzienda: this.fb.control(null),
      dataInizio: this.fb.control(null,),
      dataFine: this.fb.control(null),
      annoProtocollo: this.fb.control(null),
      numProtocollo: this.fb.control(null),
      flgDueRate: this.fb.control(null),
      flgUnicaSoluzione: this.fb.control(null),
      statoDichiarazione: this.fb.array([])
    });
    this.buildFormArray();
  }

  private buildFormArray(){
    const formArray: FormArray = this.form.controls['statoDichiarazione'] as FormArray;
    this.stati.forEach((s: Decodifica) => {
      formArray.push(new FormControl())
    });
  }

  onClickReset(){
    this.patchValueinForms(this.formPristine);
    this.statiSelezionati = [
      STATO.ACCETTATA.ID,
      STATO.AUTORIZZATA.ID,
      STATO.BOZZA.ID,
      STATO.MODIFICATA.ID,
      STATO.NON_AUTORIZZATA.ID];
    this.setFormState();
    this.resetForm.emit();
  }

  onSubmit(){
    let ricercaPerInvio = this.form.getRawValue() as FormRicercaVersamentoEsoneri;
    let statiSelezionatiTmp: number[] = [];
    this.statiSelezionati.forEach((el) => {
      if(el)
        statiSelezionatiTmp.push(el);

    });
    ricercaPerInvio.statoDichiarazione = statiSelezionatiTmp;
    ricercaPerInvio.flgDueRate = this.form.controls['flgDueRate'].value ? 'S' : 'N'
    ricercaPerInvio.flgUnicaSoluzione = this.form.controls['flgUnicaSoluzione'].value ? 'S' : 'N'
    if(!Utils.isNullOrUndefinedOrEmptyField(ricercaPerInvio.annoProtocollo)){
      ricercaPerInvio.annoProtocollo = Number(ricercaPerInvio.annoProtocollo);
    }else{
      ricercaPerInvio.annoProtocollo = null;
    }
    if(!Utils.isNullOrUndefinedOrEmptyField(ricercaPerInvio.numProtocollo)){
      ricercaPerInvio.numProtocollo = Number(ricercaPerInvio.numProtocollo);
    }
    this.logService.info(this.constructor.name,`onSubmit:: ricercaPerInvio :: ${JSON.stringify(ricercaPerInvio)}`);
    this.datiRicerca.emit(ricercaPerInvio);
  }

  setFormMode(){
    this.form.controls['codFisc'].setValidators(Validators.minLength(3));
    this.form.controls['denomAzienda'].setValidators(Validators.minLength(2));
    this.form.controls['annoProtocollo'].setValidators(Validators.minLength(4));
    this.form.controls['dataInizio'].setValidators([Validators.required,this.toDayDaRangeValidator,this.dataRangeValidator]);
    this.form.controls['dataFine'].setValidators([Validators.required,this.dataRangeValidator]);
    this.form.controls['dataInizio'].updateValueAndValidity();
    this.form.controls['dataFine'].updateValueAndValidity();
  }

  setFormState() {
    this.setNumeroProtocolloControlState();
    if(
      this.ruolo.idSilapDRuolo === RUOLO.LEGALE_RAPPRESENTANTE.ID ||
      this.ruolo.idSilapDRuolo === RUOLO.DELEGATO.ID ||
      this.ruolo.idSilapDRuolo === RUOLO.PERSONA_AUTORIZZATA.ID
    ){
      this.form.controls['codFisc'][CONTROL_STATE.DISABLE]();
      this.form.controls['denomAzienda'][CONTROL_STATE.DISABLE]();
    }
  }

  private toDayDaRangeValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    let invalid = false;
    const dataInizio = this.form.controls['dataInizio'].value;

    if (dataInizio) {
      const dtInizio: Date = new Date(dataInizio);
      dtInizio.setHours(0,0,0,0);
      invalid = dtInizio.valueOf() > this.sysDate.valueOf();
    }
    return invalid ? { today: { dataInizio  } } : null;
  };

  setNumeroProtocolloControlState() {
    const annoProtocollo = this.form.controls['annoProtocollo'].value;
    this.form.controls['numProtocollo'].removeValidators(Validators.required);
    if(Utils.isNullOrUndefinedOrEmptyField(annoProtocollo) || annoProtocollo.length < 4){
      this.form.controls['numProtocollo'].reset();
      this.form.controls['numProtocollo'][CONTROL_STATE.DISABLE]();
    }else{
      if(annoProtocollo.length === 4){
        this.form.controls['numProtocollo'][CONTROL_STATE.ENABLE]();
        this.form.controls['numProtocollo'].setValidators(Validators.required);
      }
    }
    this.form.controls['numProtocollo'].updateValueAndValidity();
  }

  selectRemoveStato(stato: number) {
    if (this.statiSelezionati) {
      if (this.statiSelezionati.length > 0) {
        const statoTmp = this.statiSelezionati.find(item => item === stato);
        const indexItemToRemove: number = this.statiSelezionati.indexOf(statoTmp);
        if (indexItemToRemove >= 0) {
          this.statiSelezionati.splice(indexItemToRemove, 1);
        } else {
          this.statiSelezionati.push(stato);
        }
      } else {
        this.statiSelezionati.push(stato);
      }

    }
  }

  updateValueAndValidity(control: AbstractControl){
    control.updateValueAndValidity();
  }

  private dataRangeValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    let invalid = false;
    const dataInizio = this.form.controls['dataInizio'].value;
    const dataFine = this.form.controls['dataFine' ].value;
    if (dataInizio && dataFine) {
       const dtInizio: Date = new Date(dataInizio);
       const dtFine: Date = new Date(dataFine);
       dtInizio.setHours(0,0,0,0);
       dtFine.setHours(0,0,0,0);
      invalid = dtInizio.valueOf() > dtFine.valueOf();
    }
    return invalid ? { invalidDataRange: { dataInizio, dataFine } } : null;
  };

  private patchParamsInForm(pFormRicerca: any){
    if(!pFormRicerca)
      return;
    let formRicerca: FormRicercaVersamentoEsoneri = {
      dataInizio: new Date(pFormRicerca.dataInizio),
      dataFine: new Date(pFormRicerca.dataFine),
      codFisc: pFormRicerca.codFisc,
      denomAzienda: pFormRicerca.denomAzienda,
      annoProtocollo: pFormRicerca.annoProtocollo,
      numProtocollo: pFormRicerca.numProtocollo,
      statoDichiarazione: []
    }

    this.f.flgDueRate.patchValue(pFormRicerca.flgDueRate === 'S' ? true : false);
    this.f.flgUnicaSoluzione.patchValue(pFormRicerca.flgUnicaSoluzione === 'S' ? true : false);
    this.statiSelezionati = JSON.parse(pFormRicerca.statoDichiarazione);
    const formArray = this.statoControls;
    this.statiSelezionati.forEach((stato: number,index: number) => {
      
        this.stati.forEach((s: Decodifica,index)=>{

          if(s.id === stato){
            if(stato === STATO.ACCETTATA.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.ACCONTO.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.ANNULLATA.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.AUTORIZZATA.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.BOZZA.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.ELIMINATA.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.MODIFICATA.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.NON_AUTORIZZATA.ID){
              formArray.at(index).patchValue(true);
             }else if(stato === STATO.SALDO.ID){
              formArray.at(index).patchValue(true);
            }else if(stato === STATO.AVVISO_INVIATO.ID){
              formArray.at(index).patchValue(true);
            }
          }
          
        });
      
    });
    this.form.patchValue(formRicerca);

    this.onSubmit();
  }

  getMsgValidazioneByCode(code: string): string{
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code) || {};
    return messaggio.dsSilapDMessaggio;
  }

}