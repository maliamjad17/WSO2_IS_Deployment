package org.wso2.identity.jaxrs.designator;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.HttpMethod;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH {}


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\identity\jaxrs\designator\PATCH.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */