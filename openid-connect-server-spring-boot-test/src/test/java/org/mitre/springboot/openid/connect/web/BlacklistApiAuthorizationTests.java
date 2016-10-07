package org.mitre.springboot.openid.connect.web;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class BlacklistApiAuthorizationTests extends ApiAuthorizationTestsBase {
	
	@Test
	public void adminGetApiBlacklistSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/blacklist", 200);
		//Note test setup data has no blacklist sites, considering adding for security testing
		//TODO test GET by ID
		//TODO test POST 
		//TODO test PUT by ID
		//TODO test DELETE by ID
	}
	
	@Test
	public void userGetApiBlacklistUnauthorized() throws Exception {
		userSession();
		checkGetAccess("/api/blacklist", 403);
	}
	
	@Test
	public void userPostApiBlacklistUnauthorized() throws Exception {
		userSession();
		checkPostAccess("/api/blacklist", 403);
	}
	
	@Test
	public void userPutApiBlacklistUnauthorized() throws Exception {
		userSession();
		checkPutAccess("/api/blacklist/1", 403);
	}
	
	@Test
	public void userDeleteApiBlacklistUnauthorized() throws Exception {
		userSession();
		checkDeleteAccess("/api/blacklist/1", 403);
	}
	
	@Test
	public void anonymousGetApiBlacklistUnauthenticated() throws Exception {
		checkGetAccess("/api/blacklist", 401);
	}
	
	@Test
	public void anonymousPostApiBlacklistUnauthenticated() throws Exception {
		checkPostAccess("/api/blacklist", 401);
	}
	
	@Test
	public void anonymousPutApiBlacklistUnauthenticated() throws Exception {
		checkPutAccess("/api/blacklist/1", 401);
	}
	
	@Test
	public void anonymousDeleteApiBlacklistUnauthenticated() throws Exception {
		checkDeleteAccess("/api/blacklist/1", 401);
	}
	
}
