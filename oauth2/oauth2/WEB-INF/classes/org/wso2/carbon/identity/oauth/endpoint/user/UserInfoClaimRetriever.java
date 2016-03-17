package org.wso2.carbon.identity.oauth.endpoint.user;

import java.util.Map;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;

public abstract interface UserInfoClaimRetriever
{
  public abstract Map<String, Object> getClaimsMap(Map<ClaimMapping, String> paramMap);
}


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\oauth2.war!\WEB-INF\classes\org\wso2\carbon\identity\oauth\endpoint\user\UserInfoClaimRetriever.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */