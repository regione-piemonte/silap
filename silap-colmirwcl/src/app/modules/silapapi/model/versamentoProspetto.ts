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
import { VersamentoPvProspetto } from './versamentoPvProspetto';

export interface VersamentoProspetto { 
    annoRiferimento?: number;
    baseComputoNazionale?: number;
    categoriaAzienda?: string;
    codUserAggiorn?: string;
    codUserInserim?: string;
    codiceRegionale?: string;
    dAggiorn?: Date;
    dInserim?: Date;
    esoTVersamentoPvProspettos?: Array<VersamentoPvProspetto>;
    idEsoTVersamento?: number;
    idEsoTVersamentoProspetto?: number;
    nTimestamp?: number;
}