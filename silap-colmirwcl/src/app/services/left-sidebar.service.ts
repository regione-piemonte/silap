/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { LeftSideBarTemplate } from '../models/left-side-bar-template';
import { Funzione } from '../modules/silapapi';

@Injectable({
  providedIn: 'root'
})
export class LeftSidebarService {

  private close: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private showSideBar: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private templateSideBar: Subject<LeftSideBarTemplate[]> = new Subject<LeftSideBarTemplate[]>();

  get showSideBar$(): Observable<boolean> { return this.showSideBar.asObservable(); }
  get closeSideBar$(): Observable<boolean> { return this.close.asObservable(); }
  get templateSideBar$(): Observable<LeftSideBarTemplate[]> {return this.templateSideBar.asObservable()};

  constructor() { }

  public showLeftSideBar(){
    this.showSideBar.next(false);
  }

  public hideLeftSideBar(){
    this.showSideBar.next(true);
  }
  public setCloseSideBar(close: boolean){
    this.close.next(close);
  }

  public setTemplateSideBar(template: LeftSideBarTemplate[]){
    this.templateSideBar.next(template);
  }
}

export interface TemplateMenu {
  [key: number]: Funzione;
}
