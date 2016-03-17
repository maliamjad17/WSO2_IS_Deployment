/*    */ package com.axiata.dialog;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import java.sql.SQLException;
/*    */ import javax.net.ssl.HttpsURLConnection;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ public class MePinStatusRequest implements java.util.concurrent.Callable<String>
/*    */ {
/* 17 */   private static Log log = LogFactory.getLog(MePinStatusRequest.class);
/*    */   private String transactionId;
/*    */   
/*    */   public MePinStatusRequest(String transactionId) {
/* 21 */     this.transactionId = transactionId;
/*    */   }
/*    */   
/*    */   public String call()
/*    */   {
/* 26 */     String allowStatus = null;
/*    */     
/* 28 */     String clientId = FileUtil.getApplicationProperty("mepin.clientid");
/* 29 */     String url = FileUtil.getApplicationProperty("mepin.url");
/* 30 */     url = url + "?transaction_id=" + this.transactionId + "&client_id=" + clientId + "";
/* 31 */     log.info("MePIN Status URL: " + url);
/*    */     
/* 33 */     String authHeader = "Basic " + FileUtil.getApplicationProperty("mepin.accesstoken");
/*    */     try
/*    */     {
/* 36 */       HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();
/*    */       
/* 38 */       connection.setRequestMethod("GET");
/* 39 */       connection.setRequestProperty("Accept", "application/json");
/* 40 */       connection.setRequestProperty("Authorization", authHeader);
/*    */       
/* 42 */       String resp = "";
/* 43 */       int statusCode = connection.getResponseCode();
/*    */       InputStream is;
/* 45 */       InputStream is; if ((statusCode == 200) || (statusCode == 201)) {
/* 46 */         is = connection.getInputStream();
/*    */       } else {
/* 48 */         is = connection.getErrorStream();
/*    */       }
/*    */       
/* 51 */       BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
/*    */       String output;
/* 53 */       while ((output = br.readLine()) != null) {
/* 54 */         resp = resp + output;
/*    */       }
/* 56 */       br.close();
/*    */       
/* 58 */       log.info("MePIN Status Response Code: " + statusCode + " " + connection.getResponseMessage());
/* 59 */       log.info("MePIN Status Response: " + resp);
/*    */       
/* 61 */       JsonObject responseJson = new JsonParser().parse(resp).getAsJsonObject();
/* 62 */       String respTransactionId = responseJson.getAsJsonPrimitive("transaction_id").getAsString();
/* 63 */       JsonPrimitive allowObject = responseJson.getAsJsonPrimitive("allow");
/*    */       
/* 65 */       if (allowObject != null) {
/* 66 */         allowStatus = allowObject.getAsString();
/* 67 */         if (Boolean.parseBoolean(allowStatus)) {
/* 68 */           allowStatus = "APPROVED";
/* 69 */           String sessionID = DatabaseUtils.getMePinSessionID(respTransactionId);
/* 70 */           DatabaseUtils.updateStatus(sessionID, allowStatus);
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (IOException e) {
/* 75 */       log.error("Error while MePIN Status request" + e);
/*    */     } catch (SQLException e) {
/* 77 */       log.error("Error in connecting to DB" + e);
/*    */     }
/* 79 */     return allowStatus;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\MePinStatusRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */