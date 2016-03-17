/*     */ package org.wso2.carbon.identity.oauth.endpoint.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*     */ import org.apache.axiom.util.base64.Base64Utils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.wso2.carbon.base.ServerConfiguration;
/*     */ import org.wso2.carbon.context.PrivilegedCarbonContext;
/*     */ import org.wso2.carbon.identity.application.authentication.framework.cache.AuthenticationRequestCacheEntry;
/*     */ import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticationRequest;
/*     */ import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkUtils;
/*     */ import org.wso2.carbon.identity.oauth.cache.SessionDataCache;
/*     */ import org.wso2.carbon.identity.oauth.cache.SessionDataCacheEntry;
/*     */ import org.wso2.carbon.identity.oauth.cache.SessionDataCacheKey;
/*     */ import org.wso2.carbon.identity.oauth.common.exception.OAuthClientException;
/*     */ import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;
/*     */ import org.wso2.carbon.identity.oauth2.OAuth2Service;
/*     */ import org.wso2.carbon.identity.oauth2.OAuth2TokenValidationService;
/*     */ import org.wso2.carbon.identity.oauth2.model.OAuth2Parameters;
/*     */ import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
/*     */ import org.wso2.carbon.ui.CarbonUIUtil;
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
/*     */ public class EndpointUtil
/*     */ {
/*  49 */   private static Log log = LogFactory.getLog(EndpointUtil.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OAuth2Service getOAuth2Service()
/*     */   {
/*  57 */     return (OAuth2Service)PrivilegedCarbonContext.getCurrentContext().getOSGiService(OAuth2Service.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OAuthServerConfiguration getOAuthServerConfiguration()
/*     */   {
/*  67 */     return (OAuthServerConfiguration)PrivilegedCarbonContext.getCurrentContext().getOSGiService(OAuthServerConfiguration.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OAuth2TokenValidationService getOAuth2TokenValidationService()
/*     */   {
/*  77 */     return (OAuth2TokenValidationService)PrivilegedCarbonContext.getCurrentContext().getOSGiService(OAuth2TokenValidationService.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getUserInfoRequestValidator()
/*     */     throws OAuthSystemException
/*     */   {
/*  87 */     return getOAuthServerConfiguration().getOpenIDConnectUserInfoEndpointRequestValidator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getAccessTokenValidator()
/*     */   {
/*  95 */     return getOAuthServerConfiguration().getOpenIDConnectUserInfoEndpointAccessTokenValidator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getUserInfoResponseBuilder()
/*     */   {
/* 103 */     return getOAuthServerConfiguration().getOpenIDConnectUserInfoEndpointResponseBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getUserInfoClaimRetriever()
/*     */   {
/* 111 */     return getOAuthServerConfiguration().getOpenIDConnectUserInfoEndpointClaimRetriever();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getUserInfoClaimDialect()
/*     */   {
/* 119 */     return getOAuthServerConfiguration().getOpenIDConnectUserInfoEndpointClaimDialect();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] extractCredentialsFromAuthzHeader(String authorizationHeader)
/*     */     throws OAuthClientException
/*     */   {
/* 130 */     String[] splitValues = authorizationHeader.trim().split(" ");
/* 131 */     byte[] decodedBytes = Base64Utils.decode(splitValues[1].trim());
/* 132 */     if (decodedBytes != null) {
/* 133 */       String userNamePassword = new String(decodedBytes);
/* 134 */       return userNamePassword.split(":");
/*     */     }
/* 136 */     String errMsg = "Error decoding authorization header. Could not retrieve client id and client secret.";
/* 137 */     throw new OAuthClientException(errMsg);
/*     */   }
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
/*     */   public static String getErrorPageURL(String errorCode, String errorMessage, String appName, String redirect_uri)
/*     */   {
/* 152 */     String errorPageUrl = null;
/* 153 */     if ((redirect_uri != null) && (!redirect_uri.equals(""))) {
/* 154 */       errorPageUrl = redirect_uri;
/*     */     } else {
/* 156 */       errorPageUrl = CarbonUIUtil.getAdminConsoleURL("/") + "../authenticationendpoint/oauth2_error.do";
/*     */     }
/*     */     try {
/* 159 */       errorPageUrl = errorPageUrl + "?oauthErrorCode=" + URLEncoder.encode(errorCode, "UTF-8") + "&" + "oauthErrorMsg" + "=" + URLEncoder.encode(errorMessage, "UTF-8");
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 166 */     if (appName != null) {
/*     */       try {
/* 168 */         errorPageUrl = errorPageUrl + "application=" + URLEncoder.encode(appName, "UTF-8");
/*     */       }
/*     */       catch (UnsupportedEncodingException e) {}
/*     */     }
/*     */     
/*     */ 
/* 174 */     return errorPageUrl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getLoginPageURL(String clientId, String sessionDataKey, boolean forceAuthenticate, boolean checkAuthentication, Set<String> scopes)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 190 */       SessionDataCacheEntry entry = (SessionDataCacheEntry)SessionDataCache.getInstance().getValueFromCache(new SessionDataCacheKey(sessionDataKey));
/*     */       
/*     */ 
/* 193 */       return getLoginPageURL(clientId, sessionDataKey, forceAuthenticate, checkAuthentication, scopes, entry.getParamMap());
/*     */     }
/*     */     finally {
/* 196 */       OAuth2Util.clearClientTenantId();
/*     */     }
/*     */   }
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
/*     */   public static String getLoginPageURL(String clientId, String sessionDataKey, boolean forceAuthenticate, boolean checkAuthentication, Set<String> scopes, Map<String, String[]> reqParams)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 218 */       String type = "oauth2";
/*     */       
/* 220 */       if ((scopes != null) && (scopes.contains("openid"))) {
/* 221 */         type = "oidc";
/*     */       }
/*     */       
/* 224 */       String commonAuthURL = CarbonUIUtil.getAdminConsoleURL("/");
/* 225 */       commonAuthURL = commonAuthURL.replace("carbon", "commonauth");
/* 226 */       String selfPath = "/oauth2/authorize";
/* 227 */       AuthenticationRequest authenticationRequest = new AuthenticationRequest();
/*     */       
/*     */ 
/* 230 */       authenticationRequest.setCommonAuthCallerPath(selfPath);
/* 231 */       authenticationRequest.setForceAuth(String.valueOf(forceAuthenticate));
/* 232 */       authenticationRequest.setPassiveAuth(String.valueOf(checkAuthentication));
/* 233 */       authenticationRequest.setRelyingParty(clientId);
/* 234 */       authenticationRequest.addRequestQueryParam("tenantId", new String[] { String.valueOf(OAuth2Util.getClientTenatId()) });
/*     */       
/* 236 */       authenticationRequest.setRequestQueryParams(reqParams);
/*     */       
/*     */ 
/* 239 */       AuthenticationRequestCacheEntry authRequest = new AuthenticationRequestCacheEntry(authenticationRequest);
/*     */       
/* 241 */       FrameworkUtils.addAuthenticationRequestToCache(sessionDataKey, authRequest);
/*     */       
/* 243 */       String loginQueryParams = "?sessionDataKey=" + sessionDataKey + "&" + "type" + "=" + type;
/*     */       
/*     */ 
/* 246 */       return commonAuthURL + loginQueryParams;
/*     */     }
/*     */     finally {
/* 249 */       OAuth2Util.clearClientTenantId();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getUserConsentURL(OAuth2Parameters params, String loggedInUser, String sessionDataKey, boolean isOIDC)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 262 */     String queryString = "";
/* 263 */     if (log.isDebugEnabled()) {
/* 264 */       log.debug("Received Session Data Key is :  " + sessionDataKey);
/* 265 */       if (params == null) {
/* 266 */         log.debug("Received OAuth2 params are Null for UserConsentURL");
/*     */       }
/*     */     }
/* 269 */     SessionDataCacheEntry entry = (SessionDataCacheEntry)SessionDataCache.getInstance().getValueFromCache(new SessionDataCacheKey(sessionDataKey));
/*     */     
/*     */ 
/*     */ 
/* 273 */     if (entry == null) {
/* 274 */       if (log.isDebugEnabled()) {
/* 275 */         log.debug("Cache Entry is Null from SessionDataCache ");
/*     */       }
/*     */     } else {
/* 278 */       queryString = URLEncoder.encode(entry.getQueryString(), "UTF-8");
/*     */     }
/*     */     
/* 281 */     String consentPage = null;
/* 282 */     if (isOIDC) {
/* 283 */       consentPage = CarbonUIUtil.getAdminConsoleURL("/") + "../authenticationendpoint/oauth2_consent.do";
/*     */     }
/*     */     else {
/* 286 */       consentPage = CarbonUIUtil.getAdminConsoleURL("/") + "../authenticationendpoint/oauth2_authz.do";
/*     */     }
/*     */     
/* 289 */     consentPage = consentPage + "?loggedInUser=" + URLEncoder.encode(loggedInUser, "UTF-8") + "&" + "application" + "=" + URLEncoder.encode(params.getApplicationName(), "ISO-8859-1") + "&" + "scope" + "=" + URLEncoder.encode(getScope(params), "ISO-8859-1") + "&" + "sessionDataKeyConsent" + "=" + URLEncoder.encode(sessionDataKey, "UTF-8") + "&" + "spQueryParams" + "=" + queryString;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 294 */     return consentPage;
/*     */   }
/*     */   
/*     */   public static String getScope(OAuth2Parameters params) {
/* 298 */     StringBuffer scopes = new StringBuffer();
/* 299 */     for (String scope : params.getScopes()) {
/* 300 */       scopes.append(getSafeText(scope) + " ");
/*     */     }
/* 302 */     return scopes.toString().trim();
/*     */   }
/*     */   
/*     */   public static String getSafeText(String text) {
/* 306 */     if (text == null) {
/* 307 */       return text;
/*     */     }
/* 309 */     text = text.trim();
/* 310 */     if (text.indexOf('<') > -1) {
/* 311 */       text = text.replace("<", "&lt;");
/*     */     }
/* 313 */     if (text.indexOf('>') > -1) {
/* 314 */       text = text.replace(">", "&gt;");
/*     */     }
/* 316 */     return text;
/*     */   }
/*     */   
/*     */   public static String getRealmInfo() {
/* 320 */     return "Basic realm=" + getHostName();
/*     */   }
/*     */   
/*     */   public static String getHostName() {
/* 324 */     return ServerConfiguration.getInstance().getFirstProperty("HostName");
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\util\EndpointUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */