/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { AfterViewInit, Component, ContentChild, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { PaginationBodyDirective } from '../../directives/pagination-body.directive';
import { PaginationHeadDirective } from '../../directives/pagination-head.directive';
import { SortableDirective } from '../../directives/sortable.directive';
import { SortEvent } from '../../models/sort-event';
import { PagedResponse } from '../../models/paged-response';
import { PaginationDataChange } from '../../models/pagination-data-change';

@Component({
  selector: 'colmirwcl-paginated-table',
  templateUrl: './paginated-table.component.html',
  styleUrls: ['./paginated-table.component.scss']
})
export class PaginatedTableComponent<T> implements OnInit, AfterViewInit {

  @Input() pagedResponse: PagedResponse<T>;
  @Input() columnNumber = 0;
  @Input() limit = 10;
  @Output() readonly changePaginationData = new EventEmitter<PaginationDataChange>();

  @ViewChildren(SortableDirective) headers: QueryList<SortableDirective>;
  @ContentChild(PaginationHeadDirective, { static: false }) tplHead: PaginationHeadDirective;
  @ContentChild(PaginationBodyDirective, { static: false }) tplBody: PaginationBodyDirective<T>;

  pageSizes: number[] = [5, 10, 20, 50];
  page: number;
  sort: SortEvent;

  constructor() { }

  ngOnInit(): void {
    this.page = this.pagedResponse.currentPage + 1;
    if (!this.columnNumber) {
      this.columnNumber = 0;
    }
  }

  changePageSize(pageSize: number) {
    console.log('paginated', pageSize);

    const currentFirstElement = (this.page - 1) * this.limit;
    this.limit = pageSize;
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
    this.page = Math.max(1, Math.min(this.pagedResponse.totalPage, pageNumber));

    this.changePaginationData.emit({
      limit: this.limit,
      sort: this.sort,
      page: this.page - 1,
      offset: (this.page - 1) * this.limit
    });
  }

  get paginationHeader(): string {
    if (this.pagedResponse.totalResult === 0) {
      return '';
    }
    const first = Math.min((this.page - 1) * this.limit + 1, this.pagedResponse.totalResult);
    const last = Math.min(this.page * this.limit, this.pagedResponse.totalResult);
    return `Record ${first} - ${last} di ${this.pagedResponse.totalResult}`;
  }

  get paginationFooter(): string {
    if (this.pagedResponse.totalResult === 0) {
      return '';
    }
    const first = Math.min((this.page - 1) * this.limit + 1, this.pagedResponse.totalResult);
    const last = Math.min(this.page * this.limit, this.pagedResponse.totalResult);
    return `Record totali ${this.pagedResponse.totalResult}`;
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
