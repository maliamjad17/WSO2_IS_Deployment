/*    */ package org.wso2.carbon.identity.scim.provider.resources;
/*    */ 
/*    */ import javax.ws.rs.HeaderParam;
/*    */ import javax.ws.rs.POST;
/*    */ import javax.ws.rs.Path;
/*    */ import javax.ws.rs.core.Response;
/*    */ import org.wso2.carbon.identity.scim.provider.impl.IdentitySCIMManager;
/*    */ import org.wso2.carbon.identity.scim.provider.util.JAXRSResponseBuilder;
/*    */ import org.wso2.charon.core.encoder.Encoder;
/*    */ import org.wso2.charon.core.exceptions.CharonException;
/*    */ import org.wso2.charon.core.exceptions.FormatNotSupportedException;
/*    */ import org.wso2.charon.core.extensions.UserManager;
/*    */ import org.wso2.charon.core.protocol.SCIMResponse;
/*    */ import org.wso2.charon.core.protocol.endpoints.AbstractResourceEndpoint;
/*    */ import org.wso2.charon.core.protocol.endpoints.BulkResourceEndpoint;
/*    */ import org.wso2.charon.core.schema.SCIMConstants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Path("/")
/*    */ public class BulkResource
/*    */ {
/*    */   @POST
/*    */   public Response createUser(@HeaderParam("Content-Type") String inputFormat, @HeaderParam("Accept") String outputFormat, @HeaderParam("Authorization") String authorization, String resourceString)
/*    */   {
/* 44 */     Encoder encoder = null;
/*    */     try
/*    */     {
/* 47 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*    */       
/*    */ 
/* 50 */       if (inputFormat == null) {
/* 51 */         String error = "Content-Type not present in the request header.";
/* 52 */         throw new FormatNotSupportedException(error);
/*    */       }
/*    */       
/*    */ 
/* 56 */       if (!outputFormat.equals("application/json")) {
/* 57 */         outputFormat = "application/json";
/*    */       }
/*    */       
/* 60 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(outputFormat));
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 68 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*    */       
/*    */ 
/* 71 */       BulkResourceEndpoint bulkResourceEndpoint = new BulkResourceEndpoint();
/* 72 */       SCIMResponse responseString = bulkResourceEndpoint.processBulkData(resourceString, inputFormat, outputFormat, userManager);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 78 */       return new JAXRSResponseBuilder().buildResponse(responseString);
/*    */     }
/*    */     catch (CharonException e)
/*    */     {
/* 82 */       if (e.getCode() == -1) {
/* 83 */         e.setCode(500);
/*    */       }
/* 85 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*    */     }
/*    */     catch (FormatNotSupportedException e) {
/* 88 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\resources\BulkResource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */