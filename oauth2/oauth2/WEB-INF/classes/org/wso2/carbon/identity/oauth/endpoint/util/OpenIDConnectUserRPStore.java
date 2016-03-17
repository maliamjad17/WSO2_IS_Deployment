/*    */ package org.wso2.carbon.identity.oauth.endpoint.util;
/*    */ 
/*    */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*    */ import org.wso2.carbon.identity.base.IdentityException;
/*    */ import org.wso2.carbon.identity.core.model.OpenIDUserRPDO;
/*    */ import org.wso2.carbon.identity.provider.openid.dao.OpenIDUserRPDAO;
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
/*    */ public class OpenIDConnectUserRPStore
/*    */ {
/* 31 */   private static OpenIDConnectUserRPStore store = new OpenIDConnectUserRPStore();
/*    */   private static final String DEFAULT_PROFILE_NAME = "default";
/*    */   
/*    */   public static OpenIDConnectUserRPStore getInstance()
/*    */   {
/* 36 */     return store;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void putUserRPToStore(String username, String appName, boolean trustedAlways)
/*    */     throws OAuthSystemException
/*    */   {
/* 49 */     OpenIDUserRPDO repDO = new OpenIDUserRPDO();
/* 50 */     repDO.setDefaultProfileName("default");
/* 51 */     repDO.setRpUrl(appName);
/* 52 */     repDO.setUserName(username);
/* 53 */     repDO.setTrustedAlways(trustedAlways);
/*    */     
/* 55 */     OpenIDUserRPDAO dao = new OpenIDUserRPDAO();
/*    */     try {
/* 57 */       dao.createOrUpdate(repDO);
/*    */     } catch (IdentityException e) {
/* 59 */       throw new OAuthSystemException("Error while storing user consent", e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public synchronized boolean hasUserApproved(String username, String appName)
/*    */     throws OAuthSystemException
/*    */   {
/* 71 */     OpenIDUserRPDAO dao = new OpenIDUserRPDAO();
/*    */     try {
/* 73 */       OpenIDUserRPDO rpDO = dao.getOpenIDUserRP(username, appName);
/* 74 */       if ((rpDO != null) && (rpDO.isTrustedAlways())) {
/* 75 */         return true;
/*    */       }
/*    */     } catch (IdentityException e) {
/* 78 */       throw new OAuthSystemException("Error while loading user consent", e);
/*    */     }
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\util\OpenIDConnectUserRPStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */