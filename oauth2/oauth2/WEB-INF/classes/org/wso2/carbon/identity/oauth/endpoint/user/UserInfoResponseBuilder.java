package org.wso2.carbon.identity.oauth.endpoint.user;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO;

public abstract interface UserInfoResponseBuilder
{
  public abstract String getResponseString(OAuth2TokenValidationResponseDTO paramOAuth2TokenValidationResponseDTO)
    throws UserInfoEndpointException, OAuthSystemException;
}


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\UserInfoResponseBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */