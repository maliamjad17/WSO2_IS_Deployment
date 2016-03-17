/*    */ package org.wso2.carbon.identity.scim.provider.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.ws.rs.core.Response;
/*    */ import javax.ws.rs.core.Response.ResponseBuilder;
/*    */ import org.wso2.charon.core.protocol.SCIMResponse;
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
/*    */ public class JAXRSResponseBuilder
/*    */ {
/*    */   public Response buildResponse(SCIMResponse scimResponse)
/*    */   {
/* 28 */     Response.ResponseBuilder responseBuilder = Response.status(scimResponse.getResponseCode());
/*    */     
/* 30 */     Map<String, String> httpHeaders = scimResponse.getHeaderParameterMap();
/* 31 */     if ((httpHeaders != null) && (httpHeaders.size() != 0)) {
/* 32 */       for (Map.Entry<String, String> entry : httpHeaders.entrySet())
/*    */       {
/* 34 */         responseBuilder.header((String)entry.getKey(), entry.getValue());
/*    */       }
/*    */     }
/*    */     
/* 38 */     if (scimResponse.getResponseMessage() != null) {
/* 39 */       responseBuilder.entity(scimResponse.getResponseMessage());
/*    */     }
/* 41 */     return responseBuilder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\util\JAXRSResponseBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */