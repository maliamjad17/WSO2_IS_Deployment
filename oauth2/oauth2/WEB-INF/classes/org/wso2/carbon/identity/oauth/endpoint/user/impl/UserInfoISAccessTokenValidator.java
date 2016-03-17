/*    */ package org.wso2.carbon.identity.oauth.endpoint.user.impl;
/*    */ 
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoAccessTokenValidator;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoEndpointException;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.util.EndpointUtil;
/*    */ import org.wso2.carbon.identity.oauth2.OAuth2TokenValidationService;
/*    */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO;
/*    */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO.OAuth2AccessToken;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserInfoISAccessTokenValidator
/*    */   implements UserInfoAccessTokenValidator
/*    */ {
/*    */   public OAuth2TokenValidationResponseDTO validateToken(String accessTokenIdentifier)
/*    */     throws UserInfoEndpointException
/*    */   {
/* 40 */     OAuth2TokenValidationRequestDTO dto = new OAuth2TokenValidationRequestDTO(); OAuth2TokenValidationRequestDTO 
/* 41 */       tmp13_12 = dto;tmp13_12.getClass();OAuth2TokenValidationRequestDTO.OAuth2AccessToken accessToken = new OAuth2TokenValidationRequestDTO.OAuth2AccessToken(tmp13_12);
/* 42 */     accessToken.setTokenType("bearer");
/* 43 */     accessToken.setIdentifier(accessTokenIdentifier);
/* 44 */     dto.setAccessToken(accessToken);
/* 45 */     OAuth2TokenValidationResponseDTO response = EndpointUtil.getOAuth2TokenValidationService().validate(dto);
/*    */     
/*    */ 
/*    */ 
/* 49 */     if (!response.isValid()) {
/* 50 */       throw new UserInfoEndpointException("invalid_token", "Access token validation failed");
/*    */     }
/*    */     
/*    */ 
/* 54 */     boolean isOpenIDScope = false;
/* 55 */     String[] scope = response.getScope();
/* 56 */     for (String curScope : scope) {
/* 57 */       if ("openid".equals(curScope)) {
/* 58 */         isOpenIDScope = true;
/*    */       }
/*    */     }
/* 61 */     if (!isOpenIDScope) {
/* 62 */       throw new UserInfoEndpointException("insufficient_scope", "Access token does not have the openid scope");
/*    */     }
/*    */     
/* 65 */     if (response.getAuthorizedUser() == null) {
/* 66 */       throw new UserInfoEndpointException("invalid_token", "Access token is not valid. No authorized user found. Invalid grant");
/*    */     }
/*    */     
/* 69 */     OAuth2TokenValidationResponseDTO tmp165_163 = response;tmp165_163.getClass();OAuth2TokenValidationResponseDTO.AuthorizationContextToken authorizationContextToken = new OAuth2TokenValidationResponseDTO.AuthorizationContextToken(tmp165_163, accessToken.getTokenType(), accessToken.getIdentifier());
/* 70 */     response.setAuthorizationContextToken(authorizationContextToken);
/* 71 */     return response;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\impl\UserInfoISAccessTokenValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */