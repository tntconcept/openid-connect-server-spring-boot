package org.mitre.springboot.openid.connect.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

@Transactional
public class ClientApiAuthorizationTests extends ApiAuthorizationTestsBase {
	
	
	
	
	@Test
	public void adminGetApiClientsSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/clients", 200);
		checkGetAccess("/api/clients/1", 200, "clientSecret", "secret");
	}
	
	@Test
	@Rollback(true)
	public void adminPostApiClientsSuccess() throws Exception {
		adminSession();
		checkPostAccess("/api/clients", 200);
	}
	
	@Test
	@Rollback(true)
	public void adminPutApiClientsSuccess() throws Exception {
		adminSession();
		checkPutAccess("/api/clients/1", 200);
	}
	
	@Test
	@Rollback(true)
	public void adminDeleteApiClientsSuccess() throws Exception {
		adminSession();
		checkDeleteAccess("/api/clients/1", 200);
	}
	
	@Test
	public void userGetApiClientsSuccess() throws Exception {
		userSession();
		checkGetAccess("/api/clients", 200);
		checkGetAccessMissingValue("/api/clients/1", 200, "clientSecret");
	}
	
	@Test
	public void userPostApiClientsUnauthorized() throws Exception {
		userSession();
		checkPostAccess("/api/clients", 403);
	}
	
	@Test
	public void userPutApiClientsUnauthorized() throws Exception {
		userSession();
		checkPutAccess("/api/clients/1", 403);
	}
	
	@Test
	public void userDeleteApiClientsUnauthorized() throws Exception {
		userSession();
		checkDeleteAccess("/api/clients/1", 403);
	}
	
	@Test
	public void anonymousGetApiClientsUnauthenticated() throws Exception {
		checkGetAccess("/api/clients", 401);
		checkGetAccess("/api/clients/1", 401);
	}
	
	@Test
	public void anonymousPostApiClientsUnauthenticated() throws Exception {
		checkPostAccess("/api/clients", 401);
	}
	
	
}
