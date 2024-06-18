/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, CanLoad, Route, Router, RouterStateSnapshot, UrlSegment, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Utils } from '../utils';
import { ROUTE } from '../constants';
import { LogService } from '../services/log.service';
import { UserService } from '../services/user.service';

@Injectable({
  providedIn: 'root'
})
export class RuoloGuard implements CanActivate, CanActivateChild, CanLoad {


  constructor(
    private router: Router,
    private userService: UserService,
    private logService: LogService
  ){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      this.logService.debug(this.constructor.name,`canActivate`);
    return this.customExecute(route);
  }
  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        this.logService.debug(this.constructor.name,`canActivateChild`);
      return this.customExecute(childRoute);
  }
  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      this.logService.debug(this.constructor.name,`canLoad`);
      return this.customExecute(route);
  }

  private customExecute(route: ActivatedRouteSnapshot | Route): boolean {
    const utente = this.userService.getUserSessione();
    const ruolo = this.userService.getRuoloSessione();
    let res: boolean = !Utils.isNullOrUndefined(utente) || !Utils.isNullOrUndefined(ruolo);
    this.logService.debug(this.constructor.name,`customExecute: res = ${res}`);
    if(!res){
      this.router.navigate([ROUTE.MODULE.APP.ACCESSO_RUOLO]);
    }
    return res;
  }

}
