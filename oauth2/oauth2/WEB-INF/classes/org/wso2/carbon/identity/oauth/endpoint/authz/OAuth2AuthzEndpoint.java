/*     */ package org.wso2.carbon.identity.oauth.endpoint.authz;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.core.Context;
/*     */ import javax.ws.rs.core.MultivaluedMap;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
/*     */ import org.apache.amber.oauth2.as.response.OAuthASResponse;
/*     */ import org.apache.amber.oauth2.as.response.OAuthASResponse.OAuthAuthorizationResponseBuilder;
/*     */ import org.apache.amber.oauth2.common.exception.OAuthProblemException;
/*     */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*     */ import org.apache.amber.oauth2.common.message.OAuthResponse;
/*     */ import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
/*     */ import org.apache.amber.oauth2.common.message.types.ResponseType;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.oltu.openidconnect.as.util.OIDCAuthzServerUtil;
/*     */ import org.wso2.carbon.context.PrivilegedCarbonContext;
/*     */ import org.wso2.carbon.identity.application.authentication.framework.cache.AuthenticationResultCache;
/*     */ import org.wso2.carbon.identity.application.authentication.framework.cache.AuthenticationResultCacheEntry;
/*     */ import org.wso2.carbon.identity.application.authentication.framework.cache.AuthenticationResultCacheKey;
/*     */ import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticationResult;
/*     */ import org.wso2.carbon.identity.application.common.cache.CacheEntry;
/*     */ import org.wso2.carbon.identity.oauth.cache.AuthorizationGrantCache;
/*     */ import org.wso2.carbon.identity.oauth.cache.AuthorizationGrantCacheEntry;
/*     */ import org.wso2.carbon.identity.oauth.cache.AuthorizationGrantCacheKey;
/*     */ import org.wso2.carbon.identity.oauth.cache.CacheKey;
/*     */ import org.wso2.carbon.identity.oauth.cache.SessionDataCache;
/*     */ import org.wso2.carbon.identity.oauth.cache.SessionDataCacheEntry;
/*     */ import org.wso2.carbon.identity.oauth.cache.SessionDataCacheKey;
/*     */ import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.OAuthRequestWrapper;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.util.EndpointUtil;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.util.OpenIDConnectUserRPStore;
/*     */ import org.wso2.carbon.identity.oauth2.OAuth2Service;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeReqDTO;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeRespDTO;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2ClientValidationResponseDTO;
/*     */ import org.wso2.carbon.identity.oauth2.model.OAuth2Parameters;
/*     */ import org.wso2.carbon.registry.core.utils.UUIDGenerator;
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
/*     */ @Path("/authorize")
/*     */ public class OAuth2AuthzEndpoint
/*     */ {
/*  73 */   private static Log log = LogFactory.getLog(OAuth2AuthzEndpoint.class);
/*     */   
/*     */   @GET
/*     */   @Path("/")
/*     */   @Consumes({"application/x-www-form-urlencoded"})
/*     */   @Produces({"text/html"})
/*     */   public Response authorize(@Context HttpServletRequest request)
/*     */     throws URISyntaxException
/*     */   {
/*  82 */     PrivilegedCarbonContext.startTenantFlow();
/*  83 */     PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
/*  84 */     carbonContext.setTenantId(64302);
/*  85 */     carbonContext.setTenantDomain("carbon.super");
/*     */     
/*  87 */     String clientId = request.getParameter(EndpointUtil.getSafeText("client_id"));
/*     */     
/*  89 */     String sessionDataKeyFromLogin = EndpointUtil.getSafeText(request.getParameter("sessionDataKey"));
/*     */     
/*  91 */     String sessionDataKeyFromConsent = EndpointUtil.getSafeText(request.getParameter("sessionDataKeyConsent"));
/*     */     
/*  93 */     CacheKey cacheKey = null;
/*  94 */     Object resultFromLogin = null;
/*  95 */     Object resultFromConsent = null;
/*  96 */     if ((sessionDataKeyFromLogin != null) && (!sessionDataKeyFromLogin.equals(""))) {
/*  97 */       cacheKey = new SessionDataCacheKey(sessionDataKeyFromLogin);
/*  98 */       resultFromLogin = SessionDataCache.getInstance().getValueFromCache(cacheKey);
/*     */     }
/* 100 */     if ((sessionDataKeyFromConsent != null) && (!sessionDataKeyFromConsent.equals(""))) {
/* 101 */       cacheKey = new SessionDataCacheKey(sessionDataKeyFromConsent);
/* 102 */       resultFromConsent = SessionDataCache.getInstance().getValueFromCache(cacheKey);
/*     */     }
/* 104 */     if ((resultFromLogin != null) && (resultFromConsent != null))
/*     */     {
/* 106 */       if (log.isDebugEnabled()) {
/* 107 */         String msg = "Invalid authorization request.'SessionDataKey' found in request as parameter and attribute, and both have non NULL objects in cache";
/*     */         
/*     */ 
/* 110 */         log.debug(msg);
/*     */       }
/* 112 */       String msg = "Invalid authorization request";
/* 113 */       return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("invalid_request", msg, null, null))).build();
/*     */     }
/*     */     
/* 116 */     if ((clientId == null) && (resultFromLogin == null) && (resultFromConsent == null))
/*     */     {
/* 118 */       if (log.isDebugEnabled()) {
/* 119 */         String msg = "Invalid authorization request.'SessionDataKey' not found in request as parameter or attribute, and client_id parameter cannot be found in request";
/*     */         
/*     */ 
/* 122 */         log.debug(msg);
/*     */       }
/* 124 */       String msg = "Invalid authorization request";
/* 125 */       return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("invalid_request", msg, null, null))).build();
/*     */     }
/*     */     
/* 128 */     if ((sessionDataKeyFromLogin != null) && (resultFromLogin == null))
/*     */     {
/* 130 */       log.debug("Session data not found in SessionDataCache for " + sessionDataKeyFromLogin);
/* 131 */       return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("access_denied", "Session Timed Out", null, null))).build();
/*     */     }
/*     */     
/* 134 */     if ((sessionDataKeyFromConsent != null) && (resultFromConsent == null))
/*     */     {
/* 136 */       if (resultFromLogin == null) {
/* 137 */         log.debug("Session data not found in SessionDataCache for " + sessionDataKeyFromConsent);
/* 138 */         return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("access_denied", "Session Timed Out", null, null))).build();
/*     */       }
/*     */       
/* 141 */       sessionDataKeyFromConsent = null;
/*     */     }
/*     */     
/*     */ 
/* 145 */     SessionDataCacheEntry sessionDataCacheEntry = null;
/*     */     
/*     */     try
/*     */     {
/* 149 */       if ((clientId != null) && (sessionDataKeyFromLogin == null) && (sessionDataKeyFromConsent == null))
/*     */       {
/* 151 */         String redirectURL = handleOAuthAuthorizationRequest(clientId, request, sessionDataCacheEntry);
/* 152 */         String retainCache; return Response.status(302).location(new URI(redirectURL)).build(); }
/*     */       String msg;
/* 154 */       if (resultFromLogin != null)
/*     */       {
/* 156 */         sessionDataCacheEntry = (SessionDataCacheEntry)resultFromLogin;
/* 157 */         OAuth2Parameters oauth2Params = sessionDataCacheEntry.getoAuth2Parameters();
/* 158 */         AuthenticationResult authnResult = getAuthenticationResultFromCache(sessionDataKeyFromLogin);
/* 159 */         String retainCache; if (authnResult != null) {
/* 160 */           AuthenticationResultCache.getInstance(0).clearCacheEntry(new AuthenticationResultCacheKey(sessionDataKeyFromLogin));
/*     */           
/*     */ 
/* 163 */           String redirectURL = null;
/* 164 */           if (authnResult.isAuthenticated())
/*     */           {
/* 166 */             String username = authnResult.getSubject();
/* 167 */             String tenantDomain = MultitenantUtils.getTenantDomain(username);
/* 168 */             String tenantAwareUserName = MultitenantUtils.getTenantAwareUsername(username);
/* 169 */             username = tenantAwareUserName + "@" + tenantDomain;
/* 170 */             username = username.toLowerCase();
/* 171 */             sessionDataCacheEntry.setLoggedInUser(username);
/* 172 */             if (authnResult.getUserAttributes() != null) {
/* 173 */               sessionDataCacheEntry.setUserAttributes(new ConcurrentHashMap(authnResult.getUserAttributes()));
/*     */             }
/* 175 */             sessionDataCacheEntry.setAuthenticatedIdPs(authnResult.getAuthenticatedIdPs());
/* 176 */             SessionDataCache.getInstance().addToCache(cacheKey, sessionDataCacheEntry);
/* 177 */             redirectURL = doUserAuthz(request, sessionDataKeyFromLogin, sessionDataCacheEntry);
/* 178 */             String retainCache; return Response.status(302).location(new URI(redirectURL)).build();
/*     */           }
/*     */           
/*     */ 
/* 182 */           OAuthProblemException oauthException = OAuthProblemException.error("access_denied", "Authentication required");
/*     */           
/* 184 */           redirectURL = OAuthASResponse.errorResponse(302).error(oauthException).location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState()).buildQueryMessage().getLocationUri();
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */           return Response.status(302).location(new URI(redirectURL)).build();
/*     */         }
/*     */         
/*     */ 
/* 194 */         String appName = null;
/* 195 */         if (sessionDataCacheEntry != null) {
/* 196 */           appName = sessionDataCacheEntry.getoAuth2Parameters().getApplicationName();
/*     */         }
/* 198 */         if (log.isDebugEnabled()) {
/* 199 */           String msg = "Invalid authorization request. 'sessionDataKey' attribute found but corresponding AuthenticationResult does not exist in the cache.";
/*     */           
/* 201 */           log.debug(msg);
/*     */         }
/* 203 */         msg = "Invalid authorization request";
/* 204 */         String retainCache; return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("invalid_request", msg, appName, null))).build();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 209 */       if (resultFromConsent != null)
/*     */       {
/* 211 */         sessionDataCacheEntry = (SessionDataCacheEntry)resultFromConsent;
/* 212 */         OAuth2Parameters oauth2Params = sessionDataCacheEntry.getoAuth2Parameters();
/* 213 */         String consent = EndpointUtil.getSafeText(request.getParameter("consent"));
/* 214 */         if (consent != null)
/*     */         {
/* 216 */           if ("deny".equals(consent))
/*     */           {
/* 218 */             String denyResponse = OAuthASResponse.errorResponse(302).setError("access_denied").location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState()).buildQueryMessage().getLocationUri();
/*     */             
/*     */             String retainCache;
/*     */             
/* 222 */             return Response.status(302).location(new URI(denyResponse)).build();
/*     */           }
/*     */           
/* 225 */           String redirectURL = handleUserConsent(request, consent, oauth2Params, sessionDataCacheEntry);
/*     */           
/* 227 */           String authenticatedIdPs = sessionDataCacheEntry.getAuthenticatedIdPs();
/*     */           
/* 229 */           if ((authenticatedIdPs != null) && (!authenticatedIdPs.isEmpty())) {
/*     */             try {
/* 231 */               redirectURL = redirectURL + "&AuthenticatedIdPs=" + URLEncoder.encode(authenticatedIdPs, "UTF-8");
/*     */             }
/*     */             catch (UnsupportedEncodingException e) {
/* 234 */               log.error(e.getMessage(), e);
/*     */             }
/*     */           }
/*     */           String retainCache;
/* 238 */           return Response.status(302).location(new URI(redirectURL)).build();
/*     */         }
/*     */         
/*     */ 
/* 242 */         appName = null;
/* 243 */         if (sessionDataCacheEntry != null) {
/* 244 */           appName = sessionDataCacheEntry.getoAuth2Parameters().getApplicationName();
/*     */         }
/* 246 */         if (log.isDebugEnabled()) {
/* 247 */           String msg = "Invalid authorization request. 'sessionDataKey' parameter found but 'consent' parameter could not be found in request";
/*     */           
/* 249 */           log.debug(msg);
/*     */         }
/* 251 */         String msg = "Invalid authorization request";
/* 252 */         String retainCache; return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("invalid_request", msg, appName, null))).build();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 259 */       String appName = null;
/* 260 */       if (sessionDataCacheEntry != null) {
/* 261 */         appName = sessionDataCacheEntry.getoAuth2Parameters().getApplicationName();
/*     */       }
/* 263 */       String msg = "Invalid authorization request";
/* 264 */       log.debug(msg);
/* 265 */       String retainCache; return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("invalid_request", msg, appName, null))).build();
/*     */     }
/*     */     catch (OAuthProblemException e)
/*     */     {
/*     */       String appName;
/*     */       
/* 271 */       String appName = null;
/* 272 */       if (sessionDataCacheEntry != null) {
/* 273 */         appName = sessionDataCacheEntry.getoAuth2Parameters().getApplicationName();
/*     */       }
/* 275 */       log.debug(e.getError(), e);
/* 276 */       return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("invalid_request", e.getMessage(), appName, null))).build();
/*     */     }
/*     */     catch (OAuthSystemException e)
/*     */     {
/*     */       String retainCache;
/* 281 */       String redirect_uri = null;
/* 282 */       if (sessionDataCacheEntry != null) {
/* 283 */         redirect_uri = sessionDataCacheEntry.getoAuth2Parameters().getRedirectURI();
/*     */       }
/* 285 */       log.debug(e.getMessage(), e);
/* 286 */       String msg = "Server error occurred while performing authorization";
/* 287 */       if ((redirect_uri != null) && (!redirect_uri.equals(""))) { String retainCache;
/* 288 */         return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("server_error", msg, null, redirect_uri))).build();
/*     */       }
/*     */       String retainCache;
/* 291 */       return Response.status(302).location(new URI(EndpointUtil.getErrorPageURL("server_error", msg, null, null))).build();
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 296 */       if (sessionDataKeyFromConsent != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 301 */         String retainCache = System.getProperty("retainCache");
/*     */         
/* 303 */         if (retainCache == null) {
/* 304 */           clearCacheEntry(sessionDataKeyFromConsent);
/*     */         }
/*     */       }
/*     */       
/* 308 */       PrivilegedCarbonContext.endTenantFlow();
/*     */     }
/*     */   }
/*     */   
/*     */   @POST
/*     */   @Path("/")
/*     */   @Consumes({"application/x-www-form-urlencoded"})
/*     */   @Produces({"text/html"})
/*     */   public Response authorizePost(@Context HttpServletRequest request, MultivaluedMap paramMap) throws URISyntaxException {
/* 317 */     HttpServletRequestWrapper httpRequest = new OAuthRequestWrapper(request, paramMap);
/* 318 */     return authorize(httpRequest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String handleUserConsent(HttpServletRequest request, String consent, OAuth2Parameters oauth2Params, SessionDataCacheEntry sessionDataCacheEntry)
/*     */     throws OAuthSystemException
/*     */   {
/* 331 */     String applicationName = sessionDataCacheEntry.getoAuth2Parameters().getApplicationName();
/* 332 */     String loggedInUser = sessionDataCacheEntry.getLoggedInUser();
/*     */     
/* 334 */     boolean skipConsent = EndpointUtil.getOAuthServerConfiguration().getOpenIDConnectSkipeUserConsentConfig();
/* 335 */     if (!skipConsent) {
/* 336 */       boolean approvedAlways = "approveAlways".equals(consent);
/*     */       
/* 338 */       OpenIDConnectUserRPStore.getInstance().putUserRPToStore(loggedInUser, applicationName, approvedAlways);
/*     */     }
/*     */     
/* 341 */     OAuthResponse oauthResponse = null;
/*     */     
/*     */ 
/* 344 */     OAuth2AuthorizeRespDTO authzRespDTO = authorize(oauth2Params, sessionDataCacheEntry);
/*     */     
/* 346 */     if ((authzRespDTO != null) && (authzRespDTO.getErrorCode() == null)) {
/* 347 */       OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, 302);
/*     */       
/*     */ 
/* 350 */       if (ResponseType.CODE.toString().equals(oauth2Params.getResponseType())) {
/* 351 */         String code = authzRespDTO.getAuthorizationCode();
/* 352 */         builder.setCode(code);
/* 353 */         addUserAttributesToCache(sessionDataCacheEntry, code);
/* 354 */       } else if (ResponseType.TOKEN.toString().equals(oauth2Params.getResponseType())) {
/* 355 */         builder.setAccessToken(authzRespDTO.getAccessToken());
/* 356 */         builder.setExpiresIn(String.valueOf(3600));
/*     */       }
/* 358 */       builder.setParam("state", oauth2Params.getState());
/* 359 */       String redirectURL = authzRespDTO.getCallbackURI();
/* 360 */       oauthResponse = builder.location(redirectURL).buildQueryMessage();
/*     */     }
/*     */     else
/*     */     {
/* 364 */       OAuthProblemException oauthProblemException = OAuthProblemException.error(authzRespDTO.getErrorCode(), authzRespDTO.getErrorMsg());
/*     */       
/* 366 */       oauthResponse = OAuthASResponse.errorResponse(302).error(oauthProblemException).location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState()).buildQueryMessage();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 371 */     return oauthResponse.getLocationUri();
/*     */   }
/*     */   
/*     */   private void addUserAttributesToCache(SessionDataCacheEntry sessionDataCacheEntry, String code) {
/* 375 */     AuthorizationGrantCacheKey authorizationGrantCacheKey = new AuthorizationGrantCacheKey(code);
/* 376 */     AuthorizationGrantCacheEntry authorizationGrantCacheEntry = new AuthorizationGrantCacheEntry(sessionDataCacheEntry.getUserAttributes());
/*     */     
/*     */ 
/* 379 */     authorizationGrantCacheEntry.setNonceValue(sessionDataCacheEntry.getoAuth2Parameters().getNonce());
/* 380 */     AuthorizationGrantCache.getInstance().addToCache(authorizationGrantCacheKey, authorizationGrantCacheEntry);
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
/*     */   private String handleOAuthAuthorizationRequest(String clientId, HttpServletRequest req, SessionDataCacheEntry sessionDataCacheEntry)
/*     */     throws OAuthSystemException, OAuthProblemException
/*     */   {
/* 410 */     OAuth2ClientValidationResponseDTO clientDTO = null;
/* 411 */     String redirect_uri = EndpointUtil.getSafeText(req.getParameter("redirect_uri"));
/* 412 */     if ((clientId == null) || (clientId.equals(""))) {
/* 413 */       String msg = "Client Id is not present in the authorization request";
/* 414 */       log.debug(msg);
/* 415 */       return EndpointUtil.getErrorPageURL("invalid_request", msg, null, null); }
/* 416 */     if ((redirect_uri == null) || (redirect_uri.equals(""))) {
/* 417 */       String msg = "Redirect URI is not present in the authorization request";
/* 418 */       log.debug(msg);
/* 419 */       return EndpointUtil.getErrorPageURL("invalid_request", msg, null, null);
/*     */     }
/* 421 */     clientDTO = validateClient(clientId, redirect_uri);
/*     */     
/*     */ 
/* 424 */     if (!clientDTO.isValidClient()) {
/* 425 */       return EndpointUtil.getErrorPageURL(clientDTO.getErrorCode(), clientDTO.getErrorMsg(), null, null);
/*     */     }
/*     */     
/*     */ 
/* 429 */     OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(req);
/*     */     
/* 431 */     OAuth2Parameters params = new OAuth2Parameters();
/* 432 */     params.setClientId(clientId);
/* 433 */     params.setRedirectURI(clientDTO.getCallbackURL());
/* 434 */     params.setResponseType(oauthRequest.getResponseType());
/* 435 */     params.setScopes(oauthRequest.getScopes());
/* 436 */     if (params.getScopes() == null) {
/* 437 */       Set<String> scopeSet = new HashSet();
/* 438 */       scopeSet.add("");
/* 439 */       params.setScopes(scopeSet);
/*     */     }
/* 441 */     params.setState(oauthRequest.getState());
/* 442 */     params.setApplicationName(clientDTO.getApplicationName());
/*     */     
/*     */ 
/* 445 */     params.setNonce(oauthRequest.getParam("nonce"));
/* 446 */     params.setDisplay(oauthRequest.getParam("display"));
/* 447 */     params.setIDTokenHint(oauthRequest.getParam("id_token_hint"));
/* 448 */     params.setLoginHint(oauthRequest.getParam("login_hint"));
/* 449 */     if ((oauthRequest.getParam("acr_values") != null) && (!oauthRequest.getParam("acr_values").equals("null")) && (!oauthRequest.getParam("acr_values").equals("")))
/*     */     {
/* 451 */       String[] acrValues = oauthRequest.getParam("acr_values").split(" ");
/* 452 */       LinkedHashSet list = new LinkedHashSet();
/* 453 */       for (String acrValue : acrValues) {
/* 454 */         list.add(acrValue);
/*     */       }
/* 456 */       params.setACRValues(list);
/*     */     }
/* 458 */     String prompt = oauthRequest.getParam("prompt");
/* 459 */     if (prompt == null) {
/* 460 */       prompt = "consent";
/*     */     }
/* 462 */     params.setPrompt(prompt);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 494 */     boolean forceAuthenticate = false;
/* 495 */     boolean checkAuthentication = false;
/*     */     
/* 497 */     if (prompt != null)
/*     */     {
/* 499 */       String[] prompts = prompt.trim().split("\\s");
/* 500 */       boolean contains_none = prompt.contains("none");
/* 501 */       if ((prompts.length > 1) && (contains_none)) {
/* 502 */         String error = "Invalid prompt variable combination. The value 'none' cannot be used with others prompts.";
/* 503 */         log.debug(error + " " + "Prompt: " + prompt);
/* 504 */         return OAuthASResponse.errorResponse(302).setError("invalid_request").setErrorDescription(error).location(params.getRedirectURI()).setState(params.getState()).buildQueryMessage().getLocationUri();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 510 */       if (prompt.contains("login")) {
/* 511 */         checkAuthentication = false;
/* 512 */         forceAuthenticate = true;
/*     */       }
/* 514 */       else if ((contains_none) || (prompt.contains("consent"))) {
/* 515 */         checkAuthentication = false;
/* 516 */         forceAuthenticate = false;
/*     */       }
/*     */     }
/*     */     
/* 520 */     String sessionDataKey = UUIDGenerator.generateUUID();
/* 521 */     CacheKey cacheKey = new SessionDataCacheKey(sessionDataKey);
/* 522 */     sessionDataCacheEntry = new SessionDataCacheEntry();
/* 523 */     sessionDataCacheEntry.setoAuth2Parameters(params);
/* 524 */     sessionDataCacheEntry.setQueryString(req.getQueryString());
/*     */     
/* 526 */     if (req.getParameterMap() != null) {
/* 527 */       sessionDataCacheEntry.setParamMap(new ConcurrentHashMap(req.getParameterMap()));
/*     */     }
/* 529 */     SessionDataCache.getInstance().addToCache(cacheKey, sessionDataCacheEntry);
/*     */     try
/*     */     {
/* 532 */       return EndpointUtil.getLoginPageURL(clientId, sessionDataKey, forceAuthenticate, checkAuthentication, oauthRequest.getScopes(), req.getParameterMap());
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 535 */       log.debug(e.getMessage(), e);
/* 536 */       throw new OAuthSystemException("Error when encoding login page URL");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private OAuth2ClientValidationResponseDTO validateClient(String clientId, String callbackURL)
/*     */   {
/* 548 */     return EndpointUtil.getOAuth2Service().validateClientInfo(clientId, callbackURL);
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
/*     */   private String doUserAuthz(HttpServletRequest request, String sessionDataKey, SessionDataCacheEntry sessionDataCacheEntry)
/*     */     throws OAuthSystemException
/*     */   {
/* 581 */     OAuth2Parameters oauth2Params = sessionDataCacheEntry.getoAuth2Parameters();
/* 582 */     String loggedInUser = sessionDataCacheEntry.getLoggedInUser();
/*     */     
/* 584 */     boolean skipConsent = EndpointUtil.getOAuthServerConfiguration().getOpenIDConnectSkipeUserConsentConfig();
/*     */     
/*     */ 
/* 587 */     String appName = oauth2Params.getApplicationName();
/* 588 */     boolean hasUserApproved = OpenIDConnectUserRPStore.getInstance().hasUserApproved(loggedInUser, appName);
/*     */     
/*     */ 
/*     */ 
/* 592 */     if ((skipConsent) || (hasUserApproved))
/*     */     {
/* 594 */       return handleUserConsent(request, "approveAlways", oauth2Params, sessionDataCacheEntry);
/*     */     }
/* 596 */     if (oauth2Params.getPrompt().contains("none"))
/*     */     {
/*     */ 
/* 599 */       return OAuthASResponse.errorResponse(302).setError("access_denied").location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState()).buildQueryMessage().getLocationUri();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 608 */       return EndpointUtil.getUserConsentURL(oauth2Params, loggedInUser, sessionDataKey, OIDCAuthzServerUtil.isOIDCAuthzRequest(oauth2Params.getScopes()));
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 611 */       log.debug(e.getMessage(), e);
/* 612 */       throw new OAuthSystemException("Error when encoding consent page URL");
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
/*     */   private OAuth2AuthorizeRespDTO authorize(OAuth2Parameters oauth2Params, SessionDataCacheEntry sessionDataCacheEntry)
/*     */   {
/* 627 */     OAuth2AuthorizeReqDTO authzReqDTO = new OAuth2AuthorizeReqDTO();
/* 628 */     authzReqDTO.setCallbackUrl(oauth2Params.getRedirectURI());
/* 629 */     authzReqDTO.setConsumerKey(oauth2Params.getClientId());
/* 630 */     authzReqDTO.setResponseType(oauth2Params.getResponseType());
/* 631 */     authzReqDTO.setScopes((String[])oauth2Params.getScopes().toArray(new String[oauth2Params.getScopes().size()]));
/* 632 */     authzReqDTO.setUsername(sessionDataCacheEntry.getLoggedInUser());
/* 633 */     authzReqDTO.setACRValues(oauth2Params.getACRValues());
/* 634 */     return EndpointUtil.getOAuth2Service().authorize(authzReqDTO);
/*     */   }
/*     */   
/*     */   private void clearCacheEntry(String sessionDataKey) {
/* 638 */     if (sessionDataKey != null) {
/* 639 */       CacheKey cacheKey = new SessionDataCacheKey(sessionDataKey);
/* 640 */       Object result = SessionDataCache.getInstance().getValueFromCache(cacheKey);
/* 641 */       if (result != null) {
/* 642 */         SessionDataCache.getInstance().clearCacheEntry(cacheKey);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private AuthenticationResult getAuthenticationResultFromCache(String sessionDataKey)
/*     */   {
/* 649 */     AuthenticationResultCacheKey authResultCacheKey = new AuthenticationResultCacheKey(sessionDataKey);
/* 650 */     CacheEntry cacheEntry = AuthenticationResultCache.getInstance(0).getValueFromCache(authResultCacheKey);
/* 651 */     AuthenticationResult authResult = null;
/*     */     
/* 653 */     if (cacheEntry != null) {
/* 654 */       AuthenticationResultCacheEntry authResultCacheEntry = (AuthenticationResultCacheEntry)cacheEntry;
/* 655 */       authResult = authResultCacheEntry.getResult();
/*     */     } else {
/* 657 */       log.error("Cannot find AuthenticationResult from the cache");
/*     */     }
/*     */     
/* 660 */     return authResult;
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\authz\OAuth2AuthzEndpoint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */