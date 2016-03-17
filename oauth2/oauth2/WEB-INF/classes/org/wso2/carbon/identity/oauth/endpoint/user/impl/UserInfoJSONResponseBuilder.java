/*    */ package org.wso2.carbon.identity.oauth.endpoint.user.impl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*    */ import org.apache.amber.oauth2.common.utils.JSONUtils;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.jettison.json.JSONException;
/*    */ import org.wso2.carbon.identity.application.common.model.ClaimMapping;
/*    */ import org.wso2.carbon.identity.oauth.cache.AuthorizationGrantCache;
/*    */ import org.wso2.carbon.identity.oauth.cache.AuthorizationGrantCacheEntry;
/*    */ import org.wso2.carbon.identity.oauth.cache.AuthorizationGrantCacheKey;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoClaimRetriever;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoEndpointException;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoResponseBuilder;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.util.ClaimUtil;
/*    */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO;
/*    */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO.AuthorizationContextToken;
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
/*    */ public class UserInfoJSONResponseBuilder
/*    */   implements UserInfoResponseBuilder
/*    */ {
/* 41 */   private static Log log = LogFactory.getLog(UserInfoJSONResponseBuilder.class);
/*    */   
/*    */ 
/*    */   public String getResponseString(OAuth2TokenValidationResponseDTO tokenResponse)
/*    */     throws UserInfoEndpointException, OAuthSystemException
/*    */   {
/* 47 */     Map<ClaimMapping, String> userAttributes = getUserAttributesFromCache(tokenResponse);
/* 48 */     Map<String, Object> claims = null;
/*    */     
/* 50 */     if ((userAttributes == null) || (userAttributes.isEmpty())) {
/* 51 */       if (log.isDebugEnabled()) {
/* 52 */         log.debug("User attributes not found in cache. Trying to retrieve from user store.");
/*    */       }
/*    */       try {
/* 55 */         claims = ClaimUtil.getClaimsFromUserStore(tokenResponse);
/*    */       } catch (Exception e) {
/* 57 */         throw new UserInfoEndpointException("Error while retrieving claims from user store.");
/*    */       }
/*    */     }
/*    */     else {
/* 61 */       UserInfoClaimRetriever retriever = UserInfoEndpointConfig.getInstance().getUserInfoClaimRetriever();
/* 62 */       claims = retriever.getClaimsMap(userAttributes);
/*    */     }
/*    */     try
/*    */     {
/* 66 */       return JSONUtils.buildJSON(claims);
/*    */     }
/*    */     catch (JSONException e) {
/* 69 */       throw new UserInfoEndpointException("Error while generating the response JSON");
/*    */     }
/*    */   }
/*    */   
/*    */   private Map<ClaimMapping, String> getUserAttributesFromCache(OAuth2TokenValidationResponseDTO tokenResponse) {
/* 74 */     AuthorizationGrantCacheKey cacheKey = new AuthorizationGrantCacheKey(tokenResponse.getAuthorizationContextToken().getTokenString());
/*    */     
/* 76 */     AuthorizationGrantCacheEntry cacheEntry = (AuthorizationGrantCacheEntry)AuthorizationGrantCache.getInstance().getValueFromCache(cacheKey);
/* 77 */     if (cacheEntry == null) {
/* 78 */       return new HashMap();
/*    */     }
/* 80 */     return cacheEntry.getUserAttributes();
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\impl\UserInfoJSONResponseBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */