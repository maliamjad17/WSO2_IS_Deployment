/*    */ package org.wso2.carbon.identity.application.authentication.endpoint.openid;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.RequestDispatcher;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class OpenIDLogin
/*    */   extends HttpServlet
/*    */ {
/*    */   protected void doPost(HttpServletRequest request, HttpServletResponse response)
/*    */     throws ServletException, IOException
/*    */   {
/* 32 */     doGet(request, response);
/*    */   }
/*    */   
/*    */ 
/*    */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*    */     throws ServletException, IOException
/*    */   {
/* 39 */     if (request.getRequestURI().contains("/openid_login.do")) {
/* 40 */       request.getRequestDispatcher("login.jsp").forward(request, response);
/* 41 */     } else if (request.getRequestURI().contains("/openid_profile.do")) {
/* 42 */       request.getRequestDispatcher("openid_profile.jsp").forward(request, response);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\openid\OpenIDLogin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */