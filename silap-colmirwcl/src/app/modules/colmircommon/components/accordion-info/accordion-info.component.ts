/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, Input, OnInit } from '@angular/core';
import { AccordionInfoService } from '../../services/accordion-info.service';
import { Messaggio } from 'src/app/modules/silapapi/model/messaggio';
import { Utils } from 'src/app/utils';

@Component({
  selector: 'colmirwcl-accordion-info',
  templateUrl: './accordion-info.component.html',
  styleUrls: ['./accordion-info.component.scss']
})
export class AccordionInfoComponent implements OnInit {

  _messaggio: Messaggio
  @Input() set messaggio(messaggio: Messaggio){
    this._messaggio = messaggio
  }

  get TITLE(): string{
    if(Utils.isNullOrUndefined(this._messaggio)){
      return "";
    }
    return this._messaggio.dsTitolo;
  }

  get CONTENT(): string{
    if(Utils.isNullOrUndefined(this._messaggio)){
      return "";
    }
    return this._messaggio.dsSilapDMessaggio;
  }

  constructor(
    private accordionInfoService: AccordionInfoService
  ) { }


  ngOnInit(): void {
    // this.accordionInfoService.infoMessaggio$.subscribe((messaggio: Messaggio) => {
    //   this.messaggio = messaggio;
    // });
  }

}
