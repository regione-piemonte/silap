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
package it.csi.silap.colmirbff.api.dto.profilazione;

import java.util.ArrayList;
import java.util.List;

public class ProfiloUtenteProdis {

  // ruoli tornati da iride - AAEP - comonl
  private ProfiloUtente profiloOrchestratore;

  // lista dei ruoli da presentare alla sceltaRuolo
  private List<Profilo> listaRuoliAmmessi;

  // ruolo effettivo
  Profilo profilo;

  public ProfiloUtenteProdis() {
  }

  public ProfiloUtenteProdis(ProfiloUtente profiloOrchestratore) {
    this.profiloOrchestratore = profiloOrchestratore;
  }

  public ProfiloUtente getProfiloOrchestratore() {
    return profiloOrchestratore;
  }

  public void setProfiloOrchestratore(ProfiloUtente profiloOrchestratore) {
    this.profiloOrchestratore = profiloOrchestratore;
  }

  public List<Profilo> getListaRuoliAmmessi() {
    return listaRuoliAmmessi;
  }

  public void setListaRuoliAmmessi(List<Profilo> listaRuoliAmmessi) {
    this.listaRuoliAmmessi = listaRuoliAmmessi;
  }

  public Profilo getProfilo() {
    return profilo;
  }

  public void setProfilo(Profilo profilo) {
    this.profilo = profilo;
  }

  public void inizializzaListaRuoli() {
    listaRuoliAmmessi = new ArrayList<Profilo>();
  }

  public void addRuoloInLista(Profilo profilo) {
    if (listaRuoliAmmessi != null) {
      listaRuoliAmmessi.add(profilo);
    }
    else {
      inizializzaListaRuoli();
      listaRuoliAmmessi.add(profilo);
    }
  }

  @Override
  public String toString() {
    return "ProfiloUtenteProdis [profiloOrchestratore=" + profiloOrchestratore + ", listaRuoliAmmessi=" + listaRuoliAmmessi + ", profilo=" + profilo + "]";
  }

}
