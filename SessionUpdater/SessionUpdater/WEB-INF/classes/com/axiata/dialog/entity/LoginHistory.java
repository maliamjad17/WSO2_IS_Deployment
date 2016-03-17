/*    */ package com.axiata.dialog.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoginHistory
/*    */ {
/*    */   private String applicationId;
/*    */   
/*    */ 
/*    */   private String authUser;
/*    */   
/*    */ 
/*    */   private String authenticated;
/*    */   
/*    */ 
/*    */   private String authenticators;
/*    */   
/*    */ 
/*    */   private String authIpaddress;
/*    */   
/*    */ 
/*    */   private String createdDate;
/*    */   
/*    */ 
/*    */   public LoginHistory(String applicationId, String authUser, String authenticated, String authIpaddress, String createdDate)
/*    */   {
/* 27 */     this.applicationId = applicationId;
/* 28 */     this.authUser = authUser;
/* 29 */     this.authenticated = authenticated;
/* 30 */     this.authIpaddress = authIpaddress;
/* 31 */     this.createdDate = createdDate;
/*    */   }
/*    */   
/*    */   public String getApplicationId() {
/* 35 */     return this.applicationId;
/*    */   }
/*    */   
/*    */   public void setApplicationId(String applicationId) {
/* 39 */     this.applicationId = applicationId;
/*    */   }
/*    */   
/*    */   public String getAuthUser() {
/* 43 */     return this.authUser;
/*    */   }
/*    */   
/*    */   public void setAuthUser(String authUser) {
/* 47 */     this.authUser = authUser;
/*    */   }
/*    */   
/*    */   public String getAuthenticated() {
/* 51 */     return this.authenticated.equalsIgnoreCase("1") ? "SUCCESS" : "FAILED";
/*    */   }
/*    */   
/*    */   public void setAuthenticated(String authenticated) {
/* 55 */     this.authenticated = authenticated;
/*    */   }
/*    */   
/*    */   public String getAuthenticators() {
/* 59 */     return this.authenticators;
/*    */   }
/*    */   
/*    */   public void setAuthenticators(String authenticators) {
/* 63 */     this.authenticators = authenticators;
/*    */   }
/*    */   
/*    */   public String getAuthIpaddress() {
/* 67 */     return this.authIpaddress;
/*    */   }
/*    */   
/*    */   public void setAuthIpaddress(String authIpaddress) {
/* 71 */     this.authIpaddress = authIpaddress;
/*    */   }
/*    */   
/*    */   public String getCreatedDate() {
/* 75 */     return this.createdDate;
/*    */   }
/*    */   
/*    */   public void setCreatedDate(String createdDate) {
/* 79 */     this.createdDate = createdDate;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\entity\LoginHistory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */