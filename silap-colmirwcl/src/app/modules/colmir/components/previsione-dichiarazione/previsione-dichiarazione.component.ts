/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { DOWNLOAD_FILE_NAME } from 'src/app/constants';
import { AlertMessageService } from 'src/app/modules/colmircommon/services/alert-message.service';
import { ParametroService } from 'src/app/modules/colmircommon/services/parametro.service';
import { ApiMessage, Messaggio } from 'src/app/modules/silapapi';
import { Parametro } from 'src/app/modules/silapapi/model/parametro';
import { PARAMETRO } from 'src/app/parametri-constants';
import { CustomVersamentoEsoneriService } from 'src/app/services/custom-versamento-esoneri.service';
import { MessageService } from 'src/app/services/message.service';
import { UtilitiesService } from 'src/app/services/utilities.service';

@Component({
  selector: 'colmirwcl-previsione-dichiarazione',
  templateUrl: './previsione-dichiarazione.component.html',
  styleUrls: ['./previsione-dichiarazione.component.scss']
})
export class PrevisioneDichiarazioneComponent implements OnInit,OnDestroy {

  @ViewChild('validatedXlsxFile', { static: false }) fileInput: HTMLInputElement;
  emailRegex: string;

  get INVALID_FILE(): boolean {
    return this.validateFile('fileXLSX');
  }

  annoCorrente: number;

  get f() {
    return this.form.controls as any;
  }

  constructor(
    private parametroService: ParametroService,
    private spinner: NgxSpinnerService,
    private utilitiesService: UtilitiesService,
    private customVersamentoEsoneriService: CustomVersamentoEsoneriService,
    private messageService: MessageService,
    private alertMessageService: AlertMessageService
  ) { }


  ngOnDestroy(): void {
    this.alertMessageService.emptyMessages();
  }

  form: FormGroup;
  fileXLSX: File;

  ngOnInit(): void {
    const now: Date = new Date();
    this.annoCorrente = now.getFullYear();
    this.asyncNgOnInit().finally(() => {
      this.initForm();
    });

  }


  private async asyncNgOnInit() {
    const mialc: Parametro = await this.parametroService.getParametroByCod(PARAMETRO.COD.MAILC);
    this.emailRegex = mialc.dsValore;
  }

  private initForm() {
    this.form = new FormGroup({
      file: new FormControl(null,Validators.required),
      anno: new FormControl(this.annoCorrente,Validators.required),
      email: new FormControl(null,[
        Validators.required,
        Validators.pattern(this.emailRegex)
      ])
    });
  }

  onClickReset(){
    this.alertMessageService.emptyMessages();
    this.form.reset();
    this.form.controls['anno'].patchValue(this.annoCorrente);
    this.fileXLSX = null;
  }

  onClickUploadFile(){
    this.spinner.show();
    this.alertMessageService.emptyMessages();
    const email: string = this.form.controls['email'].value;
    const anno: string = this.form.controls['anno'].value;
    this.customVersamentoEsoneriService.uploadPrevisioneDichiarazioniCustom(anno, this.fileXLSX, email).subscribe({
      next: (res: any) => {
        if (res.esitoPositivo) {

          const msg: Messaggio = res.msg;
          const apiMessage: ApiMessage = {
            code: '0000',
            error: false,
            message: msg.dsSilapDMessaggio,
            tipo: msg.silapDTipoMessaggio.idSilapDTipoMessaggio
          }

          let apiMessages: ApiMessage[] = [];
          apiMessages.push(apiMessage);

          this.alertMessageService.setApiMessages(apiMessages);

          this.form.reset();
          this.form.controls['anno'].patchValue(this.annoCorrente);
          this.fileXLSX = null;

        }else
          this.alertMessageService.setApiMessages(res.apiMessages);
        this.spinner.hide();
      },
      error: (error) => {
        console.error(`onClickUploadFile ${error}`);
      }
    });
  }


  onClickDownloadFile(){
    this.spinner.show();
    this.alertMessageService.emptyMessages();
    this.customVersamentoEsoneriService.downloadPrevisioneDichiarazioniTemplate('response').subscribe({
      next: (res: any) => {
        console.log(`onClickDownloadFile: ${JSON.stringify(res)}`)
        this.utilitiesService.downloadBlobFile(`${DOWNLOAD_FILE_NAME.TEMPLATE_PREVISIONI}.xlsx`, res.body);
        this.spinner.hide();
      },
      error: (error) => {
        console.log(this.constructor.name, `errore onClickEsportaFile: ${JSON.stringify(error)}`);
        this.spinner.hide();
      }
    });
  }

  chooseFile(event: any) {
    this.fileXLSX = event.target.files[0];
  }

  validateFile(nomeFile: string) {
    let valid: boolean = false;
    if (nomeFile === 'fileXLSX' && this.fileXLSX) {
      if (this.fileXLSX.name.toLowerCase().endsWith('.xlsx')) {
        valid = false;
      } else {
        valid = true;
      }
    }
    return valid;
  }

  getMsgValidazioneByCode(code: string): string{
    const messaggio: Messaggio = this.messageService.getMessaggioByCod(code);
    return messaggio.dsSilapDMessaggio;
  }

}
