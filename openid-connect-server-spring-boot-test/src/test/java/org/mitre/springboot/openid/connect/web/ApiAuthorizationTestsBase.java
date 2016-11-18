package org.mitre.springboot.openid.connect.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import javax.transaction.Transactional;

import org.mitre.springboot.EndpointTestsBase;
import org.springframework.http.MediaType;

@Transactional
public abstract class ApiAuthorizationTestsBase extends EndpointTestsBase {

	protected void checkGetAccess(String uri, int status) throws Exception {
		mockMvc.perform(
			get(uri)
			.session(mockSession)
			.locale(Locale.ENGLISH))
			.andExpect(status().is(status))
			;
	}
	
	protected void checkPostAccess(String uri, int status) throws Exception {
		checkPostAccess(uri, status, "{}");
	}
	
	protected void checkPostAccess(String uri, int status, String body) throws Exception {
		mockMvc.perform(
			post(uri)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(body)
			.session(mockSession)
			.locale(Locale.ENGLISH)
			)
			.andExpect(status().is(status))
			;
	}
	
	protected void checkPutAccess(String uri, int status) throws Exception {
		checkPutAccess(uri, status, "{}");
	}
	
	protected void checkPutAccess(String uri, int status, String body) throws Exception {
		mockMvc.perform(
			put(uri)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(body)
			.session(mockSession)
			.locale(Locale.ENGLISH)
			)
			.andExpect(status().is(status))
			;
	}
	
	protected void checkDeleteAccess(String uri, int status) throws Exception {
		mockMvc.perform(
			delete(uri)
			.session(mockSession)
			.locale(Locale.ENGLISH)
			)
			.andExpect(status().is(status))
			;
	}
	
	protected void checkGetAccess(String uri, int status, String jsonPath, String value) throws Exception {
		mockMvc.perform(
			get(uri)
			.session(mockSession)
			.locale(Locale.ENGLISH)
			)
			.andExpect(status().is(status))
			.andExpect(jsonPath(jsonPath, is(value)))
			;
	}
	
	protected void checkGetAccessMissingValue(String uri, int status, String missingJsonPath) throws Exception {
		mockMvc.perform(
			get(uri)
			.session(mockSession)
			.locale(Locale.ENGLISH)
			)
			.andExpect(status().is(status))
			.andExpect(jsonPath(missingJsonPath).doesNotExist())
			;
	}
	
}
