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
package it.csi.silap.colmirsrv.api.dto.profilazione;

import java.io.Serializable;
import java.util.List;

import it.csi.iride2.policy.entity.Actor;

public class RuoloIrideListaCasiUso implements Serializable {

  private static final long serialVersionUID = 1L;

  private int indice;

  private Actor actor;

  private List<String> listaCasiUso;

  public int getIndice() {
    return indice;
  }

  public void setIndice(int indice) {
    this.indice = indice;
  }

  public Actor getActor() {
    return actor;
  }

  public void setActor(Actor actor) {
    this.actor = actor;
  }

  public List<String> getListaCasiUso() {
    return listaCasiUso;
  }

  public void setListaCasiUso(List<String> listaCasiUso) {
    this.listaCasiUso = listaCasiUso;
  }

  @Override
  public String toString() {
    return "RuoloIrideListaCasiUso [indice=" + indice + ", actor=" + actor + ", listaCasiUso=" + listaCasiUso + "]";
  }

}
