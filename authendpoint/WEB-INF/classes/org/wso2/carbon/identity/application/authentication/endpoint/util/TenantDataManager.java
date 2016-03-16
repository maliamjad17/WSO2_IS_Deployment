/*     */ package org.wso2.carbon.identity.application.authentication.endpoint.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TenantDataManager
/*     */ {
/*  40 */   private static final Log log = LogFactory.getLog(TenantDataManager.class);
/*     */   private static final String USERNAME = "mutual.ssl.username";
/*     */   private static final String USERNAME_HEADER = "username.header";
/*     */   private static final String HOST = "identity.server.host";
/*     */   private static final String PORT = "identity.server.port";
/*     */   private static final String CLIENT_KEY_STORE = "client.keyStore";
/*     */   private static final String CLIENT_TRUST_STORE = "client.trustStore";
/*     */   private static final String CLIENT_KEY_STORE_PASSWORD = "client.keyStore.password";
/*     */   private static final String CLIENT_TRUST_STORE_PASSWORD = "client.trustStore.password";
/*     */   private static final String RETURN = "return";
/*     */   private static final String RETRIEVE_TENANTS_RESPONSE = "retrieveTenantsResponse";
/*     */   private static final String TENANT_DOMAIN = "tenantDomain";
/*     */   private static final String ACTIVE = "active";
/*     */   private static final String TENANT_LIST_ENABLED = "tenantListEnabled";
/*     */   private static Properties prop;
/*  55 */   private static String carbonLogin = "";
/*  56 */   private static String usernameHeaderName = "";
/*     */   private static List<String> tenantDomainList;
/*  58 */   private static boolean isInitialized = false;
/*     */   
/*     */   private static synchronized void init()
/*     */   {
/*     */     try {
/*  63 */       if (!isInitialized) {
/*  64 */         prop = new Properties();
/*     */         
/*  66 */         InputStream inputStream = TenantDataManager.class.getClassLoader().getResourceAsStream("TenantConfig.properties");
/*     */         
/*  68 */         if (inputStream != null) {
/*  69 */           prop.load(inputStream);
/*     */           
/*  71 */           usernameHeaderName = getPropertyValue("username.header");
/*     */           
/*  73 */           carbonLogin = getPropertyValue("mutual.ssl.username");
/*     */           
/*  75 */           byte[] base64EncodedByteArray = new Base64().encode(carbonLogin.getBytes("UTF-8"));
/*     */           
/*     */ 
/*  78 */           carbonLogin = new String(base64EncodedByteArray);
/*     */           
/*  80 */           String clientKeyStorePath = buildFilePath(getPropertyValue("client.keyStore"));
/*  81 */           String clientTrustStorePath = buildFilePath(getPropertyValue("client.trustStore"));
/*     */           
/*  83 */           MutualSSLClient.loadKeyStore(clientKeyStorePath, getPropertyValue("client.keyStore.password"));
/*  84 */           MutualSSLClient.loadTrustStore(clientTrustStorePath, getPropertyValue("client.trustStore.password"));
/*  85 */           MutualSSLClient.initMutualSSLConnection();
/*     */           
/*  87 */           tenantDomainList = new ArrayList();
/*     */           
/*  89 */           isInitialized = true;
/*     */         }
/*  91 */         else if (log.isDebugEnabled()) {
/*  92 */           log.debug("Configuration file TenantConfig.properties not found");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  98 */       log.error("Initialization failed : ", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String buildFilePath(String path) throws IOException
/*     */   {
/* 104 */     if ((path != null) && (path.startsWith("."))) {
/* 105 */       File currentDirectory = new File(new File(".").getAbsolutePath());
/* 106 */       path = currentDirectory.getCanonicalPath() + File.separator + path;
/*     */     }
/*     */     
/* 109 */     if (log.isDebugEnabled()) {
/* 110 */       log.debug("File path for KeyStore/TrustStore : " + path);
/*     */     }
/* 112 */     return path;
/*     */   }
/*     */   
/*     */   private static String getPropertyValue(String key)
/*     */   {
/* 117 */     return prop.getProperty(key);
/*     */   }
/*     */   
/*     */   private static String getServiceResponse(String url)
/*     */   {
/*     */     try {
/* 123 */       Map<String, String> headerParams = new HashMap();
/* 124 */       headerParams.put(usernameHeaderName, carbonLogin);
/* 125 */       return MutualSSLClient.sendGetRequest(url, null, headerParams);
/*     */     } catch (Exception e) {
/* 127 */       log.error("Processing request for " + url + " Failed : ", e); }
/* 128 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static List<String> getAllActiveTenantDomains()
/*     */   {
/* 134 */     if (!isInitialized) {
/* 135 */       init();
/*     */     }
/* 137 */     if ((tenantDomainList == null) || (tenantDomainList.size() == 0)) {
/* 138 */       refreshActiveTenantDomainsList();
/*     */     }
/* 140 */     return tenantDomainList;
/*     */   }
/*     */   
/*     */   public static void setTenantDataList(String dataList)
/*     */   {
/* 145 */     if (!isInitialized) {
/* 146 */       init();
/*     */     }
/*     */     
/* 149 */     if ((dataList != null) && (dataList.trim().length() > 0))
/*     */     {
/* 151 */       synchronized (tenantDomainList) {
/* 152 */         String[] domains = dataList.split(",");
/* 153 */         tenantDomainList = new ArrayList();
/* 154 */         for (String domain : domains) {
/* 155 */           tenantDomainList.add(domain);
/*     */         }
/*     */         
/* 158 */         Collections.sort(tenantDomainList);
/*     */       }
/*     */     } else {
/* 161 */       tenantDomainList = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void refreshActiveTenantDomainsList()
/*     */   {
/*     */     try {
/* 168 */       String url = "https://" + getPropertyValue("identity.server.host") + ":" + getPropertyValue("identity.server.port") + "/services/TenantMgtAdminService/retrieveTenants";
/*     */       
/*     */ 
/* 171 */       String xmlString = getServiceResponse(url);
/*     */       
/* 173 */       if ((xmlString != null) && (!"".equals(xmlString)))
/*     */       {
/* 175 */         XPathFactory xpf = XPathFactory.newInstance();
/* 176 */         XPath xpath = xpf.newXPath();
/*     */         
/* 178 */         InputSource inputSource = new InputSource(new StringReader(xmlString));
/* 179 */         String xPathExpression = "/*[local-name() = 'retrieveTenantsResponse']/*[local-name() = 'return']";
/*     */         
/*     */ 
/*     */ 
/* 183 */         NodeList nodeList = (NodeList)xpath.evaluate(xPathExpression, inputSource, XPathConstants.NODESET);
/*     */         
/*     */ 
/* 186 */         tenantDomainList = new ArrayList();
/*     */         
/* 188 */         for (int i = 0; i < nodeList.getLength(); i++) {
/* 189 */           Node node = nodeList.item(i);
/* 190 */           if ((node != null) && (node.getNodeType() == 1)) {
/* 191 */             Element element = (Element)node;
/* 192 */             NodeList tenantData = element.getChildNodes();
/* 193 */             boolean activeChecked = false;
/* 194 */             boolean domainChecked = false;
/* 195 */             boolean isActive = false;
/* 196 */             String tenantDomain = null;
/*     */             
/* 198 */             for (int j = 0; j < tenantData.getLength(); j++)
/*     */             {
/* 200 */               Node dataItem = tenantData.item(j);
/* 201 */               String localName = dataItem.getLocalName();
/*     */               
/* 203 */               if ("active".equals(localName)) {
/* 204 */                 activeChecked = true;
/* 205 */                 if ("true".equals(dataItem.getTextContent())) {
/* 206 */                   isActive = true;
/*     */                 }
/*     */               }
/*     */               
/* 210 */               if ("tenantDomain".equals(localName)) {
/* 211 */                 domainChecked = true;
/* 212 */                 tenantDomain = dataItem.getTextContent();
/*     */               }
/*     */               
/* 215 */               if ((activeChecked) && (domainChecked)) {
/* 216 */                 if (!isActive) break;
/* 217 */                 tenantDomainList.add(tenantDomain); break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 225 */         Collections.sort(tenantDomainList);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 229 */       if (log.isDebugEnabled()) {
/* 230 */         log.debug("Retrieving Active Tenant Domains Failed. Ignore this if there are no tenants : ", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isTenantListEnabled() {
/* 236 */     if (!isInitialized) {
/* 237 */       init();
/*     */     }
/* 239 */     return "true".equals(getPropertyValue("tenantListEnabled"));
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\util\TenantDataManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */