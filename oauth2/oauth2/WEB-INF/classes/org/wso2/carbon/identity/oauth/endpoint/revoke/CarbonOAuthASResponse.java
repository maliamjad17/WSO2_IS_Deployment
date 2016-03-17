/*    */ package org.wso2.carbon.identity.oauth.endpoint.revoke;
/*    */ 
/*    */ import org.apache.amber.oauth2.common.message.OAuthResponse;
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
/*    */ public class CarbonOAuthASResponse
/*    */   extends OAuthResponse
/*    */ {
/*    */   private static CarbonOAuthASResponse.OAuthRevokeResponseBuilder revokeResponseBuilder;
/*    */   
/*    */   protected CarbonOAuthASResponse(String uri, int responseStatus)
/*    */   {
/* 28 */     super(uri, responseStatus);
/*    */   }
/*    */   
/*    */   public static CarbonOAuthASResponse.OAuthRevokeResponseBuilder revokeResponse(int code) {
/* 32 */     revokeResponseBuilder = new CarbonOAuthASResponse.OAuthRevokeResponseBuilder(code);
/* 33 */     return revokeResponseBuilder;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\revoke\CarbonOAuthASResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */