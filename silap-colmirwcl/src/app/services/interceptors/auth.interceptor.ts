/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpResponse
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LogService } from '../log.service';
import { Router } from '@angular/router';
import { ApiMessage } from 'src/app/modules/silapapi';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private logService: LogService,
    private _router: Router
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.logService.debug(this.constructor.name,`intercept`);
    return next.handle(request).pipe(map((event: HttpEvent<any>) => {
      if(event instanceof HttpResponse){
        if(event.body){
          this.checkBody(event.body);
        }
      }
      return event;
    }));
  }

  private checkBody(body: any){
    this.logService.debug(this.constructor.name,`checkBody`,body);
    const esitoPositivo = body.esitoPositivo;
    if(!esitoPositivo){
      const apiMessages: ApiMessage[] = body.apiMessages;
      if(apiMessages && apiMessages.length > 0){
        apiMessages.forEach((m: ApiMessage) => {
          if(m.code === "ERR_USER"){
            this._router.navigate(['error'], { state: {apiError: m} });
          }
          return undefined;
        });
      }
    }
  }
}
