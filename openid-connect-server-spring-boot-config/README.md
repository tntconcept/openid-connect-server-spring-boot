# OpenID Connect Spring Boot Config Starter
---

##Spring Boot Application Properties

<table>
<th align="left">Application property name (Configuration Source)</th><th align="center">required</th><th align="center">default</th><th align="left">description</th>
<tr><td colspan="4"><b>MitreID OpenID Connect (<a href="https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/blob/master/openid-connect-common/src/main/java/org/mitre/openid/connect/config/ConfigurationPropertiesBean.java">ConfigurationPropertiesBean</a>) </b></td></tr>
<tr>
	<td align="left">openid.connect.server.issuer</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left">should match public URI for the server</td>
</tr>
<tr>
	<td align="left">openid.connect.server.regTokenLifeTime</td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.server.rqpTokenLifeTime</td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.server.forceHttps</td>
	<td align="center"></td>
	<td align="center">false</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.server.dualClient</td>
	<td align="center"></td>
	<td align="center">false</td>
	<td align="left"></td>
</tr>
<tr><td colspan="4"><b>Spring Datasource (<a href="https://github.com/spring-projects/spring-boot/blob/v1.4.1.RELEASE/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/jdbc/DataSourceProperties.java">DataSourceProperties</a>) </b></td></tr>
<tr>
	<td align="left">spring.datasource.initialize</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">spring.datasource.schema</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left">E.g. Embedded DB: "classpath:/db/tables/hsql_database_tables.sql,classpath:/db/tables/security-schema.sql"</td>
</tr>

<tr><td colspan="4"><b>Cryptography and JWT Signing (<a href="./src/main/java/org/mitre/springboot/config/CryptoConfig.java">CryptoConfig</a>) </b></td></tr>
<tr>
	<td align="left">openid.connect.crypto.keystore.path</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.crypto.signing.defaultSignerKeyId</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.crypto.signing.defaultSigningAlgorithmName </td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.crypto.encrypt.defaultAlgorithm</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.crypto.encrypt.defaultDecryptionKeyId</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.crypto.encrypt.defaultEncryptionKeyId</td>
	<td align="center">X</td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr><td colspan="4"><b>Scheduled Tasks (<a href="./src/main/java/org/mitre/springboot/config/ScheduledTaskConfig.java">ScheduledTaskConfig</a>) </b></td></tr>
<tr>
	<td align="left">openid.connect.scheduling.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.scheduling.corePoolSize</td>
	<td align="center"></td>
	<td align="center">5</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.scheduling.tasks.clearExpiredTokens.fixedDelay</td>
	<td align="center"></td>
	<td align="center">30000</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.scheduling.tasks.clearExpiredTokens.initialDelay</td>
	<td align="center"></td>
	<td align="center">60000</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.scheduling.tasks.clearExpiredSites.fixedDelay</td>
	<td align="center"></td>
	<td align="center">30000</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.scheduling.tasks.clearExpiredSites.initialDelay</td>
	<td align="center"></td>
	<td align="center">60000</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.scheduling.tasks.clearExpiredAuthorizationCodes.fixedDelay</td>
	<td align="center"></td>
	<td align="center">30000</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.scheduling.tasks.clearExpiredAuthorizationCodes.initialDelay</td>
	<td align="center"></td>
	<td align="center">60000</td>
	<td align="left"></td>
</tr>
<tr><td colspan="4"><b>Endpoint Configuration  </b></td></tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.whitelist.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the Whitelist API endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.approvedsite.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the ApprovedSite API endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.blacklist.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the Blacklist API endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.client.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the Client API endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.data.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the Data API endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.token.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the Token API endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.scope.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the Scope API endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.api.stats.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the StatsAPI endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.oidc.dynamicclientregistration.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the DynamicClientRegistration endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.oidc.jwksetpublishing.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the JWKSetPublishing endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.oidc.userinfo.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the UserInfo endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.oidc.discovery.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the Discovery endpoint is not exposed.</td>
</tr>
<tr>
	<td align="left">openid.connect.server.endpoints.protectedresourceregistration.enabled</td>
	<td align="center"></td>
	<td align="center">true</td>
	<td align="left">When set to false the ProtectedResourceRegistration endpoint is not exposed.</td>
</tr>

<table>

### Sample minimal application.yml
```YAML
openid:
  connect:
    server:
      issuer: http://localhost:8080/
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
  datasource:
    initialize: true
    schema: classpath:/db/tables/hsql_database_tables.sql,classpath:/db/tables/security-schema.sql

```

