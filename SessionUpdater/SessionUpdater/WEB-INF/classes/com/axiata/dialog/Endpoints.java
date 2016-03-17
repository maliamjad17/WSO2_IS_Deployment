/*     */ package com.axiata.dialog;
/*     */ 
/*     */ import com.axiata.dialog.cryptosystem.AESencrp;
/*     */ import com.axiata.dialog.entity.LoginHistory;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.sql.SQLException;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.FormParam;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.QueryParam;
/*     */ import javax.ws.rs.core.Context;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import javax.ws.rs.core.UriInfo;
/*     */ import org.apache.axis2.AxisFault;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
/*     */ import org.wso2.carbon.identity.mgt.stub.UserIdentityManagementAdminServiceIdentityMgtServiceExceptionException;
/*     */ import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
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
/*     */ @Path("/endpoint")
/*     */ public class Endpoints
/*     */ {
/*     */   @Context
/*     */   private UriInfo context;
/*  59 */   String successResponse = "\"amountTransaction\"";
/*  60 */   String serviceException = "\"serviceException\"";
/*  61 */   String policyException = "\"policyException\"";
/*  62 */   String errorReturn = "\"errorreturn\"";
/*  63 */   private static Log log = LogFactory.getLog(Endpoints.class);
/*  64 */   private static Map<String, Integer> ussdNoOfAttempts = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @POST
/*     */   @Path("/ussd/receive")
/*     */   @Consumes({"application/json"})
/*     */   @Produces({"application/json"})
/*     */   public Response ussdReceive(String jsonBody)
/*     */     throws SQLException, JSONException, IOException
/*     */   {
/*  79 */     log.info("Json Body" + jsonBody);
/*  80 */     Gson gson = new GsonBuilder().serializeNulls().create();
/*  81 */     JSONObject jsonObj = new JSONObject(jsonBody);
/*  82 */     String message = jsonObj.getJSONObject("inboundUSSDMessageRequest").getString("inboundUSSDMessage");
/*  83 */     String sessionID = jsonObj.getJSONObject("inboundUSSDMessageRequest").getString("clientCorrelator");
/*  84 */     String msisdn = extractMsisdn(jsonObj);
/*     */     
/*  86 */     int responseCode = 400;
/*  87 */     String responseString = null;
/*     */     
/*  89 */     String status = null;
/*     */     
/*     */ 
/*  92 */     String ussdSessionID = null;
/*  93 */     if ((jsonObj.getJSONObject("inboundUSSDMessageRequest").has("sessionID")) && (!jsonObj.getJSONObject("inboundUSSDMessageRequest").isNull("sessionID"))) {
/*  94 */       ussdSessionID = jsonObj.getJSONObject("inboundUSSDMessageRequest").getString("sessionID");
/*  95 */       log.info("####### LOGS  ussdSessionID 01 : " + ussdSessionID);
/*     */     }
/*  97 */     ussdSessionID = ussdSessionID != null ? ussdSessionID : "";
/*  98 */     log.info("####### LOGS  ussdSessionID 02 : " + ussdSessionID);
/*     */     
/*     */ 
/*     */ 
/* 102 */     if (message.equals("1")) {
/* 103 */       status = "Approved";
/* 104 */       responseCode = 201;
/* 105 */       DatabaseUtils.updateStatus(sessionID, status);
/* 106 */     } else if (message.equals("2")) {
/* 107 */       status = "Rejected";
/* 108 */       responseCode = 201;
/* 109 */       DatabaseUtils.updateStatus(sessionID, status);
/*     */     } else {
/* 111 */       responseString = validateUSSDResponse(message, msisdn, sessionID, ussdSessionID);
/* 112 */       if (responseString == null) {
/* 113 */         responseCode = 400;
/* 114 */         status = "Status not updated";
/* 115 */         ussdNoOfAttempts.remove(msisdn);
/* 116 */         responseString = SendUSSD.getUSSDJsonPayload(msisdn, sessionID, 3, "mtfin", ussdSessionID);
/* 117 */         return Response.status(responseCode).entity(responseString).build();
/*     */       }
/* 119 */       return Response.status(201).entity(responseString).build();
/*     */     }
/*     */     
/*     */ 
/* 123 */     if (responseCode == 400) {
/* 124 */       responseString = "{\"requestError\":{\"serviceException\":{\"messageId\":\"SVC0275\",\"text\":\"Internal server Error\"}}}";
/*     */     }
/*     */     else
/*     */     {
/* 128 */       responseString = SendUSSD.getUSSDJsonPayload(msisdn, sessionID, 5, "mtfin", ussdSessionID);
/*     */     }
/*     */     
/* 131 */     return Response.status(responseCode).entity(responseString).build();
/*     */   }
/*     */   
/*     */   private String validateUSSDResponse(String message, String msisdn, String sessionID, String ussdSessionID)
/*     */   {
/* 136 */     log.info("message : " + message);
/* 137 */     log.info("msisdn : " + msisdn);
/* 138 */     log.info("sessionID : " + sessionID);
/* 139 */     log.info("ussdSessionID : " + ussdSessionID);
/*     */     
/*     */ 
/*     */ 
/* 143 */     String responseString = null;
/* 144 */     Integer noOfAttempts = (Integer)ussdNoOfAttempts.get(msisdn);
/* 145 */     if (noOfAttempts == null) {
/* 146 */       ussdNoOfAttempts.put(msisdn, Integer.valueOf(1));
/* 147 */       noOfAttempts = Integer.valueOf(0);
/*     */     }
/* 149 */     if (noOfAttempts.intValue() < 2) {
/* 150 */       responseString = SendUSSD.getUSSDJsonPayload(msisdn, sessionID, noOfAttempts.intValue(), "mtcont", ussdSessionID);
/* 151 */       ussdNoOfAttempts.put(msisdn, Integer.valueOf(noOfAttempts.intValue() + 1));
/*     */     }
/* 153 */     return responseString;
/*     */   }
/*     */   
/*     */   private String validatePIN(String pin, String sessionID, String msisdn)
/*     */   {
/* 158 */     log.info("pin : " + pin);
/* 159 */     log.info("sessionID : " + sessionID);
/* 160 */     log.info("msisdn : " + msisdn);
/*     */     
/* 162 */     String responseString = null;
/*     */     try {
/* 164 */       LoginAdminServiceClient lAdmin = new LoginAdminServiceClient(FileUtil.getApplicationProperty("admin_url"));
/* 165 */       String sessionCookie = lAdmin.authenticate(FileUtil.getApplicationProperty("adminusername"), FileUtil.getApplicationProperty("adminpassword"));
/* 166 */       ClaimManagementClient claimManager = new ClaimManagementClient(FileUtil.getApplicationProperty("admin_url"), sessionCookie);
/* 167 */       String profilePin = claimManager.getPIN(msisdn);
/* 168 */       String hashedUserPin = getHashedPin(pin);
/* 169 */       if ((hashedUserPin != null) && (profilePin != null) && (profilePin.equals(hashedUserPin)))
/*     */       {
/* 171 */         return null;
/*     */       }
/* 173 */       Integer noOfAttempts = Integer.valueOf(DatabaseUtils.readMultiplePasswordNoOfAttempts(sessionID));
/* 174 */       if (noOfAttempts.intValue() < 2) {
/* 175 */         responseString = SendUSSD.getJsonPayload(msisdn, sessionID, 2, "mtcont");
/* 176 */         log.info("responseString 01: " + responseString);
/* 177 */         DatabaseUtils.updateMultiplePasswordNoOfAttempts(sessionID, noOfAttempts.intValue() + 1);
/*     */       } else {
/* 179 */         UserIdentityManagementClient identityClient = new UserIdentityManagementClient(FileUtil.getApplicationProperty("admin_url"), sessionCookie);
/* 180 */         identityClient.lockUser(msisdn);
/* 181 */         DatabaseUtils.deleteUser(sessionID);
/*     */       }
/*     */     }
/*     */     catch (AxisFault e) {
/* 185 */       e.printStackTrace();
/*     */     } catch (RemoteException e) {
/* 187 */       e.printStackTrace();
/*     */     } catch (LoginAuthenticationExceptionException e) {
/* 189 */       e.printStackTrace();
/*     */     } catch (SQLException ex) {
/* 191 */       Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (IOException ex) {
/* 193 */       Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (UserIdentityManagementAdminServiceIdentityMgtServiceExceptionException ex) {
/* 195 */       Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (RemoteUserStoreManagerServiceUserStoreExceptionException ex) {
/* 197 */       Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
/*     */     }
/* 199 */     return responseString;
/*     */   }
/*     */   
/*     */   private String validatePIN(String pin, String sessionID, String msisdn, String ussdSessionID)
/*     */   {
/* 204 */     String responseString = null;
/*     */     try {
/* 206 */       log.info("####### validatePIN  : ");
/* 207 */       LoginAdminServiceClient lAdmin = new LoginAdminServiceClient(FileUtil.getApplicationProperty("admin_url"));
/* 208 */       String sessionCookie = lAdmin.authenticate(FileUtil.getApplicationProperty("adminusername"), FileUtil.getApplicationProperty("adminpassword"));
/* 209 */       ClaimManagementClient claimManager = new ClaimManagementClient(FileUtil.getApplicationProperty("admin_url"), sessionCookie);
/* 210 */       String profilePin = claimManager.getPIN(msisdn);
/*     */       
/* 212 */       String hashedUserPin = getHashedPin(pin);
/*     */       
/*     */ 
/* 215 */       if ((hashedUserPin != null) && (profilePin != null) && (profilePin.equals(hashedUserPin))) {
/* 216 */         log.info("####### profilePin status : success");
/*     */         
/* 218 */         return null;
/*     */       }
/* 220 */       log.info("####### profilePin status : fail");
/* 221 */       Integer noOfAttempts = Integer.valueOf(DatabaseUtils.readMultiplePasswordNoOfAttempts(sessionID));
/* 222 */       if (noOfAttempts.intValue() < 2) {
/* 223 */         responseString = SendUSSD.getJsonPayload(msisdn, sessionID, 2, "mtcont", ussdSessionID);
/* 224 */         log.info("####### retry request  : " + responseString);
/* 225 */         DatabaseUtils.updateMultiplePasswordNoOfAttempts(sessionID, noOfAttempts.intValue() + 1);
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 233 */         String failedStatus = "FAILED_ATTEMPTS";
/* 234 */         log.info("Updating the databse with session:" + sessionID + " and status: " + failedStatus);
/* 235 */         DatabaseUtils.updateStatus(sessionID, failedStatus);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */         DatabaseUtils.deleteUser(sessionID);
/*     */         
/* 243 */         responseString = SendUSSD.getUSSDJsonPayload(msisdn, sessionID, 9, "mtfin", ussdSessionID);
/*     */       }
/*     */     }
/*     */     catch (AxisFault e) {
/* 247 */       e.printStackTrace();
/*     */     } catch (RemoteException e) {
/* 249 */       e.printStackTrace();
/*     */     } catch (LoginAuthenticationExceptionException e) {
/* 251 */       e.printStackTrace();
/*     */     } catch (SQLException ex) {
/* 253 */       Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (IOException ex) {
/* 255 */       Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
/*     */     }
/*     */     catch (RemoteUserStoreManagerServiceUserStoreExceptionException ex)
/*     */     {
/* 259 */       Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
/*     */     }
/* 261 */     return responseString;
/*     */   }
/*     */   
/*     */   @POST
/*     */   @Path("/ussd/pin")
/*     */   @Consumes({"application/json"})
/*     */   @Produces({"application/json"})
/*     */   public Response ussdPinReceive(String jsonBody) throws SQLException, JSONException {
/* 269 */     Gson gson = new GsonBuilder().serializeNulls().create();
/* 270 */     log.info("Json Body pin" + jsonBody);
/*     */     
/* 272 */     JSONObject jsonObj = new JSONObject(jsonBody);
/* 273 */     String message = jsonObj.getJSONObject("inboundUSSDMessageRequest").getString("inboundUSSDMessage");
/* 274 */     String sessionID = jsonObj.getJSONObject("inboundUSSDMessageRequest").getString("clientCorrelator");
/* 275 */     String msisdn = extractMsisdn(jsonObj);
/*     */     
/* 277 */     String ussdSessionID = null;
/*     */     
/* 279 */     if ((jsonObj.getJSONObject("inboundUSSDMessageRequest").has("sessionID")) && (!jsonObj.getJSONObject("inboundUSSDMessageRequest").isNull("sessionID")))
/*     */     {
/* 281 */       ussdSessionID = jsonObj.getJSONObject("inboundUSSDMessageRequest").getString("sessionID");
/*     */       
/* 283 */       log.info("####### LOGS  ussdSessionID 01 : " + ussdSessionID);
/*     */     }
/*     */     
/*     */ 
/* 287 */     ussdSessionID = ussdSessionID != null ? ussdSessionID : "";
/*     */     
/* 289 */     log.info("####### LOGS  ussdSessionID 02 : " + ussdSessionID);
/*     */     
/* 291 */     log.info("message>" + message);
/* 292 */     log.info("sessionID>" + sessionID);
/*     */     
/*     */ 
/*     */ 
/* 296 */     int responseCode = 400;
/*     */     
/*     */ 
/* 299 */     String responseString = validatePIN(message, sessionID, msisdn, ussdSessionID);
/* 300 */     if (responseString != null) {
/* 301 */       return Response.status(201).entity(responseString).build();
/*     */     }
/* 303 */     String status = null;
/*     */     
/*     */ 
/*     */ 
/* 307 */     if ((message != null) && (!message.isEmpty())) {
/* 308 */       status = "Approved";
/* 309 */       responseCode = 201;
/* 310 */       DatabaseUtils.updatePinStatus(sessionID, status, message, ussdSessionID);
/*     */     }
/*     */     else {
/* 313 */       responseCode = 400;
/* 314 */       status = "Status not updated";
/*     */     }
/*     */     
/*     */ 
/* 318 */     if (responseCode == 400) {
/* 319 */       responseString = "{\"requestError\":{\"serviceException\":{\"messageId\":\"SVC0275\",\"text\":\"Internal server Error\"}}}";
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 325 */       responseString = SendUSSD.getUSSDJsonPayload(msisdn, sessionID, 5, "mtfin", ussdSessionID);
/*     */     }
/* 327 */     return Response.status(responseCode).entity(responseString).build();
/*     */   }
/*     */   
/*     */ 
/*     */   @GET
/*     */   @Path("/ussd/status")
/*     */   @Produces({"application/json"})
/*     */   public Response userStatus(@QueryParam("sessionID") String sessionID, String jsonBody)
/*     */     throws SQLException
/*     */   {
/* 337 */     String userStatus = null;
/* 338 */     String responseString = null;
/*     */     
/* 340 */     userStatus = DatabaseUtils.getUSerStatus(sessionID);
/*     */     
/* 342 */     responseString = "{\"sessionID\":\"" + sessionID + "\"," + "\"status\":\"" + userStatus + "\"" + "}";
/*     */     
/*     */ 
/*     */ 
/* 346 */     return Response.status(200).entity(responseString).build();
/*     */   }
/*     */   
/*     */   private String extractMsisdn(JSONObject jsonObj) throws JSONException {
/* 350 */     String address = jsonObj.getJSONObject("inboundUSSDMessageRequest").getString("address");
/* 351 */     if (address != null) {
/* 352 */       return address.split(":\\+")[2];
/*     */     }
/* 354 */     return null;
/*     */   }
/*     */   
/*     */   private String getHashedPin(String pinvalue) {
/* 358 */     String hashString = "";
/*     */     try {
/* 360 */       MessageDigest digest = MessageDigest.getInstance("SHA-256");
/* 361 */       byte[] hash = digest.digest(pinvalue.getBytes("UTF-8"));
/*     */       
/* 363 */       StringBuilder hexString = new StringBuilder();
/*     */       
/* 365 */       for (int i = 0; i < hash.length; i++) {
/* 366 */         String hex = Integer.toHexString(0xFF & hash[i]);
/* 367 */         if (hex.length() == 1) {
/* 368 */           hexString.append('0');
/*     */         }
/* 370 */         hexString.append(hex);
/*     */       }
/*     */       
/* 373 */       hashString = hexString.toString();
/*     */     }
/*     */     catch (UnsupportedEncodingException ex) {
/* 376 */       log.info("Error getHashValue");
/*     */     } catch (NoSuchAlgorithmException ex) {
/* 378 */       log.info("Error getHashValue");
/*     */     }
/*     */     
/* 381 */     return hashString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GET
/*     */   @Path("/login/history")
/*     */   @Produces({"application/json"})
/*     */   public Response loginHistory(@QueryParam("userID") String userID, @QueryParam("appID") String appID, @QueryParam("fromDate") String strfromDate, @QueryParam("toDate") String strtoDate)
/*     */     throws SQLException, ParseException
/*     */   {
/* 392 */     String userStatus = null;
/* 393 */     String responseString = null;
/* 394 */     java.sql.Date fromDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(strfromDate).getTime());
/* 395 */     java.sql.Date toDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(strtoDate).getTime());
/* 396 */     List<LoginHistory> lsthistory = DatabaseUtils.getLoginHistory(userID, appID, fromDate, toDate);
/* 397 */     responseString = new Gson().toJson(lsthistory);
/* 398 */     return Response.status(200).entity(responseString).build();
/*     */   }
/*     */   
/*     */   @GET
/*     */   @Path("/login/applications")
/*     */   @Produces({"application/json"})
/*     */   public Response loginApps(@QueryParam("userID") String userID)
/*     */     throws SQLException, ParseException
/*     */   {
/* 407 */     List<String> lsthistory = DatabaseUtils.getLoginApps(userID);
/* 408 */     String responseString = new Gson().toJson(lsthistory);
/* 409 */     return Response.status(200).entity(responseString).build();
/*     */   }
/*     */   
/*     */   @GET
/*     */   @Path("/sms/response")
/*     */   @Produces({"text/plain"})
/*     */   public Response smsConfirm(@QueryParam("sessionID") String sessionID) throws SQLException
/*     */   {
/* 417 */     log.info("sessionID 01: " + sessionID);
/* 418 */     String responseString = null;
/* 419 */     String status = null;
/*     */     try {
/* 421 */       sessionID = AESencrp.decrypt(sessionID.replaceAll(" ", "+"));
/*     */     } catch (Exception e) {
/* 423 */       e.printStackTrace();
/* 424 */       responseString = e.getLocalizedMessage();
/* 425 */       return Response.status(500).entity(responseString).build();
/*     */     }
/* 427 */     String userStatus = DatabaseUtils.getUSerStatus(sessionID);
/* 428 */     if (userStatus.equalsIgnoreCase("PENDING")) {
/* 429 */       DatabaseUtils.updateStatus(sessionID, "APPROVED");
/* 430 */       status = "APPROVED";
/* 431 */       responseString = " You are successfully authenticated via mobile-connect";
/* 432 */     } else if (userStatus.equalsIgnoreCase("EXPIRED")) {
/* 433 */       status = "EXPIRED";
/* 434 */       responseString = " You are token expired";
/*     */     }
/*     */     else {
/* 437 */       status = "EXPIRED";
/* 438 */       responseString = " You are token already approved";
/*     */     }
/*     */     
/* 441 */     responseString = "{\"status\":\"" + status + "\"," + "\"text\":\"" + responseString + "\"" + "}";
/*     */     
/*     */ 
/* 444 */     return Response.status(200).entity(responseString).build();
/*     */   }
/*     */   
/*     */ 
/*     */   @POST
/*     */   @Path("/mepin/response")
/*     */   @Consumes({"application/x-www-form-urlencoded"})
/*     */   public Response mepinConfirm(@FormParam("identifier") String identifier, @FormParam("transaction_id") String transactionId, @FormParam("allow") String allow, @FormParam("transaction_status") String transactionStatus)
/*     */     throws SQLException
/*     */   {
/* 454 */     log.info("MePIN transactionID: " + transactionId);
/* 455 */     log.info("MePIN identifier: " + identifier);
/* 456 */     log.info("MePIN transactionStatus: " + transactionStatus);
/*     */     
/* 458 */     MePinStatusRequest mePinStatus = new MePinStatusRequest(transactionId);
/* 459 */     FutureTask<String> futureTask = new FutureTask(mePinStatus);
/* 460 */     ExecutorService executor = Executors.newFixedThreadPool(1);
/* 461 */     executor.execute(futureTask);
/*     */     
/* 463 */     return Response.status(200).build();
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\Endpoints.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */