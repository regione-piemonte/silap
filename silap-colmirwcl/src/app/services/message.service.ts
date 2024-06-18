/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { Messaggio, MessaggioService } from '../modules/silapapi';
import { Utils } from '../utils';
import { async } from 'rxjs';
import { SessionStorageService } from './storage/session-storage.service';
import { STORAGE_KEY } from '../constants';

@Injectable({
  providedIn: 'root'
})
export class MessageService {


  private msgMap: any;

  constructor(
    private messaggioService: MessaggioService,
    private sessionStorage: SessionStorageService
  ) {

  }


  //TODO completare
 loadMessages() {
    if(Utils.isNullOrUndefined(this.msgMap)){
      this.messaggioService.findAll().subscribe({
        next: (res: any) =>{
          this.msgMap = res.msgMap;
          this.sessionStorage.setItem(STORAGE_KEY.SESSION.MESSAGGI,this.msgMap);
        },
        error: (error) => {

        },
        complete() {

        }
      });
    }
  }


  getMessaggioByCod(cod: string): Messaggio{
    this.msgMap = this.sessionStorage.getItem(STORAGE_KEY.SESSION.MESSAGGI);
    if(!this.msgMap)
      this.loadMessages();
    return this.msgMap ? this.msgMap[cod] : {};
  }


}
