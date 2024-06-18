/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomePageComponent } from './components/home-page/home-page.component';
import { ColmirRoutingModule } from './colmir-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { RicercaDichiarazioneVerEsoComponent } from './components/ricerca-dichiarazione-ver-eso/ricerca-dichiarazione-ver-eso.component';
import { NgbAccordionModule, NgbDateAdapter, NgbDateNativeAdapter, NgbDateParserFormatter, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormRicercaDichiarazioneVerEsoComponent } from './components/ricerca-dichiarazione-ver-eso/form-ricerca-dichiarazione-ver-eso/form-ricerca-dichiarazione-ver-eso.component';
import { RisultatiRicercaDichiarazioneVerEsoComponent } from './components/ricerca-dichiarazione-ver-eso/risultati-ricerca-dichiarazione-ver-eso/risultati-ricerca-dichiarazione-ver-eso.component';
import { ColmircommonModule } from '../colmircommon/colmircommon.module';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbCustomDateParserFormatterService } from 'src/app/services/ngb-custom-date-parser-formatter.service';
import { AccordionInfoComponent } from '../colmircommon/components/accordion-info/accordion-info.component';
import { AccordionInfoService } from '../colmircommon/services/accordion-info.service';
import { NavMainDichiarazioneVersamentoComponent } from './components/nav-main-dichiarazione-versamento/nav-main-dichiarazione-versamento.component';
import { DatiAziendaliComponent } from './components/nav-main-dichiarazione-versamento/dati-aziendali/dati-aziendali.component';
import { VisualizzaDichiarazioneVersamentoComponent } from './components/visualizza-dichiarazione-versamento/visualizza-dichiarazione-versamento.component';
import { ProvinceComponent } from './components/nav-main-dichiarazione-versamento/province/province.component';
import { GiorniLavorativiComponent } from './components/nav-main-dichiarazione-versamento/province/giorni-lavorativi/giorni-lavorativi.component';
import { SospensioniComponent } from './components/nav-main-dichiarazione-versamento/province/sospensioni/sospensioni.component';
import { DichiarazioneComponent } from './components/nav-main-dichiarazione-versamento/province/dichiarazione/dichiarazione.component';
import { PrevisioneDichiarazioneComponent } from './components/previsione-dichiarazione/previsione-dichiarazione.component';




@NgModule({
  declarations: [
    HomePageComponent,
    RicercaDichiarazioneVerEsoComponent,
    FormRicercaDichiarazioneVerEsoComponent,
    RisultatiRicercaDichiarazioneVerEsoComponent,
    NavMainDichiarazioneVersamentoComponent,
    DatiAziendaliComponent,
    VisualizzaDichiarazioneVersamentoComponent,
    ProvinceComponent,
    GiorniLavorativiComponent,
    SospensioniComponent,
    DichiarazioneComponent,
    PrevisioneDichiarazioneComponent
  ],
  imports: [
    CommonModule,
    ColmirRoutingModule,
    ColmircommonModule,
    TranslateModule
  ],
  providers: [
    { provide: NgbDateAdapter, useClass: NgbDateNativeAdapter },
    { provide: NgbDateParserFormatter, useClass: NgbCustomDateParserFormatterService }
  ],
})
export class ColmirModule { }
