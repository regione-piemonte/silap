/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { Configuration } from './configuration';
import { HttpClient } from '@angular/common/http';


import { DecodificaService } from './api/decodifica.service';
import { MessaggioService } from './api/messaggio.service';
import { PingService } from './api/ping.service';
import { UtenteService } from './api/utente.service';
import { VersamentoEsoneriService } from './api/versamentoEsoneri.service';

@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: [
    DecodificaService,
    MessaggioService,
    PingService,
    UtenteService,
    VersamentoEsoneriService ]
})
export class ApiModule {
    public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders<ApiModule> {
        return {
            ngModule: ApiModule,
            providers: [ { provide: Configuration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: ApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
