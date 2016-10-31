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
<tr>
	<td align="left"></td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left"></td>
	<td align="center"></td>
	<td align="center"></td>
	<td align="left"></td>
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

