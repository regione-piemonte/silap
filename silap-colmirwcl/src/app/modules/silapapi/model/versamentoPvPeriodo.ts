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
import { CategoriaAzienda } from './categoriaAzienda';

export interface VersamentoPvPeriodo { 
    baseComputo?: number;
    codUserAggiorn?: string;
    codUserInserim?: string;
    dAggiorn?: Date;
    dFine?: Date;
    dInizio?: Date;
    dInserim?: Date;
    flgTipo?: string;
    idEsoTVersamentoPvPeriodo?: number;
    idEsonero?: number;
    importo?: number;
    nTimestamp?: number;
    numDisabiliInForza?: number;
    numEsoneratiAutocertificati?: number;
    numGgLavorativi?: number;
    numLavoratoriEsonerati?: number;
    numRiallineamentoNazionale?: number;
    numSoggettiCompensati?: number;
    quotaRiserva?: number;
    silapDCategoriaAzienda?: CategoriaAzienda;
    sovrapposto?: boolean;
}