package org.mitre.springboot.openid.connect.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.openid.connect.web.UserInfoEndpoint;
import org.mitre.springboot.oauth2.web.OAuthAuthorizationTestsBase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Transactional
public class UserInfoEndpointAuthorizationTests extends OAuthAuthorizationTestsBase {
	
	@Test
	public void adminGetUserInfoEndpointSuccess() throws Exception {
		String accessToken = getAdminAccessToken();
		mockMvc.perform(
			get("/"+UserInfoEndpoint.URL)
			.header(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			)
			.andExpect(status().is(200))
			.andExpect(jsonPath("sub", is("90342.ASDFJWFA")))
			.andDo(print())
			.andReturn()
			.getResponse()
			;
	}
	
	@Test
	public void userGetUserInfoEndpointSuccess() throws Exception {
		String accessToken = getUserAccessToken();
		mockMvc.perform(
			get("/"+UserInfoEndpoint.URL)
			.header(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			)
			.andExpect(status().is(200))
			.andExpect(jsonPath("sub", is("01921.FLANRJQW")))
			.andDo(print())
			.andReturn()
			.getResponse()
			;
	}
	
	@Test
	public void anonymousGetUserInfoEndpointUnauthenticated() throws Exception {
		mockMvc.perform(
			get("/"+UserInfoEndpoint.URL))
			.andExpect(status().is(401))
			;
	}
	
}
