/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, Input, OnInit } from '@angular/core';
import { ApiError } from 'src/app/modules/silapapi/model/apiError';
import { AlertMessageService } from '../../services/alert-message.service';
import { Utils } from 'src/app/utils';
import { ApiMessage } from 'src/app/modules/silapapi';


@Component({
  selector: 'colmirwcl-alert-message',
  templateUrl: './alert-message.component.html',
  styleUrls: ['./alert-message.component.scss']
})
export class AlertMessageComponent implements OnInit {

  show: boolean = false;
  typeMesage: string;
  private static readonly SCROLL_TARGET = 'em[data-scroll-marker="top"]';
  private static readonly MODAL_SCROLL_TARGET = 'em[data-scroll-marker="modal-top"]';

  @Input() flgModalTarget: boolean;
  listErrorMsg: ApiError[] = [];
  listApiMessage: ApiMessage[] = [];

  constructor(
    private alertMessageService: AlertMessageService,
  ) { }

  ngOnInit():void {
    console.log('ngOnInit alert-message.component');
    this.alertMessageService.apiMessages$.subscribe((messages: ApiMessage[]) => {
      console.log('subscribe apiMessages');
      this.reset();
      if(messages && messages.length > 0){
        this.listApiMessage = Utils.clone(messages);
        this.show = true;
        this.scrollTo();
      }
    });

    this.alertMessageService.apiErrors$.subscribe((errors: ApiError[]) => {
      console.log('subscribe apiErrors');
      this.reset();
      if(errors && errors.length > 0){
        this.listErrorMsg = Utils.clone(errors);
        this.show = true;
        this.scrollTo();
      }
    });
  }

  ngOnDestroy(): void {
    this.reset();
  }


  private reset(){
    this.show = false;
    this.listErrorMsg = [];
    this.listApiMessage = [];
  }

  private scrollTo(){
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}

