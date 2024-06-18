/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { APP_INITIALIZER, Injector, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ConfigurationService } from './services/configuration.service';
import { APP_BASE_HREF, CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { HttpClient, HttpClientModule, HttpBackend, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ColmircommonModule } from './modules/colmircommon/colmircommon.module';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { AccessoRuoloComponent } from './components/accesso-ruolo/accesso-ruolo.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslationAppInitializerFactory } from './services/factory';
import { MultiTranslateHttpLoader } from 'ngx-translate-multi-http-loader';
import { ErrorHandlerInterceptor } from './services/interceptors/error-handler.interceptor';
import { BASE_PATH } from './modules/silapapi';
import { LeftSidebarComponent } from './left-sidebar/left-sidebar.component';
import { registerLocaleData } from '@angular/common';
import localeIt from '@angular/common/locales/it';
import { LOCALE_ID } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ErrorPageComponent } from './components/error-page/error-page.component';
import { UtenteInterceptor } from './services/interceptors/utente.interceptor';
import { AuthInterceptor } from './services/interceptors/auth.interceptor';
import { Config } from './services/config';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http,ConfigurationService.getBaseHref()+"/assets/i18n/");
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    AccessoRuoloComponent,
    LeftSidebarComponent,
    ErrorPageComponent
  ],
  imports: [
    CommonModule,
    ColmircommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
      extend: true
    })

  ],
  providers: [
    { provide: APP_BASE_HREF, useValue: ConfigurationService.getBaseHref()},
    { provide: BASE_PATH, useFactory: ConfigurationService.getBERootUrl },
    { provide: LOCALE_ID, useValue: 'it' },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true},
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    { provide: HTTP_INTERCEPTORS, useClass: UtenteInterceptor, multi: true},
    { provide: Config, useValue: {
      ambiente: ConfigurationService.getAmbiente(),
      logOutUrl: ConfigurationService.getSSOLogoutURL()
    }},
    DatePipe,
    DecimalPipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    registerLocaleData(localeIt, 'it');
  }
}
