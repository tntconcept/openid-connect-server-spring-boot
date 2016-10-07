package org.mitre.springboot.openid.connect.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

@Transactional
public class DataApiAuthorizationTests extends ApiAuthorizationTestsBase {

	@Test
	public void adminGetApiDataUnauthorized() throws Exception {
		adminSession();
		checkGetAccess("/api/data", 200);
	}
	
	@Test
	@Rollback(true)
	public void adminPostApiDataSuccess() throws Exception {
		adminSession();
		checkPostAccess("/api/data", 200);
	}
	
	@Test
	public void userGetApiDataUnauthorized() throws Exception {
		userSession();
		checkGetAccess("/api/data", 403);
	}
	
	@Test
	public void userPostApiDataUnauthorized() throws Exception {
		userSession();
		checkPostAccess("/api/data", 403);
	}
	
	@Test
	public void anonymousGetApiDataUnauthenticated() throws Exception {
		checkGetAccess("/api/data", 401);
	}
	
	@Test
	public void anonymousPostApiDataUnauthenticated() throws Exception {
		checkPostAccess("/api/data", 401);
	}
	
}
