# openid-connect-spring-boot-config
OpenID Connect Spring Boot starter using MITREid Connect 


#Configuration properties

### Sample minimal application.yml
```YAML
openid:
  connect:
    server:
      issuer: http://localhost:8080/
      topbarTitle: OpenID Connect Server
      logoImageUrl: resources/images/openid_connect_small.png
    crypto:
      keystore:
        path: classpath:keystore.jwks
      signing:
        defaultSignerKeyId: rsa1
        defaultSigningAlgorithmName: RS256
      encrypt:
        defaultAlgorithm: RSA1_5
        defaultDecryptionKeyId: rsa1
        defaultEncryptionKeyId: rsa1
spring:
  jpa:
    properties:
      eclipselink:
        weaving: false
        cache.shared.default: false
  datasource:
    initialize: true
    schema: classpath:/db/tables/hsql_database_tables.sql,classpath:/db/tables/security-schema.sql

```

## openid.connect.server properties injected into [ConfigurationPropertiesBean.java](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/blob/master/openid-connect-common/src/main/java/org/mitre/openid/connect/config/ConfigurationPropertiesBean.java)