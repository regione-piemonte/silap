/*
* SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
* SPDX-License-Identifier: EUPL-1.2
*/
import { Injectable, Optional,Inject } from '@angular/core';
import { BASE_PATH, Configuration, FormRicercaVersamentoEsoneri, Messaggio, VersamentoEsoneriService, VersamentoStato } from '../modules/silapapi';
import { HttpClient, HttpEvent, HttpParams, HttpResponse } from '@angular/common/http';
import { CustomHttpUrlEncodingCodec } from '../modules/silapapi/encoder';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomVersamentoEsoneriService extends VersamentoEsoneriService{

  constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
    super(httpClient, basePath, configuration);
  }


  private canConsumeFormCustom(consumes: string[]): boolean {
      const form = 'multipart/form-data';
      for (const consume of consumes) {
          if (form === consume) {
              return true;
          }
      }
      return false;
  }

  /**
     * Restituisce la stampa del versamento visualizzato
     *
     * @param idEsoTVersamento
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
  public stampaVersamento(idEsoTVersamento: number, observe?: 'body', reportProgress?: boolean): Observable<Blob>;
  public stampaVersamento(idEsoTVersamento: number, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Blob>>;
  public stampaVersamento(idEsoTVersamento: number, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Blob>>;
  public stampaVersamento(idEsoTVersamento: number, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

      if (idEsoTVersamento === null || idEsoTVersamento === undefined) {
          throw new Error('Required parameter idEsoTVersamento was null or undefined when calling stampaVersamento.');
      }

      let headers = this.defaultHeaders;

      // to determine the Accept header
      let httpHeaderAccepts: string[] = [
          'application/pdf'
      ];
      const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
      if (httpHeaderAcceptSelected != undefined) {
          headers = headers.set('Accept', httpHeaderAcceptSelected);
      }

      // to determine the Content-Type header
      const consumes: string[] = [
      ];

      return this.httpClient.get(`${this.basePath}/versamento-esoneri/stampa-versamento/${encodeURIComponent(String(idEsoTVersamento))}`,
          {
              responseType: "blob",
              withCredentials: this.configuration.withCredentials,
              headers: headers,
              observe: observe,
              reportProgress: reportProgress
          }
      );
  }

  /**
     * Restituisce la stampa dei versamenti esoneri
     *
     * @param body
     * @param format
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
  public stampaRicercaVersamentoEsoneri(body?: FormRicercaVersamentoEsoneri, format?: string, observe?: 'body', reportProgress?: boolean): Observable<Blob>;
  public stampaRicercaVersamentoEsoneri(body?: FormRicercaVersamentoEsoneri, format?: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Blob>>;
  public stampaRicercaVersamentoEsoneri(body?: FormRicercaVersamentoEsoneri, format?: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Blob>>;
  public stampaRicercaVersamentoEsoneri(body?: FormRicercaVersamentoEsoneri, format?: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {



      let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
      if (format !== undefined && format !== null) {
          queryParameters = queryParameters.set('format', <any>format);
      }

      let headers = this.defaultHeaders;

      // to determine the Accept header
      let httpHeaderAccepts: string[] = [
          'application/pdf'
      ];
      const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
      if (httpHeaderAcceptSelected != undefined) {
          headers = headers.set('Accept', httpHeaderAcceptSelected);
      }

      // to determine the Content-Type header
      const consumes: string[] = [
          'application/json'
      ];
      const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
      if (httpContentTypeSelected != undefined) {
          headers = headers.set('Content-Type', httpContentTypeSelected);
      }

      return this.httpClient.post(`${this.basePath}/versamento-esoneri/stampa-ricerca`,
          body,
          {
              responseType: "blob",
              params: queryParameters,
              withCredentials: this.configuration.withCredentials,
              headers: headers,
              observe: observe,
              reportProgress: reportProgress
          }
      );
  }

  /**
     * Scarica il template per il batch previsione dichiarazioni
     *
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
  public downloadPrevisioneDichiarazioniTemplate(observe?: 'body', reportProgress?: boolean): Observable<Blob>;
  public downloadPrevisioneDichiarazioniTemplate(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Blob>>;
  public downloadPrevisioneDichiarazioniTemplate(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Blob>>;
  public downloadPrevisioneDichiarazioniTemplate(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

      let headers = this.defaultHeaders;

      // to determine the Accept header
      let httpHeaderAccepts: string[] = [
          'application/pdf'
      ];
      const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
      if (httpHeaderAcceptSelected != undefined) {
          headers = headers.set('Accept', httpHeaderAcceptSelected);
      }

      // to determine the Content-Type header
      const consumes: string[] = [
      ];

      return this.httpClient.get(`${this.basePath}/versamento-esoneri/download-previsione-dichiarazioni-template`,
          {
              withCredentials: this.configuration.withCredentials,
              headers: headers,
              observe: observe,
              reportProgress: reportProgress,
              responseType: "blob"
          }
      );
  }


   /**
     * Upload il file xls per lanciare il batch previsione dichiarazioni
     *
     * @param anno
     * @param attachment
     * @param email
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
   public uploadPrevisioneDichiarazioniCustom(anno?: string, attachment?: Blob, email?: string, observe?: 'body', reportProgress?: boolean): Observable<Messaggio>;
   public uploadPrevisioneDichiarazioniCustom(anno?: string, attachment?: Blob, email?: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Messaggio>>;
   public uploadPrevisioneDichiarazioniCustom(anno?: string, attachment?: Blob, email?: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Messaggio>>;
   public uploadPrevisioneDichiarazioniCustom(anno?: string, attachment?: Blob, email?: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

       let headers = this.defaultHeaders;

       // to determine the Accept header
       let httpHeaderAccepts: string[] = [
           'application/json'
       ];
       const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
       if (httpHeaderAcceptSelected != undefined) {
           headers = headers.set('Accept', httpHeaderAcceptSelected);
       }

       // to determine the Content-Type header
       const consumes: string[] = [
           'multipart/form-data'
       ];

       const canConsumeForm = this.canConsumeFormCustom(consumes);

       let formParams: { append(param: string, value: any): void | HttpParams; };
       let useForm = canConsumeForm;
       let convertFormParamsToString = false;
       if (useForm) {
           formParams = new FormData();
       } else {
           formParams = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
       }


       if (attachment !== undefined) {
           formParams = formParams.append('attachment', <any>attachment) || formParams;
       }
       if (email !== undefined) {
           formParams = formParams.append('email', <any>email) || formParams;
       }
       if (anno !== undefined) {
           formParams = formParams.append('anno', <any>anno) || formParams;
       }

       return this.httpClient.post<Messaggio>(`${this.basePath}/versamento-esoneri/upload-previsione-dichiarazioni`,
           convertFormParamsToString ? formParams.toString() : formParams,
           {
               withCredentials: this.configuration.withCredentials,
               headers: headers,
               observe: observe,
               reportProgress: reportProgress
           }
       );

   }



   /**
     * Restituisce il pdf del pagamento
     * 
     * @param idVersamento 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
   public getPdfAvvisoPagamento(idVersamento: string, observe?: 'body', reportProgress?: boolean): Observable<Blob>;
   public getPdfAvvisoPagamento(idVersamento: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Blob>>;
   public getPdfAvvisoPagamento(idVersamento: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Blob>>;
   public getPdfAvvisoPagamento(idVersamento: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

       if (idVersamento === null || idVersamento === undefined) {
           throw new Error('Required parameter idVersamento was null or undefined when calling getPdfAvvisoPagamento.');
       }

       let headers = this.defaultHeaders;

       // to determine the Accept header
       let httpHeaderAccepts: string[] = [
           'application/pdf'
       ];
       const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
       if (httpHeaderAcceptSelected != undefined) {
           headers = headers.set('Accept', httpHeaderAcceptSelected);
       }

       // to determine the Content-Type header
       const consumes: string[] = [
       ];

       return this.httpClient.get(`${this.basePath}/versamento-esoneri/get-pdf-avviso-pagamento/${encodeURIComponent(String(idVersamento))}`,
           {
               withCredentials: this.configuration.withCredentials,
               headers: headers,
               observe: observe,
               reportProgress: reportProgress,
               responseType: "blob"
           }
       );
   }


}
