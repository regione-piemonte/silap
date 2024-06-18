/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, ElementRef, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { NgbNav } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { CATEGORIA_AZIENDA, COMPENSAZIONE_DISABILI, STORAGE_KEY, WIZARD_MODE } from 'src/app/constants';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { ApiMessage, Messaggio, Ruolo, Versamento, VersamentoEsoneriService, VersamentoProspetto, VersamentoProvincia, VersamentoPvProspetto, VersamentoPvSospensione } from 'src/app/modules/silapapi';
import { MESSAGGIO } from 'src/app/msg-constants';
import { LogService } from 'src/app/services/log.service';
import { MessageService } from 'src/app/services/message.service';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-province',
  templateUrl: './province.component.html',
  styleUrls: ['./province.component.scss']
})
export class ProvinceComponent implements OnInit {

  warnings: ApiMessage[];

  get VIEW_MODE(): boolean {
    return this.mode === WIZARD_MODE.VIEW;
  }

  get EDIT_MODE(): boolean {
    return this.mode === WIZARD_MODE.EDIT;
  }

  get INS_MODE(): boolean {
    return this.mode === WIZARD_MODE.INS;
  }

  @Input() mode: string;
  ruolo: Ruolo;
  @Input() initialVersamento: Versamento;
  versamentoCorrente: Versamento;

  @Output() emitConferma = new EventEmitter<Versamento>();

  messaggioAccordionInfo: Messaggio;
  versamenetoProvinciaSelezionata: VersamentoProvincia;

  versamentoProvince: VersamentoProvincia[];

  versamentoPvProspettoWrapper: VersamentoPvProspettoWrapper[];

  prospettoRiallineamentoNazionale: VersamentoProspetto;
  riallineamentoNazionale: number;

  form: FormGroup;
  active: number = 0;
  constructor(
    private messageService: MessageService,
    private spinner: NgxSpinnerService,
    private versamentoEsoneriService: VersamentoEsoneriService,
    private alertMessageService: AlertMessageService,
    private logService: LogService,
    private sessionStorageService: SessionStorageService
  ) { }

  ngOnInit(): void {
    this.spinner.show();
    this.messaggioAccordionInfo = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100001);
    this.versamentoCorrente = Utils.clone(this.initialVersamento);
    this.versamentoProvince = this.versamentoCorrente.esoTVersamentoProvincias;
    this.versamentoProvince.sort((verProv1: VersamentoProvincia,verProv2: VersamentoProvincia) => {
      return compare(verProv1.silapDProvincia.dsSilapDProvincia, verProv2.silapDProvincia.dsSilapDProvincia);
    });
    this.initForm();
    this.form.controls['versamentoProvincia'].patchValue(this.versamentoProvince[0]);
    this.onChangeProvincia();
    this.spinner.hide();
  }

  private initForm() {
    this.form = new FormGroup({
      versamentoProvincia: new FormControl()
    });
  }

  onChangeProvincia(){

    this.active = 0;
    this.versamenetoProvinciaSelezionata = this.form.controls['versamentoProvincia'].value;
    this.versamentoPvProspettoWrapper = [];
    if(!this.versamenetoProvinciaSelezionata)
      return;
    const prospetti: VersamentoProspetto[] = this.versamentoCorrente.esoTVersamentoProspettos;

    const annoRiallineamntoNazionale: number = prospetti.length === 1 ? prospetti[0].annoRiferimento : this.versamentoCorrente.annoRiferimento - 1;
    let prospettoRiallineamentoNazionale: VersamentoProspetto;
    let pvProspettoRiallineamentoNazionale: VersamentoPvProspetto;
    if(prospetti && prospetti.length > 0){
     prospetti.forEach((prospetto: VersamentoProspetto) => {
        if(prospetto.annoRiferimento === annoRiallineamntoNazionale)
          prospettoRiallineamentoNazionale = prospetto;
        prospetto.esoTVersamentoPvProspettos.forEach((pvProspetto: VersamentoPvProspetto) => {
         if(pvProspetto.silapDProvincia.id === this.versamenetoProvinciaSelezionata.silapDProvincia.id){
           this.versamentoPvProspettoWrapper.push(
            {
              annoRiferimento: prospetto.annoRiferimento,
              baseComputoProvinciale: pvProspetto.baseComputoProvinciale,
              quotaRiservaDisabili: pvProspetto.quotaRiservaDisabili,
              numDisabiliInForza: pvProspetto.numDisabiliInForza,
              numSoggettiCompensatiDisabili: pvProspetto.catCompensazioneDisabili === COMPENSAZIONE_DISABILI.RIDUZIONE ? "-"+pvProspetto.numSoggettiCompensatiDisabili : (pvProspetto.catCompensazioneDisabili === COMPENSAZIONE_DISABILI.ECCESSO ? "+"+pvProspetto.numSoggettiCompensatiDisabili : ""+pvProspetto.numSoggettiCompensatiDisabili),
              numEsoneratiAutocertificati: pvProspetto.numEsoneratiAutocertificati
            }
           );
           if(prospetto.annoRiferimento === annoRiallineamntoNazionale)
           pvProspettoRiallineamentoNazionale = pvProspetto;
         }
        });
     });
    }
    this.calcolaRiallineamentoNazionale(prospettoRiallineamentoNazionale,pvProspettoRiallineamentoNazionale);
  }

  confermaGiorniLavorativi(event: any, nav: NgbNav){
    if (this.active < 0)
      return;
    this.spinner.show();
      const versamentoProvincia: VersamentoProvincia = event;
      this.versamenetoProvinciaSelezionata.numGgLavorativiSettimanali = versamentoProvincia.numGgLavorativiSettimanali;
      this.versamenetoProvinciaSelezionata.dFestaPatronale = versamentoProvincia.dFestaPatronale;
      this.versamenetoProvinciaSelezionata.esoTVersamentoPvGgExtraFestivis = versamentoProvincia.esoTVersamentoPvGgExtraFestivis;

      const request$ = this.mode === WIZARD_MODE.INS ? this.versamentoEsoneriService.salvaGGLavorativiProvincia(versamentoProvincia) : this.versamentoEsoneriService.modificaGGLavorativiProvincia(versamentoProvincia);

      request$.subscribe({
         next: (res: any) =>{
           if(res.esitoPositivo){
             const versamentoSalvato: Versamento = res.versamento;
             this.setDispositions(versamentoSalvato);
             nav.select(this.active + 1);
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


  confermaSospensioni(event: any, nav: NgbNav) {
    if (this.active < 0)
      return;
    this.spinner.show();
    this.versamenetoProvinciaSelezionata.esoTVersamentoPvSospensiones = event;
    const request$ = this.mode === WIZARD_MODE.INS ? this.versamentoEsoneriService.salvaSospensioniProvincia(this.versamenetoProvinciaSelezionata) : this.versamentoEsoneriService.modificaSospensioniProvincia(false,this.versamenetoProvinciaSelezionata);
    request$.subscribe({
      next: (res: any) =>{
        if(res.esitoPositivo){
          const versamentoSalvato: Versamento = res.versamento;
          this.setDispositions(versamentoSalvato, res.apiMessages);
          nav.select(this.active + 1);
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



  private setDispositions(versamento: Versamento, warnings?: ApiMessage[]){
    this.initialVersamento = versamento;
    this.sessionStorageService.setItem(STORAGE_KEY.SESSION.VERSAMENTO,this.initialVersamento);
    this.versamentoCorrente = Utils.clone(this.initialVersamento);
    this.versamentoProvince = this.versamentoCorrente.esoTVersamentoProvincias;
    const versamenetoProvinciaSelezionataUpdated: VersamentoProvincia = this.versamentoProvince.find((verProv: VersamentoProvincia) => {
      return verProv.idEsoTVersamentoProvincia === this.versamenetoProvinciaSelezionata.idEsoTVersamentoProvincia;
    });
    this.versamenetoProvinciaSelezionata = versamenetoProvinciaSelezionataUpdated;
    this.form.controls['versamentoProvincia'].patchValue(this.versamenetoProvinciaSelezionata);
    if(warnings && warnings.length > 0)
      this.warnings = warnings;
    else
      this.warnings = [];
  }

  confermaDichiarazioneVersamento(versamento: Versamento){
    this.emitConferma.emit(versamento);
  }

  getMessageByCodDecodificato(codMessaggio: string) {
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(codMessaggio);
    const mapParams: any = {
      0: this.prospettoRiallineamentoNazionale.annoRiferimento,
      1: this.riallineamentoNazionale
    };
    const message = Utils.replacePlaceHolder(messaggio.dsSilapDMessaggio, mapParams);
    return message;
  }

  private calcolaRiallineamentoNazionale(prospetto: VersamentoProspetto, pvProspetto: VersamentoPvProspetto): void {

    if(prospetto && pvProspetto){
      const quotaRiservaProvinciale: number = pvProspetto.quotaRiservaDisabili;
      const categoriaAzienda: string = prospetto.categoriaAzienda;
      this.riallineamentoNazionale = 0;
      if(categoriaAzienda === CATEGORIA_AZIENDA.A.COD){
        const baseComputoProvinciale = Number(pvProspetto.baseComputoProvinciale);
        let quotadecimale = (baseComputoProvinciale * (7/100) - Math.floor(baseComputoProvinciale * (7/100))) ;
        const quotaDecimaleArrotondata = Number(quotadecimale.toPrecision(2));
        let baseComputoProvincialeSettePercent: number;
        if(quotaDecimaleArrotondata > 0.50){
          baseComputoProvincialeSettePercent = Math.round(baseComputoProvinciale * (7/100));
        }else{
          baseComputoProvincialeSettePercent = Math.floor(baseComputoProvinciale * (7/100));
        }

        this.riallineamentoNazionale = Math.round(quotaRiservaProvinciale -  (baseComputoProvincialeSettePercent));
      } else if(categoriaAzienda === CATEGORIA_AZIENDA.B.COD){
        this.riallineamentoNazionale = quotaRiservaProvinciale - 2;
      } else if(categoriaAzienda === CATEGORIA_AZIENDA.C.COD){
        this.riallineamentoNazionale = quotaRiservaProvinciale - 1;
      } else{
        this.riallineamentoNazionale = 0;
      }

      if(this.riallineamentoNazionale !== 0){
        this.prospettoRiallineamentoNazionale = prospetto;
      }else{
        this.prospettoRiallineamentoNazionale = null;
      }
    }
  }

}

interface VersamentoPvProspettoWrapper {
  annoRiferimento?: number,
  baseComputoProvinciale?: number,
  quotaRiservaDisabili?: number,
  numDisabiliInForza?: number,
  numSoggettiCompensatiDisabili?: string,
  numEsoneratiAutocertificati?: number
}

function compare(s1: string,s2: string): number{
  if(s1 > s2)
    return 1;
  else if(s1 < s2)
    return -1;
  else
    return 0;
}


