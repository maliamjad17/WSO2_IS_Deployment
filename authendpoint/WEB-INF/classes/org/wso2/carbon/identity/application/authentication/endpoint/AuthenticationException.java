/*    */ package org.wso2.carbon.identity.application.authentication.endpoint;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class AuthenticationException
/*    */   extends IOException
/*    */ {
/*    */   public AuthenticationException() {}
/*    */   
/*    */   public AuthenticationException(Exception e)
/*    */   {
/* 28 */     super(e);
/*    */   }
/*    */   
/*    */   public AuthenticationException(String message) {
/* 32 */     super(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\AuthenticationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */