package org.mitre.springboot.openid.connect.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.openid.connect.model.BlacklistedSite;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
public class BlacklistApiAuthorizationTests extends ApiAuthorizationTestsBase {
	
	ObjectMapper mapper = new ObjectMapper();
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
	
	
	@Test
	public void adminBlacklistAddSuccess() throws Exception {
		adminSession();
		String blacklistUrl = "http://www.blacklistedsite.com"; 
		String body = "{'uri':'" + blacklistUrl + "'}";
		mockMvc.perform(
				post("/api/blacklist")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(body)
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
			    .andDo(print())
				.andExpect(status().is(200))
				;
	}

	@Test
	public void adminBlacklistDeleteSuccess() throws Exception {
		adminSession();
		String blacklistUrl = "http://www.blacklistedsite.com"; 
		String body = "{'uri':'" + blacklistUrl + "'}";
		String result = 
				mockMvc.perform(
				post("/api/blacklist")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(body)
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
			    .andDo(print())
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
				;
		BlacklistedSite site = mapper.readValue(result, BlacklistedSite.class);
		checkDeleteAccess("/api/blacklist/"+site.getId(), 200);
	}

	@Test
	public void blacklistRefusal() throws Exception {
		String blacklistUrl = "http://www.blacklistedsite.com"; 
		adminSession();
		String body = "{'uri':'" + blacklistUrl + "'}";
		mockMvc.perform(
				post("/api/blacklist")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(body)
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
				.andExpect(status().is(200))
				;
		//Now try to register a new client for that uri and get the black list error
		body = "{'client_uri':'" + blacklistUrl + "','redirect_uris':['" + blacklistUrl + "']}";
		mockMvc.perform(
				post("/register")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(body)
				.with(csrf()))
				.andDo(print())
				.andExpect(status().is(400))
				.andExpect(jsonPath("error", is("invalid_redirect_uri")))
				;				
	}
}
