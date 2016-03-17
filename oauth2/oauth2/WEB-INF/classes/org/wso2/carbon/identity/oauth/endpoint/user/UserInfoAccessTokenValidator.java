package org.wso2.carbon.identity.oauth.endpoint.user;

import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO;

public abstract interface UserInfoAccessTokenValidator
{
  public abstract OAuth2TokenValidationResponseDTO validateToken(String paramString)
    throws UserInfoEndpointException;
}


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\UserInfoAccessTokenValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */