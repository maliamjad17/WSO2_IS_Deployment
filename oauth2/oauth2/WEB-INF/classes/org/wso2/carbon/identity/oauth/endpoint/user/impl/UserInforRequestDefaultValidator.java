/*    */ package org.wso2.carbon.identity.oauth.endpoint.user.impl;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoEndpointException;
/*    */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoRequestValidator;
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
/*    */ 
/*    */ public class UserInforRequestDefaultValidator
/*    */   implements UserInfoRequestValidator
/*    */ {
/*    */   public String validateRequest(HttpServletRequest request)
/*    */     throws UserInfoEndpointException
/*    */   {
/* 36 */     String schema = request.getParameter("schema");
/* 37 */     String authzHeaders = request.getHeader("Authorization");
/*    */     
/* 39 */     if (!"openid".equals(schema)) {
/* 40 */       throw new UserInfoEndpointException("invalid_schema", "Schema should be openid");
/*    */     }
/*    */     
/*    */ 
/* 44 */     if (authzHeaders == null) {
/* 45 */       throw new UserInfoEndpointException("invalid_request", "Authorization header missing");
/*    */     }
/*    */     
/*    */ 
/* 49 */     String[] authzHeaderInfo = authzHeaders.trim().split(" ");
/* 50 */     if (!"Bearer".equals(authzHeaderInfo[0])) {
/* 51 */       throw new UserInfoEndpointException("invalid_request", "Bearer token missing");
/*    */     }
/* 53 */     return authzHeaderInfo[1];
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\impl\UserInforRequestDefaultValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */