/*    */ package org.wso2.carbon.identity.application.authentication.endpoint.passivests;
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
/*    */ public class PassiveSTSLogin
/*    */   extends HttpServlet
/*    */ {
/*    */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*    */     throws ServletException, IOException
/*    */   {
/* 32 */     request.getRequestDispatcher("login.jsp").forward(request, response);
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\passivests\PassiveSTSLogin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */