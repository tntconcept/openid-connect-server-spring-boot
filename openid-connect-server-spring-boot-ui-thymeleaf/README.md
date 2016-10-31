# OpenID Connect Spring Boot Thymeleaf UI
---


##Spring Boot Application Properties

<table>
<th align="left">Application property name (Configuration Source)</th>
<th align="center">required</th>
<th align="left">default</th>
<th align="left">description</th>
<tr><td colspan="4"><b>MitreID OpenID Connect (<a href="https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/blob/master/openid-connect-common/src/main/java/org/mitre/openid/connect/config/ConfigurationPropertiesBean.java">ConfigurationPropertiesBean</a>) </b></td></tr>
<tr>
	<td align="left">openid.connect.server.topbarTitle</td>
	<td align="center">X</td>
	<td align="left"></td>
	<td align="left">should match public URI for the server</td>
</tr>
<tr>
	<td align="left">openid.connect.server.logoImageUrl</td>
	<td align="center">X</td>
	<td align="left"></td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.server.locale</td>
	<td align="center"></td>
	<td align="left">en</td>
	<td align="left"></td>
</tr>
<tr>
	<td align="left">openid.connect.server.languageNamespaces</td>
	<td align="center"></td>
	<td align="left">messages</td>
	<td align="left"></td>
</tr>
<tr><td colspan="4"><b>Web UI Configuration (<a href="./src/main/java/org/mitre/springboot/config/ui/WebMvcConfig.java">WebMvcConfig</a>) </b></td></tr>
<tr>
	<td align="left">openid.connect.jsonMessageSource.baseDirectory</td>
	<td align="center"></td>
	<td align="left">classpath:/static/resources/js/locale/</td>
	<td align="left"></td>
</tr>
<table>

### Sample minimal application.yml
```YAML
openid:
  connect:
    server:
      topbarTitle: OpenID Connect Server
      logoImageUrl: resources/images/openid_connect_small.png
    jsonMessageSource:
      baseDirectory: classpath:/static/resources/js/locale/
      
```