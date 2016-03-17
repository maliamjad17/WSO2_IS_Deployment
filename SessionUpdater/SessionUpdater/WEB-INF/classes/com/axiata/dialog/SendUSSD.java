/*     */ package com.axiata.dialog;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.impl.client.DefaultHttpClient;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SendUSSD
/*     */ {
/*  24 */   private static Log log = LogFactory.getLog(SendUSSD.class);
/*     */   
/*  26 */   private static String CONST_MTINIT = "mtinit";
/*     */   
/*     */ 
/*     */   protected String sendUSSD(String msisdn, String sessionID, int noOfAttempts, String action)
/*     */     throws IOException
/*     */   {
/*  32 */     USSDRequest req = new USSDRequest();
/*     */     
/*  34 */     OutboundUSSDMessageRequest outboundUSSDMessageRequest = new OutboundUSSDMessageRequest();
/*  35 */     outboundUSSDMessageRequest.setAddress("tel:+" + msisdn);
/*  36 */     outboundUSSDMessageRequest.setShortCode(FileUtil.getApplicationProperty("shortcode"));
/*  37 */     outboundUSSDMessageRequest.setKeyword(FileUtil.getApplicationProperty("keyword"));
/*     */     
/*  39 */     if (noOfAttempts == 1) {
/*  40 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("message"));
/*     */     }
/*  42 */     else if (noOfAttempts == 2) {
/*  43 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("retry_message"));
/*     */     }
/*     */     else {
/*  46 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("error_message"));
/*     */     }
/*     */     
/*  49 */     outboundUSSDMessageRequest.setClientCorrelator(sessionID);
/*     */     
/*  51 */     ResponseRequest responseRequest = new ResponseRequest();
/*     */     
/*  53 */     responseRequest.setNotifyURL(FileUtil.getApplicationProperty("notifyurl"));
/*  54 */     responseRequest.setCallbackData("");
/*     */     
/*  56 */     outboundUSSDMessageRequest.setResponseRequest(responseRequest);
/*     */     
/*     */ 
/*  59 */     outboundUSSDMessageRequest.setUssdAction(action);
/*     */     
/*  61 */     req.setOutboundUSSDMessageRequest(outboundUSSDMessageRequest);
/*     */     
/*  63 */     Gson gson = new GsonBuilder().serializeNulls().create();
/*  64 */     String reqString = gson.toJson(req);
/*     */     
/*     */ 
/*  67 */     String endpoint = FileUtil.getApplicationProperty("ussdsend");
/*     */     
/*  69 */     endpoint = endpoint + "/tel:+" + msisdn;
/*     */     
/*     */ 
/*  72 */     String returnString = postRequest(endpoint, reqString);
/*  73 */     System.out.println("Returned from Backend = " + returnString);
/*     */     
/*  75 */     return returnString;
/*     */   }
/*     */   
/*     */   public static String getJsonPayload(String msisdn, String sessionID, int noOfAttempts, String action)
/*     */   {
/*  80 */     String reqString = null;
/*     */     
/*  82 */     USSDRequest req = new USSDRequest();
/*     */     
/*  84 */     OutboundUSSDMessageRequest outboundUSSDMessageRequest = new OutboundUSSDMessageRequest();
/*  85 */     outboundUSSDMessageRequest.setAddress("tel:+" + msisdn);
/*  86 */     outboundUSSDMessageRequest.setShortCode(FileUtil.getApplicationProperty("shortcode"));
/*  87 */     outboundUSSDMessageRequest.setKeyword(FileUtil.getApplicationProperty("keyword"));
/*     */     
/*  89 */     if (noOfAttempts == 1) {
/*  90 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("message"));
/*     */     }
/*  92 */     else if (noOfAttempts == 2) {
/*  93 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("retry_message"));
/*     */     }
/*     */     else {
/*  96 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("error_message"));
/*     */     }
/*     */     
/*  99 */     outboundUSSDMessageRequest.setClientCorrelator(sessionID);
/*     */     
/* 101 */     ResponseRequest responseRequest = new ResponseRequest();
/*     */     
/* 103 */     responseRequest.setNotifyURL(FileUtil.getApplicationProperty("notifyurl"));
/* 104 */     responseRequest.setCallbackData("");
/*     */     
/* 106 */     outboundUSSDMessageRequest.setResponseRequest(responseRequest);
/*     */     
/*     */ 
/* 109 */     outboundUSSDMessageRequest.setUssdAction(action);
/*     */     
/* 111 */     req.setOutboundUSSDMessageRequest(outboundUSSDMessageRequest);
/*     */     
/* 113 */     Gson gson = new GsonBuilder().serializeNulls().create();
/* 114 */     reqString = gson.toJson(req);
/*     */     
/*     */ 
/* 117 */     return reqString;
/*     */   }
/*     */   
/*     */   public static String getJsonPayload(String msisdn, String sessionID, int noOfAttempts, String action, String ussdSessionID) {
/* 121 */     String reqString = null;
/*     */     
/* 123 */     USSDRequest req = new USSDRequest();
/*     */     
/* 125 */     OutboundUSSDMessageRequest outboundUSSDMessageRequest = new OutboundUSSDMessageRequest();
/* 126 */     outboundUSSDMessageRequest.setAddress("tel:+" + msisdn);
/* 127 */     outboundUSSDMessageRequest.setShortCode(FileUtil.getApplicationProperty("shortcode"));
/* 128 */     outboundUSSDMessageRequest.setKeyword(FileUtil.getApplicationProperty("keyword"));
/*     */     
/* 130 */     if (noOfAttempts == 1) {
/* 131 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("message"));
/*     */     }
/* 133 */     else if (noOfAttempts == 2) {
/* 134 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("retry_message"));
/*     */     }
/*     */     else {
/* 137 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("error_message"));
/*     */     }
/*     */     
/* 140 */     outboundUSSDMessageRequest.setClientCorrelator(sessionID);
/*     */     
/* 142 */     outboundUSSDMessageRequest.setSessionID(ussdSessionID);
/*     */     
/* 144 */     ResponseRequest responseRequest = new ResponseRequest();
/*     */     
/* 146 */     responseRequest.setNotifyURL(FileUtil.getApplicationProperty("notifyurl"));
/* 147 */     responseRequest.setCallbackData("");
/*     */     
/* 149 */     outboundUSSDMessageRequest.setResponseRequest(responseRequest);
/*     */     
/*     */ 
/* 152 */     outboundUSSDMessageRequest.setUssdAction(action);
/*     */     
/* 154 */     req.setOutboundUSSDMessageRequest(outboundUSSDMessageRequest);
/*     */     
/* 156 */     Gson gson = new GsonBuilder().serializeNulls().create();
/* 157 */     reqString = gson.toJson(req);
/*     */     
/*     */ 
/* 160 */     return reqString;
/*     */   }
/*     */   
/*     */   public static String getUSSDJsonPayload(String msisdn, String sessionID, int noOfAttempts, String action, String ussdSessionID) {
/* 164 */     String reqString = null;
/*     */     
/* 166 */     USSDRequest req = new USSDRequest();
/*     */     
/* 168 */     OutboundUSSDMessageRequest outboundUSSDMessageRequest = new OutboundUSSDMessageRequest();
/* 169 */     outboundUSSDMessageRequest.setAddress("tel:+" + msisdn);
/* 170 */     outboundUSSDMessageRequest.setShortCode(FileUtil.getApplicationProperty("shortcode"));
/* 171 */     outboundUSSDMessageRequest.setKeyword(FileUtil.getApplicationProperty("keyword"));
/* 172 */     if (ussdSessionID != null) {
/* 173 */       outboundUSSDMessageRequest.setSessionID(ussdSessionID);
/*     */     }
/*     */     
/* 176 */     if (noOfAttempts < 2) {
/* 177 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("ussd_retry_meesage"));
/* 178 */     } else if (noOfAttempts == 5) {
/* 179 */       outboundUSSDMessageRequest.setOutboundUSSDMessage("Thank You");
/* 180 */     } else if (noOfAttempts == 9) {
/* 181 */       log.info("Reset PIN request sending....");
/* 182 */       outboundUSSDMessageRequest.setOutboundUSSDMessage("Please reset your PIN");
/*     */     }
/*     */     else {
/* 185 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("ussd_error_message"));
/*     */     }
/*     */     
/* 188 */     outboundUSSDMessageRequest.setClientCorrelator(sessionID);
/*     */     
/* 190 */     ResponseRequest responseRequest = new ResponseRequest();
/*     */     
/* 192 */     responseRequest.setNotifyURL(FileUtil.getApplicationProperty("notifyurl"));
/* 193 */     responseRequest.setCallbackData("");
/*     */     
/* 195 */     outboundUSSDMessageRequest.setResponseRequest(responseRequest);
/*     */     
/*     */ 
/* 198 */     outboundUSSDMessageRequest.setUssdAction(action);
/*     */     
/* 200 */     req.setOutboundUSSDMessageRequest(outboundUSSDMessageRequest);
/*     */     
/* 202 */     Gson gson = new GsonBuilder().serializeNulls().create();
/* 203 */     reqString = gson.toJson(req);
/*     */     
/*     */ 
/* 206 */     return reqString;
/*     */   }
/*     */   
/*     */   public static String getUSSDJsonPayload(String msisdn, String sessionID, int noOfAttempts, String action) {
/* 210 */     String reqString = null;
/*     */     
/* 212 */     USSDRequest req = new USSDRequest();
/*     */     
/* 214 */     OutboundUSSDMessageRequest outboundUSSDMessageRequest = new OutboundUSSDMessageRequest();
/* 215 */     outboundUSSDMessageRequest.setAddress("tel:+" + msisdn);
/* 216 */     outboundUSSDMessageRequest.setShortCode(FileUtil.getApplicationProperty("shortcode"));
/* 217 */     outboundUSSDMessageRequest.setKeyword(FileUtil.getApplicationProperty("keyword"));
/*     */     
/*     */ 
/* 220 */     if (noOfAttempts < 2) {
/* 221 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("ussd_retry_meesage"));
/* 222 */     } else if (noOfAttempts == 5) {
/* 223 */       outboundUSSDMessageRequest.setOutboundUSSDMessage("Thank You");
/*     */     } else {
/* 225 */       outboundUSSDMessageRequest.setOutboundUSSDMessage(FileUtil.getApplicationProperty("ussd_error_message"));
/*     */     }
/*     */     
/* 228 */     outboundUSSDMessageRequest.setClientCorrelator(sessionID);
/*     */     
/* 230 */     ResponseRequest responseRequest = new ResponseRequest();
/*     */     
/* 232 */     responseRequest.setNotifyURL(FileUtil.getApplicationProperty("notifyurl"));
/* 233 */     responseRequest.setCallbackData("");
/*     */     
/* 235 */     outboundUSSDMessageRequest.setResponseRequest(responseRequest);
/*     */     
/*     */ 
/* 238 */     outboundUSSDMessageRequest.setUssdAction(action);
/*     */     
/* 240 */     req.setOutboundUSSDMessageRequest(outboundUSSDMessageRequest);
/*     */     
/* 242 */     Gson gson = new GsonBuilder().serializeNulls().create();
/* 243 */     reqString = gson.toJson(req);
/*     */     
/*     */ 
/* 246 */     return reqString;
/*     */   }
/*     */   
/*     */ 
/*     */   private String postRequest(String url, String requestStr)
/*     */     throws IOException
/*     */   {
/* 253 */     HttpClient client = new DefaultHttpClient();
/* 254 */     HttpPost postRequest = new HttpPost(url);
/*     */     
/* 256 */     postRequest.addHeader("accept", "application/json");
/* 257 */     postRequest.addHeader("Authorization", "Bearer " + FileUtil.getApplicationProperty("accesstoken"));
/*     */     
/*     */ 
/* 260 */     StringEntity input = new StringEntity(requestStr);
/* 261 */     input.setContentType("application/json");
/*     */     
/* 263 */     postRequest.setEntity(input);
/*     */     
/* 265 */     HttpResponse response = client.execute(postRequest);
/*     */     
/* 267 */     if (response.getStatusLine().getStatusCode() != 201) {
/* 268 */       log.error("Error occured while calling end points - " + response.getStatusLine().getStatusCode() + "-" + response.getStatusLine().getReasonPhrase());
/*     */     }
/*     */     else {
/* 271 */       log.info("Success Request");
/*     */     }
/* 273 */     String responseStr = null;
/* 274 */     HttpEntity responseEntity = response.getEntity();
/* 275 */     if (responseEntity != null) {
/* 276 */       responseStr = EntityUtils.toString(responseEntity);
/*     */     }
/* 278 */     return responseStr;
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\SendUSSD.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */