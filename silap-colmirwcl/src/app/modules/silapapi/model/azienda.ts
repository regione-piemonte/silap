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
import { Ccnl } from './ccnl';
import { Comune } from './comune';

export interface Azienda { 
    capSede?: string;
    codFiscale?: string;
    comuneSede?: Comune;
    denomAzienda?: string;
    idEsoTCreditoResiduo?: number;
    idSilAziAnagrafica?: number;
    idSilAziSede?: number;
    indirizzoSede?: string;
    numCreditoResiduo?: number;
    ragioneSocialeSede?: string;
    silapDCcnl?: Ccnl;
}