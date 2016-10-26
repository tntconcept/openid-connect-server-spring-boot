package org.mitre.boot.security.ui;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.mitre.springboot.EndpointTestsBase;
import org.springframework.mock.web.MockHttpSession;

@Transactional
public class LoginEndpointTests extends EndpointTestsBase {
	
	@Test
	public void loginViewAsAdminTest() throws Exception {
        HttpSession session = mockMvc.perform(
    		post("/login")
    		.with(csrf())
            .param("username", "admin")
            .param("password", "password"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/"))
            .andDo(print())
            .andReturn()
            .getRequest()
            .getSession()
            ;     
        
        Assert.assertNotNull(session);
        
        mockMvc.perform(
        	get("/")
        	.session((MockHttpSession)session).locale(Locale.ENGLISH))
        	.andDo(print())	
        	.andExpect(status().isOk())
        	.andExpect(view().name("home"))
        	;
	}

	@Test
	public void loginViewAsUserTest() throws Exception {
		HttpSession session = mockMvc.perform(
			post("/login")
			.with(csrf())
			.param("username", "user")
			.param("password", "password"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/"))
			.andDo(print())
			.andReturn()
			.getRequest()
			.getSession();

		Assert.assertNotNull(session);

		mockMvc.perform(
        	get("/")
        	.session((MockHttpSession)session).locale(Locale.ENGLISH))
        	.andDo(print())	
        	.andExpect(status().isOk())
        	.andExpect(view().name("home"))
        	;
	}
	

	
}
