# openid-connect-spring-boot-starter
OpenID Connect Spring Boot starter using MITREid Connect 


#Configuration properties

## MITREid OpenId Connect properties [ConfigurationPropertiesBean.java](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/blob/master/openid-connect-common/src/main/java/org/mitre/openid/connect/config/ConfigurationPropertiesBean.java)

### sample application.yml
```YAML
openid:
  connect:
    server:
      host: localhost
      issuer: http://${openid.connect.server.host}:${server.port}${server.contextPath}
      topbarTitle: OpenID Connect Server
      logoImageUrl: resources/images/openid_connect_small.png
      regTokenLifeTime: 172800
      forceHttps: false
      rqpTokenLifeTime: 
      locale: en
      languageNamespaces: 
        - messages
      dualClient: false
    jsonMessageSource:
      baseDirectory: classpath:/static/resources/js/locale/
      useCodeAsDefaultMessage: true
```

### sample application.yml
## MITREid OpenId Connect Spring Boot properties 
```YAML
spring:
  jpa:
    properties:
      eclipselink:
        weaving: false
        cache.shared.default: false
  datasource:
    initialize: true
    schema: classpath:/db/tables/hsql_database_tables.sql,classpath:/db/tables/security-schema.sql
security:
  basic:
    enabled: false
```