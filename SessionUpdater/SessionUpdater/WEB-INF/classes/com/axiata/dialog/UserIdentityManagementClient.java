/*    */ package com.axiata.dialog;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.rmi.RemoteException;
/*    */ import org.apache.axis2.AxisFault;
/*    */ import org.apache.axis2.client.Options;
/*    */ import org.apache.axis2.client.ServiceClient;
/*    */ import org.wso2.carbon.identity.mgt.stub.UserIdentityManagementAdminServiceIdentityMgtServiceExceptionException;
/*    */ import org.wso2.carbon.identity.mgt.stub.UserIdentityManagementAdminServiceStub;
/*    */ import org.wso2.carbon.um.ws.api.stub.SetUserClaimValues;
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
/*    */ public class UserIdentityManagementClient
/*    */ {
/* 29 */   private final String serviceName = "UserIdentityManagementAdminService";
/*    */   private SetUserClaimValues setUserClaim;
/*    */   private String endPoint;
/*    */   private UserIdentityManagementAdminServiceStub remoteUser;
/*    */   
/*    */   public UserIdentityManagementClient(String backEndUrl, String sessionCookie) throws AxisFault
/*    */   {
/* 36 */     this.endPoint = (backEndUrl + "/services/" + "UserIdentityManagementAdminService");
/* 37 */     this.remoteUser = new UserIdentityManagementAdminServiceStub(this.endPoint);
/*    */     
/*    */ 
/* 40 */     ServiceClient serviceClient = null;
/*    */     
/*    */ 
/* 43 */     serviceClient = this.remoteUser._getServiceClient();
/*    */     
/* 45 */     Options option = serviceClient.getOptions();
/* 46 */     option.setManageSession(true);
/* 47 */     option.setProperty("Cookie", sessionCookie);
/*    */   }
/*    */   
/*    */   public void lockUser(String username)
/*    */     throws RemoteException, UserIdentityManagementAdminServiceIdentityMgtServiceExceptionException
/*    */   {
/* 53 */     System.out.println("Remote User lockUserAccount................" + username);
/* 54 */     this.remoteUser.lockUserAccount(username);
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\UserIdentityManagementClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */