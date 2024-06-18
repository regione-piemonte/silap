/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { VersamentoEsoneriService } from '../../silapapi/api/versamentoEsoneri.service';
import {Observable} from 'rxjs/Rx';
import { SessionStorageService } from 'src/app/services/storage/session-storage.service';
import { Parametro } from '../../silapapi';
import { Utils } from 'src/app/utils';

@Injectable({
  providedIn: 'root'
})
export class ParametroService {



  constructor(
    private versamentoService:VersamentoEsoneriService,
    private sessionStorageService: SessionStorageService
  ) { }


  loadParametri(codParametri: string[]){
    let requests$: any[] = [];
    codParametri.forEach((cod: string) => {
      requests$.push(this.getRequest(cod));
    });
    Observable.forkJoin(requests$)
     .subscribe({
      next: (multiResponse: any[]) => {
        multiResponse.forEach(singleRes => {
          if(singleRes.esitoPositivo)
            this.sessionStorageService.setItem(singleRes.parametro.codParametro,singleRes.parametro);
        });
      },
      error: (err) => {}
     });
  }

  private getRequest(cod: string) {
    return this.versamentoService.getParametroByCod(cod);
  }


  async getParametroByCod(cod: string): Promise<Parametro>{
    let parametro: Parametro = this.sessionStorageService.getItem(cod);
    if(Utils.isNullOrUndefined(parametro)){
      this.loadParametri([cod]);
      parametro = await this.sessionStorageService.getItem(cod);
    }
    return parametro;
  }

}
