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
package it.csi.silap.colmirbff.integration.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="eso_t_riconoscimenti_inabilita")
public class EsoTRiconoscimentoInabilita extends PanacheEntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_eso_t_riconoscimenti_inabilita")
	private Long idEsoTRiconoscimentiInabilita;
	
	@Column(name="id_sil_azi_anagrafica")
	private Long idSilAziAnagrafica;
	
	@Column(name="cod_fiscale")
	private String codFiscale;
	
	@Column(name="partiva_iva")
	private String partivaIva;
	
	@Column(name="anno_riferimento")
	private Long annoRiferimento;
	
	@Column(name="id_silap_d_provincia")
	private String idSilapDProvincia;
	
	@Column(name="d_riconosc_invalidita")
	private Date dRiconoscInvalidita;
	
	@Column(name="d_scadenza")
	private Date dScadenza;
	
	@Column(name="cod_fiscale_lav")
	private String codFiscaleLav;
	
	@Column(name="d_inizio_rapporto")
	private Date dInizioRapporto;
	
	@Column(name="num_ore_sett_med")
	private Long numOreSettMed;
	

}
