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

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

//@ApplicationScoped
public class PasswordCallbackHandler implements CallbackHandler {

  @ConfigProperty(name = "epay.endpoint.cxf.password")
  private String password;

  @ConfigProperty(name = "epay.endpoint.cxf.username")
  private String user;

  public PasswordCallbackHandler() {
    password = ConfigProvider.getConfig().getValue("epay.endpoint.cxf.password", String.class);
  }

  /**
   * It attempts to get the password from the private alias/passwords map.
   */
  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    for (int i = 0; i < callbacks.length; i++) {
      WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
      pc.setPassword(password);
      return;
    }
  }

}
