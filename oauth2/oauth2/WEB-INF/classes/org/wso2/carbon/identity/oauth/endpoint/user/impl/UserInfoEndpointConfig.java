/*     */ package org.wso2.carbon.identity.oauth.endpoint.user.impl;
/*     */ 
/*     */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoAccessTokenValidator;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoClaimRetriever;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoRequestValidator;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.user.UserInfoResponseBuilder;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.util.EndpointUtil;
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
/*     */ public class UserInfoEndpointConfig
/*     */ {
/*  34 */   private static Log log = LogFactory.getLog(UserInfoEndpointConfig.class);
/*  35 */   private static UserInfoEndpointConfig config = new UserInfoEndpointConfig();
/*  36 */   private static UserInfoRequestValidator requestValidator = null;
/*  37 */   private static UserInfoAccessTokenValidator accessTokenValidator = null;
/*  38 */   private static UserInfoResponseBuilder responseBuilder = null;
/*  39 */   private static UserInfoClaimRetriever claimRetriever = null;
/*     */   
/*     */   private UserInfoEndpointConfig() {
/*  42 */     log.debug("Initializing the UserInfoEndpointConfig singlton");
/*  43 */     initUserInfoEndpointConfig();
/*     */   }
/*     */   
/*     */   private void initUserInfoEndpointConfig() {}
/*     */   
/*     */   public static UserInfoEndpointConfig getInstance()
/*     */   {
/*  50 */     return config;
/*     */   }
/*     */   
/*     */   public UserInfoRequestValidator getUserInfoRequestValidator() throws OAuthSystemException {
/*  54 */     if (requestValidator == null) {
/*  55 */       synchronized (UserInfoRequestValidator.class) {
/*  56 */         if (requestValidator == null) {
/*     */           try {
/*  58 */             String requestValidatorClassName = EndpointUtil.getUserInfoRequestValidator();
/*  59 */             Class requestValidatorClass = getClass().getClassLoader().loadClass(requestValidatorClassName);
/*     */             
/*     */ 
/*  62 */             requestValidator = (UserInfoRequestValidator)requestValidatorClass.newInstance();
/*     */           } catch (ClassNotFoundException e) {
/*  64 */             log.error("Error while loading configuration", e);
/*     */           } catch (InstantiationException e) {
/*  66 */             log.error("Error while loading configuration", e);
/*     */           } catch (IllegalAccessException e) {
/*  68 */             log.error("Error while loading configuration", e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  73 */     return requestValidator;
/*     */   }
/*     */   
/*     */   public UserInfoAccessTokenValidator getUserInfoAccessTokenValidator() {
/*  77 */     if (accessTokenValidator == null) {
/*  78 */       synchronized (UserInfoAccessTokenValidator.class) {
/*  79 */         if (accessTokenValidator == null) {
/*     */           try {
/*  81 */             String accessTokenValidatorClassName = EndpointUtil.getAccessTokenValidator();
/*  82 */             Class accessTokenValidatorClass = getClass().getClassLoader().loadClass(accessTokenValidatorClassName);
/*     */             
/*     */ 
/*  85 */             accessTokenValidator = (UserInfoAccessTokenValidator)accessTokenValidatorClass.newInstance();
/*     */           }
/*     */           catch (ClassNotFoundException e) {
/*  88 */             log.error("Error while loading configuration", e);
/*     */           } catch (InstantiationException e) {
/*  90 */             log.error("Error while loading configuration", e);
/*     */           } catch (IllegalAccessException e) {
/*  92 */             log.error("Error while loading configuration", e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  97 */     return accessTokenValidator;
/*     */   }
/*     */   
/*     */   public UserInfoResponseBuilder getUserInfoResponseBuilder() {
/* 101 */     if (responseBuilder == null) {
/* 102 */       synchronized (UserInfoResponseBuilder.class) {
/* 103 */         if (responseBuilder == null) {
/*     */           try {
/* 105 */             String responseBilderClassName = EndpointUtil.getUserInfoResponseBuilder();
/* 106 */             Class responseBuilderClass = getClass().getClassLoader().loadClass(responseBilderClassName);
/*     */             
/*     */ 
/* 109 */             responseBuilder = (UserInfoResponseBuilder)responseBuilderClass.newInstance();
/*     */           } catch (ClassNotFoundException e) {
/* 111 */             log.error("Error while loading configuration", e);
/*     */           } catch (InstantiationException e) {
/* 113 */             log.error("Error while loading configuration", e);
/*     */           } catch (IllegalAccessException e) {
/* 115 */             log.error("Error while loading configuration", e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 120 */     return responseBuilder;
/*     */   }
/*     */   
/*     */   public UserInfoClaimRetriever getUserInfoClaimRetriever() {
/* 124 */     if (claimRetriever == null) {
/* 125 */       synchronized (UserInfoClaimRetriever.class) {
/* 126 */         if (claimRetriever == null) {
/*     */           try {
/* 128 */             String claimRetrieverClassName = EndpointUtil.getUserInfoClaimRetriever();
/* 129 */             Class claimRetrieverClass = getClass().getClassLoader().loadClass(claimRetrieverClassName);
/*     */             
/*     */ 
/* 132 */             claimRetriever = (UserInfoClaimRetriever)claimRetrieverClass.newInstance();
/*     */           } catch (ClassNotFoundException e) {
/* 134 */             log.error("Error while loading configuration", e);
/*     */           } catch (InstantiationException e) {
/* 136 */             log.error("Error while loading configuration", e);
/*     */           } catch (IllegalAccessException e) {
/* 138 */             log.error("Error while loading configuration", e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 143 */     return claimRetriever;
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\impl\UserInfoEndpointConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */