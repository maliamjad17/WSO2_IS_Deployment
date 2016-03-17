/*    */ package org.wso2.carbon.identity.oauth.endpoint;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Enumeration;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletRequestWrapper;
/*    */ import javax.ws.rs.core.MultivaluedMap;
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
/*    */ public class OAuthRequestWrapper
/*    */   extends HttpServletRequestWrapper
/*    */ {
/*    */   private MultivaluedMap<String, String> form;
/*    */   private Enumeration<String> parameterNames;
/*    */   
/*    */   public OAuthRequestWrapper(HttpServletRequest request, MultivaluedMap<String, String> form)
/*    */   {
/* 31 */     super(request);
/* 32 */     this.form = form;
/*    */     
/* 34 */     Set<String> parameterNameSet = new HashSet();
/*    */     
/* 36 */     parameterNameSet.addAll(form.keySet());
/*    */     
/* 38 */     Enumeration<String> parameterNames = request.getParameterNames();
/* 39 */     while (parameterNames.hasMoreElements()) {
/* 40 */       parameterNameSet.add(parameterNames.nextElement());
/*    */     }
/*    */     
/* 43 */     this.parameterNames = Collections.enumeration(parameterNameSet);
/*    */   }
/*    */   
/*    */   public String getParameter(String name)
/*    */   {
/* 48 */     String value = super.getParameter(name);
/* 49 */     if (value == null) {
/* 50 */       value = (String)this.form.getFirst(name);
/*    */     }
/* 52 */     return value;
/*    */   }
/*    */   
/*    */   public Enumeration<String> getParameterNames()
/*    */   {
/* 57 */     return this.parameterNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\OAuthRequestWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */