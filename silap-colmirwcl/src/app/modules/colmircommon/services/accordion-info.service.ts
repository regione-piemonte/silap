/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { MessageService } from 'src/app/services/message.service';
import { Messaggio } from '../../silapapi/model/messaggio';

@Injectable({
  providedIn: 'root'
})
export class AccordionInfoService {

  private readonly infoMessaggioSubject: Subject<Messaggio> = new Subject();

  constructor(
    private messageService: MessageService
  ) { }

  get infoMessaggio$(): Observable<Messaggio> { return this.infoMessaggioSubject.asObservable() };

  setMessaggioByCod(cod: string){
   const messaggio: Messaggio = this.messageService.getMessaggioByCod(cod);
   this.infoMessaggioSubject.next(messaggio);

  }

}
