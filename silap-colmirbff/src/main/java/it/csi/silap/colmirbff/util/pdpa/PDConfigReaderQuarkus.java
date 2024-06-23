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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import it.csi.csi.metadata.OperationMetaData;
import it.csi.csi.metadata.ServiceMetaData;
import it.csi.csi.pfh.FHInfo;
import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.PortaDelegata;
import it.csi.csi.porte.PortaDelegataMultiprotocol;
import it.csi.csi.util.Param;
import it.csi.csi.util.log.Categories;
import it.csi.csi.wrapper.ConfigException;

/**
 * Patch per gestire le PD/PA in Quarkus
 */
public class PDConfigReaderQuarkus {
	  static final String CONFIG_NODE = "config";
	  
	  static final String URL_PA_ATTR = "url-pa";
	  
	  static final String URL_QMGR_ATTR = "url-qm";
	  
	  static final String URL_PSMGR_ATTR = "url-psm";
	  
	  static final String PLUGGABLE_PD_LIST_NODE = "pluggable-pd-list";
	  
	  static final String PLUGGABLE_PD_NODE = "pluggable-pd";
	  
	  static final String PORTA_DELEGATA_NODE = "porta-delegata";
	  
	  static final String SERVICES_NODE = "services";
	  
	  static final String SERVICE_NODE = "service";
	  
	  static final String OPERATIONS_NODE = "operations";
	  
	  static final String OPERATION_NODE = "operation";
	  
	  static final String PARAM_LIST_NODE = "param-list";
	  
	  static final String PARAM_NODE = "param";
	  
	  static final String RET_VAL_NODE = "ret-val";
	  
	  static final String QUEUE_MANAGER_NODE = "queue-manager";
	  
	  static final String PUBSUB_MANAGER_NODE = "pubsub-manager";
	  
	  static final String PROPERTIES_NODE = "properties";
	  
	  static final String PROPERTY_NODE = "property";
	  
	  static final String NAME_ATTR = "name";
	  
	  static final String TYPE_ATTR = "type";
	  
	  static final String VALUE_ATTR = "value";
	  
	  static final String TOP_VERSION_ATTR = "top-version";
	  
	  static final String SINCE_ATTR = "since";
	  
	  static final String CHOOSER_CLASS_ATTR = "chooser-class";
	  
	  static final String PRE_FH_LIST_NODE = "pre-function-handler-list";
	  
	  static final String POST_FH_LIST_NODE = "post-function-handler-list";
	  
	  static final String SYNCH_CALL = "synch-call";
	  
	  static final String ASYNCH_CALL = "asynch-call";
	  
	  static final String NOTIFY = "notify";
	  
	  public static InfoPortaDelegata read(String configFile) {
	    return read(configFile, null);
	  }
	  
	  public static InfoPortaDelegata read(String configFile, String versionFilter) {
	    FileInputStream fis = null;
	    try {
	      fis = new FileInputStream(configFile);
	      return read(fis, versionFilter);
	    } catch (IOException ioe) {
	      Categories.getLogger(null, "CSI-Config").error("Errore nella lettura del file di configurazione delle porte dalla posizione:");
	      Categories.getLogger(null, "CSI-Config").error(configFile);
	      Categories.getLogger(null, "CSI-Config").error("Dettagli:" + ioe);
	      return null;
	    } catch (Exception ex) {
	      Categories.getLogger(null, "CSI-Config").error("Errore nella parsificazione del file di configurazione della PD");
	      Categories.getLogger(null, "CSI-Config").error("Dettagli:" + ex);
	      return null;
	    } finally {
	      try {
	        if (fis != null)
	          fis.close(); 
	      } catch (Exception ex) {}
	    } 
	  }
	  
	  public static InfoPortaDelegata read(InputStream is) throws Exception {
	    return read(is, null);
	  }
	  
	  public static boolean versionLEQ(String v1, String v2) throws IllegalArgumentException {
	    try {
	      String sMaj1 = v1.substring(0, v1.indexOf('.'));
	      String sMin1 = v1.substring(v1.indexOf('.') + 1, v1.lastIndexOf('.'));
	      String sPatch1 = v1.substring(v1.lastIndexOf('.') + 1);
	      String sMaj2 = v2.substring(0, v2.indexOf('.'));
	      String sMin2 = v2.substring(v2.indexOf('.') + 1, v2.lastIndexOf('.'));
	      String sPatch2 = v2.substring(v2.lastIndexOf('.') + 1);
	      int maj1 = Integer.parseInt(sMaj1);
	      int min1 = Integer.parseInt(sMin1);
	      int patch1 = Integer.parseInt(sPatch1);
	      int maj2 = Integer.parseInt(sMaj2);
	      int min2 = Integer.parseInt(sMin2);
	      int patch2 = Integer.parseInt(sPatch2);
	      return (maj1 < maj2 || (maj1 == maj2 && min1 <= min2));
	    } catch (Throwable t) {
	      throw new IllegalArgumentException("Impossibile confrontare le stringhe di versione [" + v1 + "] e [" + v2 + "]: " + "controllare il formato (errore: " + t + ")");
	    } 
	  }
	  
	  public static InfoPortaDelegata read(InputStream is, String versionFilter) throws Exception {
	    InfoPortaDelegata infoPorta = null;
	    Class<?> pubIntClass = null;
	    Vector<OperationMetaData> vOpMetaData = null;
	    ServiceMetaData smd = null;
	    FHInfo[] preFHInfo = null;
	    FHInfo[] postFHInfo = null;
	    try {
	      Document xml = getDocument(is);
	      is.close();
	      is = null;
	      Element portaDelNode = (Element)xml.getFirstChild();
	      if (portaDelNode.getNodeName().equals("porta-delegata")) {
	        String nomePorta = portaDelNode.getAttribute("name");
	        Node servicesNode = findNode(portaDelNode.getChildNodes(), "services");
	        if (servicesNode != null) {
	          NodeList servicesChilds = servicesNode.getChildNodes();
	          vOpMetaData = new Vector<OperationMetaData>();
	          Node serviceNode = findNode(servicesChilds, "service");
	          if (serviceNode != null) {
	            String serviceName = ((Element)serviceNode).getAttribute("name");
	            String pubIntClassName = ((Element)serviceNode).getAttribute("public-interface-class");
	            pubIntClass = Class.forName(pubIntClassName);
	            NodeList serviceChilds = serviceNode.getChildNodes();
	            Node operationsNode = findNode(serviceChilds, "operations");
	            if (operationsNode != null) {
	              NodeList operationsNodeChilds = operationsNode.getChildNodes();
	              for (int i = 0; i < operationsNodeChilds.getLength(); i++) {
	                Node curr = operationsNodeChilds.item(i);
	                if (curr.getNodeType() == 1 && curr.getNodeName().equals("operation")) {
	                  OperationMetaData currOp = parseOperationMetaData((Element)curr, versionFilter);
	                  if (currOp != null)
	                    vOpMetaData.addElement(currOp); 
	                } 
	              } 
	              OperationMetaData[] omds = new OperationMetaData[vOpMetaData.size()];
	              vOpMetaData.copyInto((Object[])omds);
	              smd = new ServiceMetaData(serviceName, pubIntClass, omds);
	            } else {
	              throw new Exception("Nodo <operations> non trovato.");
	            } 
	          } else {
	            throw new Exception("Nodo <service> non trovato.");
	          } 
	        } else {
	          throw new Exception("Nodo <services> non trovato.");
	        } 
	        Node configNode = findNode(portaDelNode.getChildNodes(), "config");
	        if (configNode != null) {
	          Node urlPANode = findNode(configNode.getChildNodes(), "url-pa");
	          Node urlQMgrNode = findNode(configNode.getChildNodes(), "queue-manager");
	          Node urlPSMgrNode = findNode(configNode.getChildNodes(), "pubsub-manager");
	          if (urlPANode != null) {
	            String urlPaString = urlPANode.getNodeValue();
	            String urlQMgrString = (urlQMgrNode == null) ? null : ((Element)urlQMgrNode).getAttribute("url");
	            String urlPSMgrString = (urlPSMgrNode == null) ? null : ((Element)urlPSMgrNode).getAttribute("url");
	            infoPorta = new InfoPortaDelegata(nomePorta, urlPaString, urlQMgrString, urlPSMgrString, pubIntClass, PortaDelegataMultiprotocol.class, null);
	            Node preFHNode = findNode(configNode.getChildNodes(), "pre-function-handler-list");
	            Node postFHNode = findNode(configNode.getChildNodes(), "post-function-handler-list");
	            if (preFHNode != null)
	              preFHInfo = parsePFH(preFHNode.getChildNodes()); 
	            if (postFHNode != null)
	              postFHInfo = parsePFH(postFHNode.getChildNodes()); 
	            Node pluginsNode = findNode(configNode.getChildNodes(), "pluggable-pd-list");
	            if (pluginsNode != null) {
	              String chooserClassName = ((Element)pluginsNode).getAttribute("chooser-class");
	              try {
	                Class<?> chClass = Thread.currentThread().getContextClassLoader().loadClass(chooserClassName);
	                infoPorta.setChooserClass(chClass);
	              } catch (Exception ecc) {
	                ecc.printStackTrace();
	                throw new Exception("Errore nella lettura della chooser-class:" + ecc.getMessage());
	              } 
	              NodeList pluginNodes = pluginsNode.getChildNodes();
	              Vector<InfoPortaDelegata> pluginInfo = new Vector<InfoPortaDelegata>();
	              for (int i = 0; i < pluginNodes.getLength(); i++) {
	                Node curr = pluginNodes.item(i);
	                if (curr.getNodeType() == 1)
	                  if (curr.getNodeName().equals("pluggable-pd")) {
	                    String pluginUrlPa = ((Element)curr).getAttribute("url-pa");
	                    String pluginUrlQm = ((Element)curr).getAttribute("url-qm");
	                    String pluginUrlPSm = ((Element)curr).getAttribute("url-psm");
	                    Class<? extends PortaDelegata> pluginClass = null;
	                    String pluginClassName = ((Element)curr).getAttribute("class");
	                    try {
	                      pluginClass = Class.forName(pluginClassName).asSubclass(PortaDelegata.class);
	                    } catch (Throwable e) {
	                      Categories.getLogger(null, "CSI-Config").fatal("Errore durante la lettura della configurazione PD (caricamento plugin class=" + pluginClassName + "):" + e, e);
	                      throw new ConfigException("Errore durante la lettura della configurazione PD (caricaento plugin class=" + pluginClassName + "):" + e, e);
	                    } 
	                    String pluginName = ((Element)curr).getAttribute("name");
	                    NodeList pluginNodeChilds = curr.getChildNodes();
	                    Properties props = new Properties();
	                    if (pluginNodeChilds != null) {
	                      Node propertiesNode = findNode(pluginNodeChilds, "properties");
	                      if (propertiesNode != null) {
	                        NodeList propNodes = propertiesNode.getChildNodes();
	                        if (propNodes != null) {
	                          int nProps = propNodes.getLength();
	                          for (int iProps = 0; iProps < nProps; iProps++) {
	                            Node currNode = propNodes.item(iProps);
	                            if (currNode.getNodeType() == 1 && currNode.getNodeName().equals("property")) {
	                              Element propertyEl = (Element)currNode;
	                              String name = propertyEl.getAttribute("name");
	                              String value = propertyEl.getAttribute("value");
	                              props.put(name, value);
	                            } 
	                          } 
	                        } 
	                      } 
	                    } 
	                    InfoPortaDelegata currInfo = new InfoPortaDelegata(pluginName, pluginUrlPa, !pluginUrlQm.equals("") ? pluginUrlQm : urlQMgrString, !pluginUrlPSm.equals("") ? pluginUrlPSm : urlPSMgrString, pubIntClass, pluginClass, null);
	                    currInfo.setProperties(props);
	                    pluginInfo.addElement(currInfo);
	                  } else {
	                    throw new Exception("Nodo <pluggable-pd> non trovato.");
	                  }  
	              } 
	              int nPlugin = pluginInfo.size();
	              InfoPortaDelegata[] ris = new InfoPortaDelegata[nPlugin];
	              for (int j = 0; j < pluginInfo.size(); j++)
	                ris[j] = pluginInfo.elementAt(j); 
	              infoPorta.setPlugins(ris);
	              infoPorta.setServiceMetaData(smd);
	              infoPorta.setPreFH(preFHInfo);
	              infoPorta.setPostFH(postFHInfo);
	            } else {
	              throw new Exception("Nodo <pluggable-pd-list> non trovato.");
	            } 
	          } else {
	            throw new Exception("Occorre specificare l'url della controparte PA");
	          } 
	        } else {
	          throw new Exception("Nodo <config> non trovato.");
	        } 
	      } else {
	        throw new Exception("Nodo <porta-delegata> non trovato.");
	      } 
	    } catch (Exception e) {
	      Categories.getLogger(null, "CSI-Config").fatal("Errore durante la lettura della configurazione PD:" + e, e);
	      throw e;
	    } 
	    return infoPorta;
	  }
	  
	  static Param parseParamMetaData(Element currOpNode) throws Exception {
	    String name = currOpNode.getAttribute("name");
	    String type = currOpNode.getAttribute("type");
	    Class cType = parseType(type);
	    return new Param(name, cType, null, "");
	  }
	  
	  static FHInfo[] parsePFH(NodeList pfhListChilds) throws Exception {
	    FHInfo[] allInfo = null;
	    Vector<FHInfo> v = new Vector<FHInfo>();
	    int n = pfhListChilds.getLength();
	    for (int i = 0; i < n; i++) {
	      Node currNode = pfhListChilds.item(i);
	      if (currNode.getNodeType() == 1) {
	        Element currPFH = (Element)currNode;
	        FHInfo currInfo = PFHConfigReaderQuarkus.read(currPFH);
	        v.addElement(currInfo);
	      } 
	    } 
	    if (v.size() > 0) {
	      allInfo = new FHInfo[v.size()];
	      v.copyInto((Object[])allInfo);
	    } 
	    return allInfo;
	  }
	  
	  static OperationMetaData parseOperationMetaData(Element currOpNode, String versionFilter) throws Exception {
	    String name = currOpNode.getAttribute("name");
	    String type = currOpNode.getAttribute("type");
	    String since = currOpNode.getAttribute("since");
	    if (versionFilter != null && since != null && !"".equals(since) && !versionLEQ(since, versionFilter))
	      return null; 
	    int typeCode = type.equals("synch-call") ? 1 : (type.equals("asynch-call") ? 2 : (type.equals("notify") ? 3 : -1));
	    Node paramsNode = findNode(currOpNode.getChildNodes(), "param-list");
	    Vector<Param> vParMetaData = new Vector<Param>();
	    if (paramsNode != null) {
	      NodeList paramsNodeChilds = paramsNode.getChildNodes();
	      for (int i = 0; i < paramsNodeChilds.getLength(); i++) {
	        Node curr = paramsNodeChilds.item(i);
	        if (curr.getNodeType() == 1 && curr.getNodeName().equals("param")) {
	          Param currPar = parseParamMetaData((Element)curr);
	          vParMetaData.addElement(currPar);
	        } 
	      } 
	    } 
	    Param[] params = new Param[vParMetaData.size()];
	    vParMetaData.copyInto((Object[])params);
	    Node retValNode = findNode(currOpNode.getChildNodes(), "ret-val");
	    String retType = ((Element)retValNode).getAttribute("type");
	    Class retTypeClass = retType.equals("void") ? null : parseType(retType);
	    OperationMetaData omd = new OperationMetaData(name, params, retTypeClass, typeCode);
	    return omd;
	  }
	  
	  static Document getDocument(InputStream _in) throws Exception {
	    try {
	      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	      Document doc = docBuilder.parse(_in);
	      return doc;
	    } catch (SAXParseException e) {
	      System.out.println(e.getMessage() + ":" + e.getColumnNumber() + ":" + e.getLineNumber());
	      e.printStackTrace();
	      throw e;
	    } catch (SAXException e) {
	      System.out.println(e.getException());
	      throw e;
	    } catch (Exception e) {
	      throw e;
	    } 
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
	  
	  private static Class parseType(String type) throws Exception {
	    if (type.endsWith("[]")) {
	      String elType = type.substring(0, type.length() - 2).trim();
	      Class<?> elTypeClass = parseType(elType);
	      Object sampleArrObj = Array.newInstance(elTypeClass, 1);
	      Class<?> cl = sampleArrObj.getClass();
	      return cl;
	    } 
	    if (type.equals("int"))
	      return int.class; 
	    if (type.equals("long"))
	      return long.class; 
	    if (type.equals("float"))
	      return float.class; 
	    if (type.equals("double"))
	      return double.class; 
	    if (type.equals("byte"))
	      return byte.class; 
	    if (type.equals("boolean"))
	      return boolean.class; 
	    return Class.forName(type);
	  }
	  
	  protected String _acn = null;
	  
	  public static void main(String[] args) {
	    read("c:/progetti/centroservizi/porte/src/defpdelegata.xml");
	  }
}
