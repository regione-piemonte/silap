/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Directive, EventEmitter, HostBinding, HostListener, Input, Output } from '@angular/core';
import { SortDirection } from '../models/sort-direction';
import { SortEvent } from '../models/sort-event';


@Directive({
  selector: '[colmirwclSortable]'
})
export class SortableDirective {

  @Input('colmirwclSortable') sortable: string;
  @Input() direction: SortDirection = '';
  @Output() readonly sort = new EventEmitter<SortEvent>();

  @HostBinding('class.asc') get asc() { return this.direction === 'asc'; }
  @HostBinding('class.desc') get desc() { return this.direction === 'desc'; }

  constructor() { }

  @HostListener('click') rotate() {
    this.direction = this.rotate[this.direction];
    this.sort.emit({column: this.sortable, direction: this.direction});
  }

}
