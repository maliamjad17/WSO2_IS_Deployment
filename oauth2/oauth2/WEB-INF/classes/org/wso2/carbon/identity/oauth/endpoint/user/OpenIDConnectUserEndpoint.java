/*     */ package org.wso2.carbon.identity.oauth.endpoint.user;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.core.Context;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import org.apache.amber.oauth2.as.response.OAuthASResponse;
/*     */ import org.apache.amber.oauth2.common.exception.OAuthSystemException;
/*     */ import org.apache.amber.oauth2.common.message.OAuthResponse;
/*     */ import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.wso2.carbon.identity.oauth.endpoint.user.impl.UserInfoEndpointConfig;
/*     */ import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO;
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
/*     */ @Path("/userinfo")
/*     */ public class OpenIDConnectUserEndpoint
/*     */ {
/*  43 */   private static Log log = LogFactory.getLog(OpenIDConnectUserEndpoint.class);
/*     */   
/*     */   @GET
/*     */   @Path("/")
/*     */   @Produces({"application/json"})
/*     */   public Response getUserClaims(@Context HttpServletRequest request) throws OAuthSystemException
/*     */   {
/*  50 */     String response = null;
/*     */     try
/*     */     {
/*  53 */       UserInfoRequestValidator requestValidator = UserInfoEndpointConfig.getInstance().getUserInfoRequestValidator();
/*  54 */       String accessToken = requestValidator.validateRequest(request);
/*     */       
/*  56 */       UserInfoAccessTokenValidator tokenValidator = UserInfoEndpointConfig.getInstance().getUserInfoAccessTokenValidator();
/*  57 */       OAuth2TokenValidationResponseDTO tokenResponse = tokenValidator.validateToken(accessToken);
/*     */       
/*     */ 
/*  60 */       UserInfoResponseBuilder userInfoResponseBuilder = UserInfoEndpointConfig.getInstance().getUserInfoResponseBuilder();
/*  61 */       response = userInfoResponseBuilder.getResponseString(tokenResponse);
/*     */     }
/*     */     catch (UserInfoEndpointException e) {
/*  64 */       return handleError(e);
/*     */     } catch (OAuthSystemException e) {
/*  66 */       log.error("UserInfoEndpoint Failed", e);
/*  67 */       throw new OAuthSystemException("UserInfoEndpoint Failed");
/*     */     }
/*     */     
/*  70 */     Response.ResponseBuilder respBuilder = Response.status(200).header("Cache-Control", "no-store").header("Pragma", "no-cache");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */     return respBuilder.entity(response).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Response handleError(UserInfoEndpointException e)
/*     */     throws OAuthSystemException
/*     */   {
/*  88 */     log.debug(e);
/*  89 */     OAuthResponse res = null;
/*     */     try {
/*  91 */       res = OAuthASResponse.errorResponse(400).setError(e.getErrorCode()).setErrorDescription(e.getErrorMessage()).buildJSONMessage();
/*     */ 
/*     */     }
/*     */     catch (OAuthSystemException e1)
/*     */     {
/*  96 */       OAuthResponse response = OAuthASResponse.errorResponse(500).setError("server_error").setErrorDescription(e1.getMessage()).buildJSONMessage();
/*     */       
/*     */ 
/*     */ 
/* 100 */       return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
/*     */     }
/* 102 */     return Response.status(res.getResponseStatus()).entity(res.getBody()).build();
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\OpenIDConnectUserEndpoint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */