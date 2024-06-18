/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Directive, ElementRef, HostListener, Renderer2, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
  selector: '[colmirwclOnlyNegativePositiveNumber]',
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => OnlyNegativePositiveNumbersDirective),
    multi: true
  }]
})
export class OnlyNegativePositiveNumbersDirective implements ControlValueAccessor {

    private onChange: (val: string) => void;
    private onTouched: () => void;
    private value: string;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2
  ) { }

  @HostListener('input', ['$event.target.value'])
  onInputChange(value: string) {
    const filteredValue: string = filterValue(value);
    this.updateTextInput(filteredValue, this.value !== filteredValue);
  }

  @HostListener('blur')
    onBlur() {
        this.onTouched();
  }

  private updateTextInput(value: string, propagateChange: boolean) {
    this.renderer.setProperty(this.elementRef.nativeElement, 'value', value);
    if (propagateChange) {
        this.onChange(value);
    }
    this.value = value;
  }

  // ControlValueAccessor Interface
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.renderer.setProperty(this.elementRef.nativeElement, 'disabled', isDisabled);
  }

  writeValue(value: any): void {
    value = value ? String(value) : '';
    this.updateTextInput(value, false);
  }

}

function filterValue(value: any): string {
  if (value === null || value === undefined) {
    return '';
  }

  // Rimuovi tutti i caratteri non numerici, tranne il primo segno meno all'inizio
  const cleanedValue = value.toString().replace(/[^0-9-]/g, '');

  // Assicurati che ci sia solo un segno meno all'inizio
  if (cleanedValue.startsWith('-')) {
    return '-' + cleanedValue.slice(1).replace(/-/g, '');
  } else {
    return cleanedValue.replace(/-/g, '');
  }
}

