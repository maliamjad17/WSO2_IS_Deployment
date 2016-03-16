/*    */ package org.wso2.carbon.identity.application.authentication.endpoint;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.servlet.RequestDispatcher;
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
/*    */ 
/*    */ public class AuthenticationEndpoint
/*    */   extends HttpServlet
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*    */     throws AuthenticationException
/*    */   {
/*    */     try
/*    */     {
/* 37 */       String loadPage = null;
/* 38 */       String hrdParam = request.getParameter("hrd");
/* 39 */       Map<String, String> idpAuthenticatorMapping = new HashMap();
/*    */       
/* 41 */       String authenticators = request.getParameter("authenticators");
/*    */       
/* 43 */       if (authenticators != null)
/*    */       {
/* 45 */         String[] authenticatorIdPMappings = authenticators.split(";");
/* 46 */         for (String authenticatorIdPMapping : authenticatorIdPMappings) {
/* 47 */           String[] authenticatorIdPMapArr = authenticatorIdPMapping.split(":");
/* 48 */           for (int i = 1; i < authenticatorIdPMapArr.length; i++) {
/* 49 */             if (idpAuthenticatorMapping.containsKey(authenticatorIdPMapArr[i])) {
/* 50 */               idpAuthenticatorMapping.put(authenticatorIdPMapArr[i], (String)idpAuthenticatorMapping.get(authenticatorIdPMapArr[i]) + "," + authenticatorIdPMapArr[0]);
/*    */             }
/*    */             else {
/* 53 */               idpAuthenticatorMapping.put(authenticatorIdPMapArr[i], authenticatorIdPMapArr[0]);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 59 */       if (idpAuthenticatorMapping != null) {
/* 60 */         request.setAttribute("idpAuthenticatorMap", idpAuthenticatorMapping);
/*    */       }
/*    */       
/* 63 */       if ((hrdParam != null) && ("true".equalsIgnoreCase(hrdParam))) {
/* 64 */         request.getRequestDispatcher("domain.jsp").forward(request, response);
/* 65 */         return;
/*    */       }
/*    */       
/* 68 */       if (request.getRequestURI().contains("retry.do")) {
/* 69 */         request.getRequestDispatcher("retry.jsp").forward(request, response);
/* 70 */         return;
/*    */       }
/*    */       
/* 73 */       if (request.getParameter("type").equals("samlsso")) {
/* 74 */         loadPage = "samlsso_login.do";
/* 75 */       } else if (request.getParameter("type").equals("openid")) {
/* 76 */         loadPage = "openid_login.do";
/* 77 */       } else if (request.getParameter("type").equals("passivests")) {
/* 78 */         loadPage = "passivests_login.do";
/* 79 */       } else if ((request.getParameter("type").equals("oauth2")) || (request.getParameter("type").equals("oidc"))) {
/* 80 */         loadPage = "oauth2_login.do";
/*    */       }
/* 82 */       request.getRequestDispatcher(loadPage).forward(request, response);
/*    */     } catch (Exception e) {
/* 84 */       e.printStackTrace();
/* 85 */       throw new AuthenticationException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\AuthenticationEndpoint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */