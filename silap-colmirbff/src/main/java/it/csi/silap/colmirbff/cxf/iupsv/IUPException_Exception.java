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
 * 2023-07-13T10:01:43.605+02:00
 * Generated source version: 2.7.2
 */

@WebFault(name = "fault3", targetNamespace = "urn:iupsv")
public class IUPException_Exception extends Exception {
    
    private it.csi.silap.colmirbff.cxf.iupsv.IUPException fault3;

    public IUPException_Exception() {
        super();
    }
    
    public IUPException_Exception(String message) {
        super(message);
    }
    
    public IUPException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public IUPException_Exception(String message, it.csi.silap.colmirbff.cxf.iupsv.IUPException fault3) {
        super(message);
        this.fault3 = fault3;
    }

    public IUPException_Exception(String message, it.csi.silap.colmirbff.cxf.iupsv.IUPException fault3, Throwable cause) {
        super(message, cause);
        this.fault3 = fault3;
    }

    public it.csi.silap.colmirbff.cxf.iupsv.IUPException getFaultInfo() {
        return this.fault3;
    }
}