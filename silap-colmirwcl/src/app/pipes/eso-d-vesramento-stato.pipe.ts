/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Pipe, PipeTransform } from '@angular/core';
import { Utils } from '../utils';
import { STATO } from '../constants';

@Pipe({
  name: 'statoDVersamento'
})
export class EsoDVesramentoStatoPipe implements PipeTransform {

  transform(value: number): string {
    if(
      typeof value !== 'number' ||
      Utils.isNullOrUndefined(value)
      ){
      return undefined;
    }else if(value === STATO.BOZZA.ID){
      return STATO.BOZZA.DS;
    }else if(value === STATO.ACCETTATA.ID){
      return STATO.ACCETTATA.DS;
    }else if(value === STATO.MODIFICATA.ID){
      return STATO.MODIFICATA.DS;
    }else if(value === STATO.AUTORIZZATA.ID){
      return STATO.AUTORIZZATA.DS;
    }else if(value === STATO.NON_AUTORIZZATA.ID){
      return STATO.NON_AUTORIZZATA.DS;
    }else if(value === STATO.ANNULLATA.ID){
      return STATO.ANNULLATA.DS;
    }else if(value === STATO.ELIMINATA.ID){
      return STATO.ELIMINATA.DS;
    }else if(value === STATO.AVVISO_INVIATO.ID){
        return STATO.AVVISO_INVIATO.DS;
    }else if(value === STATO.ACCONTO.ID){
      return STATO.ACCONTO.DS;
    }else if(value === STATO.SALDO.ID){
      return STATO.SALDO.DS;
    }
    return '';
  }

}
