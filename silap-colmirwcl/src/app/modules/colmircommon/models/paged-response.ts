/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
export interface PagedResponse<T> {
  list?: Array<T>;
  currentPage?: number;
  totalPage?: number;
  totalResult?: number;
  recordOfPage?: number;
  recordPage?: number;
}
