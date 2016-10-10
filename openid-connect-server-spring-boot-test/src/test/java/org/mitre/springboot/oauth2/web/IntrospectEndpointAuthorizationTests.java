package org.mitre.springboot.oauth2.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.oauth2.web.IntrospectionEndpoint;

@Transactional
public class IntrospectEndpointAuthorizationTests extends OAuthAuthorizationTestsBase {
	
	
	//TODO verify JWT access via JWTBearerClientAssertionTokenEndpointFilter rather than basic auth
	@Test
	public void userGetIntrospectEndpointSuccess() throws Exception {
		String accessToken = getUserAccessToken();
		mockMvc.perform(
			get("/"+IntrospectionEndpoint.URL)
			.param("token", accessToken)
			.with(httpBasic("client","secret"))
			)
			.andExpect(status().is(200))
			.andExpect(jsonPath("sub", is("01921.FLANRJQW")))
			.andReturn()
			.getResponse()
			;
	}
	
	@Test
	public void userGetIntrospectClientCredentialsOnUriEndpointSuccess() throws Exception {
		String accessToken = getUserAccessToken();
		mockMvc.perform(
			get("/"+IntrospectionEndpoint.URL)
			.param("token", accessToken)
			.param("client_id", "client")
			.param("client_secret", "secret")
			)
			.andExpect(status().is(200))
			.andExpect(jsonPath("sub", is("01921.FLANRJQW")))
			.andReturn()
			.getResponse()
			;
	}
	
	@Test
	public void anonymousGetIntrospectEndpointUnauthenticated() throws Exception {
		mockMvc.perform(
			get("/"+IntrospectionEndpoint.URL))
			.andExpect(status().is(401))
			;
	}
}
