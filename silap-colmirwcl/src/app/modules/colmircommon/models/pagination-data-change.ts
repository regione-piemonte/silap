/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { SortEvent } from "./sort-event";

export interface PaginationDataChange {
  limit: number;
  offset: number;
  page: number;
  sort: SortEvent;
}
