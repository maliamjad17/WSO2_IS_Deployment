/*     */ package org.wso2.carbon.identity.application.authentication.endpoint.oauth2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OAuth2Login
/*     */   extends HttpServlet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  39 */   private static Map<String, String> pages = new HashMap();
/*     */   
/*     */   public void init(ServletConfig config) throws ServletException
/*     */   {
/*  43 */     Enumeration initParams = config.getInitParameterNames();
/*  44 */     while (initParams.hasMoreElements()) {
/*  45 */       String paramName = (String)initParams.nextElement();
/*  46 */       pages.put(paramName, config.getInitParameter(paramName));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/*  54 */     String loginPage = null;
/*  55 */     String authzPage = null;
/*  56 */     String errorPage = null;
/*  57 */     String consentPage = null;
/*     */     
/*     */ 
/*  60 */     String applicationName = request.getParameter("application");
/*     */     
/*  62 */     if (applicationName != null) {
/*  63 */       String loginPageParam = applicationName.trim() + "-LoginPage";
/*  64 */       String authzPageParam = applicationName.trim() + "-AuthzPage";
/*  65 */       String errorPageParam = applicationName.trim() + "-ErrorPage";
/*  66 */       String consentPageParam = applicationName.trim() + "-ConsentPage";
/*     */       
/*  68 */       String value = (String)pages.get(loginPageParam);
/*  69 */       if (value != null) {
/*  70 */         loginPage = value;
/*     */       }
/*  72 */       value = (String)pages.get(errorPageParam);
/*  73 */       if (value != null) {
/*  74 */         errorPage = value;
/*     */       }
/*  76 */       value = (String)pages.get(consentPageParam);
/*  77 */       if (value != null) {
/*  78 */         consentPage = value;
/*     */       }
/*  80 */       value = (String)pages.get(authzPageParam);
/*  81 */       if (value != null) {
/*  82 */         authzPage = value;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  87 */     if (request.getRequestURI().contains("/oauth2_login.do")) {
/*  88 */       String queryString = getSafeText(request.getQueryString());
/*  89 */       if (loginPage != null) {
/*  90 */         response.sendRedirect(loginPage + "?" + queryString);
/*  91 */       } else if (pages.get("Global-LoginPage") != null) {
/*  92 */         response.sendRedirect((String)pages.get("Global-LoginPage") + "?" + queryString);
/*     */       } else {
/*  94 */         request.getRequestDispatcher("login.jsp").forward(request, response);
/*     */       }
/*     */       
/*     */     }
/*  98 */     else if (request.getRequestURI().contains("/oauth2_authz.do")) {
/*  99 */       String queryString = getSafeText(request.getQueryString());
/* 100 */       if (errorPage != null) {
/* 101 */         response.sendRedirect(authzPage + "?" + queryString);
/* 102 */       } else if (pages.get("Global-AuthzPage") != null) {
/* 103 */         response.sendRedirect((String)pages.get("Global-AuthzPage") + "?" + queryString);
/*     */       } else {
/* 105 */         request.getRequestDispatcher("oauth2_authz.jsp").forward(request, response);
/*     */       }
/*     */       
/*     */     }
/* 109 */     else if (request.getRequestURI().contains("/oauth2_error.do")) {
/* 110 */       String queryString = getSafeText(request.getQueryString());
/* 111 */       if (errorPage != null) {
/* 112 */         response.sendRedirect(errorPage + "?" + queryString);
/* 113 */       } else if (pages.get("Global-ErrorPage") != null) {
/* 114 */         response.sendRedirect((String)pages.get("Global-ErrorPage") + "?" + queryString);
/*     */       } else {
/* 116 */         request.getRequestDispatcher("oauth2_error.jsp").forward(request, response);
/*     */       }
/*     */       
/*     */     }
/* 120 */     else if (request.getRequestURI().contains("/oauth2_consent.do")) {
/* 121 */       String queryString = getSafeText(request.getQueryString());
/* 122 */       if (consentPage != null) {
/* 123 */         response.sendRedirect(consentPage + "?" + queryString);
/* 124 */       } else if (pages.get("Global-ConsentPage") != null) {
/* 125 */         response.sendRedirect((String)pages.get("Global-ConsentPage") + "?" + queryString);
/*     */       } else {
/* 127 */         request.getRequestDispatcher("oauth2_consent.jsp").forward(request, response);
/*     */       }
/*     */     }
/*     */     else {
/* 131 */       request.getRequestDispatcher("oauth2_error.jsp").forward(request, response);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getSafeText(String text)
/*     */   {
/* 138 */     if (text == null) {
/* 139 */       return text;
/*     */     }
/* 141 */     text = text.trim();
/* 142 */     if (text.indexOf('<') > -1) {
/* 143 */       text = text.replace("<", "&lt;");
/*     */     }
/* 145 */     if (text.indexOf('>') > -1) {
/* 146 */       text = text.replace(">", "&gt;");
/*     */     }
/* 148 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\oauth2\OAuth2Login.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */