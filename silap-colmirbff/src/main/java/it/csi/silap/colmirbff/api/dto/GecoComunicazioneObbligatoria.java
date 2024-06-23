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
package it.csi.silap.colmirbff.api.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.csi.silap.colmirbff.util.Format;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GecoComunicazioneObbligatoria {

	private Integer id = null;

	private String codiceFiscaleAzienda = null;
	private Long idAziendaSilp = null;
	private Long idComunicazione = null;
	private Long idComunicazionePrec = null;
	private String codComunicazioneTu = null;
	private String dsComunicazioneTu = null;
	private String codiceFiscaleLavoratore = null;
	private String cognomeLavoratore = null;
	private String nomeLavoratore = null;
	private Long idLavoratoreSILP = null;
	private String codiceFiscaleLavoratoreObb = null;
	private String cognomeLavoratoreObb = null;
	private String nomeLavoratoreObb = null;
	private Long idLavoratoreSILPObb = null;
	private String codTipoComunicazione = null;
	private String dsTipoComunicazione = null;
	private String codTipoTracciato = null;
	private Date dataComunicazione = null;
	private Date dataInizioRapporto = null;
	private Date dataTrasformazione = null;
	private Date dataFineProroga = null;
	private Date dataFineRapporto = null;
	private Date dataCessazione = null;
	private String codComunicazione = null;
	private String codRegioneSedeLavoroMin = null;
	private String dsRegioneSedeLavoroMin = null;
	private String codProvinciaSedeLavoroMin = null;
	private String siglaProvinciaSedeLavoro = null;
	private String codComuneSedeLavoroMin = null;
	private String dsComuneSedeLavoroMin = null;
	private Long idSedeLavoroSILP = null;
	private String codRegioneSedeLavoroMinPrec = null;
	private String dsRegioneSedeLavoroMinPrec = null;
	private String codProvinciaSedeLavoroMinPrec = null;
	private String siglaProvinciaSedeLavoroPrec = null;
	private String codComuneSedeLavoroMinPrec = null;
	private String dsComuneSedeLavoroMinPrec = null;
	private Long idSedeLavoroPrecSILP = null;
	private String codTipoTrasformazione = null;
	private String dsTipoTrasformazione = null;
	private String codTipoContratto = null;
	private String dsTipoContratto = null;
	private String flgForma = null;
	private String flgFullTime = null;
	private Long oreSettimanaliMedie = null;
	private Long oreSettimanaliMediePrec = null;
	private String flgLegge68 = null;
	private String codiceCCNL = null;
	private String dsCCNL = null;
	private String codQualificaMin = null;
	private String dsQualificaMin = null;
	private String flgSocio = null;
	private String flgDistaccoEstero = null;
	private Date dataInvio = null;
	private String flgCurrentRecord = null;
	
	
	
	public static GecoComunicazioneObbligatoria copy(GecoComunicazioneObbligatoria other) {
		GecoComunicazioneObbligatoria succ = new GecoComunicazioneObbligatoria();
		
		succ.setCodComunicazione(other.getCodComunicazione());
		succ.setCodRegioneSedeLavoroMin(other.getCodRegioneSedeLavoroMin());
		succ.setDsRegioneSedeLavoroMin(other.getDsRegioneSedeLavoroMin());
		succ.setCodProvinciaSedeLavoroMin(other.getCodProvinciaSedeLavoroMin());
		succ.setSiglaProvinciaSedeLavoro(other.getSiglaProvinciaSedeLavoro());
		succ.setCodComuneSedeLavoroMin(other.getCodComuneSedeLavoroMin());
		succ.setDsComuneSedeLavoroMin(other.getDsComuneSedeLavoroMin());
		succ.setCodRegioneSedeLavoroMinPrec(other.getCodRegioneSedeLavoroMinPrec());
		succ.setDsRegioneSedeLavoroMinPrec(other.getDsRegioneSedeLavoroMinPrec());
		succ.setCodProvinciaSedeLavoroMinPrec(other.getCodProvinciaSedeLavoroMinPrec());
		succ.setSiglaProvinciaSedeLavoroPrec(other.getSiglaProvinciaSedeLavoroPrec());
		succ.setCodComuneSedeLavoroMinPrec(other.getCodComuneSedeLavoroMinPrec());
		succ.setDsComuneSedeLavoroMinPrec(other.getDsComuneSedeLavoroMinPrec());
		succ.setCodTipoTrasformazione(other.getCodTipoTrasformazione());
		succ.setDsTipoTrasformazione(other.getDsTipoTrasformazione());
		succ.setCodTipoContratto(other.getCodTipoContratto());
		succ.setDsTipoContratto(other.getDsTipoContratto());
		succ.setFlgForma(other.getFlgForma());
		succ.setFlgFullTime(other.getFlgFullTime());
		succ.setOreSettimanaliMedie(other.getOreSettimanaliMedie());
		succ.setOreSettimanaliMediePrec(other.getOreSettimanaliMediePrec());
		succ.setFlgLegge68(other.getFlgLegge68());
		succ.setCodiceCCNL(other.getCodiceCCNL());
		succ.setDsCCNL(other.getDsCCNL());
		succ.setCodQualificaMin(other.getCodQualificaMin());
		succ.setDsQualificaMin(other.getDsQualificaMin());
		succ.setFlgSocio(other.getFlgSocio());
		succ.setFlgDistaccoEstero(other.getFlgDistaccoEstero());
		
		succ.setCodiceFiscaleLavoratore(other.getCodiceFiscaleLavoratore());
		
		
		succ.setDataComunicazione(other.getDataComunicazione());
		succ.setDataInizioRapporto(other.getDataInizioRapporto());
		succ.setDataTrasformazione(other.getDataTrasformazione());
		succ.setDataFineProroga(other.getDataFineProroga());
		succ.setDataFineRapporto(other.getDataFineRapporto());
		succ.setDataCessazione(other.getDataCessazione());
		
		
		succ.setDsTipoContratto(other.getDsTipoContratto());
		
		succ.setCognomeLavoratore(other.getCognomeLavoratore());
		succ.setNomeLavoratore(other.getNomeLavoratore());

		
		return succ;

	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GecoComunicazioneObbligatoria other = (GecoComunicazioneObbligatoria) obj;
		return Objects.equals(siglaProvinciaSedeLavoro, other.siglaProvinciaSedeLavoro)
				&& Objects.equals(codTipoComunicazione, other.codTipoComunicazione)
				&& Objects.equals(codTipoContratto, other.codTipoContratto)
				&& Objects.equals(codiceFiscaleLavoratore, other.codiceFiscaleLavoratore)
				&& Format.isDateSoloGiornoUguali(dataComunicazione, other.dataComunicazione)
				&& Format.isDateSoloGiornoUguali(dataInizioRapporto, other.dataInizioRapporto)
				&& Objects.equals(flgForma, other.flgForma) 
				&& Objects.equals(flgFullTime, other.flgFullTime)
				&& Objects.equals(flgLegge68, other.flgLegge68)
				&& Objects.equals(flgSocio, other.flgSocio)
				&& Objects.equals(oreSettimanaliMedie, other.oreSettimanaliMedie);
	}
	
	
}
