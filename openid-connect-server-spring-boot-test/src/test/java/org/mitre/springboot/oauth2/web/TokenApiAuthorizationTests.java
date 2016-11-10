package org.mitre.springboot.oauth2.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.springboot.openid.connect.web.ApiAuthorizationTestsBase;

@Transactional
public class TokenApiAuthorizationTests extends ApiAuthorizationTestsBase {

	@Test
	public void adminGetApiTokenSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/tokens/access", 200);
	}
	
	//TODO token tests for the token API endpoints
	@Test
	public void TODO(){}
	
}
