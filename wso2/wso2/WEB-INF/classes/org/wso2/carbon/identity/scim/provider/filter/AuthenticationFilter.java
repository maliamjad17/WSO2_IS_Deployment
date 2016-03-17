/*    */ package org.wso2.carbon.identity.scim.provider.filter;
/*    */ 
/*    */ import javax.ws.rs.core.Response;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.apache.cxf.jaxrs.ext.RequestHandler;
/*    */ import org.apache.cxf.jaxrs.model.ClassResourceInfo;
/*    */ import org.apache.cxf.message.Message;
/*    */ import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;
/*    */ import org.wso2.carbon.identity.scim.provider.auth.SCIMAuthenticationHandler;
/*    */ import org.wso2.carbon.identity.scim.provider.auth.SCIMAuthenticatorRegistry;
/*    */ import org.wso2.carbon.identity.scim.provider.util.JAXRSResponseBuilder;
/*    */ import org.wso2.charon.core.encoder.json.JSONEncoder;
/*    */ import org.wso2.charon.core.exceptions.UnauthorizedException;
/*    */ import org.wso2.charon.core.protocol.endpoints.AbstractResourceEndpoint;
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
/*    */ public class AuthenticationFilter
/*    */   implements RequestHandler
/*    */ {
/* 37 */   private static Log log = LogFactory.getLog(AuthenticationFilter.class);
/*    */   
/*    */ 
/*    */   public Response handleRequest(Message message, ClassResourceInfo classResourceInfo)
/*    */   {
/*    */     
/*    */     
/* 44 */     if (log.isDebugEnabled()) {
/* 45 */       log.debug("Authenticating SCIM request..");
/*    */     }
/* 47 */     SCIMAuthenticatorRegistry SCIMAuthRegistry = SCIMAuthenticatorRegistry.getInstance();
/* 48 */     if (SCIMAuthRegistry != null) {
/* 49 */       SCIMAuthenticationHandler SCIMAuthHandler = SCIMAuthRegistry.getAuthenticator(message, classResourceInfo);
/*    */       
/* 51 */       boolean isAuthenticated = false;
/* 52 */       if (SCIMAuthHandler != null) {
/* 53 */         isAuthenticated = SCIMAuthHandler.isAuthenticated(message, classResourceInfo);
/* 54 */         if (isAuthenticated) {
/* 55 */           return null;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 60 */     UnauthorizedException unauthorizedException = new UnauthorizedException("Authentication failed for this resource.");
/*    */     
/* 62 */     return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(new JSONEncoder(), unauthorizedException));
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\filter\AuthenticationFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */