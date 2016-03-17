/*     */ package org.wso2.carbon.identity.oauth.endpoint.revoke;
/*     */ 
/*     */ import java.util.Enumeration;
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
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuthRevocationRequestDTO;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuthRevocationResponseDTO;
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
/*     */ @Path("/revoke")
/*     */ public class OAuthRevocationEndpoint
/*     */ {
/*  54 */   private static Log log = LogFactory.getLog(OAuthRevocationEndpoint.class);
/*     */   
/*     */   /* Error */
/*     */   @javax.ws.rs.POST
/*     */   @Path("/")
/*     */   @javax.ws.rs.Consumes({"application/x-www-form-urlencoded"})
/*     */   public Response revokeAccessToken(@javax.ws.rs.core.Context HttpServletRequest request, javax.ws.rs.core.MultivaluedMap<String, String> paramMap)
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
/*     */     //   31: getstatic 9	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:log	Lorg/apache/commons/logging/Log;
/*     */     //   34: invokeinterface 10 1 0
/*     */     //   39: ifeq +9 -> 48
/*     */     //   42: aload_0
/*     */     //   43: aload 4
/*     */     //   45: invokespecial 11	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:logAccessTokenRevocationRequest	(Ljavax/servlet/http/HttpServletRequest;)V
/*     */     //   48: aload 4
/*     */     //   50: ldc 12
/*     */     //   52: invokevirtual 13	javax/servlet/http/HttpServletRequestWrapper:getParameter	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   55: astore 5
/*     */     //   57: aload 5
/*     */     //   59: ifnull +13 -> 72
/*     */     //   62: aload 5
/*     */     //   64: ldc 14
/*     */     //   66: invokevirtual 15	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   69: ifeq +45 -> 114
/*     */     //   72: aload_2
/*     */     //   73: ldc 12
/*     */     //   75: invokeinterface 16 2 0
/*     */     //   80: ifnull +34 -> 114
/*     */     //   83: aload_2
/*     */     //   84: invokeinterface 17 1 0
/*     */     //   89: ifne +25 -> 114
/*     */     //   92: aload_2
/*     */     //   93: ldc 12
/*     */     //   95: invokeinterface 16 2 0
/*     */     //   100: checkcast 18	java/util/List
/*     */     //   103: iconst_0
/*     */     //   104: invokeinterface 19 2 0
/*     */     //   109: checkcast 20	java/lang/String
/*     */     //   112: astore 5
/*     */     //   114: aload 4
/*     */     //   116: ldc 21
/*     */     //   118: invokevirtual 13	javax/servlet/http/HttpServletRequestWrapper:getParameter	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   121: astore 6
/*     */     //   123: aload 6
/*     */     //   125: ifnull +13 -> 138
/*     */     //   128: aload 6
/*     */     //   130: ldc 14
/*     */     //   132: invokevirtual 15	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   135: ifeq +55 -> 190
/*     */     //   138: aload_2
/*     */     //   139: ldc 21
/*     */     //   141: invokeinterface 16 2 0
/*     */     //   146: ifnull +44 -> 190
/*     */     //   149: aload_2
/*     */     //   150: ldc 21
/*     */     //   152: invokeinterface 16 2 0
/*     */     //   157: checkcast 18	java/util/List
/*     */     //   160: invokeinterface 22 1 0
/*     */     //   165: ifne +25 -> 190
/*     */     //   168: aload_2
/*     */     //   169: ldc 21
/*     */     //   171: invokeinterface 16 2 0
/*     */     //   176: checkcast 18	java/util/List
/*     */     //   179: iconst_0
/*     */     //   180: invokeinterface 19 2 0
/*     */     //   185: checkcast 20	java/lang/String
/*     */     //   188: astore 6
/*     */     //   190: aload 4
/*     */     //   192: ldc 23
/*     */     //   194: invokevirtual 13	javax/servlet/http/HttpServletRequestWrapper:getParameter	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   197: invokestatic 24	org/wso2/carbon/identity/oauth/endpoint/util/EndpointUtil:getSafeText	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   200: astore 7
/*     */     //   202: aload 7
/*     */     //   204: ifnonnull +58 -> 262
/*     */     //   207: aload_2
/*     */     //   208: ldc 23
/*     */     //   210: invokeinterface 16 2 0
/*     */     //   215: ifnull +47 -> 262
/*     */     //   218: aload_2
/*     */     //   219: ldc 23
/*     */     //   221: invokeinterface 16 2 0
/*     */     //   226: checkcast 18	java/util/List
/*     */     //   229: invokeinterface 22 1 0
/*     */     //   234: ifne +28 -> 262
/*     */     //   237: aload_2
/*     */     //   238: ldc 23
/*     */     //   240: invokeinterface 16 2 0
/*     */     //   245: checkcast 18	java/util/List
/*     */     //   248: iconst_0
/*     */     //   249: invokeinterface 19 2 0
/*     */     //   254: checkcast 20	java/lang/String
/*     */     //   257: invokestatic 24	org/wso2/carbon/identity/oauth/endpoint/util/EndpointUtil:getSafeText	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   260: astore 7
/*     */     //   262: iconst_0
/*     */     //   263: istore 8
/*     */     //   265: aload_1
/*     */     //   266: ldc 25
/*     */     //   268: invokeinterface 26 2 0
/*     */     //   273: ifnull +95 -> 368
/*     */     //   276: aload_1
/*     */     //   277: ldc 25
/*     */     //   279: invokeinterface 26 2 0
/*     */     //   284: invokestatic 27	org/wso2/carbon/identity/oauth/endpoint/util/EndpointUtil:extractCredentialsFromAuthzHeader	(Ljava/lang/String;)[Ljava/lang/String;
/*     */     //   287: astore 9
/*     */     //   289: aload_2
/*     */     //   290: ldc 28
/*     */     //   292: invokeinterface 29 2 0
/*     */     //   297: ifeq +28 -> 325
/*     */     //   300: aload_2
/*     */     //   301: ldc 30
/*     */     //   303: invokeinterface 29 2 0
/*     */     //   308: ifeq +17 -> 325
/*     */     //   311: aload_0
/*     */     //   312: aload 7
/*     */     //   314: invokespecial 31	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:handleBasicAuthFailure	(Ljava/lang/String;)Ljavax/ws/rs/core/Response;
/*     */     //   317: astore 10
/*     */     //   319: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   322: aload 10
/*     */     //   324: areturn
/*     */     //   325: aload_2
/*     */     //   326: ldc 28
/*     */     //   328: aload 9
/*     */     //   330: iconst_0
/*     */     //   331: aaload
/*     */     //   332: invokeinterface 33 3 0
/*     */     //   337: aload_2
/*     */     //   338: ldc 30
/*     */     //   340: aload 9
/*     */     //   342: iconst_1
/*     */     //   343: aaload
/*     */     //   344: invokeinterface 33 3 0
/*     */     //   349: goto +19 -> 368
/*     */     //   352: astore 9
/*     */     //   354: aload_0
/*     */     //   355: aload 7
/*     */     //   357: invokespecial 31	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:handleBasicAuthFailure	(Ljava/lang/String;)Ljavax/ws/rs/core/Response;
/*     */     //   360: astore 10
/*     */     //   362: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   365: aload 10
/*     */     //   367: areturn
/*     */     //   368: new 35	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationRequestDTO
/*     */     //   371: dup
/*     */     //   372: invokespecial 36	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationRequestDTO:<init>	()V
/*     */     //   375: astore 9
/*     */     //   377: aload 9
/*     */     //   379: aload_2
/*     */     //   380: ldc 28
/*     */     //   382: invokeinterface 16 2 0
/*     */     //   387: checkcast 18	java/util/List
/*     */     //   390: iconst_0
/*     */     //   391: invokeinterface 19 2 0
/*     */     //   396: checkcast 20	java/lang/String
/*     */     //   399: invokevirtual 37	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationRequestDTO:setConsumerKey	(Ljava/lang/String;)V
/*     */     //   402: aload 9
/*     */     //   404: aload_2
/*     */     //   405: ldc 30
/*     */     //   407: invokeinterface 16 2 0
/*     */     //   412: checkcast 18	java/util/List
/*     */     //   415: iconst_0
/*     */     //   416: invokeinterface 19 2 0
/*     */     //   421: checkcast 20	java/lang/String
/*     */     //   424: invokevirtual 38	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationRequestDTO:setConsumerSecret	(Ljava/lang/String;)V
/*     */     //   427: aload 5
/*     */     //   429: ifnull +23 -> 452
/*     */     //   432: aload 5
/*     */     //   434: ldc 14
/*     */     //   436: invokevirtual 15	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   439: ifne +13 -> 452
/*     */     //   442: aload 9
/*     */     //   444: aload 5
/*     */     //   446: invokevirtual 39	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationRequestDTO:setToken	(Ljava/lang/String;)V
/*     */     //   449: goto +10 -> 459
/*     */     //   452: aload_0
/*     */     //   453: aload 7
/*     */     //   455: invokespecial 40	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:handleClientFailure	(Ljava/lang/String;)Ljavax/ws/rs/core/Response;
/*     */     //   458: pop
/*     */     //   459: aload 6
/*     */     //   461: ifnull +20 -> 481
/*     */     //   464: aload 6
/*     */     //   466: ldc 14
/*     */     //   468: invokevirtual 15	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   471: ifne +10 -> 481
/*     */     //   474: aload 9
/*     */     //   476: aload 6
/*     */     //   478: invokevirtual 41	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationRequestDTO:setToken_type	(Ljava/lang/String;)V
/*     */     //   481: aload_0
/*     */     //   482: aload 9
/*     */     //   484: invokespecial 42	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:revokeTokens	(Lorg/wso2/carbon/identity/oauth2/dto/OAuthRevocationRequestDTO;)Lorg/wso2/carbon/identity/oauth2/dto/OAuthRevocationResponseDTO;
/*     */     //   487: astore 10
/*     */     //   489: aload 10
/*     */     //   491: invokevirtual 43	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationResponseDTO:getErrorMsg	()Ljava/lang/String;
/*     */     //   494: ifnull +73 -> 567
/*     */     //   497: ldc 44
/*     */     //   499: aload 10
/*     */     //   501: invokevirtual 45	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationResponseDTO:getErrorCode	()Ljava/lang/String;
/*     */     //   504: invokevirtual 15	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   507: ifeq +17 -> 524
/*     */     //   510: aload_0
/*     */     //   511: aload 7
/*     */     //   513: invokespecial 31	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:handleBasicAuthFailure	(Ljava/lang/String;)Ljavax/ws/rs/core/Response;
/*     */     //   516: astore 11
/*     */     //   518: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   521: aload 11
/*     */     //   523: areturn
/*     */     //   524: ldc 46
/*     */     //   526: aload 10
/*     */     //   528: invokevirtual 45	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationResponseDTO:getErrorCode	()Ljava/lang/String;
/*     */     //   531: invokevirtual 15	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   534: ifeq +17 -> 551
/*     */     //   537: aload_0
/*     */     //   538: aload 7
/*     */     //   540: invokespecial 47	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:handleAuthorizationFailure	(Ljava/lang/String;)Ljavax/ws/rs/core/Response;
/*     */     //   543: astore 11
/*     */     //   545: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   548: aload 11
/*     */     //   550: areturn
/*     */     //   551: aload_0
/*     */     //   552: aload 7
/*     */     //   554: aload 10
/*     */     //   556: invokespecial 48	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:handleClientFailure	(Ljava/lang/String;Lorg/wso2/carbon/identity/oauth2/dto/OAuthRevocationResponseDTO;)Ljavax/ws/rs/core/Response;
/*     */     //   559: astore 11
/*     */     //   561: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   564: aload 11
/*     */     //   566: areturn
/*     */     //   567: aload 7
/*     */     //   569: ifnull +42 -> 611
/*     */     //   572: sipush 200
/*     */     //   575: invokestatic 49	org/wso2/carbon/identity/oauth/endpoint/revoke/CarbonOAuthASResponse:revokeResponse	(I)Lorg/wso2/carbon/identity/oauth/endpoint/revoke/CarbonOAuthASResponse$OAuthRevokeResponseBuilder;
/*     */     //   578: invokevirtual 50	org/wso2/carbon/identity/oauth/endpoint/revoke/CarbonOAuthASResponse$OAuthRevokeResponseBuilder:buildBodyMessage	()Lorg/apache/amber/oauth2/common/message/OAuthResponse;
/*     */     //   581: astore 11
/*     */     //   583: aload 11
/*     */     //   585: new 51	java/lang/StringBuilder
/*     */     //   588: dup
/*     */     //   589: invokespecial 52	java/lang/StringBuilder:<init>	()V
/*     */     //   592: aload 7
/*     */     //   594: invokevirtual 53	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   597: ldc 54
/*     */     //   599: invokevirtual 53	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   602: invokevirtual 55	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   605: invokevirtual 56	org/apache/amber/oauth2/common/message/OAuthResponse:setBody	(Ljava/lang/String;)V
/*     */     //   608: goto +14 -> 622
/*     */     //   611: sipush 200
/*     */     //   614: invokestatic 49	org/wso2/carbon/identity/oauth/endpoint/revoke/CarbonOAuthASResponse:revokeResponse	(I)Lorg/wso2/carbon/identity/oauth/endpoint/revoke/CarbonOAuthASResponse$OAuthRevokeResponseBuilder;
/*     */     //   617: invokevirtual 50	org/wso2/carbon/identity/oauth/endpoint/revoke/CarbonOAuthASResponse$OAuthRevokeResponseBuilder:buildBodyMessage	()Lorg/apache/amber/oauth2/common/message/OAuthResponse;
/*     */     //   620: astore 11
/*     */     //   622: aload 10
/*     */     //   624: invokevirtual 57	org/wso2/carbon/identity/oauth2/dto/OAuthRevocationResponseDTO:getResponseHeaders	()[Lorg/wso2/carbon/identity/oauth2/ResponseHeader;
/*     */     //   627: astore 12
/*     */     //   629: aload 11
/*     */     //   631: invokevirtual 58	org/apache/amber/oauth2/common/message/OAuthResponse:getResponseStatus	()I
/*     */     //   634: invokestatic 59	javax/ws/rs/core/Response:status	(I)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   637: ldc 60
/*     */     //   639: ldc 61
/*     */     //   641: invokevirtual 62	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   644: ldc 63
/*     */     //   646: ldc 64
/*     */     //   648: invokevirtual 62	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   651: astore 13
/*     */     //   653: aload 12
/*     */     //   655: ifnull +56 -> 711
/*     */     //   658: aload 12
/*     */     //   660: arraylength
/*     */     //   661: ifle +50 -> 711
/*     */     //   664: iconst_0
/*     */     //   665: istore 14
/*     */     //   667: iload 14
/*     */     //   669: aload 12
/*     */     //   671: arraylength
/*     */     //   672: if_icmpge +39 -> 711
/*     */     //   675: aload 12
/*     */     //   677: iload 14
/*     */     //   679: aaload
/*     */     //   680: ifnull +25 -> 705
/*     */     //   683: aload 13
/*     */     //   685: aload 12
/*     */     //   687: iload 14
/*     */     //   689: aaload
/*     */     //   690: invokevirtual 65	org/wso2/carbon/identity/oauth2/ResponseHeader:getKey	()Ljava/lang/String;
/*     */     //   693: aload 12
/*     */     //   695: iload 14
/*     */     //   697: aaload
/*     */     //   698: invokevirtual 66	org/wso2/carbon/identity/oauth2/ResponseHeader:getValue	()Ljava/lang/String;
/*     */     //   701: invokevirtual 62	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   704: pop
/*     */     //   705: iinc 14 1
/*     */     //   708: goto -41 -> 667
/*     */     //   711: aload 7
/*     */     //   713: ifnull +16 -> 729
/*     */     //   716: aload 13
/*     */     //   718: ldc 67
/*     */     //   720: ldc 68
/*     */     //   722: invokevirtual 62	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   725: pop
/*     */     //   726: goto +13 -> 739
/*     */     //   729: aload 13
/*     */     //   731: ldc 67
/*     */     //   733: ldc 69
/*     */     //   735: invokevirtual 62	javax/ws/rs/core/Response$ResponseBuilder:header	(Ljava/lang/String;Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   738: pop
/*     */     //   739: aload 13
/*     */     //   741: aload 11
/*     */     //   743: invokevirtual 70	org/apache/amber/oauth2/common/message/OAuthResponse:getBody	()Ljava/lang/String;
/*     */     //   746: invokevirtual 71	javax/ws/rs/core/Response$ResponseBuilder:entity	(Ljava/lang/Object;)Ljavax/ws/rs/core/Response$ResponseBuilder;
/*     */     //   749: invokevirtual 72	javax/ws/rs/core/Response$ResponseBuilder:build	()Ljavax/ws/rs/core/Response;
/*     */     //   752: astore 14
/*     */     //   754: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   757: aload 14
/*     */     //   759: areturn
/*     */     //   760: astore 9
/*     */     //   762: aload_0
/*     */     //   763: aload 7
/*     */     //   765: aload 9
/*     */     //   767: invokespecial 73	org/wso2/carbon/identity/oauth/endpoint/revoke/OAuthRevocationEndpoint:handleServerFailure	(Ljava/lang/String;Ljava/lang/Exception;)Ljavax/ws/rs/core/Response;
/*     */     //   770: astore 10
/*     */     //   772: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   775: aload 10
/*     */     //   777: areturn
/*     */     //   778: astore 15
/*     */     //   780: invokestatic 32	org/wso2/carbon/context/PrivilegedCarbonContext:endTenantFlow	()V
/*     */     //   783: aload 15
/*     */     //   785: athrow
/*     */     // Line number table:
/*     */     //   Java source line #63	-> byte code offset #0
/*     */     //   Java source line #64	-> byte code offset #3
/*     */     //   Java source line #66	-> byte code offset #7
/*     */     //   Java source line #67	-> byte code offset #14
/*     */     //   Java source line #69	-> byte code offset #20
/*     */     //   Java source line #71	-> byte code offset #31
/*     */     //   Java source line #72	-> byte code offset #42
/*     */     //   Java source line #74	-> byte code offset #48
/*     */     //   Java source line #75	-> byte code offset #57
/*     */     //   Java source line #76	-> byte code offset #72
/*     */     //   Java source line #77	-> byte code offset #92
/*     */     //   Java source line #80	-> byte code offset #114
/*     */     //   Java source line #81	-> byte code offset #123
/*     */     //   Java source line #82	-> byte code offset #138
/*     */     //   Java source line #84	-> byte code offset #168
/*     */     //   Java source line #87	-> byte code offset #190
/*     */     //   Java source line #88	-> byte code offset #202
/*     */     //   Java source line #89	-> byte code offset #207
/*     */     //   Java source line #90	-> byte code offset #237
/*     */     //   Java source line #96	-> byte code offset #262
/*     */     //   Java source line #97	-> byte code offset #265
/*     */     //   Java source line #101	-> byte code offset #276
/*     */     //   Java source line #106	-> byte code offset #289
/*     */     //   Java source line #108	-> byte code offset #311
/*     */     //   Java source line #179	-> byte code offset #319
/*     */     //   Java source line #112	-> byte code offset #325
/*     */     //   Java source line #113	-> byte code offset #337
/*     */     //   Java source line #118	-> byte code offset #349
/*     */     //   Java source line #115	-> byte code offset #352
/*     */     //   Java source line #117	-> byte code offset #354
/*     */     //   Java source line #179	-> byte code offset #362
/*     */     //   Java source line #122	-> byte code offset #368
/*     */     //   Java source line #123	-> byte code offset #377
/*     */     //   Java source line #124	-> byte code offset #402
/*     */     //   Java source line #125	-> byte code offset #427
/*     */     //   Java source line #126	-> byte code offset #442
/*     */     //   Java source line #128	-> byte code offset #452
/*     */     //   Java source line #130	-> byte code offset #459
/*     */     //   Java source line #131	-> byte code offset #474
/*     */     //   Java source line #133	-> byte code offset #481
/*     */     //   Java source line #135	-> byte code offset #489
/*     */     //   Java source line #137	-> byte code offset #497
/*     */     //   Java source line #138	-> byte code offset #510
/*     */     //   Java source line #179	-> byte code offset #518
/*     */     //   Java source line #139	-> byte code offset #524
/*     */     //   Java source line #140	-> byte code offset #537
/*     */     //   Java source line #179	-> byte code offset #545
/*     */     //   Java source line #143	-> byte code offset #551
/*     */     //   Java source line #179	-> byte code offset #561
/*     */     //   Java source line #146	-> byte code offset #567
/*     */     //   Java source line #147	-> byte code offset #572
/*     */     //   Java source line #148	-> byte code offset #583
/*     */     //   Java source line #150	-> byte code offset #611
/*     */     //   Java source line #152	-> byte code offset #622
/*     */     //   Java source line #153	-> byte code offset #629
/*     */     //   Java source line #159	-> byte code offset #653
/*     */     //   Java source line #160	-> byte code offset #664
/*     */     //   Java source line #161	-> byte code offset #675
/*     */     //   Java source line #162	-> byte code offset #683
/*     */     //   Java source line #160	-> byte code offset #705
/*     */     //   Java source line #166	-> byte code offset #711
/*     */     //   Java source line #167	-> byte code offset #716
/*     */     //   Java source line #169	-> byte code offset #729
/*     */     //   Java source line #172	-> byte code offset #739
/*     */     //   Java source line #179	-> byte code offset #754
/*     */     //   Java source line #175	-> byte code offset #760
/*     */     //   Java source line #176	-> byte code offset #762
/*     */     //   Java source line #179	-> byte code offset #772
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	786	0	this	OAuthRevocationEndpoint
/*     */     //   0	786	1	request	HttpServletRequest
/*     */     //   0	786	2	paramMap	javax.ws.rs.core.MultivaluedMap<String, String>
/*     */     //   6	9	3	carbonContext	org.wso2.carbon.context.PrivilegedCarbonContext
/*     */     //   29	162	4	httpRequest	javax.servlet.http.HttpServletRequestWrapper
/*     */     //   55	390	5	token	String
/*     */     //   121	356	6	token_type	String
/*     */     //   200	564	7	callback	String
/*     */     //   263	3	8	basicAuthUsed	boolean
/*     */     //   287	54	9	clientCredentials	String[]
/*     */     //   352	3	9	e	OAuthClientException
/*     */     //   375	108	9	revokeRequest	OAuthRevocationRequestDTO
/*     */     //   760	6	9	e	OAuthClientException
/*     */     //   317	49	10	localResponse1	Response
/*     */     //   487	289	10	oauthRevokeResp	Object
/*     */     //   516	49	11	localResponse2	Response
/*     */     //   581	3	11	response	OAuthResponse
/*     */     //   620	122	11	response	OAuthResponse
/*     */     //   627	67	12	headers	org.wso2.carbon.identity.oauth2.ResponseHeader[]
/*     */     //   651	89	13	respBuilder	Response.ResponseBuilder
/*     */     //   665	93	14	i	int
/*     */     //   778	6	15	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   276	319	352	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   325	349	352	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   368	518	760	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   524	545	760	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   551	561	760	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   567	754	760	org/wso2/carbon/identity/oauth/common/exception/OAuthClientException
/*     */     //   0	319	778	finally
/*     */     //   325	362	778	finally
/*     */     //   368	518	778	finally
/*     */     //   524	545	778	finally
/*     */     //   551	561	778	finally
/*     */     //   567	754	778	finally
/*     */     //   760	772	778	finally
/*     */     //   778	780	778	finally
/*     */   }
/*     */   
/*     */   private Response handleBasicAuthFailure(String callback)
/*     */     throws OAuthSystemException
/*     */   {
/* 186 */     if ((callback == null) || (callback.equals(""))) {
/* 187 */       OAuthResponse response = OAuthASResponse.errorResponse(401).setError("invalid_client").setErrorDescription("Client Authentication failed.").buildJSONMessage();
/*     */       
/*     */ 
/* 190 */       return Response.status(response.getResponseStatus()).header("WWW-Authenticate", EndpointUtil.getRealmInfo()).header("Content-Type", "text/html").entity(response.getBody()).build();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 195 */     OAuthResponse response = OAuthASResponse.errorResponse(401).setError("invalid_client").buildJSONMessage();
/*     */     
/* 197 */     return Response.status(response.getResponseStatus()).header("WWW-Authenticate", EndpointUtil.getRealmInfo()).header("Content-Type", "application/javascript").entity(callback + "(" + response.getBody() + ");").build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Response handleAuthorizationFailure(String callback)
/*     */     throws OAuthSystemException
/*     */   {
/* 206 */     if ((callback == null) || (callback.equals(""))) {
/* 207 */       OAuthResponse response = OAuthASResponse.errorResponse(401).setError("unauthorized_client").setErrorDescription("Client Authentication failed.").buildJSONMessage();
/*     */       
/*     */ 
/* 210 */       return Response.status(response.getResponseStatus()).header("WWW-Authenticate", EndpointUtil.getRealmInfo()).header("Content-Type", "text/html").entity(response.getBody()).build();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 215 */     OAuthResponse response = OAuthASResponse.errorResponse(401).setError("unauthorized_client").buildJSONMessage();
/*     */     
/* 217 */     return Response.status(response.getResponseStatus()).header("WWW-Authenticate", EndpointUtil.getRealmInfo()).header("Content-Type", "application/javascript").entity(callback + "(" + response.getBody() + ");").build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Response handleServerFailure(String callback, Exception e)
/*     */     throws OAuthSystemException
/*     */   {
/* 226 */     if ((callback == null) || (callback.equals(""))) {
/* 227 */       OAuthResponse response = OAuthASResponse.errorResponse(500).setError("server_error").setErrorDescription(e.getMessage()).buildJSONMessage();
/*     */       
/*     */ 
/* 230 */       return Response.status(response.getResponseStatus()).header("Content-Type", "text/html").entity(response.getBody()).build();
/*     */     }
/*     */     
/*     */ 
/* 234 */     OAuthResponse response = OAuthASResponse.errorResponse(500).setError("server_error").buildJSONMessage();
/*     */     
/* 236 */     return Response.status(response.getResponseStatus()).header("Content-Type", "application/javascript").entity(callback + "(" + response.getBody() + ");").build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Response handleClientFailure(String callback)
/*     */     throws OAuthSystemException
/*     */   {
/* 244 */     if ((callback == null) || (callback.equals(""))) {
/* 245 */       OAuthResponse response = OAuthASResponse.errorResponse(400).setError("invalid_request").setErrorDescription("Invalid revocation request").buildJSONMessage();
/*     */       
/*     */ 
/* 248 */       return Response.status(response.getResponseStatus()).header("Content-Type", "text/html").entity(response.getBody()).build();
/*     */     }
/*     */     
/*     */ 
/* 252 */     OAuthResponse response = OAuthASResponse.errorResponse(400).setError("invalid_request").buildJSONMessage();
/*     */     
/* 254 */     return Response.status(response.getResponseStatus()).header("Content-Type", "application/javascript").entity(callback + "(" + response.getBody() + ");").build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Response handleClientFailure(String callback, OAuthRevocationResponseDTO dto)
/*     */     throws OAuthSystemException
/*     */   {
/* 262 */     if ((callback == null) || (callback.equals(""))) {
/* 263 */       OAuthResponse response = OAuthASResponse.errorResponse(400).setError(dto.getErrorCode()).setErrorDescription(dto.getErrorMsg()).buildJSONMessage();
/*     */       
/*     */ 
/* 266 */       return Response.status(response.getResponseStatus()).header("Content-Type", "text/html").entity(response.getBody()).build();
/*     */     }
/*     */     
/*     */ 
/* 270 */     OAuthResponse response = OAuthASResponse.errorResponse(400).setError(dto.getErrorCode()).buildJSONMessage();
/*     */     
/* 272 */     return Response.status(response.getResponseStatus()).header("Content-Type", "application/javascript").entity(callback + "(" + response.getBody() + ");").build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void logAccessTokenRevocationRequest(HttpServletRequest request)
/*     */   {
/* 279 */     log.debug("Received a access token revocation request : " + request.getRequestURI());
/*     */     
/* 281 */     log.debug("----------logging request headers.----------");
/* 282 */     Enumeration headerNames = request.getHeaderNames();
/* 283 */     while (headerNames.hasMoreElements()) {
/* 284 */       String headerName = (String)headerNames.nextElement();
/* 285 */       Enumeration headers = request.getHeaders(headerName);
/* 286 */       while (headers.hasMoreElements()) {
/* 287 */         log.debug(headerName + " : " + headers.nextElement());
/*     */       }
/*     */     }
/*     */     
/* 291 */     log.debug("----------logging request parameters.----------");
/* 292 */     log.debug("token - " + request.getParameter("token"));
/*     */   }
/*     */   
/*     */   private OAuthRevocationResponseDTO revokeTokens(OAuthRevocationRequestDTO oauthRequest)
/*     */     throws OAuthClientException
/*     */   {
/* 298 */     OAuthRevocationRequestDTO revokeReqDTO = new OAuthRevocationRequestDTO();
/*     */     
/* 300 */     revokeReqDTO.setConsumerKey(oauthRequest.getConsumerKey());
/* 301 */     revokeReqDTO.setConsumerSecret(oauthRequest.getConsumerSecret());
/* 302 */     revokeReqDTO.setToken(oauthRequest.getToken());
/* 303 */     revokeReqDTO.setToken_type(oauthRequest.getToken_type());
/*     */     
/* 305 */     return EndpointUtil.getOAuth2Service().revokeTokenByOAuthClient(revokeReqDTO);
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\revoke\OAuthRevocationEndpoint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */