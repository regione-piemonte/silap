/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.api;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;

import io.quarkus.logging.Log;
import it.csi.silap.colmirsrv.api.cxf.dto.epaytypes.ResponseType;
import it.csi.silap.colmirsrv.api.cxf.dto.epaytypes.TrasmettiNotifichePagamentoResponseType;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.EsitoAggiornaPosizioniDebitorieRequest;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.EsitoInserimentoListaDiCaricoRequest;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.TrasmettiAvvisiScadutiRequest;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.TrasmettiNotifichePagamentoRequest;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.TrasmettiRTRequest;
import it.csi.silap.colmirsrv.api.cxf.dto.epaywso2enti.TrasmettiRichiesteDiRevocaRequest;
import it.csi.silap.colmirsrv.api.cxf.interf.epaywso2entisrv.IEPaywso2EntiService;
import it.csi.silap.colmirsrv.api.impl.ColmirsrvServiceImpl;



//@Policy(placement = Policy.Placement.BINDING, uri = "username-token-policy.xml")
//@EndpointProperties(value = {
//    @EndpointProperty(key = "ws-security.callback-handler", value = "it.csi.silap.colmirsrv.api.PasswordCallbackHandler")
//})
@javax.jws.WebService(
    serviceName = "EPaywso2EntiService",
    portName = "CustomBinding_IEPaywso2EntiService",
    targetNamespace = "http://www.csi.it/epay/epaywso/epaywso2entisrv"
    )
@RolesAllowed("epay")
public class ColmirsrvService implements IEPaywso2EntiService {

  @Inject
  private ColmirsrvServiceImpl colmirsrvServiceImpl;

 

  @Override
  public TrasmettiNotifichePagamentoResponseType trasmettiNotifichePagamento(TrasmettiNotifichePagamentoRequest trasmettiNotifichePagamentoRequest) {
    Log.info("request="+trasmettiNotifichePagamentoRequest);
    return colmirsrvServiceImpl.trasmettiNotifichePagamento(trasmettiNotifichePagamentoRequest);
  }



@Override
public ResponseType esitoInserimentoListaDiCarico(
		EsitoInserimentoListaDiCaricoRequest esitoInserimentoListaDiCaricoRequest) {
	// TODO Auto-generated method stub
	return null;
}



@Override
public ResponseType esitoAggiornaPosizioniDebitorie(
		EsitoAggiornaPosizioniDebitorieRequest esitoAggiornaPosizioniDebitorieRequest) {
	// TODO Auto-generated method stub
	return null;
}



@Override
public ResponseType trasmettiRichiesteDiRevoca(TrasmettiRichiesteDiRevocaRequest trasmettiRichiesteDiRevocaRequest) {
	// TODO Auto-generated method stub
	return null;
}



@Override
public ResponseType trasmettiRT(TrasmettiRTRequest trasmettiRTRequest) {
	// TODO Auto-generated method stub
	return null;
}





@Override
public ResponseType trasmettiAvvisiScaduti(TrasmettiAvvisiScadutiRequest trasmettiAvvisiScadutiRequest) {
	// TODO Auto-generated method stub
	return null;
}



 

}
