/**
 * 
 * 
 *
 * 
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { Operatore } from './operatore';
import { RuoloFunzione } from './ruoloFunzione';

export interface Ruolo { 
    codiceFiscaleSoggettoAbilitato?: string;
    denominazioneSoggettoAbilitato?: string;
    dsDescrizione?: string;
    dsSilapDRuolo?: string;
    idSilapDRuolo?: number;
    operatore?: Operatore;
    ruoloFunzioni?: Array<RuoloFunzione>;
}