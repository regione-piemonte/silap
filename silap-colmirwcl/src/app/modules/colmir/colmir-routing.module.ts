/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ROUTE } from 'src/app/constants';
import { HomePageComponent } from './components/home-page/home-page.component';
import { RicercaDichiarazioneVerEsoComponent } from './components/ricerca-dichiarazione-ver-eso/ricerca-dichiarazione-ver-eso.component';
import { NavMainDichiarazioneVersamentoComponent } from './components/nav-main-dichiarazione-versamento/nav-main-dichiarazione-versamento.component';
import { PrevisioneDichiarazioneComponent } from './components/previsione-dichiarazione/previsione-dichiarazione.component';



const routes: Routes = [
  { path: '', redirectTo: ROUTE.MODULE.COLMIR.HOME_PAGE, pathMatch: 'full' },
  { path: ROUTE.MODULE.COLMIR.HOME_PAGE, component: HomePageComponent },
  { path: 'ricerca-versamento-esoneri', component: RicercaDichiarazioneVerEsoComponent },
  { path: 'nav-main', component: NavMainDichiarazioneVersamentoComponent },
  { path: 'previsione-dichiarazione', component: PrevisioneDichiarazioneComponent }
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes)]
})
export class ColmirRoutingModule { }
