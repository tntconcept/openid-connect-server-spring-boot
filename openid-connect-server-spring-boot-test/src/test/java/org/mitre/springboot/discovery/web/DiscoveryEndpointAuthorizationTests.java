package org.mitre.springboot.discovery.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.springboot.openid.connect.web.ApiAuthorizationTestsBase;
import org.springframework.beans.factory.annotation.Value;

@Transactional
public class DiscoveryEndpointAuthorizationTests extends ApiAuthorizationTestsBase {

	@Value("${openid.connect.server.issuer}")
	protected String issuer;
	
	@Test
	public void adminGetDiscoveryEndpointSuccess() throws Exception {
		adminSession();
		checkGetAccess("/.well-known/openid-configuration", 200);
		checkGetAccess("/.well-known/webfinger?resource="+issuer+"&rel=http://openid.net/specs/connect/1.0/issuer", 200);
	}
	
	@Test
	public void userGetDiscoveryEndpointSuccess() throws Exception {
		userSession();
		checkGetAccess("/.well-known/openid-configuration", 200);
		checkGetAccess("/.well-known/webfinger?resource="+issuer+"&rel=http://openid.net/specs/connect/1.0/issuer", 200);
	}
	
	@Test
	public void anonymousGetDiscoverySuccess() throws Exception {
		checkGetAccess("/.well-known/openid-configuration", 200);
		checkGetAccess("/.well-known/webfinger?resource="+issuer+"&rel=http://openid.net/specs/connect/1.0/issuer", 200);
	}
	
}
