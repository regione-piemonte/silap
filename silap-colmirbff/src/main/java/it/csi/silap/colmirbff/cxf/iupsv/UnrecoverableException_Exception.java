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

package it.csi.silap.colmirbff.cxf.iupsv;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.7.2
 * 2023-07-13T10:01:43.616+02:00
 * Generated source version: 2.7.2
 */

@WebFault(name = "fault2", targetNamespace = "urn:iupsv")
public class UnrecoverableException_Exception extends Exception {
    
    private it.csi.silap.colmirbff.cxf.iupsv.UnrecoverableException fault2;

    public UnrecoverableException_Exception() {
        super();
    }
    
    public UnrecoverableException_Exception(String message) {
        super(message);
    }
    
    public UnrecoverableException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public UnrecoverableException_Exception(String message, it.csi.silap.colmirbff.cxf.iupsv.UnrecoverableException fault2) {
        super(message);
        this.fault2 = fault2;
    }

    public UnrecoverableException_Exception(String message, it.csi.silap.colmirbff.cxf.iupsv.UnrecoverableException fault2, Throwable cause) {
        super(message, cause);
        this.fault2 = fault2;
    }

    public it.csi.silap.colmirbff.cxf.iupsv.UnrecoverableException getFaultInfo() {
        return this.fault2;
    }
}