/*    */ package org.wso2.carbon.identity.application.authentication.endpoint;
/*    */ 
/*    */ import javax.servlet.http.HttpServlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.wso2.carbon.identity.application.authentication.endpoint.util.TenantDataManager;
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
/*    */ public class TenantListRefreshEndpoint
/*    */   extends HttpServlet
/*    */ {
/*    */   protected void doGet(HttpServletRequest req, HttpServletResponse res)
/*    */     throws AuthenticationException
/*    */   {
/* 31 */     String tenantList = req.getParameter("tenantList");
/* 32 */     if (tenantList != null) {
/* 33 */       TenantDataManager.setTenantDataList(tenantList);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\TenantListRefreshEndpoint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */