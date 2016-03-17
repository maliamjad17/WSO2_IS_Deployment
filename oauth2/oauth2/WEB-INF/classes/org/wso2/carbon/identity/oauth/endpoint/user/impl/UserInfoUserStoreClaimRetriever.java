/*    */ package org.wso2.carbon.identity.oauth.endpoint.user.impl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.wso2.carbon.identity.application.common.model.Claim;
/*    */ import org.wso2.carbon.identity.application.common.model.ClaimMapping;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoClaimRetriever;
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
/*    */ public class UserInfoUserStoreClaimRetriever
/*    */   implements UserInfoClaimRetriever
/*    */ {
/*    */   public Map<String, Object> getClaimsMap(Map<ClaimMapping, String> userAttributes)
/*    */   {
/* 33 */     Map<String, Object> claims = new HashMap();
/* 34 */     if ((userAttributes != null) && (userAttributes.size() > 0)) {
/* 35 */       for (ClaimMapping claimMapping : userAttributes.keySet()) {
/* 36 */         claims.put(claimMapping.getRemoteClaim().getClaimUri(), userAttributes.get(claimMapping));
/*    */       }
/*    */     }
/* 39 */     return claims;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\impl\UserInfoUserStoreClaimRetriever.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */