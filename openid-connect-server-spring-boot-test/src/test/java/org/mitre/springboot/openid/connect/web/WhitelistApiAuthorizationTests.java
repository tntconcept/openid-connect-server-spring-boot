package org.mitre.springboot.openid.connect.web;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class WhitelistApiAuthorizationTests extends ApiAuthorizationTestsBase {
	
	@Test
	public void adminGetApiWhitelistSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/whitelist", 200);
		//Note test setup data has no whitelist sites, considering adding for security testing
		//TODO test GET by ID
		//TODO test PUT by ID
		//TODO test DELETE by ID
	}
	
	@Test
	public void adminGetWhitelistByIdSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/whitelist/1", 200);
	}

	@Test
	public void adminDeleteWhitelistByIdSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/whitelist/1", 200);
		checkDeleteAccess("/api/whitelist/1", 200);
		checkGetAccess("/api/whitelist/1", 404);
	}

	
	@Test
	public void adminPostApiWhitelistSucess() throws Exception {
		adminSession();
		checkPostAccess("/api/whitelist", 200);
	}
	
	@Test
	public void userGetApiWhitelistSucess() throws Exception {
		userSession();
		checkGetAccess("/api/whitelist", 200);
		//TODO get by ID
	}
	
	@Test
	public void userPostApiWhitelistUnauthorized() throws Exception {
		userSession();
		checkPostAccess("/api/whitelist", 403);
	}
	
	@Test
	public void userPutApiWhitelistUnauthorized() throws Exception {
		userSession();
		checkPutAccess("/api/whitelist/1", 403);
	}
	
	@Test
	public void userDeleteApiWhitelistUnauthorized() throws Exception {
		userSession();
		checkDeleteAccess("/api/whitelist/1", 403);
	}
	
	@Test
	public void anonymousGetApiWhitelistUnauthenticated() throws Exception {
		checkGetAccess("/api/whitelist", 401);
	}
	
	@Test
	public void anonymousPostApiWhitelistUnauthenticated() throws Exception {
		checkPostAccess("/api/whitelist", 401);
	}
	
	@Test
	public void anonymousPutApiWhitelistUnauthenticated() throws Exception {
		checkPutAccess("/api/whitelist/1", 401);
	}
	
	@Test
	public void anonymousDeleteApiWhitelistUnauthenticated() throws Exception {
		checkDeleteAccess("/api/whitelist/1", 401);
	}
	
}
