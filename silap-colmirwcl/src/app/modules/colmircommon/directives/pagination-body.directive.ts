/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Directive, TemplateRef } from '@angular/core';

@Directive({
  selector: 'ng-template[colmirwclPaginationBody]'
})
export class PaginationBodyDirective<T> {

  constructor(
    public templateRef: TemplateRef<{$implicit: T}>
  ) { }

}
