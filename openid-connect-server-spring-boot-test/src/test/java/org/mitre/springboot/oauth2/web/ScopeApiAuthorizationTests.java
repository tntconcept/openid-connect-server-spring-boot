package org.mitre.springboot.oauth2.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.springboot.openid.connect.web.ApiAuthorizationTestsBase;
import org.springframework.test.annotation.Rollback;

@Transactional
public class ScopeApiAuthorizationTests extends ApiAuthorizationTestsBase {

	
	@Test
	public void adminGetApiScopesSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/scopes", 200);
		checkGetAccess("/api/scopes/1", 200);
	}
	
	@Test
	@Rollback(true)
	public void adminPostApiScopesSuccess() throws Exception {
		adminSession();
		checkPostAccess("/api/scopes", 200,"{'value':'test'}");
	}
	
	@Test
	@Rollback(true)
	public void adminPutApiScopesSuccess() throws Exception {
		adminSession();
		checkPutAccess("/api/scopes/1", 200, "{'id':1,'value':'test'}");
	}
	
	@Test
	@Rollback(true)
	public void adminDeleteApiScopesSuccess() throws Exception {
		adminSession();
		checkDeleteAccess("/api/scopes/1", 200);
	}
	
	@Test
	public void userGetApiScopesSuccess() throws Exception {
		userSession();
		checkGetAccess("/api/scopes", 200);
		checkGetAccess("/api/scopes/1", 200);
	}
	
	@Test
	@Rollback(true)
	public void userPostApiScopesUauthorized() throws Exception {
		userSession();
		checkPostAccess("/api/scopes", 403);
	}
	
	@Test
	@Rollback(true)
	public void userPutApiScopesUauthorized() throws Exception {
		userSession();
		checkPutAccess("/api/scopes/1", 403);
	}
	
	@Test
	@Rollback(true)
	public void userDeleteApiScopesUauthorized() throws Exception {
		userSession();
		checkDeleteAccess("/api/scopes/1", 403);
	}
	
	
	@Test
	public void anonymousGetApiScopesUnauthenticated() throws Exception {
		checkGetAccess("/api/scopes", 401);
		checkGetAccess("/api/scopes/1", 401);
	}
	
	@Test
	public void anonymousPostApiScopesUnauthenticated() throws Exception {
		checkPostAccess("/api/scopes", 401);
	}
	
	@Test
	public void userPutApiScopesUnauthenticated() throws Exception {
		checkPutAccess("/api/scopes/1", 401);
	}
	
	@Test
	public void userDeleteApiScopesUnauthenticated() throws Exception {
		checkDeleteAccess("/api/scopes/1", 401);
	}
	
	
}
