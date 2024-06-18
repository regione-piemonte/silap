/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { SessionStorageService } from './storage/session-storage.service';
import { STORAGE_KEY } from '../constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  user: any

  constructor(
    private sessionStorageService: SessionStorageService
  ) { }


  setUserSessione(User: any){
    this.sessionStorageService.setItem(STORAGE_KEY.SESSION.UTENTE,User);
  }

  setRuoloSessione(ruolo: any){
    this.sessionStorageService.setItem(STORAGE_KEY.SESSION.RUOLO,ruolo);
  }

  getUserSessione(){
    return this.sessionStorageService.getItem(STORAGE_KEY.SESSION.UTENTE);
  }

  getRuoloSessione(){
    return this.sessionStorageService.getItem(STORAGE_KEY.SESSION.RUOLO);
  }

  rilasciaUser(){
    this.sessionStorageService.clearItems(STORAGE_KEY.SESSION.UTENTE,STORAGE_KEY.SESSION.RUOLO);
  }  




}
