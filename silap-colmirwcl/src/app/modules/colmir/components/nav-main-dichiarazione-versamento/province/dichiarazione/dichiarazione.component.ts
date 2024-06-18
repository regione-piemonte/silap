/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { Observable } from 'rxjs';
import { APP_PARAMAS, RUOLO, STORAGE_KEY, TIPO_PERIODO, WIZARD_MODE } from 'src/app/constants';
import { PaginatedArrayTableComponent } from 'src/app/modules/colmircommon/components/paginated-array-table/paginated-array-table.component';
import { PaginationDataChange } from 'src/app/modules/colmircommon/models/pagination-data-change';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { PromptModalService } from 'src/app/modules/colmircommon/services/prompt-modal.service';
import { ApiMessage, Decodifica, DecodificaService, Messaggio, Ruolo, Versamento, VersamentoEsoneriService, VersamentoProvincia, VersamentoPvConvenzione, VersamentoPvEsonero, VersamentoPvPeriodo, VersamentoPvSospensione } from 'src/app/modules/silapapi';
import { MESSAGGIO } from 'src/app/msg-constants';
import { CategoriaAziendaPipe } from 'src/app/pipes/categoria-azienda.pipe';
import { LogService } from 'src/app/services/log.service';
import { MessageService } from 'src/app/services/message.service';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-dichiarazione',
  templateUrl: './dichiarazione.component.html',
  styleUrls: ['./dichiarazione.component.scss']
})
export class DichiarazioneComponent implements OnInit, OnDestroy{

  @Input() mode: string;
  @Input() versamento: Versamento;

  @Input() warnings: ApiMessage[];

  @Output() emitConferma = new EventEmitter<Versamento>();

  @ViewChild("d_dataFine", { static: false }) d_dataFine: any;
  @ViewChild("d_dataInizio", { static: false }) d_dataInizio: any;
  @ViewChild("paginatedTable", { static: false }) paginatedTable: PaginatedArrayTableComponent<VersamentoProvincia>;


  @Input() versamenetoProvinciaSelezionata : VersamentoProvincia;

  versamentoPvPeriodo: VersamentoPvPeriodo[] = [];
  versamentoPvPeriodoCalcolati: VersamentoPvPeriodo[] = [];

  insertItem: VersamentoPvPeriodo;

  periodiSelezionati: VersamentoPvPeriodo[] = [];

  paginationDataChange: PaginationDataChange = {
    page: 0,
    limit: APP_PARAMAS.PAGINAZIONE.LIMIT,
    sort: undefined,
    offset: 0 
  }

  sysDate: Date = new Date();


  showTable: boolean = false;
  showTablePeriodiCalcolati: boolean = false;

  categorieAzienda: Decodifica[];

  get VIEW_MODE(): boolean {
    return this.mode === WIZARD_MODE.VIEW;
  }

  get TOTALE_PERIODI(): number{
    let totale: number = 0;
    if(this.versamentoPvPeriodo && this.versamentoPvPeriodo.length > 0){
      this.versamentoPvPeriodo.forEach((periodo: VersamentoPvPeriodo) => {
        if(periodo.flgTipo !== 'C')
          totale = totale + periodo.importo;
      });
    }

    return totale;
  }

  get f() {
    return this.form.controls as any;
  }

  get DICHIARAZIONE_INVALID(): boolean {
    if (this.form.invalid) {
      return true;
    }
    return Utils.isNullOrUndefined(this.f['dataInizio'].value &&
      this.f['dataFine'].value &&
      this.f['baseComputoInput'].value &&
      this.f['disabForzaInput'].value &&
      this.f['numCompensatiInput'].value);
  }

  ruolo: Ruolo;

  totaleCalcolato = 0;
  tooltipText = 'tooltip text';

  formArray: FormGroup;
  selectAll: boolean;


  get fArrayControls() { return this.formArray.controls as any; }

  constructor(
    private messageService: MessageService,
    private alertMessageService: AlertMessageService,
    private translateService: TranslateService,
    private promptModalService: PromptModalService,
    private spinner: NgxSpinnerService,
    private versamentoService:VersamentoEsoneriService,
    private datePipe: DatePipe,
    private logService: LogService,
    private sessionStorageService: SessionStorageService,
    private decodificaService: DecodificaService,
    private categoriaAziendaPipe: CategoriaAziendaPipe,
    private formBuilder: FormBuilder
  ) {
    this.formArray = this.formBuilder.group({
      periods: new FormArray([])
    });
   }


  ngOnDestroy(): void {
    this.alertMessageService.emptyMessages();
  }


  ngOnInit(): void {
    this.ruolo = this.sessionStorageService.getItem(STORAGE_KEY.SESSION.RUOLO);
    this.tooltipText = this.getMsgByCode('100109');
    this.loadDecodifiche();
    let showCalcolato: boolean = this.mode === WIZARD_MODE.VIEW && (this.ruolo.idSilapDRuolo === RUOLO.REGIONE.ID || this.ruolo.idSilapDRuolo === RUOLO.AMMINISTRATORE.ID);
    if(!Utils.isNullOrUndefinedOrEmptyField(this.versamenetoProvinciaSelezionata.esoTVersamentoPvPeriodos)){
      this.versamenetoProvinciaSelezionata.esoTVersamentoPvPeriodos.sort((periodo1, periodo2) => {
        periodo1.dInizio = new Date(periodo1.dInizio);
        periodo2.dInizio = new Date(periodo2.dInizio);
        periodo1.dInizio.setHours(0,0,0,0);
        periodo2.dInizio.setHours(0,0,0,0);
        return periodo1.dInizio.valueOf() - periodo2.dInizio.valueOf();
      });
      this.versamenetoProvinciaSelezionata.esoTVersamentoPvPeriodos.forEach((periodo: VersamentoPvPeriodo) => {
        if(periodo.flgTipo !== 'C'){
          this.versamentoPvPeriodo.push(Utils.clone(periodo));
          // const control = new FormControl();
          // (this.formArray.controls['periodos'] as FormArray).push(control);
        }
        if(showCalcolato){
          if(periodo.flgTipo !== 'O'){
            this.versamentoPvPeriodoCalcolati.push(periodo);
            if(periodo.flgTipo === 'A' || periodo.flgTipo === 'C')
              this.totaleCalcolato = this.totaleCalcolato + periodo.importo;
          }
        }
      });
    }
    this.showTable = true;
    if(showCalcolato)
      this.showTablePeriodiCalcolati = true;

    this.setFormMode();
    if(this.warnings && this.warnings.length > 0)
      this.alertMessageService.setApiMessages(this.warnings);
    this.spinner.hide();
  }

  private loadDecodifiche() {
    this.decodificaService.findCategoriaAzienda().subscribe({
      next: (res: any) => {
        if (res.esitoPositivo) 
          this.categorieAzienda = res.list;
      },
      error: (error) => {
        console.error(`findDVersamentoMotivoSospensione ${error}`);
      }
    });
  }

  form: FormGroup = new FormGroup({
    dataInizio: new FormControl(null),
    dataFine: new FormControl(null),
    baseComputoInput: new FormControl(null),
    disabForzaInput: new FormControl(null),
    numCompensatiInput: new FormControl(null),
    numEsoCertInput: new FormControl(null),
    numRiallineamentoNazionale: new FormControl(null),
    iDcategoriaAzeinda: new FormControl(null)
  });


  updateValueAndValidity(control: AbstractControl) {
    control.updateValueAndValidity();
  }

  setFormMode() {
    this.f['dataInizio'].setValidators([
      Validators.required,
      this.dataRangeValidator,
      this.periodoValidator
    ]);
    this.f['dataFine'].setValidators([
      Validators.required
    ]);
    this.f['dataInizio'].updateValueAndValidity();
    this.f['dataFine'].updateValueAndValidity();
    this.f['baseComputoInput'].setValidators([Validators.required,Validators.min(0)]);
    this.f['disabForzaInput'].setValidators([
      Validators.required,
      this.disabiliInForzaValidator
    ]);
    this.f['numCompensatiInput'].setValidators([
      Validators.required,
      Validators.pattern(/^-?\d+$/)
    ]);
    this.f['numRiallineamentoNazionale'].setValidators([
      Validators.required,
      Validators.pattern(/^-?\d+$/)
    ]);
    this.f['iDcategoriaAzeinda'].setValidators(Validators.required);
    this.f['numRiallineamentoNazionale'].setValue('0');
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

  private disabiliInForzaValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    let invalid: boolean = false;
    const disabiiliInForza: number = Number(this.f['disabForzaInput'].value);
    const baseComputo: number = Number(this.f['baseComputoInput'].value);
    if(baseComputo >= 0)
      invalid = disabiiliInForza < 0 || disabiiliInForza > baseComputo;

    return invalid ? {disabiliInForza: {disabiiliInForza}} : null;
  }

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



  getMsgByCode(code: string): string {
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code);
    return messaggio.dsSilapDMessaggio;
  }

  onClickReset() {
    this.form.reset();
    this.f['numRiallineamentoNazionale'].setValue('0');
    this.periodiSelezionati = [];
    this.selectAll = false;
  }

  async onClickElimina(periodo: VersamentoPvPeriodo) {
    this.alertMessageService.emptyMessages();
    const title = this.translate(marker('MODAL.TITLE.PERIODO'));
    const message = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100067);
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const mapParams= {
      0: this.datePipe.transform(periodo.dInizio, 'dd/MM/yyyy'),
      1: this.datePipe.transform(periodo.dFine, 'dd/MM/yyyy')
    }

    const messaggioReplaced = Utils.replacePlaceHolder(message.dsSilapDMessaggio,mapParams);

    const userChoice = await this.promptModalService.openPrompt(title, messaggioReplaced, pYes, pNo, 'warning');

    if (userChoice) {
      this.eliminaPriodo(periodo);

    } else {
      console.log(`non eliminare${JSON.stringify(this.versamentoPvPeriodo)}`);
    }
  }


  private eliminaPriodo(periodo: VersamentoPvPeriodo) {
    this.spinner.show();
    let request$: any;
    if(periodo.flgTipo === TIPO_PERIODO.AUTOMATICO)
      request$ = this.versamentoService.cancellaPeriodoAutomaticoProvincia(this.versamenetoProvinciaSelezionata.idEsoTVersamentoProvincia,periodo);
    else
      request$ = this.versamentoService.cancellaPeriodoProvincia(periodo);
      request$.subscribe({
        next: (res: any) => {
          if(res.esitoPositivo){

            const index = this.versamentoPvPeriodo.findIndex((item) => { return periodo.idEsoTVersamentoPvPeriodo === item.idEsoTVersamentoPvPeriodo; });
            this.versamentoPvPeriodo.splice(index,1);

            if(periodo.flgTipo === TIPO_PERIODO.AUTOMATICO){

              this.setSovrapposti(res.periodi);

              

            }

            this.onClickReset();

          }else{
            this.alertMessageService.setApiMessages(res.apiMessages);
          }
          this.spinner.hide();
        },
        error: (err: any) => {
          this.logService.error(this.constructor.name,`eliminaPriodo ${JSON.stringify(err)} `);
          this.spinner.hide();
        }
      });
  }

  private setSovrapposti(periodi: VersamentoPvPeriodo[]){
    this.versamentoPvPeriodo.forEach((periodo: VersamentoPvPeriodo) =>{
      periodi.forEach((periodoDb: VersamentoPvPeriodo) => {
        if(periodo.idEsoTVersamentoPvPeriodo === periodoDb.idEsoTVersamentoPvPeriodo){
          periodo.sovrapposto = periodoDb.sovrapposto;
        }
      });

    });
  }

  translate(key: string) {
    return this.translateService.instant(key);
  }

  getMessageByCodDecodificato(codMessaggio: string) {
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(codMessaggio);
    const mapParams: any = {
      0: this.versamento.annoRiferimento,
      1: this.versamento.dsDenominazioneAzienda,
      2: this.versamento.codFiscale
    };
    const message = Utils.replacePlaceHolder(messaggio.dsSilapDMessaggio, mapParams);
    return message;
  }


  //controllo sulle percentuali di esonero differenti 10015



  getSospensioneByPeriodo(dataInizio: Date, dataFine: Date): VersamentoPvSospensione{
    const sospensioni: VersamentoPvSospensione [] = this.versamenetoProvinciaSelezionata.esoTVersamentoPvSospensiones;
    let sospensione;
    if(sospensioni && sospensioni.length > 0){
      let dtInizio: Date = new Date(dataInizio);
      dtInizio.setHours(0,0,0,0);
      let dtFine: Date = new Date(dataFine)
      dtFine.setHours(0,0,0,0);
      for(let i: number = 0; i < sospensioni.length && Utils.isNullOrUndefined(sospensione); i++){
        let dtInizioSospensione: Date = new Date(sospensioni[i].dInizioSospensione);
        dtInizioSospensione.setHours(0,0,0,0);
        let dtFineSospensione: Date = new Date(sospensioni[i].dFineSospensione);
        dtFineSospensione.setHours(0,0,0,0);
        if(
          (dtInizio.valueOf() >= dtInizioSospensione.valueOf() && dtFine.valueOf() <= dtFineSospensione.valueOf())
        ){
          sospensione = sospensioni[i];
        }
      }
    }

    return sospensione;
  }


  async onClickConferma() {
    this.spinner.show();
    if (this.form.valid) {
      const nuovoVersamentoPvPeriodo: VersamentoPvPeriodo = {
        dInizio: this.f.dataInizio.value,
        dFine: this.f.dataFine.value,
        baseComputo: this.f.baseComputoInput.value,
        numDisabiliInForza: this.f.disabForzaInput.value,
        numSoggettiCompensati: this.f.numCompensatiInput.value,
        numEsoneratiAutocertificati: this.f.numEsoCertInput.value,
        flgTipo: 'O',
        numRiallineamentoNazionale: this.f.numRiallineamentoNazionale.value,
        silapDCategoriaAzienda: {
          id: Number(this.f.iDcategoriaAzeinda.value),
          codCategoriaAzienda: this.categoriaAziendaPipe.transform(this.f.iDcategoriaAzeinda.value)
        }
      };
      this.inviaPeriodo(nuovoVersamentoPvPeriodo);
    }
  }

  private inviaPeriodo(nuovoVersamentoPvPeriodo: VersamentoPvPeriodo) {
     this.versamentoService.inserisciPeriodoProvincia(this.versamenetoProvinciaSelezionata.idEsoTVersamentoProvincia,nuovoVersamentoPvPeriodo).subscribe({
       next: (res: any) =>{
         if(res.esitoPositivo){
           this.insertPeridoInTable(res.periodo);
           this.onClickReset();
         }
          else
            this.alertMessageService.setApiMessages(res.apiMessages);

           this.spinner.hide();
       },
       error: (err: any) =>{
         this.spinner.hide();
       }
     });

  }

  private insertPeridoInTable(versamentoPvPeriodo: VersamentoPvPeriodo) {
    let dInizio: Date = new Date(versamentoPvPeriodo.dInizio);
    dInizio.setHours(0,0,0,0);
    let indexFound = 0;
    this.versamentoPvPeriodo.forEach((vp: VersamentoPvPeriodo, index) => {
      if(vp.flgTipo !== 'C'){
        let dFinePeriodo = new Date(vp.dFine);
        dFinePeriodo.setHours(0, 0, 0, 0);
        if (dInizio.valueOf() > dFinePeriodo.valueOf()) {
          indexFound = index + 1;
        }
      }
    });
    this.insertItem = versamentoPvPeriodo;
    let periodiTmp = this.versamentoPvPeriodo.splice(0, indexFound, versamentoPvPeriodo);
    this.versamentoPvPeriodo = periodiTmp.concat(this.versamentoPvPeriodo);
    let pageNumber: number  = Math.floor(indexFound/this.paginationDataChange.limit)
    //senza ritardo sembra non funzionare
    pageNumber = pageNumber + 1;
    setTimeout(()=>{
        this.paginatedTable.goToPage(pageNumber);
        this.spinner.hide();
    },200);
  }

  onClickConfermaProsegui() {
    this.spinner.show();
     this.versamentoService.confermaVersamentoProvincie(this.versamento.idEsoTVersamento).subscribe({
       next: (response: any) => {
         if(response.esitoPositivo)
           this.emitConferma.emit(response.versamento);
         else{
           this.alertMessageService.setApiMessages(response.apiMessages);
           this.spinner.hide();
         }
       },
       error: (err) => {
         console.error(`${JSON.stringify(err)}`);
         this.spinner.hide();
       }
     });

  }

  async onClickRipristinaDati(){
    const messaggio: Messaggio = this.messageService.getMessaggioByCod('100101');
    const title = this.translate(marker('DICHIARAZIONE.MODAL.RIPRISTINO_DATI.TITLE'));
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));
    const mapParams = {
      0: this.versamenetoProvinciaSelezionata.silapDProvincia.dsSilapDProvincia,
    };

    const message = Utils.replacePlaceHolder(messaggio.dsSilapDMessaggio, mapParams);
    const userChoice = await this.promptModalService.openPrompt(title, message, pYes, pNo, 'warning');

    if (userChoice){
      this.modificaSospensioni();
    }

  }

  //Utilizzato per ripristinare i periodi automatici.
  modificaSospensioni(){
    this.spinner.show();
    this.versamentoService.modificaSospensioniProvincia(true,this.versamenetoProvinciaSelezionata).subscribe({
      next: (res: any) => {
        if (res.esitoPositivo){
          this.periodiSelezionati = [];
          this.selectAll = false;
          this.pristinePeriodi(res.versamento);
        }
        this.spinner.hide();
      },
      error: (error) => {
        this.spinner.hide();
        console.error(`onClickRipristinaDati ${error}`);
      }
    });
  }

  pristinePeriodi(versamento: Versamento) {
    const province: VersamentoProvincia[] = versamento.esoTVersamentoProvincias;
    this.versamentoPvPeriodo = [];
    const findProvinciaSelezionata: VersamentoProvincia = province.find((pv: VersamentoProvincia) => {
      return this.versamenetoProvinciaSelezionata.idEsoTVersamentoProvincia = pv.idEsoTVersamentoProvincia;
    });
    if(!Utils.isNullOrUndefined(findProvinciaSelezionata)){
      //this.versamenetoProvinciaSelezionata = findProvinciaSelezionata;
      findProvinciaSelezionata.esoTVersamentoPvPeriodos.sort((periodo1, periodo2) => {
        periodo1.dInizio = new Date(periodo1.dInizio);
        periodo2.dInizio = new Date(periodo2.dInizio);
        periodo1.dInizio.setHours(0,0,0,0);
        periodo2.dInizio.setHours(0,0,0,0);
        return periodo1.dInizio.valueOf() - periodo2.dInizio.valueOf();
      });
      this.versamentoPvPeriodo = Utils.clone(findProvinciaSelezionata.esoTVersamentoPvPeriodos);
    }
  }

  async onClickEliminaPeriodiSelezionati(){
    this.alertMessageService.emptyMessages();
    const title = this.translate(marker('MODAL.TITLE.PERIODO'));
    const message = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100126);
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const userChoice = await this.promptModalService.openPrompt(title, message.dsSilapDMessaggio, pYes, pNo, 'warning');

    if (userChoice)
      this.eliminaSelezionati();
  }

  private eliminaSelezionati() {
    this.spinner.show();
    this.alertMessageService.emptyMessages();
    this.versamentoService.cancellaPeriodiProvincia(this.versamenetoProvinciaSelezionata.idEsoTVersamentoProvincia, this.periodiSelezionati).subscribe({
      next: (res) => {
        if (res.esitoPositivo) {
          this.versamentoPvPeriodo = this.versamentoPvPeriodo.filter((item1: VersamentoPvPeriodo) => !this.periodiSelezionati.includes(item1));
          this.setSovrapposti(res.periodi);
          this.periodiSelezionati = [];
          this.selectAll = false;
        }
        this.alertMessageService.setApiMessages(res.apiMessages);
        this.spinner.hide();
      },
      error: (err) => {
        this.spinner.hide();
      }
    });
  }

  changePagination(paginationDataChange: PaginationDataChange){
    this.periodiSelezionati = [];
    this.selectAll = false;
    this.paginationDataChange = paginationDataChange;
  }

  isChecked(idEsoTVersamentoPvPeriodo: number){
    return this.periodiSelezionati.findIndex((el: VersamentoPvPeriodo) => {return el.idEsoTVersamentoPvPeriodo === idEsoTVersamentoPvPeriodo}) >= 0;
  }

  keyDownSelectAll(){
    this.selectAll = !this.selectAll;
    this.onClickSelectAll();
  }

  onClickSelectAll(){
    this.periodiSelezionati = [];

    if(this.selectAll){
      const startIndex = this.paginationDataChange.page * this.paginationDataChange.limit;
      const endIndex = startIndex + this.paginationDataChange.limit;
      this.periodiSelezionati = this.versamentoPvPeriodo.slice(startIndex,endIndex);
    }
  }

  onClickCheckBox(item: VersamentoPvPeriodo){
    this.selectAll = false;
    const indexFound: number = this.periodiSelezionati.findIndex((el: VersamentoPvPeriodo) => {return el.idEsoTVersamentoPvPeriodo === item.idEsoTVersamentoPvPeriodo});
    if(indexFound >= 0)
      this.periodiSelezionati.splice(indexFound,1);
    else
      this.periodiSelezionati.push(item);
  }

  private periodoValidator: ValidatorFn = (): {
    [key: string]: any;
  } | null => {
    const dataInizioControlValue = this.form.controls['dataInizio'].value;
    const dataFineControlValue = this.form.controls['dataFine'].value;
    let validPeriodi: boolean = true;
    let validEsoneri: boolean = true;
    let validConvenzioni: boolean = true;
    let validSospensioni: boolean = true;
    let invalid: boolean = false;
    let dataInizio: Date;
    if(!Utils.isNullOrUndefinedOrEmptyField(dataInizioControlValue))
      dataInizio = new Date(dataInizioControlValue);
    let dataFine: Date;
    if(!Utils.isNullOrUndefinedOrEmptyField(dataFineControlValue))
      dataFine = new Date(dataFineControlValue);
    if(dataInizio && !isNaN(dataInizio.valueOf()) && dataFine && !isNaN(dataFine.valueOf())){

      dataInizio.setHours(0,0,0,0);
      dataFine.setHours(0,0,0,0);


      for(let i = 0; i < this.versamentoPvPeriodo.length && validPeriodi; i++){

        if(this.versamentoPvPeriodo[i].flgTipo !== 'C'){
          let dataFinePeriodo: Date = new Date(this.versamentoPvPeriodo[i].dFine);
          dataFinePeriodo.setHours(0,0,0,0);
          let dataInizioPeriodo: Date = new Date(this.versamentoPvPeriodo[i].dInizio);
          dataInizioPeriodo.setHours(0,0,0,0);
          validPeriodi = dataInizio.valueOf() > dataFinePeriodo.valueOf() || (dataInizio.valueOf() < dataInizioPeriodo.valueOf() && dataFine.valueOf() < dataInizioPeriodo.valueOf());
        }
      }
      invalid = !validPeriodi;
    }
    return invalid ? {periodoValidator:{ invalidPeriodo: invalid}} : null;
  }


  getMsgValidazioneByCode(code: string): string{
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code) || {};
    return messaggio.dsSilapDMessaggio;
  }

}

