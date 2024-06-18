/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { ApiError, ApiMessage } from '../../silapapi';
import { TYPE_ALERT } from 'src/app/constants';


@Injectable({
  providedIn: 'root'
})
export class AlertMessageService {

  //private apiMessages: Subject<ApiMessage[]> = new Subject<ApiMessage[]>();

  private apiMessages: BehaviorSubject<ApiMessage[]> = new BehaviorSubject<ApiMessage[]>(undefined);

  get apiMessages$(): Observable<ApiMessage[]> { return this.apiMessages.asObservable(); }

  private apiErrors: BehaviorSubject<ApiError[]> = new BehaviorSubject<ApiError[]>(undefined);

  get apiErrors$(): Observable<ApiError[]> { return this.apiErrors.asObservable(); }


  constructor() { }


  public setApiMessages(messages: ApiMessage[]){
    this.apiMessages.next(messages);
  }

  public setApiErrors(error: ApiError[]){
    this.apiErrors.next(error);
  }

  public unsubscribeAll(): void{
    this.apiMessages.unsubscribe();
    this.apiErrors.unsubscribe();
  }

  public unsubscribeApiMessages(): void{
    this.apiMessages.unsubscribe();
  }

  public unsubscribeApiErrors(): void{
    this.apiErrors.unsubscribe();
  }

  public emptyMessages(){
    this.apiMessages.next(undefined);
    this.apiErrors.next(undefined);
  }


  public setSingleErrorMessage(message: string){
    const apiMessage = {
      code: 'deafult',
      message: message,
      tipo: TYPE_ALERT.ERROR,
      error: false
    }
    let apiMessages: ApiMessage[] = [];
    apiMessages.push(apiMessage);
    this.setApiMessages(apiMessages);
  }

  public setSingleWarningMessage(message: string){
    const apiMessage = {
      code: 'deafult',
      message: message,
      tipo: TYPE_ALERT.WARNING,
      error: false
    }
    let apiMessages: ApiMessage[] = [];
    apiMessages.push(apiMessage);
    this.setApiMessages(apiMessages);
  }

  public setSingleSuccessMessage(message: string){
    const apiMessage = {
      code: 'deafult',
      message: message,
      tipo: TYPE_ALERT.SUCCESS,
      error: false

    }
    let apiMessages: ApiMessage[] = [];
    apiMessages.push(apiMessage);
    this.setApiMessages(apiMessages);
  }

  public setSingleInfoMessage(message: string){
    const apiMessage = {
      code: 'deafult',
      message: message,
      tipo: TYPE_ALERT.INFO,
      error: false

    }
    let apiMessages: ApiMessage[] = [];
    apiMessages.push(apiMessage);
    this.setApiMessages(apiMessages);
  }




}
