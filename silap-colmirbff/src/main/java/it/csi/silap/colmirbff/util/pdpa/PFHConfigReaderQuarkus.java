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
package it.csi.silap.colmirbff.util.pdpa;

import java.util.Properties;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.csi.csi.pfh.FHInfo;
import it.csi.csi.util.log.Categories;

/**
 * Patch per gestire le PD/PA in Quarkus
 */
public class PFHConfigReaderQuarkus {
	
	  private static final String PFH_NODE = "pluggable-fh";
	  
	  private static final String NAME_ATTR = "name";
	  
	  private static final String CLASS_ATTR = "class";
	  
	  private static final String PROPERTIES_NODE = "properties";
	  
	  private static final String PROPERTY_NODE = "property";
	  
	  private static final String VALUE_ATTR = "value";
	  
	  public static FHInfo read(Element pfhElement) {
	    Class<?> hndClass = null;
	    String hndName = null;
	    Properties props = new Properties();
	    if (pfhElement.getNodeName().equals("pluggable-fh")) {
	      String className = pfhElement.getAttribute("class");
	      try {
	        hndClass = Thread.currentThread().getContextClassLoader().loadClass(className);
	      } catch (ClassNotFoundException cnfe) {
	        Categories.getLogger(null, "CSI-Config").fatal("Classe di PFH (" + className + ") non trovata");
	        return null;
	      } 
	      hndName = pfhElement.getAttribute("name");
	      Node propertiesNode = findNode(pfhElement.getChildNodes(), "properties");
	      if (propertiesNode != null) {
	        NodeList propNodes = propertiesNode.getChildNodes();
	        int n = propNodes.getLength();
	        for (int i = 0; i < n; i++) {
	          Node currNode = propNodes.item(i);
	          if (currNode.getNodeType() == 1 && currNode.getNodeName().equals("property")) {
	            Element propertyEl = (Element)currNode;
	            String name = propertyEl.getAttribute("name");
	            String value = propertyEl.getAttribute("value");
	            props.put(name, value);
	          } 
	        } 
	      } 
	      return new FHInfo(hndName, hndClass, props);
	    } 
	    Categories.getLogger(null, "CSI-Config").fatal("PFHCfgReader: nodo 'pluggable-fh' non trovato.");
	    return null;
	  }
	  
	  static Node findNode(NodeList nl, String nodeName) {
	    if (nl == null || nl.getLength() == 0)
	      return null; 
	    int n = nl.getLength();
	    for (int i = 0; i < n; i++) {
	      Node curr = nl.item(i);
	      if (curr.getNodeName().equals(nodeName))
	        return curr; 
	    } 
	    return null;
	  }
}
