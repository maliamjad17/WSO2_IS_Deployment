/*     */ package org.wso2.carbon.identity.scim.provider.resources;
/*     */ 
/*     */ import javax.ws.rs.DELETE;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.HeaderParam;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.PUT;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.PathParam;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.QueryParam;
/*     */ import javax.ws.rs.core.Response;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.wso2.carbon.identity.scim.provider.impl.IdentitySCIMManager;
/*     */ import org.wso2.carbon.identity.scim.provider.util.JAXRSResponseBuilder;
/*     */ import org.wso2.charon.core.encoder.Encoder;
/*     */ import org.wso2.charon.core.exceptions.BadRequestException;
/*     */ import org.wso2.charon.core.exceptions.CharonException;
/*     */ import org.wso2.charon.core.exceptions.FormatNotSupportedException;
/*     */ import org.wso2.charon.core.extensions.UserManager;
/*     */ import org.wso2.charon.core.protocol.SCIMResponse;
/*     */ import org.wso2.charon.core.protocol.endpoints.AbstractResourceEndpoint;
/*     */ import org.wso2.charon.core.protocol.endpoints.GroupResourceEndpoint;
/*     */ import org.wso2.charon.core.schema.SCIMConstants;
/*     */ import org.wso2.identity.jaxrs.designator.PATCH;
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
/*     */ @Path("/")
/*     */ public class GroupResource
/*     */   extends AbstractResource
/*     */ {
/*  50 */   private static Log logger = LogFactory.getLog(GroupResource.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GET
/*     */   @Path("{id}")
/*     */   @Produces({"application/json"})
/*     */   public Response getGroup(@PathParam("id") String id, @HeaderParam("Accept") String format, @HeaderParam("Auth_Type") String authMechanism, @HeaderParam("Authorization") String authorization)
/*     */   {
/*  60 */     Encoder encoder = null;
/*     */     try {
/*  62 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/*  65 */       format = identifyOutputFormat(format);
/*     */       
/*  67 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(format));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/*  80 */       GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();
/*     */       
/*  82 */       SCIMResponse scimResponse = groupResourceEndpoint.get(id, format, userManager);
/*     */       
/*     */ 
/*  85 */       return new JAXRSResponseBuilder().buildResponse(scimResponse);
/*     */     }
/*     */     catch (CharonException e) {
/*  88 */       if (logger.isDebugEnabled()) {
/*  89 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/*  92 */       if (e.getCode() == -1) {
/*  93 */         e.setCode(500);
/*     */       }
/*  95 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/*  98 */       if (logger.isDebugEnabled()) {
/*  99 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 101 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @POST
/*     */   public Response createGroup(@HeaderParam("Content-Type") String inputFormat, @HeaderParam("Accept") String outputFormat, @HeaderParam("Authorization") String authorization, String resourceString)
/*     */   {
/* 112 */     Encoder encoder = null;
/*     */     try
/*     */     {
/* 115 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 118 */       if (inputFormat == null) {
/* 119 */         String error = "Content-Type not present in the request header";
/* 120 */         throw new FormatNotSupportedException(error);
/*     */       }
/*     */       
/* 123 */       inputFormat = identifyInputFormat(inputFormat);
/*     */       
/*     */ 
/* 126 */       outputFormat = identifyOutputFormat(outputFormat);
/*     */       
/* 128 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(outputFormat));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 140 */       GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();
/*     */       
/* 142 */       SCIMResponse response = groupResourceEndpoint.create(resourceString, inputFormat, outputFormat, userManager);
/*     */       
/*     */ 
/* 145 */       return new JAXRSResponseBuilder().buildResponse(response);
/*     */     }
/*     */     catch (CharonException e) {
/* 148 */       if (logger.isDebugEnabled()) {
/* 149 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/* 152 */       if (e.getCode() == -1) {
/* 153 */         e.setCode(500);
/*     */       }
/* 155 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 158 */       if (logger.isDebugEnabled()) {
/* 159 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 161 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @DELETE
/*     */   @Path("{id}")
/*     */   public Response deleteGroup(@PathParam("id") String id, @HeaderParam("Accept") String format, @HeaderParam("Authorization") String authorization)
/*     */   {
/* 171 */     Encoder encoder = null;
/*     */     try {
/* 173 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 176 */       if (format == null) {
/* 177 */         format = "application/json";
/*     */       }
/*     */       
/*     */ 
/* 181 */       format = identifyOutputFormat(format);
/*     */       
/* 183 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(format));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 191 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 195 */       GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();
/*     */       
/* 197 */       SCIMResponse scimResponse = groupResourceEndpoint.delete(id, userManager, format);
/*     */       
/*     */ 
/* 200 */       return new JAXRSResponseBuilder().buildResponse(scimResponse);
/*     */     }
/*     */     catch (CharonException e) {
/* 203 */       if (logger.isDebugEnabled()) {
/* 204 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/* 207 */       if (e.getCode() == -1) {
/* 208 */         e.setCode(500);
/*     */       }
/* 210 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 213 */       e.printStackTrace();
/* 214 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GET
/*     */   public Response getGroup(@HeaderParam("Accept") String format, @HeaderParam("Authorization") String authorization, @QueryParam("attributes") String searchAttribute, @QueryParam("filter") String filter, @QueryParam("startIndex") String startIndex, @QueryParam("count") String count, @QueryParam("sortBy") String sortBy, @QueryParam("sortOrder") String sortOrder)
/*     */   {
/* 227 */     Encoder encoder = null;
/*     */     try {
/* 229 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 232 */       format = identifyOutputFormat(format);
/*     */       
/* 234 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(format));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 242 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 246 */       GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();
/* 247 */       SCIMResponse scimResponse = null;
/* 248 */       if (searchAttribute != null) {
/* 249 */         scimResponse = groupResourceEndpoint.listByAttribute(searchAttribute, userManager, format);
/* 250 */       } else if (filter != null) {
/* 251 */         scimResponse = groupResourceEndpoint.listByFilter(filter, userManager, format);
/* 252 */       } else if ((startIndex != null) && (count != null)) {
/* 253 */         scimResponse = groupResourceEndpoint.listWithPagination(Integer.valueOf(startIndex).intValue(), Integer.valueOf(count).intValue(), userManager, format);
/*     */ 
/*     */       }
/* 256 */       else if (sortBy != null) {
/* 257 */         scimResponse = groupResourceEndpoint.listBySort(sortBy, sortOrder, userManager, format);
/* 258 */       } else if ((searchAttribute == null) && (filter == null) && (startIndex == null) && (count == null) && (sortBy == null))
/*     */       {
/* 260 */         scimResponse = groupResourceEndpoint.list(userManager, format);
/*     */       }
/*     */       else {
/* 263 */         throw new BadRequestException("GET request does not support the requested URL query parameter combination.");
/*     */       }
/*     */       
/* 266 */       return new JAXRSResponseBuilder().buildResponse(scimResponse);
/*     */     }
/*     */     catch (CharonException e) {
/* 269 */       if (logger.isDebugEnabled()) {
/* 270 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/* 273 */       if (e.getCode() == -1) {
/* 274 */         e.setCode(500);
/*     */       }
/* 276 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 279 */       if (logger.isDebugEnabled()) {
/* 280 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 282 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (BadRequestException e) {
/* 285 */       if (logger.isDebugEnabled()) {
/* 286 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 288 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @PUT
/*     */   @Path("{id}")
/*     */   public Response updateGroup(@PathParam("id") String id, @HeaderParam("Content-Type") String inputFormat, @HeaderParam("Accept") String outputFormat, @HeaderParam("Authorization") String authorization, String resourceString)
/*     */   {
/* 300 */     Encoder encoder = null;
/*     */     try
/*     */     {
/* 303 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 306 */       if (inputFormat == null) {
/* 307 */         String error = "Content-Type not present in the request header";
/* 308 */         throw new FormatNotSupportedException(error);
/*     */       }
/*     */       
/* 311 */       inputFormat = identifyInputFormat(inputFormat);
/*     */       
/*     */ 
/* 314 */       outputFormat = identifyOutputFormat(outputFormat);
/*     */       
/* 316 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(outputFormat));
/*     */       
/*     */ 
/* 319 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 323 */       GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();
/*     */       
/* 325 */       SCIMResponse response = groupResourceEndpoint.updateWithPUT(id, resourceString, inputFormat, outputFormat, userManager);
/*     */       
/*     */ 
/* 328 */       return new JAXRSResponseBuilder().buildResponse(response);
/*     */     }
/*     */     catch (CharonException e) {
/* 331 */       if (logger.isDebugEnabled()) {
/* 332 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/* 335 */       if (e.getCode() == -1) {
/* 336 */         e.setCode(500);
/*     */       }
/* 338 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 341 */       if (logger.isDebugEnabled()) {
/* 342 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 344 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @PATCH
/*     */   @Path("{id}")
/*     */   public Response patchGroup(@PathParam("id") String id, @HeaderParam("Content-Type") String inputFormat, @HeaderParam("Accept") String outputFormat, @HeaderParam("Authorization") String authorization, String resourceString)
/*     */   {
/* 356 */     Encoder encoder = null;
/*     */     try
/*     */     {
/* 359 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 362 */       if (inputFormat == null) {
/* 363 */         String error = "Content-Type not present in the request header";
/* 364 */         throw new FormatNotSupportedException(error);
/*     */       }
/*     */       
/* 367 */       inputFormat = identifyInputFormat(inputFormat);
/*     */       
/*     */ 
/* 370 */       outputFormat = identifyOutputFormat(outputFormat);
/*     */       
/* 372 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(outputFormat));
/*     */       
/*     */ 
/* 375 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 379 */       GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();
/*     */       
/* 381 */       SCIMResponse response = groupResourceEndpoint.updateWithPATCH(id, resourceString, inputFormat, outputFormat, userManager);
/*     */       
/*     */ 
/* 384 */       return new JAXRSResponseBuilder().buildResponse(response);
/*     */     }
/*     */     catch (CharonException e) {
/* 387 */       if (logger.isDebugEnabled()) {
/* 388 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/* 391 */       if (e.getCode() == -1) {
/* 392 */         e.setCode(500);
/*     */       }
/* 394 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 397 */       if (logger.isDebugEnabled()) {
/* 398 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 400 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\resources\GroupResource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */