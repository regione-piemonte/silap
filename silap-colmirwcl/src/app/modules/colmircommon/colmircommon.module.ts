/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxSpinnerModule } from 'ngx-spinner';
import { PaginatedArrayTableComponent } from './components/paginated-array-table/paginated-array-table.component';
import { PaginatedTableComponent } from './components/paginated-table/paginated-table.component';
import { NgbAccordionModule, NgbModalModule, NgbModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { PaginationBodyDirective } from './directives/pagination-body.directive';
import { PaginationHeadDirective } from './directives/pagination-head.directive';
import { SortableDirective } from './directives/sortable.directive';
import { AccordionInfoComponent } from './components/accordion-info/accordion-info.component';
import { AlertMessageComponent } from './components/alert-message/alert-message.component';
import { EscapeHtmlPipe } from 'src/app/pipes/escape-html.pipe';
import { EsoDVesramentoStatoPipe } from 'src/app/pipes/eso-d-vesramento-stato.pipe';
import { PromptModalComponent } from './components/prompt-modal/prompt-modal.component';
import { PromptModalService } from './services/prompt-modal.service';
import { OnlyNumberDirective } from './directives/only-number.directive';
import { OnlyNegativePositiveNumbersDirective } from './directives/only-negative-positive-numbers.directive';
import { CategoriaAziendaPipe } from 'src/app/pipes/categoria-azienda.pipe';
import { SiNoPipe } from 'src/app/pipes/si-no.pipe';


@NgModule({
  declarations: [
    PaginatedArrayTableComponent,
    PaginatedTableComponent,
    PaginationHeadDirective,
    PaginationBodyDirective,
    SortableDirective,
    AccordionInfoComponent,
    AlertMessageComponent,
    EscapeHtmlPipe,
    EsoDVesramentoStatoPipe,
    PromptModalComponent,
    OnlyNumberDirective,
    OnlyNegativePositiveNumbersDirective,
    CategoriaAziendaPipe,
    SiNoPipe
  ],
  imports: [
    CommonModule,
    NgxSpinnerModule,
    NgbPaginationModule,
    ReactiveFormsModule,
    FormsModule,
    NgbModule,
    NgbModalModule,
    TranslateModule,
    NgbAccordionModule
  ],
  exports: [
    AlertMessageComponent,
    NgxSpinnerModule,
    NgbAccordionModule,
    ReactiveFormsModule,
    FormsModule,
    PaginatedArrayTableComponent,
    PaginatedTableComponent,
    NgbPaginationModule,
    PaginationBodyDirective,
    PaginationHeadDirective,
    SortableDirective,
    NgbModule,
    AccordionInfoComponent,
    EscapeHtmlPipe,
    EsoDVesramentoStatoPipe,
    OnlyNumberDirective,
    OnlyNegativePositiveNumbersDirective,
    CategoriaAziendaPipe,
    SiNoPipe
  ],
  providers: [
    PromptModalService,
    CategoriaAziendaPipe,
    SiNoPipe
  ],
  entryComponents: [PromptModalComponent]
})
export class ColmircommonModule { }
