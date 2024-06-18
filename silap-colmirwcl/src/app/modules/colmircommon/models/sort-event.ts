/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { SortDirection } from "./sort-direction";

export interface SortEvent {
  column: string;
  direction: SortDirection;
}
