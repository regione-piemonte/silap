/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ROUTE, RUOLO } from 'src/app/constants';
import { Utente, UtenteService } from 'src/app/modules/silapapi';
import { Ruolo } from 'src/app/modules/silapapi/model/ruolo';
import { LeftSidebarService } from 'src/app/services/left-sidebar.service';
import { UserService } from 'src/app/services/user.service';
import { Utils } from 'src/app/utils';



@Component({
  selector: 'colmirwcl-accesso-ruolo',
  templateUrl: './accesso-ruolo.component.html',
  styleUrls: ['./accesso-ruolo.component.scss']
})
export class AccessoRuoloComponent implements OnInit {

  ruoli: Ruolo[];
  utente: Utente;
  tabIndex: number;
  showRuoli: any;
  constructor(
    private userService: UserService,
    private router: Router,
    private utenteService: UtenteService,
    private spinner: NgxSpinnerService,
    private leftSideBarService: LeftSidebarService

  ) { }

  title = $localize`accesso-ruolo works!`;

  ngOnInit(): void {
    this.spinner.show();
    this.leftSideBarService.hideLeftSideBar();
    this.userService.rilasciaUser();
    this.loadRuoli();

  }

  loadRuoli() {
     this.utenteService.self().subscribe({
      next: (response: any) => {
        if(response.esitoPositivo){
          this.utente = response.utente;
          this.ruoli = this.utente.ruoli;
        }
        this.showRuoli = response.esitoPositivo;
      },
      error: (err) => {
        console.error(`${JSON.stringify(err)}`);
      },
      complete: ()=>{this.spinner.hide();}
    });
  }

  onClickSelectRuolo(ruolo: any){
    if(Utils.isNullOrUndefined(ruolo))
      return;
    this.userService.setRuoloSessione(ruolo);
    this.userService.setUserSessione(this.utente);
    this.router.navigate([ROUTE.MODULE.COLMIR.ROOT,ROUTE.MODULE.COLMIR.HOME_PAGE]);
  }

  getDenominazione(ruolo: Ruolo): string{
    if(ruolo.idSilapDRuolo === RUOLO.AMMINISTRATORE.ID || ruolo.idSilapDRuolo === RUOLO.REGIONE.ID)
      return this.utente.nome + " - " +this.utente.cognome;
    else
      return ruolo.denominazioneSoggettoAbilitato;
  }

  getCodiceFiscale(ruolo: Ruolo){
    if(ruolo.idSilapDRuolo === RUOLO.AMMINISTRATORE.ID || ruolo.idSilapDRuolo === RUOLO.REGIONE.ID)
    return this.utente.codFisc;
  else
    return ruolo.codiceFiscaleSoggettoAbilitato;
  }
  calcoloTabIndex(){
    if(Utils.isNullOrUndefined(this.tabIndex)){
      this.tabIndex = 0;
    }else{
      this.tabIndex = this.tabIndex + 1;
    }

    return this.tabIndex;
  }

}

