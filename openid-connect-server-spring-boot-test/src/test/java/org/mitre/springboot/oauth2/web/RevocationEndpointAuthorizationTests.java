package org.mitre.springboot.oauth2.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.oauth2.web.RevocationEndpoint;

@Transactional
public class RevocationEndpointAuthorizationTests extends OAuthAuthorizationTestsBase {

	//TODO verify JWT access via JWTBearerClientAssertionTokenEndpointFilter rather than basic auth
	@Test
	public void userGetRevocationEndpointClientIdAuthenticationSuccess() throws Exception {
		String accessToken = getUserAccessToken();
		mockMvc.perform(
			get("/"+RevocationEndpoint.URL)
			.param("token", accessToken)
			.with(httpBasic("client","secret"))
			)
			.andExpect(status().is(200))
			.andDo(print())
			.andReturn()
			.getResponse()
			;
	}
	
	@Test
	public void userGetRevocationEndpointClientCredentialsOnUriEndpointSuccess() throws Exception {
		String accessToken = getUserAccessToken();
		mockMvc.perform(
			get("/"+RevocationEndpoint.URL)
			.param("token", accessToken)
			.param("client_id", "client")
			.param("client_secret", "secret")
			)
			.andExpect(status().is(200))
			.andReturn()
			.getResponse()
			;
	}
	
	@Test
	public void anonymousGetRevocationEndpointUnauthenticated() throws Exception {
		mockMvc.perform(
			get("/"+RevocationEndpoint.URL))
			.andExpect(status().is(401))
			;
	}
	
}
