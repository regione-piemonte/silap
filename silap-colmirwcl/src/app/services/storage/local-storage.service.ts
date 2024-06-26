/*
* SPDX-FileCopyrightText: Copyright 2024 | CSI Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { LogService } from '../log.service';
import { BaseStorageService } from './base-storage.service';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService extends BaseStorageService {

  constructor(
    logService: LogService
  ) {
    super(logService, localStorage);
  }
}
