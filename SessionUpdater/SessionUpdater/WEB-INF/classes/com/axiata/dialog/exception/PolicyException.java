/*    */ package com.axiata.dialog.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PolicyException
/*    */ {
/*    */   private String messageId;
/*    */   
/*    */ 
/*    */ 
/*    */   private String text;
/*    */   
/*    */ 
/*    */   private String variables;
/*    */   
/*    */ 
/*    */ 
/*    */   public PolicyException(String messageId, String text, String variables)
/*    */   {
/* 21 */     this.messageId = messageId;
/* 22 */     this.text = text;
/* 23 */     this.variables = variables;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getMessageId()
/*    */   {
/* 29 */     return this.messageId;
/*    */   }
/*    */   
/*    */   public void setMessageId(String messageId) {
/* 33 */     this.messageId = messageId;
/*    */   }
/*    */   
/*    */   public String getText()
/*    */   {
/* 38 */     return this.text;
/*    */   }
/*    */   
/*    */   public void setText(String text) {
/* 42 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String getVariables()
/*    */   {
/* 47 */     return this.variables;
/*    */   }
/*    */   
/*    */   public void setVariables(String variables) {
/* 51 */     this.variables = variables;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\exception\PolicyException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */