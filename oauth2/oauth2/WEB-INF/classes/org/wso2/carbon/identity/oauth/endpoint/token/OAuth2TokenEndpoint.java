/*     */ package org.wso2.carbon.identity.oauth.endpoint.token;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import org.apache.amber.oauth2.as.response.OAuthASResponse;
/*     */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*     */ import org.apache.amber.oauth2.common.message.OAuthResponse;
/*     */ import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.wso2.carbon.identity.oauth.common.exception.OAuthClientException;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.util.EndpointUtil;
/*     */ import org.wso2.carbon.identity.oauth2.OAuth2Service;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenReqDTO;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenRespDTO;
/*     */ import org.wso2.carbon.identity.oauth2.model.CarbonOAuthTokenRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Path("/token")
/*     */ public class OAuth2TokenEndpoint
/*     */ {
/*  59 */   private static Log log = LogFactory.getLog(OAuth2TokenEndpoint.class);
/*     */   
/*     */   /* Error */
/*     */   @javax.ws.rs.POST
/*     */   @Path("/")
/*     */   @javax.ws.rs.Consumes({"application/x-www-form-urlencoded"})
/*     */   @javax.ws.rs.Produces({"application/json"})
/*     */   public Response issueAccessToken(@javax.ws.rs.core.Context HttpServletRequest request, javax.ws.rs.core.MultivaluedMap<String, String> paramMap)
/*     */     throws OAuthSystemException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 2	org/wso2/carbon/context/PrivilegedCarbonContext:startTenantFlow	()V
/*     */     //   3: invokestatic 3	org/wso2/carbon/context/PrivilegedCarbonContext:getThreadLocalCarbonContext	()Lorg/wso2/carbon/context/PrivilegedCarbonContext;
/*     */     //   6: astore_3
/*     */     //   7: aload_3
/*     */     //   8: sipush 64302
/*     */     //   11: invokevirtual 4	org/wso2/carbon/context/PrivilegedCarbonContext:setTenantId	(I)V
/*     */     //   14: aload_3
/*     */     //   15: ldc 5
/*     */     //   17: invokevirtual 6	org/wso2/carbon/context/PrivilegedCarbonContext:setTenantDomain	(Ljava/lang/String;)V
/*     */     //   20: new 7	org/wso2/carbon/identity/oauth/endpoint/OAuthRequestWrapper
/*     */     //   23: dup
/*     */     //   24: aload_1
/*     */     //   25: aload_2
/*     */     //   26: invokespecial 8	org/wso2/carbon/identity/oauth/endpoint/OAuthRequestWrapper:<init>	(Ljavax/servlet/http/HttpServletRequest;Ljavax/ws/rs/core/MultivaluedMap;)V
/*     */     //   29: astore 4
/*     */     //   31: getstatic 9	org/wso2/carbon/identity/oauth/endpoint/token/OAuth2TokenEndpoint:log	Lorg/apache/commons/logging/Log;
/*     */     //   34: invokeinterface 10 1 0
/*     */     //   39: ifeq +9 -> 48
/*     */     //   42: aload_0
/*     */     //   43: aload 4
/*     */     //   45: invokespecial 11	org/wso2/carbon/identity/oauth/endpoint/token/OAuth2TokenEndpoint:logAccessTokenRequest	(Ljavax/servlet/http/HttpServletRequest;)V
/*     */     //   48: aload_1
/*     */     //   49: ldc 12
/*     */     //   51: invokeinterface 13 2 0
/*     */     //   56: ifnull +91 -> 147
/*     */     //   59: aload_1
/*     */     //   60: ldc 12
/*     */     //   62: invokeinterface 13 2 0
/*     */     //   67: invokestatic 14	org/wso2/carbon/identity/oauth/endpoint/util/EndpointUtil:extractCredentialsFromAuthzHeader	(Ljava/lang/String;)[Ljava/lang/String;
/*     */     //   70: astore 5
/*     */     //   72: aload_2
/*     */     //   73: ldc 15
/*     */     //   75: invokeinterface 16 2 0
/*     */     //   80: ifeq +26 -> 106
/*     */     //   83: aload_2
/*     */     //   84: ldc 17
/*     */     //   86: invokeinterface 16 2 0
/*     */     //   91: ifeq +15 -> 106
/*     */     //   94: aload_0
/*     */     //   95: invokespecial 18	org/wso2/carbon/identity/oauth/endpoint/token/OAuth2TokenEndpoint:handleBasicAuthFailure	()Ljavax/ws/rs/core/Response;
/*     */     //   98: astore 6
/*     */     //   100: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   103: aload 6
/*     */     //   105: areturn
/*     */     //   106: aload_2
/*     */     //   107: ldc 15
/*     */     //   109: aload 5
/*     */     //   111: iconst_0
/*     */     //   112: aaload
/*     */     //   113: invokeinterface 20 3 0
/*     */     //   118: aload_2
/*     */     //   119: ldc 17
/*     */     //   121: aload 5
/*     */     //   123: iconst_1
/*     */     //   124: aaload
/*     */     //   125: invokeinterface 20 3 0
/*     */     //   130: goto +17 -> 147
/*     */     //   133: astore 5
/*     */     //   135: aload_0
/*     */     //   136: invokespecial 18	org/wso2/carbon/identity/oauth/endpoint/token/OAuth2TokenEndpoint:handleBasicAuthFailure	()Ljavax/ws/rs/core/Response;
/*     */     //   139: astore 6
/*     */     //   141: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   144: aload 6
/*     */     //   146: areturn
/*     */     //   147: new 22	org/wso2/carbon/identity/oauth2/model/CarbonOAuthTokenRequest
/*     */     //   150: dup
/*     */     //   151: aload 4
/*     */     //   153: invokespecial 23	org/wso2/carbon/identity/oauth2/model/CarbonOAuthTokenRequest:<init>	(Ljavax/servlet/http/HttpServletRequest;)V
/*     */     //   156: astore 5
/*     */     //   158: aload_0
/*     */     //   159: aload 5
/*     */     //   161: invokespecial 24	org/wso2/carbon/identity/oauth/endpoint/token/OAuth2TokenEndpoint:getAccessToken	(Lorg/wso2/carbon/identity/oauth2/model/CarbonOAuthTokenRequest;)Lorg/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO;
/*     */     //   164: astore 6
/*     */     //   166: aload 6
/*     */     //   168: invokevirtual 25	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getErrorMsg	()Ljava/lang/String;
/*     */     //   171: ifnull +82 -> 253
/*     */     //   174: ldc 26
/*     */     //   176: aload 6
/*     */     //   178: invokevirtual 27	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getErrorCode	()Ljava/lang/String;
/*     */     //   181: invokevirtual 28	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   184: ifeq +15 -> 199
/*     */     //   187: aload_0
/*     */     //   188: invokespecial 18	org/wso2/carbon/identity/oauth/endpoint/token/OAuth2TokenEndpoint:handleBasicAuthFailure	()Ljavax/ws/rs/core/Response;
/*     */     //   191: astore 7
/*     */     //   193: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   196: aload 7
/*     */     //   198: areturn
/*     */     //   199: sipush 400
/*     */     //   202: invokestatic 29	org/apache/amber/oauth2/as/response/OAuthASResponse:errorResponse	(I)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   205: aload 6
/*     */     //   207: invokevirtual 27	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getErrorCode	()Ljava/lang/String;
/*     */     //   210: invokevirtual 30	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:setError	(Ljava/lang/String;)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   213: aload 6
/*     */     //   215: invokevirtual 25	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getErrorMsg	()Ljava/lang/String;
/*     */     //   218: invokevirtual 31	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:setErrorDescription	(Ljava/lang/String;)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   221: invokevirtual 32	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:buildJSONMessage	()Lorg/apache/amber/oauth2/common/message/OAuthResponse;
/*     */     //   224: astore 7
/*     */     //   226: aload 7
/*     */     //   228: invokevirtual 33	org/apache/amber/oauth2/common/message/OAuthResponse:getResponseStatus	()I
/*     */     //   231: invokestatic 34	javax/ws/rs/core/Response:status	(I)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   234: aload 7
/*     */     //   236: invokevirtual 35	org/apache/amber/oauth2/common/message/OAuthResponse:getBody	()Ljava/lang/String;
/*     */     //   239: invokevirtual 36	javax/ws/rs/core/Response$ResponseBuilder:entity	(Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   242: invokevirtual 37	javax/ws/rs/core/Response$ResponseBuilder:build	()Ljavax/ws/rs/core/Response;
/*     */     //   245: astore 8
/*     */     //   247: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   250: aload 8
/*     */     //   252: areturn
/*     */     //   253: sipush 200
/*     */     //   256: invokestatic 38	org/apache/amber/oauth2/as/response/OAuthASResponse:tokenResponse	(I)Lorg/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder;
/*     */     //   259: aload 6
/*     */     //   261: invokevirtual 39	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getAccessToken	()Ljava/lang/String;
/*     */     //   264: invokevirtual 40	org/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder:setAccessToken	(Ljava/lang/String;)Lorg/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder;
/*     */     //   267: aload 6
/*     */     //   269: invokevirtual 41	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getRefreshToken	()Ljava/lang/String;
/*     */     //   272: invokevirtual 42	org/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder:setRefreshToken	(Ljava/lang/String;)Lorg/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder;
/*     */     //   275: aload 6
/*     */     //   277: invokevirtual 43	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getExpiresIn	()J
/*     */     //   280: invokestatic 44	java/lang/Long:toString	(J)Ljava/lang/String;
/*     */     //   283: invokevirtual 45	org/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder:setExpiresIn	(Ljava/lang/String;)Lorg/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder;
/*     */     //   286: ldc 46
/*     */     //   288: invokevirtual 47	org/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder:setTokenType	(Ljava/lang/String;)Lorg/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder;
/*     */     //   291: astore 7
/*     */     //   293: aload 7
/*     */     //   295: aload 6
/*     */     //   297: invokevirtual 48	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getAuthorizedScopes	()Ljava/lang/String;
/*     */     //   300: invokevirtual 49	org/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder:setScope	(Ljava/lang/String;)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthResponseBuilder;
/*     */     //   303: pop
/*     */     //   304: aload 6
/*     */     //   306: invokevirtual 50	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getIDToken	()Ljava/lang/String;
/*     */     //   309: ifnull +16 -> 325
/*     */     //   312: aload 7
/*     */     //   314: ldc 51
/*     */     //   316: aload 6
/*     */     //   318: invokevirtual 50	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getIDToken	()Ljava/lang/String;
/*     */     //   321: invokevirtual 52	org/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder:setParam	(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder;
/*     */     //   324: pop
/*     */     //   325: aload 7
/*     */     //   327: invokevirtual 53	org/apache/amber/oauth2/as/response/OAuthASResponse$OAuthTokenResponseBuilder:buildJSONMessage	()Lorg/apache/amber/oauth2/common/message/OAuthResponse;
/*     */     //   330: astore 8
/*     */     //   332: aload 6
/*     */     //   334: invokevirtual 54	org/wso2/carbon/identity/oauth2/dto/OAuth2AccessTokenRespDTO:getResponseHeaders	()[Lorg/wso2/carbon/identity/oauth2/ResponseHeader;
/*     */     //   337: astore 9
/*     */     //   339: aload 8
/*     */     //   341: invokevirtual 33	org/apache/amber/oauth2/common/message/OAuthResponse:getResponseStatus	()I
/*     */     //   344: invokestatic 34	javax/ws/rs/core/Response:status	(I)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   347: ldc 55
/*     */     //   349: ldc 56
/*     */     //   351: invokevirtual 57	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   354: ldc 58
/*     */     //   356: ldc 59
/*     */     //   358: invokevirtual 57	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   361: astore 10
/*     */     //   363: aload 9
/*     */     //   365: ifnull +56 -> 421
/*     */     //   368: aload 9
/*     */     //   370: arraylength
/*     */     //   371: ifle +50 -> 421
/*     */     //   374: iconst_0
/*     */     //   375: istore 11
/*     */     //   377: iload 11
/*     */     //   379: aload 9
/*     */     //   381: arraylength
/*     */     //   382: if_icmpge +39 -> 421
/*     */     //   385: aload 9
/*     */     //   387: iload 11
/*     */     //   389: aaload
/*     */     //   390: ifnull +25 -> 415
/*     */     //   393: aload 10
/*     */     //   395: aload 9
/*     */     //   397: iload 11
/*     */     //   399: aaload
/*     */     //   400: invokevirtual 60	org/wso2/carbon/identity/oauth2/ResponseHeader:getKey	()Ljava/lang/String;
/*     */     //   403: aload 9
/*     */     //   405: iload 11
/*     */     //   407: aaload
/*     */     //   408: invokevirtual 61	org/wso2/carbon/identity/oauth2/ResponseHeader:getValue	()Ljava/lang/String;
/*     */     //   411: invokevirtual 57	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   414: pop
/*     */     //   415: iinc 11 1
/*     */     //   418: goto -41 -> 377
/*     */     //   421: aload 10
/*     */     //   423: aload 8
/*     */     //   425: invokevirtual 35	org/apache/amber/oauth2/common/message/OAuthResponse:getBody	()Ljava/lang/String;
/*     */     //   428: invokevirtual 36	javax/ws/rs/core/Response$ResponseBuilder:entity	(Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   431: invokevirtual 37	javax/ws/rs/core/Response$ResponseBuilder:build	()Ljavax/ws/rs/core/Response;
/*     */     //   434: astore 11
/*     */     //   436: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   439: aload 11
/*     */     //   441: areturn
/*     */     //   442: astore 5
/*     */     //   444: getstatic 9	org/wso2/carbon/identity/oauth/endpoint/token/OAuth2TokenEndpoint:log	Lorg/apache/commons/logging/Log;
/*     */     //   447: aload 5
/*     */     //   449: invokevirtual 63	org/apache/amber/oauth2/common/exception/OAuthProblemException:getError	()Ljava/lang/String;
/*     */     //   452: invokeinterface 64 2 0
/*     */     //   457: sipush 400
/*     */     //   460: invokestatic 29	org/apache/amber/oauth2/as/response/OAuthASResponse:errorResponse	(I)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   463: aload 5
/*     */     //   465: invokevirtual 65	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:error	(Lorg/apache/amber/oauth2/common/exception/OAuthProblemException;)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   468: invokevirtual 32	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:buildJSONMessage	()Lorg/apache/amber/oauth2/common/message/OAuthResponse;
/*     */     //   471: astore 6
/*     */     //   473: aload 6
/*     */     //   475: invokevirtual 33	org/apache/amber/oauth2/common/message/OAuthResponse:getResponseStatus	()I
/*     */     //   478: invokestatic 34	javax/ws/rs/core/Response:status	(I)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   481: aload 6
/*     */     //   483: invokevirtual 35	org/apache/amber/oauth2/common/message/OAuthResponse:getBody	()Ljava/lang/String;
/*     */     //   486: invokevirtual 36	javax/ws/rs/core/Response$ResponseBuilder:entity	(Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   489: invokevirtual 37	javax/ws/rs/core/Response$ResponseBuilder:build	()Ljavax/ws/rs/core/Response;
/*     */     //   492: astore 7
/*     */     //   494: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   497: aload 7
/*     */     //   499: areturn
/*     */     //   500: astore 5
/*     */     //   502: sipush 500
/*     */     //   505: invokestatic 29	org/apache/amber/oauth2/as/response/OAuthASResponse:errorResponse	(I)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   508: ldc 66
/*     */     //   510: invokevirtual 30	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:setError	(Ljava/lang/String;)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   513: aload 5
/*     */     //   515: invokevirtual 67	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException:getMessage	()Ljava/lang/String;
/*     */     //   518: invokevirtual 31	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:setErrorDescription	(Ljava/lang/String;)Lorg/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder;
/*     */     //   521: invokevirtual 32	org/apache/amber/oauth2/common/message/OAuthResponse$OAuthErrorResponseBuilder:buildJSONMessage	()Lorg/apache/amber/oauth2/common/message/OAuthResponse;
/*     */     //   524: astore 6
/*     */     //   526: aload 6
/*     */     //   528: invokevirtual 33	org/apache/amber/oauth2/common/message/OAuthResponse:getResponseStatus	()I
/*     */     //   531: invokestatic 34	javax/ws/rs/core/Response:status	(I)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   534: aload 6
/*     */     //   536: invokevirtual 35	org/apache/amber/oauth2/common/message/OAuthResponse:getBody	()Ljava/lang/String;
/*     */     //   539: invokevirtual 36	javax/ws/rs/core/Response$ResponseBuilder:entity	(Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   542: invokevirtual 37	javax/ws/rs/core/Response$ResponseBuilder:build	()Ljavax/ws/rs/core/Response;
/*     */     //   545: astore 7
/*     */     //   547: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   550: aload 7
/*     */     //   552: areturn
/*     */     //   553: astore 12
/*     */     //   555: invokestatic 19	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   558: aload 12
/*     */     //   560: athrow
/*     */     // Line number table:
/*     */     //   Java source line #69	-> byte code offset #0
/*     */     //   Java source line #70	-> byte code offset #3
/*     */     //   Java source line #72	-> byte code offset #7
/*     */     //   Java source line #73	-> byte code offset #14
/*     */     //   Java source line #75	-> byte code offset #20
/*     */     //   Java source line #77	-> byte code offset #31
/*     */     //   Java source line #78	-> byte code offset #42
/*     */     //   Java source line #83	-> byte code offset #48
/*     */     //   Java source line #86	-> byte code offset #59
/*     */     //   Java source line #90	-> byte code offset #72
/*     */     //   Java source line #92	-> byte code offset #94
/*     */     //   Java source line #171	-> byte code offset #100
/*     */     //   Java source line #96	-> byte code offset #106
/*     */     //   Java source line #97	-> byte code offset #118
/*     */     //   Java source line #102	-> byte code offset #130
/*     */     //   Java source line #99	-> byte code offset #133
/*     */     //   Java source line #101	-> byte code offset #135
/*     */     //   Java source line #171	-> byte code offset #141
/*     */     //   Java source line #106	-> byte code offset #147
/*     */     //   Java source line #108	-> byte code offset #158
/*     */     //   Java source line #110	-> byte code offset #166
/*     */     //   Java source line #112	-> byte code offset #174
/*     */     //   Java source line #113	-> byte code offset #187
/*     */     //   Java source line #171	-> byte code offset #193
/*     */     //   Java source line #116	-> byte code offset #199
/*     */     //   Java source line #121	-> byte code offset #226
/*     */     //   Java source line #171	-> byte code offset #247
/*     */     //   Java source line #123	-> byte code offset #253
/*     */     //   Java source line #129	-> byte code offset #293
/*     */     //   Java source line #132	-> byte code offset #304
/*     */     //   Java source line #133	-> byte code offset #312
/*     */     //   Java source line #135	-> byte code offset #325
/*     */     //   Java source line #136	-> byte code offset #332
/*     */     //   Java source line #137	-> byte code offset #339
/*     */     //   Java source line #144	-> byte code offset #363
/*     */     //   Java source line #145	-> byte code offset #374
/*     */     //   Java source line #146	-> byte code offset #385
/*     */     //   Java source line #147	-> byte code offset #393
/*     */     //   Java source line #145	-> byte code offset #415
/*     */     //   Java source line #152	-> byte code offset #421
/*     */     //   Java source line #171	-> byte code offset #436
/*     */     //   Java source line #155	-> byte code offset #442
/*     */     //   Java source line #156	-> byte code offset #444
/*     */     //   Java source line #157	-> byte code offset #457
/*     */     //   Java source line #160	-> byte code offset #473
/*     */     //   Java source line #171	-> byte code offset #494
/*     */     //   Java source line #161	-> byte code offset #500
/*     */     //   Java source line #162	-> byte code offset #502
/*     */     //   Java source line #166	-> byte code offset #526
/*     */     //   Java source line #171	-> byte code offset #547
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	561	0	this	OAuth2TokenEndpoint
/*     */     //   0	561	1	request	HttpServletRequest
/*     */     //   0	561	2	paramMap	javax.ws.rs.core.MultivaluedMap<String, String>
/*     */     //   6	9	3	carbonContext	org.wso2.carbon.context.PrivilegedCarbonContext
/*     */     //   29	123	4	httpRequest	javax.servlet.http.HttpServletRequestWrapper
/*     */     //   70	52	5	clientCredentials	String[]
/*     */     //   133	3	5	e	OAuthClientException
/*     */     //   156	4	5	oauthRequest	CarbonOAuthTokenRequest
/*     */     //   442	22	5	e	org.apache.amber.oauth2.common.exception.OAuthProblemException
/*     */     //   500	14	5	e	OAuthClientException
/*     */     //   98	47	6	localResponse1	Response
/*     */     //   164	169	6	oauth2AccessTokenResp	OAuth2AccessTokenRespDTO
/*     */     //   471	11	6	res	OAuthResponse
/*     */     //   524	11	6	response	OAuthResponse
/*     */     //   191	6	7	localResponse2	Response
/*     */     //   224	11	7	response	OAuthResponse
/*     */     //   291	260	7	oAuthRespBuilder	Object
/*     */     //   245	6	8	localResponse3	Response
/*     */     //   330	94	8	response	OAuthResponse
/*     */     //   337	67	9	headers	org.wso2.carbon.identity.oauth2.ResponseHeader[]
/*     */     //   361	61	10	respBuilder	Response.ResponseBuilder
/*     */     //   375	65	11	i	int
/*     */     //   553	6	12	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   59	100	133	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   106	130	133	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   147	193	442	org/apache/amber/oauth2/common/exception/OAuthProblemException
/*     */     //   199	247	442	org/apache/amber/oauth2/common/exception/OAuthProblemException
/*     */     //   253	436	442	org/apache/amber/oauth2/common/exception/OAuthProblemException
/*     */     //   147	193	500	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   199	247	500	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   253	436	500	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   0	100	553	finally
/*     */     //   106	141	553	finally
/*     */     //   147	193	553	finally
/*     */     //   199	247	553	finally
/*     */     //   253	436	553	finally
/*     */     //   442	494	553	finally
/*     */     //   500	547	553	finally
/*     */     //   553	555	553	finally
/*     */   }
/*     */   
/*     */   private Response handleBasicAuthFailure()
/*     */     throws OAuthSystemException
/*     */   {
/* 177 */     OAuthResponse response = OAuthASResponse.errorResponse(401).setError("invalid_client").setErrorDescription("Client Authentication failed.").buildJSONMessage();
/*     */     
/*     */ 
/* 180 */     return Response.status(response.getResponseStatus()).header("WWW-Authenticate", EndpointUtil.getRealmInfo()).entity(response.getBody()).build();
/*     */   }
/*     */   
/*     */ 
/*     */   private void logAccessTokenRequest(HttpServletRequest request)
/*     */   {
/* 186 */     log.debug("Received a request : " + request.getRequestURI());
/*     */     
/* 188 */     log.debug("----------logging request headers.----------");
/* 189 */     Enumeration headerNames = request.getHeaderNames();
/* 190 */     while (headerNames.hasMoreElements()) {
/* 191 */       String headerName = (String)headerNames.nextElement();
/* 192 */       Enumeration headers = request.getHeaders(headerName);
/* 193 */       while (headers.hasMoreElements()) {
/* 194 */         log.debug(headerName + " : " + headers.nextElement());
/*     */       }
/*     */     }
/*     */     
/* 198 */     log.debug("----------logging request parameters.----------");
/* 199 */     log.debug("grant_type - " + request.getParameter("grant_type"));
/* 200 */     log.debug("client_id - " + request.getParameter("client_id"));
/* 201 */     log.debug("code - " + request.getParameter("code"));
/* 202 */     log.debug("redirect_uri - " + request.getParameter("redirect_uri"));
/*     */   }
/*     */   
/*     */   private OAuth2AccessTokenRespDTO getAccessToken(CarbonOAuthTokenRequest oauthRequest)
/*     */     throws OAuthClientException
/*     */   {
/* 208 */     OAuth2AccessTokenReqDTO tokenReqDTO = new OAuth2AccessTokenReqDTO();
/* 209 */     String grantType = oauthRequest.getGrantType();
/* 210 */     tokenReqDTO.setGrantType(grantType);
/* 211 */     tokenReqDTO.setClientId(oauthRequest.getClientId());
/* 212 */     tokenReqDTO.setClientSecret(oauthRequest.getClientSecret());
/* 213 */     tokenReqDTO.setCallbackURI(oauthRequest.getRedirectURI());
/* 214 */     tokenReqDTO.setScope((String[])oauthRequest.getScopes().toArray(new String[oauthRequest.getScopes().size()]));
/*     */     
/* 216 */     if (org.apache.amber.oauth2.common.message.types.GrantType.AUTHORIZATION_CODE.toString().equals(grantType)) {
/* 217 */       tokenReqDTO.setAuthorizationCode(oauthRequest.getCode());
/* 218 */     } else if (org.apache.amber.oauth2.common.message.types.GrantType.PASSWORD.toString().equals(grantType)) {
/* 219 */       tokenReqDTO.setResourceOwnerUsername(oauthRequest.getUsername().toLowerCase());
/* 220 */       tokenReqDTO.setResourceOwnerPassword(oauthRequest.getPassword());
/* 221 */     } else if (org.apache.amber.oauth2.common.message.types.GrantType.REFRESH_TOKEN.toString().equals(grantType)) {
/* 222 */       tokenReqDTO.setRefreshToken(oauthRequest.getRefreshToken());
/* 223 */     } else if (org.wso2.carbon.identity.oauth.common.GrantType.SAML20_BEARER.toString().equals(grantType)) {
/* 224 */       tokenReqDTO.setAssertion(oauthRequest.getAssertion());
/* 225 */       tokenReqDTO.setTenantDomain(oauthRequest.getTenantDomain());
/* 226 */     } else if (org.wso2.carbon.identity.oauth.common.GrantType.IWA_NTLM.toString().equals(grantType)) {
/* 227 */       tokenReqDTO.setWindowsToken(oauthRequest.getWindowsToken());
/*     */     }
/*     */     else {
/* 230 */       tokenReqDTO.setRequestParameters(oauthRequest.getRequestParameters());
/*     */     }
/*     */     
/* 233 */     return EndpointUtil.getOAuth2Service().issueAccessToken(tokenReqDTO);
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\token\OAuth2TokenEndpoint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */