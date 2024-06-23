/*-
 * ========================LICENSE_START=================================
 * colmirbff
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirbff.api.impl.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import it.csi.silap.colmirbff.api.dto.ApiMessage;
import it.csi.silap.colmirbff.api.dto.DatiServiziProdisSilp;
import it.csi.silap.colmirbff.api.dto.Provincia;
import it.csi.silap.colmirbff.api.dto.RiconoscimentiInabilita;
import it.csi.silap.colmirbff.api.dto.SilapConstants;
import it.csi.silap.colmirbff.api.dto.VersamentoProvincia;
import it.csi.silap.colmirbff.api.dto.VersamentoPvConvenzione;
import it.csi.silap.colmirbff.api.dto.VersamentoPvEsonero;
import it.csi.silap.colmirbff.api.dto.VersamentoTipoConvenzione;
import it.csi.silap.colmirbff.api.impl.generic.BaseApiServiceImpl;
import it.csi.silap.colmirbff.integration.entity.EsoTRiconoscimentoInabilita;
import it.csi.silap.colmirbff.integration.silprest.dto.CallInfoParam;
import it.csi.silap.colmirbff.integration.silprest.dto.Convenzione;
import it.csi.silap.colmirbff.integration.silprest.dto.ConvenzioneEsoneroFindParam;
import it.csi.silap.colmirbff.integration.silprest.dto.ConvenzioneFindResponse;
import it.csi.silap.colmirbff.integration.silprest.dto.Esonero;
import it.csi.silap.colmirbff.integration.silprest.dto.EsoneroFindResponse;
import it.csi.silap.colmirbff.util.CommonUtils;

@Dependent
public class SilprestManager extends BaseApiServiceImpl {

	@ConfigProperty(name = "silprest.url")
	String silprestUrl;

	@ConfigProperty(name = "silprest.user")
	String silprestUser;

	@ConfigProperty(name = "silprest.password")
	String silprestPassword;

	
	
	public DatiServiziProdisSilp findConvenzioni(Long idAzienda, String codFiscaleAzienda, String partitaIvaAzienda, String annoRiferimento)  {
		DatiServiziProdisSilp resp = new DatiServiziProdisSilp();
		ConvenzioneEsoneroFindParam body = new ConvenzioneEsoneroFindParam();
		 
		body.setCallInfoParam( createCallInfo() );
		
		body.setAnno(  annoRiferimento );
		body.setIdAzienda(idAzienda);
		body.setCodFiscaleAzienda(codFiscaleAzienda);
		body.setPartitaIvaAzienda(partitaIvaAzienda);
		
		ConvenzioneFindResponse conven = postService(silprestUrl + "/convenzioni/find", null,
				null, body, ConvenzioneFindResponse.class, silprestUser, silprestPassword);
		if (conven.isEsitoPositivo()) {
			List<VersamentoProvincia> elencoProv = new ArrayList<VersamentoProvincia>();
			resp.setProvinceConvenzioni(elencoProv);
			for (Iterator<Convenzione> iterConv = conven.getConvenzioni().iterator(); iterConv.hasNext();) {
				 
				Convenzione silpConv = (Convenzione) iterConv.next();
				VersamentoProvincia versProv = null;
				for (Iterator<VersamentoProvincia> iterator = elencoProv.iterator(); iterator.hasNext();) {
					VersamentoProvincia versamentoProvincia = (VersamentoProvincia) iterator.next();
					if (silpConv.getIdProvincia().equals(versamentoProvincia.getSilapDProvincia().getId())) {
						versProv = versamentoProvincia;
						break;
					} 
				}
				if (CommonUtils.isVoid(versProv))    {
					Provincia newProv = new Provincia();
					newProv.setId(silpConv.getIdProvincia() );
					newProv.setDescr(silpConv.getSiglaProvincia());
					newProv.setDsSilapDProvincia(silpConv.getDsProvincia());
					
					versProv = new VersamentoProvincia();
			        versProv.setSilapDProvincia(newProv);
			        versProv.setEsoTVersamentoPvConvenziones(new ArrayList<VersamentoPvConvenzione>());
			        VersamentoPvConvenzione silapConv = mappingConvenzione(silpConv);
			        versProv.getEsoTVersamentoPvConvenziones().add(silapConv);
			        
			        elencoProv.add(versProv);
				} else {
					VersamentoPvConvenzione silapConv = mappingConvenzione(silpConv);
			        versProv.getEsoTVersamentoPvConvenziones().add(silapConv);
				}

			}
		} else {
			addSilpErrorToRisposta(resp, conven.getApiMessages());
		}
		return resp;
	}
	
	
    private VersamentoPvConvenzione mappingConvenzione(Convenzione convSilp) {
    	VersamentoPvConvenzione out = new VersamentoPvConvenzione();
    	
    	out.setDScadenza(convSilp.getDataScadenza());
    	out.setDStipula(convSilp.getDataStipula());
    	out.setNumPosizioniAperte(convSilp.getNumPosAttive());

    	//  occorre collegare tipo convenzione
     	VersamentoTipoConvenzione versTipo = new VersamentoTipoConvenzione();
     	versTipo.setIdSilL68TipoConvenzione(convSilp.getIdTipoConvenzione());
    	versTipo.setDsEsoDVersamentoTipoConvenzione(convSilp.getDsTipoConvenzione());
    	out.setEsoDVersamentoTipoConvenzione(versTipo);
    	
    	return out;
	}


	public DatiServiziProdisSilp findEsoneri(Long idAzienda, String codFiscaleAzienda, String partitaIvaAzienda, String annoRiferimento, String codProvinciaMin)  {
    	DatiServiziProdisSilp resp = new DatiServiziProdisSilp();
		ConvenzioneEsoneroFindParam body = new ConvenzioneEsoneroFindParam();
		 
		body.setCallInfoParam( createCallInfo() );
		
		body.setAnno(  annoRiferimento );
		body.setIdAzienda(idAzienda);
		body.setCodFiscaleAzienda(codFiscaleAzienda);
		body.setPartitaIvaAzienda(partitaIvaAzienda);
		if (codProvinciaMin != null) {
			body.setIdProvincia(codProvinciaMin);
		}
		body.setIdRegione(SilapConstants.ID_REGIONE_PIEMONTE);
		
		EsoneroFindResponse eson = postService(silprestUrl + "/esoneri/find", null,
				null, body, EsoneroFindResponse.class, silprestUser, silprestPassword);
		
		
		if (eson.isEsitoPositivo()) {
			List<VersamentoProvincia> elencoProv = new ArrayList<VersamentoProvincia>();
			resp.setProvinceEsoneri(elencoProv);
			
			for (Iterator<Esonero> iterEson = eson.getEsoneri().iterator(); iterEson.hasNext();) {
				 
				Esonero silpEson = (Esonero) iterEson.next();
				VersamentoProvincia versProv = null;
				for (Iterator<VersamentoProvincia> iterator = elencoProv.iterator(); iterator.hasNext();) {
					VersamentoProvincia versamentoProvincia = (VersamentoProvincia) iterator.next();
					if (silpEson.getIdProvincia().equals(versamentoProvincia.getSilapDProvincia().getId())) {
						versProv = versamentoProvincia;
						break;
					} 
				}
				if (CommonUtils.isVoid(versProv))    {
					Provincia newProv = new Provincia();
					newProv.setId(silpEson.getIdProvincia() );
					newProv.setDescr(silpEson.getSiglaProvincia());
					newProv.setDsSilapDProvincia(silpEson.getDsProvincia());
					
					versProv = new VersamentoProvincia();
			        versProv.setSilapDProvincia(newProv);
			        versProv.setEsoTVersamentoPvEsoneros(new ArrayList<VersamentoPvEsonero>());
			         
			        versProv.getEsoTVersamentoPvEsoneros().add( mappingEsonero(silpEson) );
			        elencoProv.add(versProv);
				} else {

			        versProv.getEsoTVersamentoPvEsoneros().add( mappingEsonero(silpEson) );
				}

			}
		} else {
			addSilpErrorToRisposta(resp, eson.getApiMessages());
		}
		
		return resp;
	}


	private VersamentoPvEsonero mappingEsonero(Esonero elem) {
		VersamentoPvEsonero out = new VersamentoPvEsonero();

		 
		
		out.setDRichiesta(elem.getDataRichiesta());
		out.setDConcessione(elem.getDataConcessione());
		out.setDDiniego(elem.getDataDiniego());
		out.setDScadenza(elem.getDataScadenza());
		out.setPercConcessione(elem.getPercentualeConcessione());
		out.setPercRichiesta(elem.getPercentualeRichiesta());
		
		// recupera anche i dati dell'ultima comunicazione
		out.setDClassificazione(elem.getDataClassificazione());
		out.setDsTipoComunicazione(elem.getDsTipoComunicazione());
		out.setNumClassificazione(elem.getNumClassificazione());
		 
		
		return out;
	}




	private CallInfoParam createCallInfo() {
		CallInfoParam cInfo = new CallInfoParam();
		cInfo.setCodApplicativo(SilapConstants.SILPREST_CALLER);
		cInfo.setCodUser(SilapConstants.SILPREST_USER);
		return cInfo;
	}
	

    private void addSilpErrorToRisposta(DatiServiziProdisSilp resp,
			List<it.csi.silap.colmirbff.integration.silprest.dto.ApiMessage> listaErr) {
		 
		for (Iterator<it.csi.silap.colmirbff.integration.silprest.dto.ApiMessage> errM = listaErr.iterator(); errM.hasNext();) {
			it.csi.silap.colmirbff.integration.silprest.dto.ApiMessage apiMsg = (it.csi.silap.colmirbff.integration.silprest.dto.ApiMessage) errM.next();
			ApiMessage msg = new ApiMessage();
			
			   msg.setCode(apiMsg.getCode());
			   msg.setMessage(apiMsg.getMessage());
			   msg.setTipo("ERROR");
			 
			   msg.setError(true);
			   resp.getMessaggi().add(msg);
		 
		}
	}
    
    
    /**
     * 
     * Questo metodo con la ricerca alla tabella verra eliminato e sostituito con il servizio
     * 
     * @param idAzienda
     * @param codFiscaleAzienda
     * @param partitaIvaAzienda
     * @param annoRiferimento
     * @return
     */
    public List<RiconoscimentiInabilita> findRiconoscimentiInabilita(String codFiscaleAzienda, String partitaIvaAzienda, String annoRiferimento) {
    	List<EsoTRiconoscimentoInabilita> esoTRiconoscimentoInabilitaList = 
    			EsoTRiconoscimentoInabilita.list("codFiscale = ?1 and annoRiferimento = ?2", codFiscaleAzienda, Long.parseLong(annoRiferimento));
    	
    	return mappers.RICONOSCIMENTI_INABILITA.toModels(esoTRiconoscimentoInabilitaList);
    }
    
}
