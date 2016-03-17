/*    */ package org.wso2.carbon.identity.scim.provider.auth;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.apache.axiom.om.OMElement;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.wso2.carbon.base.ServerConfigurationException;
/*    */ import org.wso2.carbon.identity.core.util.IdentityConfigParser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SCIMAuthConfigReader
/*    */ {
/* 36 */   private static Log logger = LogFactory.getLog(SCIMAuthConfigReader.class);
/*    */   
/*    */   public List<SCIMAuthenticationHandler> buildSCIMAuthenticators() {
/*    */     try {
/* 40 */       IdentityConfigParser identityConfig = IdentityConfigParser.getInstance();
/* 41 */       OMElement SCIMAuthElement = identityConfig.getConfigElement("SCIMAuthenticators");
/*    */       
/* 43 */       Iterator<OMElement> authenticators = SCIMAuthElement.getChildrenWithName(new QName("Authenticator"));
/*    */       
/* 45 */       List<SCIMAuthenticationHandler> SCIMAuthHandlers = new ArrayList();
/* 46 */       if (authenticators != null)
/*    */       {
/* 48 */         while (authenticators.hasNext()) {
/* 49 */           OMElement authenticatorElement = (OMElement)authenticators.next();
/*    */           
/* 51 */           String authenticatorClassName = authenticatorElement.getAttributeValue(new QName("class"));
/*    */           
/*    */ 
/* 54 */           Class authenticatorClass = Class.forName(authenticatorClassName);
/* 55 */           SCIMAuthenticationHandler authHandler = (SCIMAuthenticationHandler)authenticatorClass.newInstance();
/*    */           
/*    */ 
/*    */ 
/* 59 */           Iterator<OMElement> propertyElements = authenticatorElement.getChildrenWithName(new QName("Property"));
/*    */           
/* 61 */           if (propertyElements != null) {
/* 62 */             Map<String, String> properties = new HashMap();
/* 63 */             while (propertyElements.hasNext()) {
/* 64 */               OMElement propertyElement = (OMElement)propertyElements.next();
/* 65 */               String attributeName = propertyElement.getAttributeValue(new QName("name"));
/*    */               
/* 67 */               String attributeValue = propertyElement.getText();
/* 68 */               properties.put(attributeName, attributeValue);
/*    */             }
/* 70 */             authHandler.setProperties(properties);
/*    */           }
/* 72 */           SCIMAuthHandlers.add(authHandler);
/*    */         }
/* 74 */         return SCIMAuthHandlers;
/*    */       }
/*    */     }
/*    */     catch (ServerConfigurationException e) {
/* 78 */       logger.error("Error in reading authenticator config from identity.xml when initializing the SCIM webapp...");
/*    */     }
/*    */     catch (ClassNotFoundException e)
/*    */     {
/* 82 */       logger.error("Error in loading the authenticator class...", e);
/*    */     } catch (InstantiationException e) {
/* 84 */       logger.error("Error while instantiating the authenticator..", e);
/*    */     } catch (IllegalAccessException e) {
/* 86 */       logger.error("Error while instantiating the authenticator..", e);
/*    */     }
/* 88 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\auth\SCIMAuthConfigReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */