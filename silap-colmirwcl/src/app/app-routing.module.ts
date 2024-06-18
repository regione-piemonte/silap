/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccessoRuoloComponent } from './components/accesso-ruolo/accesso-ruolo.component';
import { ROUTE } from './constants';
import { RuoloGuard } from './guards/ruolo.guard';
import { ErrorPageComponent } from './components/error-page/error-page.component';


const routes: Routes = [
  { path: ROUTE.MODULE.APP.ROOT, redirectTo: ROUTE.MODULE.APP.ACCESSO_RUOLO, pathMatch: 'full' },
  {
    path: ROUTE.MODULE.APP.ACCESSO_RUOLO,
    component: AccessoRuoloComponent
  },
  {
    path: ROUTE.MODULE.COLMIR.ROOT,
    canLoad: [RuoloGuard], canActivate: [ RuoloGuard ], canActivateChild: [ RuoloGuard ],
    data: {module: 'COLMIR'},
    loadChildren: () => import('./modules/colmir/colmir.module').then(m => m.ColmirModule)
  },
  { path: 'error', component: ErrorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
