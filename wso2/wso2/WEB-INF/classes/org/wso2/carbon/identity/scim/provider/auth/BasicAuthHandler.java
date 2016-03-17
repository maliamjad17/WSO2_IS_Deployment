/*     */ package org.wso2.carbon.identity.scim.provider.auth;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.axiom.om.util.Base64;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.cxf.jaxrs.model.ClassResourceInfo;
/*     */ import org.apache.cxf.message.Message;
/*     */ import org.wso2.carbon.context.PrivilegedCarbonContext;
/*     */ import org.wso2.carbon.identity.application.common.model.ThreadLocalProvisioningServiceProvider;
/*     */ import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;
/*     */ import org.wso2.carbon.user.api.UserRealm;
/*     */ import org.wso2.carbon.user.api.UserStoreException;
/*     */ import org.wso2.carbon.user.api.UserStoreManager;
/*     */ import org.wso2.carbon.user.core.service.RealmService;
/*     */ import org.wso2.carbon.user.core.tenant.TenantManager;
/*     */ import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
/*     */ import org.wso2.charon.core.exceptions.InternalServerException;
/*     */ import org.wso2.charon.core.exceptions.UnauthorizedException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicAuthHandler
/*     */   implements SCIMAuthenticationHandler
/*     */ {
/*  47 */   private static Log log = LogFactory.getLog(BasicAuthHandler.class);
/*     */   
/*     */ 
/*     */   private Map<String, String> properties;
/*     */   
/*     */ 
/*     */   private int priority;
/*     */   
/*     */ 
/*  56 */   private final String BASIC_AUTH_HEADER = "Basic";
/*  57 */   private final int DEFAULT_PRIORITY = 5;
/*     */   
/*     */   public void setDefaultPriority() {
/*  60 */     this.priority = 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPriority()
/*     */   {
/*  69 */     return this.priority;
/*     */   }
/*     */   
/*     */   public void setPriority(int priority) {
/*  73 */     this.priority = priority;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean canHandle(Message message, ClassResourceInfo classResourceInfo)
/*     */   {
/*  80 */     TreeMap protocolHeaders = (TreeMap)message.get(Message.PROTOCOL_HEADERS);
/*     */     
/*  82 */     ArrayList authzHeaders = (ArrayList)protocolHeaders.get("Authorization");
/*     */     
/*  84 */     if (authzHeaders != null)
/*     */     {
/*  86 */       String authzHeader = (String)authzHeaders.get(0);
/*  87 */       if ((authzHeader != null) && (authzHeader.contains("Basic"))) {
/*  88 */         return true;
/*     */       }
/*     */     }
/*  91 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAuthenticated(Message message, ClassResourceInfo classResourceInfo)
/*     */   {
/*  98 */     TreeMap protocolHeaders = (TreeMap)message.get(Message.PROTOCOL_HEADERS);
/*     */     
/* 100 */     ArrayList authzHeaders = (ArrayList)protocolHeaders.get("Authorization");
/*     */     
/* 102 */     if (authzHeaders != null)
/*     */     {
/* 104 */       String authzHeader = (String)authzHeaders.get(0);
/*     */       
/*     */ 
/* 107 */       byte[] decodedAuthHeader = Base64.decode(authzHeader.split(" ")[1]);
/* 108 */       String authHeader = new String(decodedAuthHeader);
/* 109 */       String userName = authHeader.split(":")[0];
/* 110 */       String password = authHeader.split(":")[1];
/* 111 */       if ((userName != null) && (password != null)) {
/* 112 */         String tenantDomain = MultitenantUtils.getTenantDomain(userName);
/* 113 */         String tenantLessUserName = MultitenantUtils.getTenantAwareUsername(userName);
/*     */         
/*     */         try
/*     */         {
/* 117 */           RealmService realmService = (RealmService)PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(RealmService.class);
/*     */           
/* 119 */           if (realmService != null) {
/* 120 */             int tenantId = realmService.getTenantManager().getTenantId(tenantDomain);
/* 121 */             if (tenantId == -1) {
/* 122 */               log.error("Invalid tenant domain " + tenantDomain);
/* 123 */               return false;
/*     */             }
/*     */             
/* 126 */             UserRealm userRealm = realmService.getTenantUserRealm(tenantId);
/* 127 */             boolean authenticated = userRealm.getUserStoreManager().authenticate(tenantLessUserName, password);
/*     */             
/* 129 */             if (authenticated)
/*     */             {
/*     */ 
/*     */ 
/* 133 */               ThreadLocalProvisioningServiceProvider serviceProvider = new ThreadLocalProvisioningServiceProvider();
/* 134 */               serviceProvider.setServiceProviderName("wso2carbon-local-sp");
/*     */               
/* 136 */               serviceProvider.setClaimDialect("urn:scim:schemas:core:1.0");
/*     */               
/* 138 */               serviceProvider.setTenantDomain(MultitenantUtils.getTenantDomain(userName));
/*     */               
/* 140 */               IdentityApplicationManagementUtil.setThreadLocalProvisioningServiceProvider(serviceProvider);
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 145 */               authzHeaders.set(0, userName);
/* 146 */               return true;
/*     */             }
/* 148 */             UnauthorizedException unauthorizedException = new UnauthorizedException("Authentication failed for the user: " + tenantLessUserName + "@" + tenantDomain);
/*     */             
/*     */ 
/* 151 */             log.error(unauthorizedException.getDescription());
/* 152 */             return false;
/*     */           }
/*     */           
/* 155 */           log.error("Error in getting Realm Service for user: " + userName);
/* 156 */           InternalServerException internalServerException = new InternalServerException("Internal server error while authenticating the user: " + tenantLessUserName + "@" + tenantDomain);
/*     */           
/*     */ 
/* 159 */           log.error(internalServerException.getDescription());
/* 160 */           return false;
/*     */         }
/*     */         catch (UserStoreException e)
/*     */         {
/* 164 */           InternalServerException internalServerException = new InternalServerException("Internal server error while authenticating the user.");
/*     */           
/* 166 */           log.error(internalServerException.getDescription(), e);
/* 167 */           return false;
/*     */         }
/*     */       }
/* 170 */       UnauthorizedException unauthorizedException = new UnauthorizedException("Authentication required for this resource. Username or password not provided.");
/*     */       
/* 172 */       log.error(unauthorizedException.getDescription());
/* 173 */       return false;
/*     */     }
/*     */     
/* 176 */     UnauthorizedException unauthorizedException = new UnauthorizedException("Authentication required for this resource. Authorization header not present in the request.");
/*     */     
/* 178 */     log.error(unauthorizedException.getDescription());
/* 179 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperties(Map<String, String> authenticatorProperties)
/*     */   {
/* 191 */     this.properties = authenticatorProperties;
/* 192 */     String priorityString = (String)this.properties.get("Priority");
/* 193 */     if (priorityString != null) {
/* 194 */       this.priority = Integer.parseInt((String)this.properties.get("Priority"));
/*     */     }
/*     */     else {
/* 197 */       this.priority = 5;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\auth\BasicAuthHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */