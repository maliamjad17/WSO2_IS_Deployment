/*    */ package org.wso2.carbon.identity.oauth.endpoint.user.impl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.apache.oltu.oauth2.jwt.JWTBuilder;
/*    */ import org.apache.oltu.oauth2.jwt.JWTException;
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
/*    */ public class UserInfoJWTResponse
/*    */   implements UserInfoResponseBuilder
/*    */ {
/* 38 */   private static Log log = LogFactory.getLog(UserInfoJWTResponse.class);
/*    */   
/*    */   public String getResponseString(OAuth2TokenValidationResponseDTO tokenResponse) throws UserInfoEndpointException, OAuthSystemException
/*    */   {
/* 42 */     Map<ClaimMapping, String> userAttributes = getUserAttributesFromCache(tokenResponse);
/*    */     
/* 44 */     Map<String, Object> claims = null;
/*    */     
/* 46 */     if ((userAttributes == null) || (userAttributes.isEmpty())) {
/* 47 */       if (log.isDebugEnabled()) {
/* 48 */         log.debug("User attributes not found in cache. Trying to retrieve from user store.");
/*    */       }
/*    */       try {
/* 51 */         claims = ClaimUtil.getClaimsFromUserStore(tokenResponse);
/*    */       } catch (Exception e) {
/* 53 */         throw new UserInfoEndpointException("Error while retrieving claims from user store.");
/*    */       }
/*    */     }
/*    */     else {
/* 57 */       UserInfoClaimRetriever retriever = UserInfoEndpointConfig.getInstance().getUserInfoClaimRetriever();
/* 58 */       claims = retriever.getClaimsMap(userAttributes);
/*    */     }
/* 60 */     JWTBuilder jwtBuilder = new JWTBuilder();
/*    */     try {
/* 62 */       return jwtBuilder.setClaims(claims).buildJWT();
/*    */     } catch (JWTException e) {
/* 64 */       throw new UserInfoEndpointException("Error while generating the response JWT");
/*    */     }
/*    */   }
/*    */   
/*    */   private Map<ClaimMapping, String> getUserAttributesFromCache(OAuth2TokenValidationResponseDTO tokenResponse) {
/* 69 */     AuthorizationGrantCacheKey cacheKey = new AuthorizationGrantCacheKey(tokenResponse.getAuthorizationContextToken().getTokenString());
/*    */     
/* 71 */     AuthorizationGrantCacheEntry cacheEntry = (AuthorizationGrantCacheEntry)AuthorizationGrantCache.getInstance().getValueFromCache(cacheKey);
/* 72 */     if (cacheEntry == null) {
/* 73 */       return new HashMap();
/*    */     }
/* 75 */     return cacheEntry.getUserAttributes();
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\impl\UserInfoJWTResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */