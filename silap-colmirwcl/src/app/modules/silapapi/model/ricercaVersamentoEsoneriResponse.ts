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
import { ApiMessage } from './apiMessage';
import { VersamentoEsoneriRidotto } from './versamentoEsoneriRidotto';

export interface RicercaVersamentoEsoneriResponse { 
    apiMessages?: Array<ApiMessage>;
    currentPage?: number;
    esitoPositivo?: boolean;
    list?: Array<VersamentoEsoneriRidotto>;
    recordOfPage?: number;
    recordPage?: number;
    totalPage?: number;
    totalResult?: number;
}