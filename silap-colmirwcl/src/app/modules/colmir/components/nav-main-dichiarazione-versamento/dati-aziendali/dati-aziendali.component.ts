/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { TranslateService } from '@ngx-translate/core';


import { NgxSpinnerService } from 'ngx-spinner';
import { Observable } from 'rxjs';
import { CONTROL_STATE, MONTH } from 'src/app/constants';
import { WIZARD_MODE } from 'src/app/constants';
import { RUOLO } from 'src/app/constants';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { ParametroService } from 'src/app/modules/colmircommon/services/parametro.service';
import { PromptModalService } from 'src/app/modules/colmircommon/services/prompt-modal.service';
import { Azienda, Messaggio, Parametro, Ruolo, Versamento, VersamentoEsoneriService } from 'src/app/modules/silapapi';
import { MESSAGGIO } from 'src/app/msg-constants';
import { PARAMETRO } from 'src/app/parametri-constants';
import { LogService } from 'src/app/services/log.service';
import { MessageService } from 'src/app/services/message.service';
import { UserService } from 'src/app/services/user.service';
import { UtilitiesService } from 'src/app/services/utilities.service';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-dati-aziendali',
  templateUrl: './dati-aziendali.component.html',
  styleUrls: ['./dati-aziendali.component.scss']
})
export class DatiAziendaliComponent implements OnInit {

  @Input() mode: string;
  ruolo: Ruolo;
  @Input() initialVersamento: Versamento;
  versamentoCorrente: Versamento;

  @Output() emitConferma = new EventEmitter<Versamento>();

  initialAzienda: Azienda = {
    comuneSede: {
      id: "",
      descr: ""
    },
    silapDCcnl: {
      cod: "",
      descr: ""
    }
  }

  aziendaCorrente: Azienda;


  form: FormGroup;
  messaggioAccordionInfo: Messaggio;
  currentDate: Date;
  annoCorrenteValue: number;
  annoPrecedenteValue: number;
  emailRegex: string;

  get f() {
    return this.form.controls as any;
  }

  get AZIENDA_LOCKED(): boolean {
    return this.ruolo.idSilapDRuolo === RUOLO.DELEGATO.ID || this.ruolo.idSilapDRuolo === RUOLO.PERSONA_AUTORIZZATA.ID || this.ruolo.idSilapDRuolo === RUOLO.LEGALE_RAPPRESENTANTE.ID
  }

  get VIEW_MODE(): boolean {
    return this.mode === WIZARD_MODE.VIEW;
  }

  get EDIT_MODE(): boolean {
    return this.mode === WIZARD_MODE.EDIT;
  }

  get INS_MODE(): boolean {
    return this.mode === WIZARD_MODE.INS;
  }


  get DATI_AZIENDALI_INVALID(): boolean {
    if (this.form.invalid) {
      return true;
    }
    return Utils.isNullOrUndefined(this.aziendaCorrente) || Utils.isNullOrUndefined(this.aziendaCorrente.codFiscale) || this.aziendaCorrente.codFiscale !== this.f.codFiscale.value;
  }

  get IS_OPERATORE_REGIONALE(): boolean{
    return this.ruolo.idSilapDRuolo === RUOLO.REGIONE.ID;
  }

  constructor(
    private userService: UserService,
    private spinner: NgxSpinnerService,
    private versamentoService: VersamentoEsoneriService,
    private logService: LogService,
    private alertMessageService: AlertMessageService,
    private messageService: MessageService,
    private utilitiesService: UtilitiesService,
    private parametroService: ParametroService,
    private promptModalService: PromptModalService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.currentDate = new Date(this.utilitiesService.getCurrentDate());
    this.messaggioAccordionInfo = this.messageService.getMessaggioByCod(MESSAGGIO.COD_100001);
    this.versamentoCorrente = Utils.clone(this.initialVersamento);
    this.aziendaCorrente = Utils.clone(this.initialAzienda);
    this.ruolo = this.userService.getRuoloSessione();
    this.determinaAnnoDiRiferimento();
    this.asyncNgOnInit()
      .finally(() => {
        this.initForm();
        this.patchValueInForm();
        this.setFormState();
        if (this.mode === WIZARD_MODE.VIEW)
          this.spinner.hide();
      });

  }

  private async asyncNgOnInit() {
    const mialc: Parametro = await this.parametroService.getParametroByCod(PARAMETRO.COD.MAILC);
    this.emailRegex = mialc.dsValore;
  }

  private determinaAnnoDiRiferimento() {
    const currentMonth: number = this.currentDate.getMonth();
    const currentYear: number = this.currentDate.getFullYear();
    const previousYear: number = this.currentDate.getFullYear() - 1;
    this.annoPrecedenteValue = previousYear;
    if (currentMonth !== MONTH.GENNAIO && currentMonth !== MONTH.FEBBRAIO)
      this.annoCorrenteValue = currentYear;
  }

  private setFormState() {
    if (this.mode === WIZARD_MODE.VIEW)
      this.form[CONTROL_STATE.DISABLE]();
    else if (this.mode === WIZARD_MODE.INS) {
      if (this.AZIENDA_LOCKED) {
        this.form.controls['codFiscale'][CONTROL_STATE.DISABLE]();
      }
      if (Utils.isNullOrUndefinedOrEmptyField(this.annoCorrenteValue)) {
        this.form.controls['annoRiferimento'][CONTROL_STATE.DISABLE]();
      }
    } else if (this.mode === WIZARD_MODE.EDIT) {
      this.form.controls['codFiscale'][CONTROL_STATE.DISABLE]();
    }
  }

  private patchValueInForm() {

    if (this.AZIENDA_LOCKED)
      this.f.codFiscale.patchValue(this.ruolo.codiceFiscaleSoggettoAbilitato);
    else
      this.f.codFiscale.patchValue(this.initialVersamento.codFiscale);

    let res$: Observable<Azienda>;
    if (this.mode === WIZARD_MODE.EDIT || this.mode === WIZARD_MODE.INS)
      res$ = this.cercaAzienda$();
    else if (this.mode === WIZARD_MODE.VIEW) {
      this.aziendaCorrente.codFiscale = this.versamentoCorrente.codFiscale;
      this.initialAzienda.indirizzoSede = this.versamentoCorrente.versamentoSede.dsIndirizzo;
      this.initialAzienda.capSede = this.versamentoCorrente.versamentoSede.codCap;
      this.initialAzienda.denomAzienda = this.versamentoCorrente.dsDenominazioneAzienda;
      this.initialAzienda.idSilAziAnagrafica = this.versamentoCorrente.idSilAziAnagrafica;
      this.initialAzienda.comuneSede = this.versamentoCorrente.versamentoSede.comune;
      this.initialAzienda.silapDCcnl = this.versamentoCorrente.silapDCcnl ? this.versamentoCorrente.silapDCcnl : null;
      this.initialAzienda.idSilAziSede = this.versamentoCorrente.versamentoSede.idSilAziSede;
      this.initialAzienda.ragioneSocialeSede = this.versamentoCorrente.versamentoSede.dsDenominazioneSede;
      this.initialAzienda.numCreditoResiduo = this.versamentoCorrente.numCreditoResiduo;
      this.initialAzienda.idEsoTCreditoResiduo = this.versamentoCorrente.esoTCreditoResiduo ? this.versamentoCorrente.esoTCreditoResiduo.idEsoTCreditoResiduo : null;
      this.aziendaCorrente = Utils.clone(this.initialAzienda);
    }
    if (res$) {
      this.spinner.show();
      res$.subscribe({
        next: (res: any) => {
          if (res.esitoPositivo) {
            const azienda: Azienda = res.azienda;
            if (!Utils.isNullOrUndefinedOrEmptyField(azienda)) {

              this.initialAzienda.codFiscale = azienda.codFiscale;
              this.f.codFiscale.patchValue(azienda.codFiscale);
              this.initialAzienda.indirizzoSede = azienda.indirizzoSede;
              this.initialAzienda.capSede = azienda.capSede;
              this.initialAzienda.denomAzienda = azienda.denomAzienda;
              this.initialAzienda.idSilAziAnagrafica = azienda.idSilAziAnagrafica;
              this.initialAzienda.comuneSede = azienda.comuneSede;
              this.initialAzienda.silapDCcnl = azienda.silapDCcnl;
              this.initialAzienda.idSilAziSede = azienda.idSilAziSede;
              this.initialAzienda.ragioneSocialeSede = azienda.ragioneSocialeSede;
              this.initialAzienda.numCreditoResiduo = azienda.numCreditoResiduo;
              this.initialAzienda.idEsoTCreditoResiduo = azienda.idEsoTCreditoResiduo;
              this.aziendaCorrente = Utils.clone(this.initialAzienda);

            } else {
              this.initialAzienda = null;
            }

          } else {
            this.alertMessageService.setApiMessages(res.apiMessages);
          }
          this.spinner.hide();
        },
        error: (error: any) => {
          this.logService.error(this.constructor.name, `loadMessage: ${JSON.stringify(error)}`);
          this.spinner.hide();
        }
      });
    }
    this.f.numRate.patchValue(this.versamentoCorrente.numRate);
    this.f.dsEmailRiferimento.patchValue(this.versamentoCorrente.dsEmailRiferimento);
    if (Utils.isNullOrUndefinedOrEmptyField(this.annoCorrenteValue)) {
      this.f.annoRiferimento.patchValue(this.annoPrecedenteValue);
    } else {
      this.f.annoRiferimento.patchValue(this.versamentoCorrente.annoRiferimento);
    }
  }

  private initForm() {
    this.form = new FormGroup({
      codFiscale: new FormControl(null, [
        Validators.required,
        Validators.minLength(11),
      ]),
      annoRiferimento: new FormControl(null, Validators.required),
      dsEmailRiferimento: new FormControl(null, [
        Validators.required,
        Validators.pattern(this.emailRegex)]),
      numRate: new FormControl(null, Validators.required)
    });
  }

  onClickCercaAzienda() {
    const res = this.cercaAzienda$();
    if (!Utils.isNullOrUndefined(res)) {
      res.subscribe({
        next: (res: any) => {
          if (res.esitoPositivo) {
            const azienda: Azienda = res.azienda;
            this.aziendaCorrente.codFiscale = azienda.codFiscale;
            this.f.codFiscale.patchValue(azienda.codFiscale);
            this.aziendaCorrente.indirizzoSede = azienda.indirizzoSede;
            this.aziendaCorrente.capSede = azienda.capSede;
            this.aziendaCorrente.denomAzienda = azienda.denomAzienda;
            this.aziendaCorrente.idSilAziAnagrafica = azienda.idSilAziAnagrafica;
            this.aziendaCorrente.comuneSede = azienda.comuneSede;
            this.aziendaCorrente.idSilAziSede = azienda.idSilAziSede;
            this.aziendaCorrente.silapDCcnl = azienda.silapDCcnl;
            this.aziendaCorrente.numCreditoResiduo = azienda.numCreditoResiduo;
            this.aziendaCorrente.idEsoTCreditoResiduo = azienda.idEsoTCreditoResiduo;

            this.aziendaCorrente.ragioneSocialeSede = azienda.ragioneSocialeSede;
          } else {
            this.alertMessageService.setApiMessages(res.apiMessages);
          }
          this.spinner.hide();
        },
        error: (error: any) => {
          this.logService.error(this.constructor.name, `loadMessage: ${JSON.stringify(error)}`);
          this.spinner.hide();
        }
      });
    }
  }


  cercaAzienda$(): Observable<Azienda> {
    this.alertMessageService.emptyMessages();
    const codFiscale: string = this.f.codFiscale.value;
    if (Utils.isNullOrUndefinedOrEmptyField(codFiscale))
      return undefined;
    this.spinner.show();
    return this.versamentoService.ricercaAzienda(codFiscale, this.versamentoCorrente.idEsoTVersamento);
  }

  onClickReset() {
    this.alertMessageService.emptyMessages();
    this.form.reset();
    this.aziendaCorrente = Utils.clone(this.initialAzienda);
    this.f.codFiscale.patchValue(this.aziendaCorrente.codFiscale);
    this.f.annoRiferimento.patchValue(this.versamentoCorrente.annoRiferimento);
    this.f.numRate.patchValue(this.versamentoCorrente.numRate);
    this.f.dsEmailRiferimento.patchValue(this.versamentoCorrente.dsEmailRiferimento);
  }

  onClickConferma() {
    this.spinner.show();
    this.alertMessageService.emptyMessages();
    let versamentoDaSalvare: Versamento = Utils.clone(this.versamentoCorrente);
    versamentoDaSalvare.codFiscale = this.aziendaCorrente.codFiscale;
    versamentoDaSalvare.dsDenominazioneAzienda = this.aziendaCorrente.denomAzienda;
    versamentoDaSalvare.idSilAziAnagrafica = this.aziendaCorrente.idSilAziAnagrafica;
    versamentoDaSalvare.versamentoSede.codCap = this.aziendaCorrente.capSede;
    versamentoDaSalvare.versamentoSede.idSilAziSede = this.aziendaCorrente.idSilAziSede;
    versamentoDaSalvare.versamentoSede.dsIndirizzo = this.aziendaCorrente.indirizzoSede;
    versamentoDaSalvare.versamentoSede.dsDenominazioneSede = this.aziendaCorrente.ragioneSocialeSede;
    versamentoDaSalvare.versamentoSede.comune = this.aziendaCorrente.comuneSede;
    versamentoDaSalvare.annoRiferimento = this.f.annoRiferimento.value;
    versamentoDaSalvare.numRate = this.f.numRate.value;
    versamentoDaSalvare.dsEmailRiferimento = this.f.dsEmailRiferimento.value;
    versamentoDaSalvare.numCreditoResiduo = this.aziendaCorrente.numCreditoResiduo;
    if (!Utils.isNullOrUndefinedOrEmptyField(this.aziendaCorrente.idEsoTCreditoResiduo)) {
      versamentoDaSalvare.esoTCreditoResiduo = {
        idEsoTCreditoResiduo: this.aziendaCorrente.idEsoTCreditoResiduo,
        numValore: this.aziendaCorrente.numCreditoResiduo,
        codFiscale: this.aziendaCorrente.codFiscale
      }
    }
    versamentoDaSalvare.silapDCcnl = this.aziendaCorrente.silapDCcnl;
    if (this.aziendaCorrente.silapDCcnl && !this.aziendaCorrente.silapDCcnl.id)
      versamentoDaSalvare.silapDCcnl = null;
    if (versamentoDaSalvare.codFiscale.length == 11) {
      versamentoDaSalvare.partivaIva = versamentoDaSalvare.codFiscale;
    }
    const request$: Observable<Versamento> = this.mode === WIZARD_MODE.INS ? this.versamentoService.salvaDatiAziendali(versamentoDaSalvare) : this.versamentoService.controlloModifica(versamentoDaSalvare);

    request$.subscribe({
      next: (res: any) => {
        if (res.esitoPositivo) {
          const versamentoSalvato: Versamento = res.versamento;
          if (
            this.mode === WIZARD_MODE.EDIT &&
            res.apiMessages &&
            res.apiMessages.length > 0
          ) {
            this.openPopUpModifica(versamentoDaSalvare, res.apiMessages[0].title , res.apiMessages[0].message);
          } else
            this.emitConfermaSalvataggio(versamentoSalvato)
        } else {
          this.alertMessageService.setApiMessages(res.apiMessages);
        }
        this.spinner.hide();
      },
      error: (error: any) => {
        this.logService.error(this.constructor.name, `onClickConferma: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });

  }

  async openPopUpModifica(versamento: Versamento, dsTitolo: string, mesg: string) {
    const title = dsTitolo;
    const message = mesg;
    const pYes = this.translate(marker('APP.YES'));
    const pNo = this.translate(marker('APP.NO'));

    const userChoice = await this.promptModalService.openPrompt(title, message, pYes, pNo, 'warning');

    if (userChoice) {
      this.goToModifica(versamento);
    }
  }

  goToModifica(versamento: Versamento) {
    this.spinner.show();
    this.versamentoService.modificaDatiAziendali(versamento).subscribe({
      next: (res: any) => {
        if (res.esitoPositivo) {
          const versamentoSalvato: Versamento = res.versamento;
          this.emitConfermaSalvataggio(versamentoSalvato)
        } else {
          this.alertMessageService.setApiMessages(res.apiMessages);
        }
        this.spinner.hide();
      },
      error: (error: any) => {
        this.logService.error(this.constructor.name, `onClickConferma: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }

  translate(key: string) {
    return this.translateService.instant(key);
  }

  private emitConfermaSalvataggio(versamento: Versamento) {
    this.emitConferma.emit(versamento);
  }

  getMsgValidazioneByCode(code: string): string {
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code);
    return messaggio.dsSilapDMessaggio;
  }

}
