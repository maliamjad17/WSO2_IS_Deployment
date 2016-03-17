/*    */ package org.wso2.carbon.identity.oauth.endpoint.user;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserInfoEndpointException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -4449780649560053452L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String ERROR_CODE_INVALID_SCHEMA = "invalid_schema";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String ERROR_CODE_INVALID_REQUEST = "invalid_request";
/*    */   
/*    */ 
/*    */ 
/*    */   public static final String ERROR_CODE_INVALID_TOKEN = "invalid_token";
/*    */   
/*    */ 
/*    */ 
/*    */   public static final String ERROR_CODE_INSUFFICIENT_SCOPE = "insufficient_scope";
/*    */   
/*    */ 
/*    */ 
/* 32 */   private String errorCode = null;
/* 33 */   private String errorMessage = null;
/*    */   
/*    */   public UserInfoEndpointException(String errorCode, String errorMessage) {
/* 36 */     super(errorMessage);
/* 37 */     this.errorCode = errorCode;
/* 38 */     this.errorMessage = errorMessage;
/*    */   }
/*    */   
/*    */   public UserInfoEndpointException(String errorMessage) {
/* 42 */     super(errorMessage);
/* 43 */     this.errorMessage = errorMessage;
/*    */   }
/*    */   
/*    */   public String getErrorCode() {
/* 47 */     return this.errorCode;
/*    */   }
/*    */   
/*    */   public String getErrorMessage() {
/* 51 */     return this.errorMessage;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\UserInfoEndpointException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */