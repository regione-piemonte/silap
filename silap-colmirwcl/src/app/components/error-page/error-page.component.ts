/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs';
import { ApiError } from 'src/app/modules/silapapi';

@Component({
  selector: 'colmirwcl-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.scss']
})
export class ErrorPageComponent implements OnInit, OnDestroy {

  apiError: ApiError;
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private spinner: NgxSpinnerService
  ) {
    const state = this.router.getCurrentNavigation().extras.state;
    if(state)
      this.apiError = state['apiError'];
  }

  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    this.spinner.hide();
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}
