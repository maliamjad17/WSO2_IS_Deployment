/*     */ package org.wso2.carbon.identity.scim.provider.impl;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.wso2.carbon.context.PrivilegedCarbonContext;
/*     */ import org.wso2.carbon.identity.scim.common.utils.SCIMCommonUtils;
/*     */ import org.wso2.carbon.user.api.AuthorizationManager;
/*     */ import org.wso2.carbon.user.api.UserRealm;
/*     */ import org.wso2.carbon.user.api.UserStoreException;
/*     */ import org.wso2.carbon.user.core.UserStoreManager;
/*     */ import org.wso2.carbon.user.core.claim.ClaimManager;
/*     */ import org.wso2.carbon.user.core.service.RealmService;
/*     */ import org.wso2.carbon.user.core.util.UserCoreUtil;
/*     */ import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
/*     */ import org.wso2.charon.core.encoder.Decoder;
/*     */ import org.wso2.charon.core.encoder.Encoder;
/*     */ import org.wso2.charon.core.encoder.json.JSONDecoder;
/*     */ import org.wso2.charon.core.encoder.json.JSONEncoder;
/*     */ import org.wso2.charon.core.exceptions.CharonException;
/*     */ import org.wso2.charon.core.exceptions.FormatNotSupportedException;
/*     */ import org.wso2.charon.core.exceptions.UnauthorizedException;
/*     */ import org.wso2.charon.core.extensions.AuthenticationHandler;
/*     */ import org.wso2.charon.core.extensions.AuthenticationInfo;
/*     */ import org.wso2.charon.core.extensions.CharonManager;
/*     */ import org.wso2.charon.core.extensions.TenantDTO;
/*     */ import org.wso2.charon.core.extensions.UserManager;
/*     */ import org.wso2.charon.core.protocol.endpoints.AbstractResourceEndpoint;
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
/*     */ public class IdentitySCIMManager
/*     */   implements CharonManager
/*     */ {
/*  58 */   private static Log log = LogFactory.getLog(IdentitySCIMManager.class);
/*     */   
/*     */   private static volatile IdentitySCIMManager identitySCIMManager;
/*     */   
/*  62 */   private static Map<String, Encoder> encoderMap = new HashMap();
/*  63 */   private static Map<String, Decoder> decoderMap = new HashMap();
/*  64 */   private static Map<String, Map> authenticators = new HashMap();
/*  65 */   private static Map<String, String> endpointURLs = new HashMap();
/*     */   
/*  67 */   private static Map<Integer, UserManager> userManagers = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */   private static final String INSTANCE = "instance";
/*     */   
/*     */ 
/*     */ 
/*     */   private void init()
/*     */     throws CharonException
/*     */   {
/*  77 */     encoderMap.put("json", new JSONEncoder());
/*  78 */     decoderMap.put("json", new JSONDecoder());
/*     */     
/*     */ 
/*  81 */     registerCoders();
/*     */     
/*     */ 
/*  84 */     endpointURLs.put("/Users", SCIMCommonUtils.getSCIMUserURL());
/*  85 */     endpointURLs.put("/Groups", SCIMCommonUtils.getSCIMGroupURL());
/*     */     
/*     */ 
/*  88 */     registerEndpointURLs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IdentitySCIMManager getInstance()
/*     */     throws CharonException
/*     */   {
/*  98 */     if (identitySCIMManager == null) {
/*  99 */       synchronized (IdentitySCIMManager.class) {
/* 100 */         if (identitySCIMManager == null) {
/* 101 */           identitySCIMManager = new IdentitySCIMManager();
/* 102 */           return identitySCIMManager;
/*     */         }
/* 104 */         return identitySCIMManager;
/*     */       }
/*     */     }
/*     */     
/* 108 */     return identitySCIMManager;
/*     */   }
/*     */   
/*     */   private IdentitySCIMManager() throws CharonException
/*     */   {
/* 113 */     init();
/*     */   }
/*     */   
/*     */   public Encoder getEncoder(String format) throws FormatNotSupportedException {
/* 117 */     if (!encoderMap.containsKey(format))
/*     */     {
/* 119 */       throw new FormatNotSupportedException(406, "Requested format is not supported.");
/*     */     }
/*     */     
/* 122 */     return (Encoder)encoderMap.get(format);
/*     */   }
/*     */   
/*     */   public Decoder getDecoder(String format) throws FormatNotSupportedException {
/* 126 */     if (!decoderMap.containsKey(format))
/*     */     {
/* 128 */       throw new FormatNotSupportedException(406, "Requested format is not supported.");
/*     */     }
/*     */     
/* 131 */     return (Decoder)decoderMap.get(format);
/*     */   }
/*     */   
/*     */ 
/*     */   public AuthenticationHandler getAuthenticationHandler(String authMechanism)
/*     */     throws CharonException
/*     */   {
/* 138 */     if (authenticators.size() != 0) {
/* 139 */       Map authenticatorProperties = (Map)authenticators.get(authMechanism);
/* 140 */       if ((authenticatorProperties != null) && (authenticatorProperties.size() != 0)) {
/* 141 */         return (AuthenticationHandler)authenticatorProperties.get("instance");
/*     */       }
/*     */     }
/* 144 */     String error = "Requested authentication mechanism is not supported.";
/* 145 */     throw new CharonException(error);
/*     */   }
/*     */   
/*     */   public UserManager getUserManager(String userName) throws CharonException {
/* 149 */     SCIMUserManager scimUserManager = null;
/* 150 */     String tenantDomain = MultitenantUtils.getTenantDomain(userName);
/* 151 */     String tenantLessUserName = MultitenantUtils.getTenantAwareUsername(userName);
/*     */     
/*     */     try
/*     */     {
/* 155 */       RealmService realmService = (RealmService)PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(RealmService.class);
/*     */       
/* 157 */       if (realmService != null) {
/* 158 */         int tenantId = realmService.getTenantManager().getTenantId(tenantDomain);
/*     */         
/* 160 */         UserRealm userRealm = realmService.getTenantUserRealm(tenantId);
/*     */         
/* 162 */         if (userRealm != null)
/*     */         {
/* 164 */           ClaimManager claimManager = (ClaimManager)userRealm.getClaimManager();
/*     */           
/* 166 */           if (tenantLessUserName.indexOf("/") < 0) {
/* 167 */             String domain = UserCoreUtil.getDomainFromThreadLocal();
/* 168 */             if (domain != null) {
/* 169 */               tenantLessUserName = domain + "/" + tenantLessUserName;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 174 */           boolean isUserAuthorized = userRealm.getAuthorizationManager().isUserAuthorized(tenantLessUserName, "/permission/admin/configure/security/usermgt/provisioning", "ui.execute");
/*     */           
/*     */ 
/* 177 */           if (!isUserAuthorized) {
/* 178 */             String error = "User is not authorized to perform provisioning";
/* 179 */             log.error(error);
/* 180 */             throw new CharonException(error);
/*     */           }
/*     */           
/*     */ 
/* 184 */           String authenticatedUser = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
/* 185 */           if (authenticatedUser == null) {
/* 186 */             PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername(tenantLessUserName);
/* 187 */             if (log.isDebugEnabled()) {
/* 188 */               log.debug("User read from carbon context is null, hence setting authenticated user: " + tenantLessUserName);
/*     */             }
/*     */           }
/*     */           
/* 192 */           scimUserManager = new SCIMUserManager((UserStoreManager)userRealm.getUserStoreManager(), userName, claimManager);
/*     */         }
/*     */       }
/*     */       else {
/* 196 */         String error = "Can not obtain carbon realm service..";
/* 197 */         throw new CharonException(error);
/*     */       }
/*     */     }
/*     */     catch (UserStoreException e) {
/* 201 */       String error = "Error obtaining user realm for the user: " + userName;
/* 202 */       throw new CharonException(error);
/*     */     }
/* 204 */     return scimUserManager;
/*     */   }
/*     */   
/*     */   public org.wso2.charon.core.extensions.TenantManager getTenantManager() {
/* 208 */     return null;
/*     */   }
/*     */   
/*     */   public AuthenticationInfo registerTenant(TenantDTO tenantDTO) throws CharonException {
/* 212 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isAuthenticationSupported(String s) {
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   public AuthenticationInfo handleAuthentication(Map<String, String> authHeaderMap) throws UnauthorizedException
/*     */   {
/* 221 */     AuthenticationInfo authInfo = null;
/*     */     try {
/* 223 */       String authType = identifyAuthType(authHeaderMap);
/* 224 */       Map authPropertyMap = (Map)authenticators.get(authType);
/* 225 */       if (authHeaderMap != null) {
/* 226 */         AuthenticationHandler authHandler = (AuthenticationHandler)authPropertyMap.get("instance");
/* 227 */         if (authHandler != null) {
/* 228 */           authHandler.setCharonManager(this);
/* 229 */           authHandler.isAuthenticated(authHeaderMap);
/* 230 */           return authHandler.getAuthenticationInfo();
/*     */         }
/*     */       }
/*     */     } catch (CharonException e) {
/* 234 */       throw new UnauthorizedException();
/*     */     }
/* 236 */     throw new UnauthorizedException();
/*     */   }
/*     */   
/*     */ 
/*     */   private void registerCoders()
/*     */     throws CharonException
/*     */   {
/* 243 */     if (!encoderMap.isEmpty()) {
/* 244 */       for (Map.Entry<String, Encoder> encoderEntry : encoderMap.entrySet()) {
/* 245 */         AbstractResourceEndpoint.registerEncoder((String)encoderEntry.getKey(), (Encoder)encoderEntry.getValue());
/*     */       }
/*     */     }
/* 248 */     if (!encoderMap.isEmpty()) {
/* 249 */       for (Map.Entry<String, Decoder> decoderEntry : decoderMap.entrySet()) {
/* 250 */         AbstractResourceEndpoint.registerDecoder((String)decoderEntry.getKey(), (Decoder)decoderEntry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerEndpointURLs()
/*     */   {
/* 257 */     if ((endpointURLs != null) && (endpointURLs.size() != 0)) {
/* 258 */       AbstractResourceEndpoint.registerResourceEndpointURLs(endpointURLs);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String identifyAuthType(Map<String, String> authHeaders)
/*     */     throws CharonException, UnauthorizedException
/*     */   {
/* 271 */     String authorizationHeader = (String)authHeaders.get("Authorization");
/* 272 */     String authenticationType = null;
/* 273 */     if (authorizationHeader != null) {
/* 274 */       authenticationType = authorizationHeader.split(" ")[0];
/*     */     } else {
/* 276 */       String error = "No Authorization header found";
/* 277 */       log.error(error);
/* 278 */       throw new UnauthorizedException();
/*     */     }
/* 280 */     if ("Basic".equals(authenticationType))
/* 281 */       return "Basic";
/* 282 */     if ("Bearer".equals(authenticationType))
/* 283 */       return "Bearer";
/* 284 */     if (authHeaders.get("Auth_Type") != null) {
/* 285 */       return (String)authHeaders.get("Auth_Type");
/*     */     }
/* 287 */     String error = "Provided authentication headers do not contain supported authentication headers.";
/* 288 */     throw new CharonException(error);
/*     */   }
/*     */   
/*     */   public UserManager getUserManager(String userName, String accessPermission) throws CharonException
/*     */   {
/* 293 */     SCIMUserManager scimUserManager = null;
/* 294 */     String tenantDomain = MultitenantUtils.getTenantDomain(userName);
/* 295 */     String tenantLessUserName = MultitenantUtils.getTenantAwareUsername(userName);
/*     */     
/*     */     try
/*     */     {
/* 299 */       RealmService realmService = (RealmService)PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(RealmService.class);
/*     */       
/* 301 */       if (realmService != null) {
/* 302 */         int tenantId = realmService.getTenantManager().getTenantId(tenantDomain);
/*     */         
/* 304 */         UserRealm userRealm = realmService.getTenantUserRealm(tenantId);
/*     */         
/* 306 */         if (userRealm != null)
/*     */         {
/* 308 */           ClaimManager claimManager = (ClaimManager)userRealm.getClaimManager();
/*     */           
/* 310 */           if (tenantLessUserName.indexOf("/") < 0) {
/* 311 */             String domain = UserCoreUtil.getDomainFromThreadLocal();
/* 312 */             if (domain != null) {
/* 313 */               tenantLessUserName = domain + "/" + tenantLessUserName;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 318 */           boolean isUserAuthorized = userRealm.getAuthorizationManager().isUserAuthorized(tenantLessUserName, accessPermission, "ui.execute");
/*     */           
/*     */ 
/* 321 */           if (!isUserAuthorized) {
/* 322 */             String error = "User is not authorized to perform provisioning";
/* 323 */             log.error(error);
/* 324 */             throw new CharonException(error);
/*     */           }
/*     */           
/*     */ 
/* 328 */           String authenticatedUser = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
/* 329 */           if (authenticatedUser == null) {
/* 330 */             PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername(tenantLessUserName);
/* 331 */             if (log.isDebugEnabled()) {
/* 332 */               log.debug("User read from carbon context is null, hence setting authenticated user: " + tenantLessUserName);
/*     */             }
/*     */           }
/*     */           
/* 336 */           scimUserManager = new SCIMUserManager((UserStoreManager)userRealm.getUserStoreManager(), userName, claimManager);
/*     */         }
/*     */       }
/*     */       else {
/* 340 */         String error = "Can not obtain carbon realm service..";
/* 341 */         throw new CharonException(error);
/*     */       }
/*     */     }
/*     */     catch (UserStoreException e) {
/* 345 */       String error = "Error obtaining user realm for the user: " + userName;
/* 346 */       throw new CharonException(error);
/*     */     }
/* 348 */     return scimUserManager;
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\impl\IdentitySCIMManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */