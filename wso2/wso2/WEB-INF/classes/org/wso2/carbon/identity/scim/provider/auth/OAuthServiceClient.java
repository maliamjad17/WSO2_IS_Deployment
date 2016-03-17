/*     */ package org.wso2.carbon.identity.scim.provider.auth;
/*     */ 
/*     */ import java.rmi.RemoteException;
/*     */ import org.apache.axis2.AxisFault;
/*     */ import org.apache.axis2.context.ConfigurationContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.wso2.carbon.identity.oauth2.stub.OAuth2TokenValidationServiceStub;
/*     */ import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2ClientApplicationDTO;
/*     */ import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO;
/*     */ import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_OAuth2AccessToken;
/*     */ import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationResponseDTO;
/*     */ import org.wso2.carbon.utils.CarbonUtils;
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
/*     */ public class OAuthServiceClient
/*     */ {
/*  34 */   private OAuth2TokenValidationServiceStub stub = null;
/*  35 */   private static final Log log = LogFactory.getLog(OAuthServiceClient.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String BEARER_TOKEN_TYPE = "bearer";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OAuthServiceClient(String backendServerURL, String username, String password, ConfigurationContext configCtx)
/*     */     throws Exception
/*     */   {
/*  49 */     String serviceURL = backendServerURL + "OAuth2TokenValidationService";
/*     */     try {
/*  51 */       this.stub = new OAuth2TokenValidationServiceStub(configCtx, serviceURL);
/*  52 */       CarbonUtils.setBasicAccessSecurityHeaders(username, password, true, this.stub._getServiceClient());
/*     */     } catch (AxisFault e) {
/*  54 */       log.error("Error initializing OAuth2 Client");
/*  55 */       throw new Exception("Error initializing OAuth Client", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OAuth2TokenValidationResponseDTO validateAccessToken(String accessTokenIdentifier)
/*     */     throws Exception
/*     */   {
/*  68 */     OAuth2TokenValidationRequestDTO oauthReq = new OAuth2TokenValidationRequestDTO();
/*  69 */     OAuth2TokenValidationRequestDTO_OAuth2AccessToken accessToken = new OAuth2TokenValidationRequestDTO_OAuth2AccessToken();
/*     */     
/*  71 */     accessToken.setTokenType("bearer");
/*  72 */     accessToken.setIdentifier(accessTokenIdentifier);
/*  73 */     oauthReq.setAccessToken(accessToken);
/*     */     try {
/*  75 */       return this.stub.validate(oauthReq);
/*     */     } catch (RemoteException e) {
/*  77 */       log.error("Error while validating OAuth2 request");
/*  78 */       throw new Exception("Error while validating OAuth2 request", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OAuth2ClientApplicationDTO findOAuthConsumerIfTokenIsValid(String accessTokenIdentifier)
/*     */     throws Exception
/*     */   {
/*  90 */     OAuth2TokenValidationRequestDTO oauthReq = new OAuth2TokenValidationRequestDTO();
/*  91 */     OAuth2TokenValidationRequestDTO_OAuth2AccessToken accessToken = new OAuth2TokenValidationRequestDTO_OAuth2AccessToken();
/*     */     
/*  93 */     accessToken.setTokenType("bearer");
/*  94 */     accessToken.setIdentifier(accessTokenIdentifier);
/*  95 */     oauthReq.setAccessToken(accessToken);
/*     */     try {
/*  97 */       return this.stub.findOAuthConsumerIfTokenIsValid(oauthReq);
/*     */     } catch (RemoteException e) {
/*  99 */       log.error("Error while validating OAuth2 request");
/* 100 */       throw new Exception("Error while validating OAuth2 request", e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\auth\OAuthServiceClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */