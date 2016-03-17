/*    */ package com.axiata.dialog;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.rmi.RemoteException;
/*    */ import org.apache.axis2.AxisFault;
/*    */ import org.apache.axis2.client.Options;
/*    */ import org.apache.axis2.client.ServiceClient;
/*    */ import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
/*    */ import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
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
/*    */ 
/*    */ public class ClaimManagementClient
/*    */ {
/* 30 */   private final String serviceName = "RemoteUserStoreManagerService";
/*    */   private SetUserClaimValues setUserClaim;
/*    */   private String endPoint;
/*    */   private RemoteUserStoreManagerServiceStub remoteUser;
/*    */   
/*    */   public ClaimManagementClient(String backEndUrl, String sessionCookie) throws AxisFault
/*    */   {
/* 37 */     this.endPoint = (backEndUrl + "/services/" + "RemoteUserStoreManagerService");
/* 38 */     this.remoteUser = new RemoteUserStoreManagerServiceStub(this.endPoint);
/*    */     
/*    */ 
/*    */ 
/* 42 */     ServiceClient serviceClient = null;
/*    */     
/*    */ 
/* 45 */     serviceClient = this.remoteUser._getServiceClient();
/*    */     
/* 47 */     Options option = serviceClient.getOptions();
/* 48 */     option.setManageSession(true);
/* 49 */     option.setProperty("Cookie", sessionCookie);
/*    */   }
/*    */   
/*    */   public void setClaim()
/*    */     throws RemoteException, RemoteUserStoreManagerServiceUserStoreExceptionException
/*    */   {
/* 55 */     System.out.println("Remote User " + this.remoteUser.getProfileNames("admin").toString());
/*    */   }
/*    */   
/*    */   public String getPIN(String msisdn) throws RemoteException, RemoteUserStoreManagerServiceUserStoreExceptionException {
/* 59 */     return this.remoteUser.getUserClaimValue(msisdn, "http://wso2.org/claims/pin", "default");
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\ClaimManagementClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */