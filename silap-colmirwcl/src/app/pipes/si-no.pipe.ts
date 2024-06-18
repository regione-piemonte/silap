/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'siNo'
})
export class SiNoPipe implements PipeTransform {

  constructor() {}

  transform(value: string): string {
    if (value === null || value === undefined) {
      return 'NO';
    }
    if (typeof value === 'boolean') {
      return value ? 'SI' : 'NO';
    }
    switch (value.toUpperCase()) {
      case 'Y':
      case 'YES':
      case 'S':
      case 'SI':
        return 'SI';
      case 'N':
      case 'NO':
        return 'NO';
      default:
        return value;
    }
  }

}
