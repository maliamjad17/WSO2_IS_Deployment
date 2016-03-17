package org.wso2.carbon.identity.scim.provider.auth;

import java.util.Map;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;

public abstract interface SCIMAuthenticationHandler
{
  public abstract int getPriority();
  
  public abstract void setPriority(int paramInt);
  
  public abstract boolean canHandle(Message paramMessage, ClassResourceInfo paramClassResourceInfo);
  
  public abstract boolean isAuthenticated(Message paramMessage, ClassResourceInfo paramClassResourceInfo);
  
  public abstract void setProperties(Map<String, String> paramMap);
}


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\auth\SCIMAuthenticationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */