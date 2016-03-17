/*    */ package com.axiata.dialog;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.rmi.RemoteException;
/*    */ import org.apache.axis2.AxisFault;
/*    */ import org.apache.axis2.client.ServiceClient;
/*    */ import org.apache.axis2.context.OperationContext;
/*    */ import org.apache.axis2.context.ServiceContext;
/*    */ import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
/*    */ import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
/*    */ import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
/*    */ import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
/*    */ import org.wso2.carbon.um.ws.api.stub.SetUserClaimValues;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoginAdminServiceClient
/*    */ {
/* 21 */   private final String serviceName = "AuthenticationAdmin";
/*    */   
/*    */ 
/*    */   private AuthenticationAdminStub authenticationAdminStub;
/*    */   
/*    */   private String endPoint;
/*    */   
/*    */ 
/*    */   public LoginAdminServiceClient(String backEndUrl)
/*    */     throws AxisFault
/*    */   {
/* 32 */     this.endPoint = (backEndUrl + "/services/" + "AuthenticationAdmin");
/* 33 */     this.authenticationAdminStub = new AuthenticationAdminStub(this.endPoint);
/*    */   }
/*    */   
/*    */ 
/*    */   public String authenticate(String userName, String password)
/*    */     throws RemoteException, LoginAuthenticationExceptionException
/*    */   {
/* 40 */     String sessionCookie = null;
/*    */     
/* 42 */     if (this.authenticationAdminStub.login(userName, password, "localhost")) {
/* 43 */       System.out.println("Login Successful");
/*    */       
/* 45 */       ServiceContext serviceContext = this.authenticationAdminStub._getServiceClient().getLastOperationContext().getServiceContext();
/*    */       
/*    */ 
/* 48 */       sessionCookie = (String)serviceContext.getProperty("Cookie");
/*    */       
/* 50 */       System.out.println(sessionCookie);
/*    */     }
/* 52 */     return sessionCookie;
/*    */   }
/*    */   
/*    */   public void logOut() throws RemoteException, LogoutAuthenticationExceptionException {
/* 56 */     this.authenticationAdminStub.logout();
/*    */   }
/*    */   
/*    */   public String LoginUser(String userName, String password) throws RemoteUserStoreManagerServiceUserStoreExceptionException {
/* 60 */     String sessionKey = null;
/*    */     
/*    */ 
/*    */ 
/*    */     try
/*    */     {
/* 66 */       LoginAdminServiceClient lAdmin = new LoginAdminServiceClient(FileUtil.getApplicationProperty("admin_url"));
/* 67 */       String sessionCookie = lAdmin.authenticate("admin", "admin");
/* 68 */       ClaimManagementClient claimManager = new ClaimManagementClient(FileUtil.getApplicationProperty("admin_url"), sessionCookie);
/* 69 */       claimManager.setClaim();
/*    */     } catch (AxisFault e) {
/* 71 */       e.printStackTrace();
/*    */     } catch (RemoteException e) {
/* 73 */       e.printStackTrace();
/*    */     } catch (LoginAuthenticationExceptionException e) {
/* 75 */       e.printStackTrace();
/*    */     }
/* 77 */     return sessionKey;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPIN(String pin)
/*    */   {
/* 86 */     SetUserClaimValues claimAdmin = new SetUserClaimValues();
/*    */     
/*    */ 
/*    */ 
/* 90 */     System.out.println("Username is = " + claimAdmin.getUserName());
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\LoginAdminServiceClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */