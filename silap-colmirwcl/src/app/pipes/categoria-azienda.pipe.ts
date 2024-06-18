/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Pipe, PipeTransform } from '@angular/core';
import { Utils } from '../utils';
import { CATEGORIA_AZIENDA } from '../constants';

@Pipe({
  name: 'categoriaAzienda'
})
export class CategoriaAziendaPipe implements PipeTransform {

  transform(value: number): string {
    if(typeof value !== 'number' ||
        Utils.isNullOrUndefined(value)){
      return undefined;
    }else if(value === CATEGORIA_AZIENDA.A.ID){
      return CATEGORIA_AZIENDA.A.COD
    }
    else if(value === CATEGORIA_AZIENDA.B.ID){
      return CATEGORIA_AZIENDA.B.COD
    }else if(value === CATEGORIA_AZIENDA.C.ID){
      return CATEGORIA_AZIENDA.C.COD
    }
    return '';
  }

}
