/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { LogService } from '../log.service';
import { UserService } from '../user.service';
import { Ruolo } from 'src/app/modules/silapapi';
import { HEADER_KEYS } from 'src/app/constants';

@Injectable()
export class UtenteInterceptor implements HttpInterceptor {

  ruolo: Ruolo;

  constructor(
    private logService: LogService,
    private userService: UserService

  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.logService.debug(this.constructor.name,`intercept`);
    this.ruolo = this.userService.getRuoloSessione();
    const modifiedReq = request.clone({ headers: request.headers.append(HEADER_KEYS.ID_RUOLO_KEY, this.ruolo ? String(this.ruolo.idSilapDRuolo) : '0')
      .append(HEADER_KEYS.COD_FISC_SOGG_ABILITATO, this.ruolo && this.ruolo.codiceFiscaleSoggettoAbilitato ? this.ruolo.codiceFiscaleSoggettoAbilitato : '0')
  });
    return next.handle(modifiedReq);
  }
}
