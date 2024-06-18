/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'colmirwcl-prompt-modal',
  templateUrl: './prompt-modal.component.html',
  styleUrls: ['./prompt-modal.component.scss']
})
export class PromptModalComponent implements OnInit {

  @Input() title: string;
  @Input() message: string;
  @Input() yesLabel: string;
  @Input() noLabel: string;
  @Input() callback: any;
  @Input() modal: any;
  @Input() type: string;

  constructor() { }

  ngOnInit(): void {
  }

}
