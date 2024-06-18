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
import { Utils } from 'src/app/utils';
import { HTTP_RESPONSE, TYPE_ALERT } from 'src/app/constants';
import { catchError } from 'rxjs/operators';
import { ErrorObservable } from 'rxjs/observable/ErrorObservable';
import { Router } from '@angular/router';
import { ApiMessage } from 'src/app/modules/silapapi';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {

  constructor(
    private logService: LogService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.logService.debug(this.constructor.name,`intercept`,request);
    return next.handle(request).pipe(
      catchError((err: any) => this.handleError(this,request,err))
    );
  }

  private handleError(interceptor: ErrorHandlerInterceptor, req: HttpRequest<any>, err: any): Observable<any>{
    this.logService.debug(this.constructor.name,`handleError`,err);
    if(!Utils.isNullOrUndefined(err) && !err.ok){
      if(err.status === HTTP_RESPONSE.SATUS.UNKNOW_ERROR){

      }else if(err.status === HTTP_RESPONSE.SATUS.INTERNAL_SERVER_ERROR){

      }else if(err.status === HTTP_RESPONSE.SATUS.NOT_FOUND){

      }
      const error: ApiMessage = {
        code: err.status,
        error: true,
        message: err.message,
        tipo: TYPE_ALERT.ERROR
      }
      interceptor.router.navigate(['/error'], {state: {apiError: error}})
    }

    return ErrorObservable.create(err);
  }
}

