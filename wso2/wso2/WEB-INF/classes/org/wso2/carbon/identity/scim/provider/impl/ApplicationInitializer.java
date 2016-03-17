/*    */ package org.wso2.carbon.identity.scim.provider.impl;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.wso2.carbon.identity.scim.provider.auth.BasicAuthHandler;
/*    */ import org.wso2.carbon.identity.scim.provider.auth.OAuthHandler;
/*    */ import org.wso2.carbon.identity.scim.provider.auth.SCIMAuthConfigReader;
/*    */ import org.wso2.carbon.identity.scim.provider.auth.SCIMAuthenticationHandler;
/*    */ import org.wso2.carbon.identity.scim.provider.auth.SCIMAuthenticatorRegistry;
/*    */ import org.wso2.charon.core.exceptions.CharonException;
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
/*    */ public class ApplicationInitializer
/*    */   implements ServletContextListener
/*    */ {
/* 40 */   private Log logger = LogFactory.getLog(ApplicationInitializer.class);
/*    */   
/*    */   public void contextInitialized(ServletContextEvent servletContextEvent) {
/* 43 */     if (this.logger.isDebugEnabled()) {
/* 44 */       this.logger.debug("Initializing SCIM Webapp...");
/*    */     }
/*    */     try
/*    */     {
/* 48 */       initSCIMAuthenticatorRegistry();
/*    */       
/*    */ 
/* 51 */       IdentitySCIMManager.getInstance();
/*    */     }
/*    */     catch (CharonException e) {
/* 54 */       this.logger.error("Error in initializing the IdentitySCIMManager at the initialization of SCIM webapp");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void contextDestroyed(ServletContextEvent servletContextEvent) {}
/*    */   
/*    */ 
/*    */   private void initSCIMAuthenticatorRegistry()
/*    */   {
/* 64 */     SCIMAuthenticatorRegistry scimAuthRegistry = SCIMAuthenticatorRegistry.getInstance();
/* 65 */     if (scimAuthRegistry != null)
/*    */     {
/* 67 */       SCIMAuthConfigReader configReader = new SCIMAuthConfigReader();
/* 68 */       List<SCIMAuthenticationHandler> SCIMAuthenticators = configReader.buildSCIMAuthenticators();
/* 69 */       if ((SCIMAuthenticators != null) && (!SCIMAuthenticators.isEmpty())) {
/* 70 */         for (SCIMAuthenticationHandler scimAuthenticator : SCIMAuthenticators) {
/* 71 */           scimAuthRegistry.setAuthenticator(scimAuthenticator);
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 76 */         BasicAuthHandler basicAuthHandler = new BasicAuthHandler();
/* 77 */         basicAuthHandler.setDefaultPriority();
/* 78 */         scimAuthRegistry.setAuthenticator(basicAuthHandler);
/*    */         
/* 80 */         OAuthHandler oauthHandler = new OAuthHandler();
/* 81 */         oauthHandler.setDefaultPriority();
/* 82 */         oauthHandler.setDefaultAuthzServer();
/* 83 */         scimAuthRegistry.setAuthenticator(oauthHandler);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\impl\ApplicationInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */