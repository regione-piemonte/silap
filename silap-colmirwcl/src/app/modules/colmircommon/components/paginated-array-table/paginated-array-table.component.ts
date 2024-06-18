/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { AfterViewInit, Component, ContentChild, EventEmitter, Input, OnInit, Output, QueryList, TemplateRef, ViewChild, ViewChildren } from '@angular/core';
import { PaginationBodyDirective } from '../../directives/pagination-body.directive';
import { PaginationHeadDirective } from '../../directives/pagination-head.directive';
import { SortableDirective } from '../../directives/sortable.directive';
import { PaginationDataChange } from '../../models/pagination-data-change';
import { SortEvent } from '../../models/sort-event';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'colmirwcl-paginated-array-table',
  templateUrl: './paginated-array-table.component.html',
  styleUrls: ['./paginated-array-table.component.scss']
})
export class PaginatedArrayTableComponent<T> implements OnInit, AfterViewInit {

  @Input() pagedResponse: T[];
  @Input() columnNumber = 0;
  @Input() limit = 10;
  @Output() readonly changePaginationData = new EventEmitter<PaginationDataChange>();

  @ViewChildren(SortableDirective) headers: QueryList<SortableDirective>;

  @ContentChild(PaginationHeadDirective, { static: false }) tplHead: PaginationHeadDirective;
  @ContentChild(PaginationBodyDirective, { static: false }) tplBody: PaginationBodyDirective<T>;

  pageSizes: number[] = [1, 5, 10, 20, 50];
  page: number;
  sort: SortEvent;
  totalPages: number;

  

  constructor() {}

  ngOnInit(): void {
    this.page = 1;
    if (!this.pagedResponse) {
      this.pagedResponse = [];
    }
    this.totalPages = Math.ceil(this.pagedResponse.length / this.limit);
    if (!this.columnNumber) {
      this.columnNumber = 0;
    }
  }

  changePageSize(pageSize: any) {
    const currentFirstElement = (this.page - 1) * this.limit;
    this.limit = Number(pageSize.target.value);
    const page = Math.floor(currentFirstElement / this.limit);
    const offset = page * this.limit;
    this.page = page + 1;

    this.changePaginationData.emit({
      limit: this.limit,
      sort: this.sort,
      page,
      offset
    });
  }
  onSort(sortEvent: SortEvent) {
    this.headers
      .filter(header => header.sortable !== sortEvent.column)
      .forEach(header => header.direction = '');
    this.sort = sortEvent;
    this.changePaginationData.emit({
      limit: this.limit,
      sort: this.sort,
      page: this.page - 1,
      offset: (this.page - 1) * this.limit
    });
  }

  goToPage(pageNumber: number): void {
    const totalPages = Math.ceil(this.pagedResponse.length / this.limit);
    this.page = Math.max(1, Math.min(totalPages, pageNumber));

    this.changePaginationData.emit({
      limit: this.limit,
      sort: this.sort,
      page: this.page - 1,
      offset: (this.page - 1) * this.limit
    });

    console.log('pageNUmber: '+pageNumber);
  }

  get pageSlice(): T[] {
    return this.pagedResponse.slice((this.page - 1) * this.limit, this.page * this.limit);
  }

  get paginationFooter(): string {
    if (this.pagedResponse.length === 0) {
      return '';
    }
    const first = Math.min((this.page - 1) * this.limit + 1, this.pagedResponse.length);
    const last = Math.min(this.page * this.limit, this.pagedResponse.length);
    return `${first} - ${last} di ${this.pagedResponse.length} elementi`;
  }


  /*per accessibilità*/
  ngAfterViewInit(): void {
    let aF: HTMLAnchorElement = document.querySelector(
      'a[aria-label="First"]'
    );
    aF.setAttribute('aria-label', 'Inizio.');

    let aP: HTMLAnchorElement = document.querySelector(
      'a[aria-label="Previous"]'
    );
    aP.setAttribute('aria-label', 'Prec.');

    let aN: HTMLAnchorElement = document.querySelector(
      'a[aria-label="Next"]'
    );
    aN.setAttribute('aria-label', 'Succ.');

    let aL: HTMLAnchorElement = document.querySelector(
      'a[aria-label="Last"]'
    );
    aL.setAttribute('aria-label', 'Fine');
  }


}
