/*    */ package org.wso2.carbon.identity.oauth.endpoint.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.wso2.carbon.claim.mgt.ClaimManagerHandler;
/*    */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO;
/*    */ import org.wso2.carbon.user.core.UserRealm;
/*    */ import org.wso2.carbon.user.core.UserStoreManager;
/*    */ import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
/*    */ 
/*    */ public class ClaimUtil
/*    */ {
/* 17 */   private static Log log = org.apache.commons.logging.LogFactory.getLog(ClaimUtil.class);
/*    */   static final String spDialect = "http://wso2.org/oidc/claim";
/*    */   
/*    */   public static Map<String, Object> getClaimsFromUserStore(OAuth2TokenValidationResponseDTO tokenResponse) throws Exception {
/* 21 */     String username = tokenResponse.getAuthorizedUser();
/* 22 */     String tenantUser = MultitenantUtils.getTenantAwareUsername(username);
/* 23 */     String tenantDomain = MultitenantUtils.getTenantDomain(tokenResponse.getAuthorizedUser());
/*    */     
/*    */ 
/* 26 */     List<String> claimURIList = new java.util.ArrayList();
/* 27 */     Map<String, Object> mappedAppClaims = new HashMap();
/*    */     try
/*    */     {
/* 30 */       UserRealm realm = org.wso2.carbon.identity.core.util.IdentityTenantUtil.getRealm(tenantDomain, username);
/*    */       
/* 32 */       if (realm == null) {
/* 33 */         log.warn("No valid tenant domain provider. Empty claim returned back");
/* 34 */         return new HashMap();
/*    */       }
/*    */       
/* 37 */       org.wso2.carbon.user.api.Claim[] claims = realm.getUserStoreManager().getUserClaimValues(tenantUser, null);
/*    */       
/*    */ 
/* 40 */       UserStoreManager userstore = realm.getUserStoreManager();
/*    */       
/*    */ 
/* 43 */       Map<String, String> requestedLocalClaimMap = ClaimManagerHandler.getInstance().getMappingsMapFromOtherDialectToCarbon("http://wso2.org/oidc/claim", null, tenantDomain, true);
/*    */       
/* 45 */       if ((requestedLocalClaimMap != null) && (requestedLocalClaimMap.size() > 0)) {
/* 46 */         Iterator<String> iterator = requestedLocalClaimMap.keySet().iterator();
/* 47 */         while (iterator.hasNext()) {
/* 48 */           claimURIList.add(iterator.next());
/*    */         }
/*    */         
/* 51 */         if (log.isDebugEnabled()) {
/* 52 */           log.debug("Requested number of local claims: " + claimURIList.size());
/*    */         }
/*    */         
/* 55 */         Map<String, String> spToLocalClaimMappings = ClaimManagerHandler.getInstance().getMappingsMapFromOtherDialectToCarbon("http://wso2.org/oidc/claim", null, tenantDomain, false);
/*    */         
/*    */ 
/*    */ 
/* 59 */         userClaims = userstore.getUserClaimValues(MultitenantUtils.getTenantAwareUsername(username), (String[])claimURIList.toArray(new String[claimURIList.size()]), null);
/*    */         
/*    */ 
/* 62 */         if (log.isDebugEnabled()) {
/* 63 */           log.debug("User claims retrieved from user store: " + userClaims.size());
/*    */         }
/*    */         
/* 66 */         if ((userClaims == null) || (userClaims.size() == 0)) {
/* 67 */           return new HashMap();
/*    */         }
/*    */         
/* 70 */         for (iterator = spToLocalClaimMappings.entrySet().iterator(); iterator.hasNext();) {
/* 71 */           Map.Entry<String, String> entry = (Map.Entry)iterator.next();
/* 72 */           String value = (String)userClaims.get(entry.getValue());
/* 73 */           if (value != null) {
/* 74 */             mappedAppClaims.put(entry.getKey(), value);
/* 75 */             if (log.isDebugEnabled())
/* 76 */               log.debug("Mapped claim: key -  " + (String)entry.getKey() + " value -" + value);
/*    */           }
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/*    */       Map<String, String> userClaims;
/*    */       Iterator<Map.Entry<String, String>> iterator;
/* 83 */       throw new org.wso2.carbon.identity.oauth.endpoint.user.UserInfoEndpointException(e.getMessage());
/*    */     }
/* 85 */     return mappedAppClaims;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\util\ClaimUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */