package org.mitre.springboot.openid.connect.web;

import javax.transaction.Transactional;

import org.junit.Test;

@Transactional
public class ApprovedSiteApiAuthorizationTests extends ApiAuthorizationTestsBase {
	
	
	@Test
	public void adminGetApiApprovedSuccess() throws Exception {
		adminSession();
		checkGetAccess("/api/approved", 200);
		//Note test setup data has no approved sites, considering adding for security test
		//TODO test GET by ID
		//TODO test DELETE by ID
	}
	
	@Test
	public void userGetApiApprovedSuccess() throws Exception {
		userSession();
		checkGetAccess("/api/approved", 200);
		//Note test setup data has no approved sites, considering adding for security test
		//TODO test GET by ID
		//TODO test DELETE by ID
	}
	
	@Test
	public void anonymousGetApiApprovedUnauthorized() throws Exception {
		checkGetAccess("/api/approved", 401);
		//TODO test GET by ID
		//TODO test DELETE by ID
	}

	
}
