/*    */ package org.wso2.carbon.identity.scim.provider.resources;
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
/*    */ public class AbstractResource
/*    */ {
/*    */   public String identifyOutputFormat(String format)
/*    */   {
/* 24 */     if ((format == null) || ("*/*".equals(format)) || ((format != null) && (format.startsWith("application/json")))) {
/* 25 */       return "application/json";
/*    */     }
/* 27 */     return format;
/*    */   }
/*    */   
/*    */   public String identifyInputFormat(String format)
/*    */   {
/* 32 */     if ((format == null) || ("*/*".equals(format)) || ((format != null) && (format.startsWith("application/json"))))
/*    */     {
/* 34 */       return "application/json";
/*    */     }
/* 36 */     return format;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\resources\AbstractResource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */