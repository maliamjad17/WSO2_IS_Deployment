/*    */ package org.wso2.carbon.identity.scim.provider.auth;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ import org.apache.cxf.jaxrs.model.ClassResourceInfo;
/*    */ import org.apache.cxf.message.Message;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SCIMAuthenticatorRegistry
/*    */ {
/*    */   private static SCIMAuthenticatorRegistry SCIMAuthRegistry;
/* 33 */   private static Map<Integer, SCIMAuthenticationHandler> SCIMAuthHandlers = new TreeMap();
/*    */   
/*    */   public static SCIMAuthenticatorRegistry getInstance()
/*    */   {
/* 37 */     if (SCIMAuthRegistry == null) {
/* 38 */       synchronized (SCIMAuthenticatorRegistry.class) {
/* 39 */         if (SCIMAuthRegistry == null) {
/* 40 */           SCIMAuthRegistry = new SCIMAuthenticatorRegistry();
/* 41 */           return SCIMAuthRegistry;
/*    */         }
/* 43 */         return SCIMAuthRegistry;
/*    */       }
/*    */     }
/*    */     
/* 47 */     return SCIMAuthRegistry;
/*    */   }
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
/*    */   public SCIMAuthenticationHandler getAuthenticator(Message message, ClassResourceInfo classResourceInfo)
/*    */   {
/* 62 */     for (SCIMAuthenticationHandler scimAuthenticationHandler : SCIMAuthHandlers.values()) {
/* 63 */       if (scimAuthenticationHandler.canHandle(message, classResourceInfo)) {
/* 64 */         return scimAuthenticationHandler;
/*    */       }
/*    */     }
/* 67 */     return null;
/*    */   }
/*    */   
/*    */   public void setAuthenticator(SCIMAuthenticationHandler SCIMAuthHandler) {
/* 71 */     SCIMAuthHandlers.put(Integer.valueOf(SCIMAuthHandler.getPriority()), SCIMAuthHandler);
/*    */   }
/*    */   
/*    */   public void removeAuthenticator(SCIMAuthenticationHandler scimAuthenticationHandler) {
/* 75 */     SCIMAuthHandlers.remove(Integer.valueOf(scimAuthenticationHandler.getPriority()));
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\auth\SCIMAuthenticatorRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */