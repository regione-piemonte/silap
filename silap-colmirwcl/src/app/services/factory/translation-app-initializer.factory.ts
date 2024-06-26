/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injector } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LOCATION_INITIALIZED } from '@angular/common';
import { LogService } from 'src/app/services/log.service';
import { Language, LANGUAGE_STORAGE } from 'src/app/models';
import { marker } from '@biesbjerg/ngx-translate-extract-marker';
import { SessionStorageService } from '../storage/session-storage.service';

export function TranslationAppInitializerFactory(
    translate: TranslateService,
    logService: LogService,
    storageService: SessionStorageService,
    injector: Injector) {
  let lang: Language = storageService.getItem(LANGUAGE_STORAGE);
  if (!lang) {
    // Force the language as 'it' if not found
    lang = {iconCode: 'it', locale: 'it-IT', langCode: 'it', text: marker('LANGUAGE.ITALIAN')};
    storageService.setItem(LANGUAGE_STORAGE, lang);
  }
  return () => new Promise<any>((resolve: any) => {
    injector
      .get(LOCATION_INITIALIZED, Promise.resolve(null))
      .then(() => {
        translate.setDefaultLang('it');
        translate.use(lang.langCode).subscribe({
          next: (res) => {
            logService.debug('TranslationAppInitializerFactory', '<init>', `Successfully initialized '${lang.langCode}' language.'`, res);
          },
          error: (err) => {
            
            logService.error('TranslationAppInitializerFactory', '<init>', `Problem with '${lang.langCode}' language initialization.`, err);
          },
          complete() {
            
          },
        });
      });
  });
}
