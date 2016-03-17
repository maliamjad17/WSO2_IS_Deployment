package org.wso2.carbon.identity.oauth.endpoint.user;

import javax.servlet.http.HttpServletRequest;

public abstract interface UserInfoRequestValidator
{
  public abstract String validateRequest(HttpServletRequest paramHttpServletRequest)
    throws UserInfoEndpointException;
}


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\UserInfoRequestValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */