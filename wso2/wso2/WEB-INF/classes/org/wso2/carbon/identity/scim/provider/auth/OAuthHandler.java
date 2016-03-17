/*     */ package org.wso2.carbon.identity.scim.provider.auth;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.axis2.AxisFault;
/*     */ import org.apache.axis2.context.ConfigurationContext;
/*     */ import org.apache.axis2.context.ConfigurationContextFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.cxf.jaxrs.model.ClassResourceInfo;
/*     */ import org.apache.cxf.message.Message;
/*     */ import org.wso2.carbon.identity.application.common.model.ProvisioningServiceProviderType;
/*     */ import org.wso2.carbon.identity.application.common.model.ThreadLocalProvisioningServiceProvider;
/*     */ import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;
/*     */ import org.wso2.carbon.identity.oauth2.OAuth2TokenValidationService;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO.OAuth2AccessToken;
/*     */ import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
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
/*     */ public class OAuthHandler
/*     */   implements SCIMAuthenticationHandler
/*     */ {
/*  44 */   private static Log log = LogFactory.getLog(BasicAuthHandler.class);
/*     */   
/*     */   private Map<String, String> properties;
/*     */   
/*     */   private String remoteServiceURL;
/*     */   
/*     */   private int priority;
/*     */   
/*     */   private String userName;
/*     */   
/*     */   private String password;
/*     */   
/*  56 */   private final String BEARER_AUTH_HEADER = "Bearer";
/*  57 */   private final String LOCAL_PREFIX = "local";
/*  58 */   private final int DEFAULT_PRIORITY = 10;
/*  59 */   private final String LOCAL_AUTH_SERVER = "local://services";
/*     */   
/*     */ 
/*     */   public int getPriority()
/*     */   {
/*  64 */     return this.priority;
/*     */   }
/*     */   
/*     */   public void setPriority(int priority) {
/*  68 */     this.priority = priority;
/*     */   }
/*     */   
/*     */   public void setDefaultPriority() {
/*  72 */     this.priority = 10;
/*     */   }
/*     */   
/*     */   public void setDefaultAuthzServer() {
/*  76 */     this.remoteServiceURL = "local://services";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean canHandle(Message message, ClassResourceInfo classResourceInfo)
/*     */   {
/*  83 */     TreeMap protocolHeaders = (TreeMap)message.get(Message.PROTOCOL_HEADERS);
/*     */     
/*  85 */     ArrayList authzHeaders = (ArrayList)protocolHeaders.get("Authorization");
/*     */     
/*  87 */     if (authzHeaders != null)
/*     */     {
/*  89 */       String authzHeader = (String)authzHeaders.get(0);
/*  90 */       if ((authzHeader != null) && (authzHeader.contains("Bearer"))) {
/*  91 */         return true;
/*     */       }
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAuthenticated(Message message, ClassResourceInfo classResourceInfo)
/*     */   {
/*  99 */     TreeMap protocolHeaders = (TreeMap)message.get(Message.PROTOCOL_HEADERS);
/*     */     
/* 101 */     ArrayList authzHeaders = (ArrayList)protocolHeaders.get("Authorization");
/*     */     
/* 103 */     if (authzHeaders != null)
/*     */     {
/* 105 */       String authzHeader = (String)authzHeaders.get(0);
/*     */       
/*     */ 
/* 108 */       String accessToken = authzHeader.trim().substring(7).trim();
/*     */       try
/*     */       {
/* 111 */         org.wso2.carbon.identity.oauth2.dto.OAuth2ClientApplicationDTO validationApp = validateAccessToken(accessToken);
/* 112 */         org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO validationResponse = null;
/*     */         
/* 114 */         if (validationApp != null) {
/* 115 */           validationResponse = validationApp.getAccessTokenValidationResponse();
/*     */         }
/*     */         
/* 118 */         if ((validationResponse != null) && 
/* 119 */           (validationResponse.isValid())) {
/* 120 */           String userName = validationResponse.getAuthorizedUser();
/* 121 */           authzHeaders.set(0, userName);
/*     */           
/*     */ 
/* 124 */           ThreadLocalProvisioningServiceProvider serviceProvider = new ThreadLocalProvisioningServiceProvider();
/* 125 */           serviceProvider.setServiceProviderName(validationApp.getConsumerKey());
/* 126 */           serviceProvider.setServiceProviderType(ProvisioningServiceProviderType.OAUTH);
/*     */           
/* 128 */           serviceProvider.setClaimDialect("urn:scim:schemas:core:1.0");
/* 129 */           serviceProvider.setTenantDomain(MultitenantUtils.getTenantDomain(userName));
/* 130 */           IdentityApplicationManagementUtil.setThreadLocalProvisioningServiceProvider(serviceProvider);
/*     */           
/*     */ 
/* 133 */           return true;
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 137 */         String error = "Error in validating OAuth access token.";
/* 138 */         log.error(error, e);
/*     */       }
/*     */     }
/* 141 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperties(Map<String, String> authenticatorProperties)
/*     */   {
/* 150 */     this.properties = authenticatorProperties;
/* 151 */     String priorityString = (String)this.properties.get("Priority");
/* 152 */     if (priorityString != null) {
/* 153 */       this.priority = Integer.parseInt(priorityString);
/*     */     } else {
/* 155 */       this.priority = 10;
/*     */     }
/* 157 */     String remoteURLString = (String)this.properties.get("AuthorizationServer");
/* 158 */     if (remoteURLString != null) {
/* 159 */       this.remoteServiceURL = remoteURLString;
/*     */     } else {
/* 161 */       this.remoteServiceURL = "local://services";
/*     */     }
/* 163 */     this.userName = ((String)this.properties.get("UserName"));
/* 164 */     this.password = ((String)this.properties.get("Password"));
/*     */   }
/*     */   
/*     */   private String getOAuthAuthzServerURL() {
/* 168 */     if ((this.remoteServiceURL != null) && 
/* 169 */       (!this.remoteServiceURL.endsWith("/"))) {
/* 170 */       this.remoteServiceURL += "/";
/*     */     }
/*     */     
/* 173 */     return this.remoteServiceURL;
/*     */   }
/*     */   
/*     */ 
/*     */   private org.wso2.carbon.identity.oauth2.dto.OAuth2ClientApplicationDTO validateAccessToken(String accessTokenIdentifier)
/*     */     throws Exception
/*     */   {
/* 180 */     if (this.remoteServiceURL.startsWith("local")) {
/* 181 */       OAuth2TokenValidationRequestDTO oauthValidationRequest = new OAuth2TokenValidationRequestDTO(); OAuth2TokenValidationRequestDTO 
/* 182 */         tmp25_24 = oauthValidationRequest;tmp25_24.getClass();OAuth2TokenValidationRequestDTO.OAuth2AccessToken accessToken = new OAuth2TokenValidationRequestDTO.OAuth2AccessToken(tmp25_24);
/* 183 */       accessToken.setTokenType("bearer");
/* 184 */       accessToken.setIdentifier(accessTokenIdentifier);
/* 185 */       oauthValidationRequest.setAccessToken(accessToken);
/*     */       
/* 187 */       OAuth2TokenValidationService oauthValidationService = new OAuth2TokenValidationService();
/* 188 */       org.wso2.carbon.identity.oauth2.dto.OAuth2ClientApplicationDTO oauthValidationResponse = oauthValidationService.findOAuthConsumerIfTokenIsValid(oauthValidationRequest);
/*     */       
/*     */ 
/* 191 */       return oauthValidationResponse;
/*     */     }
/*     */     try
/*     */     {
/* 195 */       ConfigurationContext configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
/*     */       
/* 197 */       OAuthServiceClient oauthClient = new OAuthServiceClient(getOAuthAuthzServerURL(), this.userName, this.password, configContext);
/*     */       
/*     */ 
/* 200 */       org.wso2.carbon.identity.oauth2.stub.dto.OAuth2ClientApplicationDTO validationResponse = oauthClient.findOAuthConsumerIfTokenIsValid(accessTokenIdentifier);
/*     */       
/* 202 */       org.wso2.carbon.identity.oauth2.dto.OAuth2ClientApplicationDTO appDTO = new org.wso2.carbon.identity.oauth2.dto.OAuth2ClientApplicationDTO();
/* 203 */       appDTO.setConsumerKey(validationResponse.getConsumerKey());
/*     */       
/* 205 */       org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO validationDto = new org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO();
/* 206 */       validationDto.setAuthorizedUser(validationResponse.getAccessTokenValidationResponse().getAuthorizedUser());
/*     */       
/* 208 */       validationDto.setValid(validationResponse.getAccessTokenValidationResponse().getValid());
/*     */       
/* 210 */       appDTO.setAccessTokenValidationResponse(validationDto);
/* 211 */       return appDTO;
/*     */     } catch (AxisFault axisFault) {
/* 213 */       throw axisFault;
/*     */     } catch (Exception exception) {
/* 215 */       throw exception;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\auth\OAuthHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */