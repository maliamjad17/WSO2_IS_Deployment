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
/*     */ import org.wso2.carbon.utils.multitenancy.MultitenantUtils;
/*     */ import org.wso2.charon.core.encoder.Encoder;
/*     */ import org.wso2.charon.core.exceptions.BadRequestException;
/*     */ import org.wso2.charon.core.exceptions.CharonException;
/*     */ import org.wso2.charon.core.exceptions.FormatNotSupportedException;
/*     */ import org.wso2.charon.core.extensions.UserManager;
/*     */ import org.wso2.charon.core.protocol.SCIMResponse;
/*     */ import org.wso2.charon.core.protocol.endpoints.AbstractResourceEndpoint;
/*     */ import org.wso2.charon.core.protocol.endpoints.UserResourceEndpoint;
/*     */ import org.wso2.charon.core.schema.SCIMConstants;
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
/*     */ @Path("/")
/*     */ public class UserResource
/*     */   extends AbstractResource
/*     */ {
/*  53 */   private static Log logger = LogFactory.getLog(UserResource.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @GET
/*     */   @Path("{id}")
/*     */   @Produces({"application/json"})
/*     */   public Response getUser(@PathParam("id") String id, @HeaderParam("Accept") String format, @HeaderParam("Auth_Type") String authMechanism, @HeaderParam("Authorization") String authorization)
/*     */   {
/*  63 */     Encoder encoder = null;
/*     */     try {
/*  65 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/*  68 */       format = identifyOutputFormat(format);
/*     */       
/*  70 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(format));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/*  84 */       UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();
/*     */       
/*  86 */       SCIMResponse scimResponse = userResourceEndpoint.get(id, format, userManager);
/*     */       
/*     */ 
/*  89 */       return new JAXRSResponseBuilder().buildResponse(scimResponse);
/*     */     }
/*     */     catch (CharonException e) {
/*  92 */       if (logger.isDebugEnabled()) {
/*  93 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/*     */ 
/*  97 */       if (e.getCode() == -1) {
/*  98 */         e.setCode(500);
/*     */       }
/* 100 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 103 */       e.printStackTrace();
/* 104 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @POST
/*     */   public Response createUser(@HeaderParam("Content-Type") String inputFormat, @HeaderParam("Accept") String outputFormat, @HeaderParam("Authorization") String authorization, String resourceString)
/*     */   {
/* 115 */     Encoder encoder = null;
/*     */     try
/*     */     {
/* 118 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 121 */       if (inputFormat == null) {
/* 122 */         String error = "Content-Type not present in the request header";
/*     */         
/* 124 */         throw new FormatNotSupportedException(error);
/*     */       }
/*     */       
/* 127 */       inputFormat = identifyInputFormat(inputFormat);
/*     */       
/*     */ 
/*     */ 
/* 131 */       outputFormat = identifyOutputFormat(outputFormat);
/*     */       
/* 133 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(outputFormat));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 147 */       UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();
/*     */       
/* 149 */       SCIMResponse response = userResourceEndpoint.create(resourceString, inputFormat, outputFormat, userManager);
/*     */       
/*     */ 
/* 152 */       return new JAXRSResponseBuilder().buildResponse(response);
/*     */     }
/*     */     catch (CharonException e) {
/* 155 */       if (logger.isDebugEnabled()) {
/* 156 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/*     */ 
/* 160 */       if (e.getCode() == -1) {
/* 161 */         e.setCode(500);
/*     */       }
/* 163 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 166 */       if (logger.isDebugEnabled()) {
/* 167 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 169 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @DELETE
/*     */   @Path("{id}")
/*     */   public Response deleteUser(@PathParam("id") String id, @HeaderParam("Accept") String format, @HeaderParam("Authorization") String authorization)
/*     */   {
/* 179 */     Encoder encoder = null;
/*     */     try {
/* 181 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 184 */       if (format == null) {
/* 185 */         format = "application/json";
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 190 */       format = identifyOutputFormat(format);
/*     */       
/* 192 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(format));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 206 */       UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();
/*     */       
/* 208 */       SCIMResponse scimResponse = userResourceEndpoint.delete(id, userManager, format);
/*     */       
/*     */ 
/* 211 */       return new JAXRSResponseBuilder().buildResponse(scimResponse);
/*     */     }
/*     */     catch (CharonException e) {
/* 214 */       if (logger.isDebugEnabled()) {
/* 215 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/*     */ 
/* 219 */       if (e.getCode() == -1) {
/* 220 */         e.setCode(500);
/*     */       }
/* 222 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 225 */       if (logger.isDebugEnabled()) {
/* 226 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 228 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GET
/*     */   @Produces({"application/json"})
/*     */   public Response getUser(@HeaderParam("Accept") String format, @HeaderParam("Authorization") String authorization, @QueryParam("attributes") String searchAttribute, @QueryParam("filter") String filter, @QueryParam("startIndex") String startIndex, @QueryParam("count") String count, @QueryParam("sortBy") String sortBy, @QueryParam("sortOrder") String sortOrder)
/*     */   {
/* 240 */     Encoder encoder = null;
/*     */     try {
/* 242 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 245 */       format = identifyOutputFormat(format);
/*     */       
/* 247 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(format));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 257 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 261 */       UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();
/* 262 */       SCIMResponse scimResponse = null;
/* 263 */       if (searchAttribute != null) {
/* 264 */         scimResponse = userResourceEndpoint.listByAttribute(searchAttribute, userManager, format);
/*     */       }
/* 266 */       else if (filter != null) {
/* 267 */         scimResponse = userResourceEndpoint.listByFilter(filter, userManager, format);
/* 268 */       } else if ((startIndex != null) && (count != null)) {
/* 269 */         scimResponse = userResourceEndpoint.listWithPagination(Integer.valueOf(startIndex).intValue(), Integer.valueOf(count).intValue(), userManager, format);
/*     */       }
/* 271 */       else if (sortBy != null) {
/* 272 */         scimResponse = userResourceEndpoint.listBySort(sortBy, sortOrder, userManager, format);
/*     */       }
/* 274 */       else if ((searchAttribute == null) && (filter == null) && (startIndex == null) && (count == null) && (sortBy == null))
/*     */       {
/* 276 */         scimResponse = userResourceEndpoint.list(userManager, format);
/*     */       }
/*     */       else {
/* 279 */         throw new BadRequestException("GET request does not support the requested URL query parameter combination.");
/*     */       }
/*     */       
/* 282 */       return new JAXRSResponseBuilder().buildResponse(scimResponse);
/*     */     }
/*     */     catch (CharonException e) {
/* 285 */       if (logger.isDebugEnabled()) {
/* 286 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/*     */ 
/* 290 */       if (e.getCode() == -1) {
/* 291 */         e.setCode(500);
/*     */       }
/* 293 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 296 */       if (logger.isDebugEnabled()) {
/* 297 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 299 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (BadRequestException e) {
/* 302 */       if (logger.isDebugEnabled()) {
/* 303 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 305 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @PUT
/*     */   @Path("{id}")
/*     */   public Response updateUser(@PathParam("id") String id, @HeaderParam("Content-Type") String inputFormat, @HeaderParam("Accept") String outputFormat, @HeaderParam("Authorization") String authorization, String resourceString)
/*     */   {
/* 317 */     Encoder encoder = null;
/*     */     try
/*     */     {
/* 320 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/*     */       
/*     */ 
/* 323 */       if (inputFormat == null) {
/* 324 */         String error = "Content-Type not present in the request header";
/*     */         
/* 326 */         throw new FormatNotSupportedException(error);
/*     */       }
/*     */       
/* 329 */       inputFormat = identifyInputFormat(inputFormat);
/*     */       
/*     */ 
/*     */ 
/* 333 */       outputFormat = identifyOutputFormat(outputFormat);
/*     */       
/* 335 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(outputFormat));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 345 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization);
/*     */       
/*     */ 
/*     */ 
/* 349 */       UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();
/*     */       
/* 351 */       SCIMResponse response = userResourceEndpoint.updateWithPUT(id, resourceString, inputFormat, outputFormat, userManager);
/*     */       
/*     */ 
/* 354 */       return new JAXRSResponseBuilder().buildResponse(response);
/*     */     }
/*     */     catch (CharonException e) {
/* 357 */       if (logger.isDebugEnabled()) {
/* 358 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/*     */ 
/* 362 */       if (e.getCode() == -1) {
/* 363 */         e.setCode(500);
/*     */       }
/* 365 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 368 */       if (logger.isDebugEnabled()) {
/* 369 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 371 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GET
/*     */   @Path("/me")
/*     */   @Produces({"application/json"})
/*     */   public Response getAuthorizedUser(@HeaderParam("Accept") String format, @HeaderParam("Authorization") String authorization)
/*     */   {
/* 382 */     Encoder encoder = null;
/*     */     try {
/* 384 */       IdentitySCIMManager identitySCIMManager = IdentitySCIMManager.getInstance();
/* 385 */       String filter = "userNameEq" + MultitenantUtils.getTenantAwareUsername(authorization);
/*     */       
/*     */ 
/* 388 */       format = identifyOutputFormat(format);
/*     */       
/* 390 */       encoder = identitySCIMManager.getEncoder(SCIMConstants.identifyFormat(format));
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
/* 401 */       String SCIM_LIST_USER_PERMISSION = "/permission/admin/login";
/* 402 */       UserManager userManager = IdentitySCIMManager.getInstance().getUserManager(authorization, SCIM_LIST_USER_PERMISSION);
/*     */       
/*     */ 
/*     */ 
/* 406 */       UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();
/* 407 */       SCIMResponse scimResponse = null;
/* 408 */       scimResponse = userResourceEndpoint.listByFilter(filter, userManager, format);
/*     */       
/*     */ 
/* 411 */       return new JAXRSResponseBuilder().buildResponse(scimResponse);
/*     */     }
/*     */     catch (CharonException e) {
/* 414 */       if (logger.isDebugEnabled()) {
/* 415 */         logger.debug(e.getMessage(), e);
/*     */       }
/*     */       
/*     */ 
/* 419 */       if (e.getCode() == -1) {
/* 420 */         e.setCode(500);
/*     */       }
/* 422 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */     catch (FormatNotSupportedException e) {
/* 425 */       if (logger.isDebugEnabled()) {
/* 426 */         logger.debug(e.getMessage(), e);
/*     */       }
/* 428 */       return new JAXRSResponseBuilder().buildResponse(AbstractResourceEndpoint.encodeSCIMException(encoder, e));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\resources\UserResource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */