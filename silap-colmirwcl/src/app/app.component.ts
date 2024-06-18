/*************************************************
Copyright Regione Piemonte - 2024
SPDX-License-Identifier: EUPL-1.2-or-later
***************************************************/
import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { LeftSidebarService } from './services/left-sidebar.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  hideSideBar: boolean;
  closeSideBar: boolean = true;
  title = 'colmirwcl';

  constructor(
    private spinner: NgxSpinnerService,
    private leftSideBarService: LeftSidebarService,
  ) { }


  ngOnInit(): void {
    this.spinner.show();
    this.leftSideBarService.showSideBar$.subscribe(value => {
      setTimeout(() => {
        this.hideSideBar = value;
        this.leftSideBarService.closeSideBar$.subscribe(close => this.closeSideBar = close);
      });
    });
    this.spinner.hide();
  }
  setStateSidebar(state: any) {
    this.closeSideBar = state;
  }

}
