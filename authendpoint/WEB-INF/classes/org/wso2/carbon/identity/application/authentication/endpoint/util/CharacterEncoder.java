/*    */ package org.wso2.carbon.identity.application.authentication.endpoint.util;
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
/*    */ public class CharacterEncoder
/*    */ {
/*    */   public static String getSafeText(String text)
/*    */   {
/* 26 */     if (text == null) {
/* 27 */       return text;
/*    */     }
/* 29 */     text = text.trim();
/* 30 */     if (text.indexOf('<') > -1) {
/* 31 */       text = text.replace("<", "&lt;");
/*    */     }
/* 33 */     if (text.indexOf('>') > -1) {
/* 34 */       text = text.replace(">", "&gt;");
/*    */     }
/* 36 */     if (text.contains("\"")) {
/* 37 */       text = text.replace("\"", "&quot;");
/*    */     }
/* 39 */     if (text.contains("'")) {
/* 40 */       text = text.replace("'", "&#x27;");
/*    */     }
/* 42 */     if (text.contains("&")) {
/* 43 */       text = text.replace("&", "&amp;");
/*    */     }
/* 45 */     if (text.contains("/")) {
/* 46 */       text = text.replace("/", "&#x2F;");
/*    */     }
/* 48 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\util\CharacterEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */