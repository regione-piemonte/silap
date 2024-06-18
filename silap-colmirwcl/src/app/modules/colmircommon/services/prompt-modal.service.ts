/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable } from '@angular/core';
import { PromptModalComponent } from '../components/prompt-modal/prompt-modal.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Injectable({
  providedIn: 'root'
})
export class PromptModalService {

  constructor(private modalService: NgbModal) { }


  openPrompt(pTitle: string, pMessage: string, pYes: string, pNo: string, type: string) {
    const opt = pMessage.length > 50 ? {size: 'xl', scrollable: true} : undefined;
    const modalRef = this.modalService.open(PromptModalComponent, {...opt, backdrop: false});

    modalRef.componentInstance.title = pTitle;
    modalRef.componentInstance.message = pMessage;
    modalRef.componentInstance.yesLabel = pYes;
    modalRef.componentInstance.noLabel = pNo;
    modalRef.componentInstance.callback = this.callback;
    modalRef.componentInstance.modal = modalRef;
    modalRef.componentInstance.type = type;
    return modalRef.result;
}

openPromptInfo(pTitle: string, pMessage: string, pNo: string, type: string) {
  const opt = pMessage.length > 50 ? {size: 'xl', scrollable: true} : undefined;
  const modalRef = this.modalService.open(PromptModalComponent, {...opt, backdrop: false});

  modalRef.componentInstance.title = pTitle;
  modalRef.componentInstance.message = pMessage;
  modalRef.componentInstance.noLabel = pNo;
  modalRef.componentInstance.callback = this.callback;
  modalRef.componentInstance.modal = modalRef;
  modalRef.componentInstance.type = type;
  return modalRef.result;
}

  callback(modal: NgbModalRef, val: boolean) {
      modal.close(val);
  }
}
