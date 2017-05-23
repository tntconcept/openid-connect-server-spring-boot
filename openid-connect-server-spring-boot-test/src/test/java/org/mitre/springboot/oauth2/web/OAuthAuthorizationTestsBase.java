package org.mitre.springboot.oauth2.web;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.mitre.springboot.EndpointTestsBase;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;

public abstract class OAuthAuthorizationTestsBase extends EndpointTestsBase {

	protected String getUserAccessToken() throws Exception {
		return getToken("user","password","token","access_token");
	}
	
	protected String getAdminAccessToken() throws Exception {
		return getToken("admin","password","token", "access_token");
	}
	
	protected String getUserIdToken() throws Exception {
		return getToken("user","password","token id_token", "id_token");
	}
	
	protected String getAdminIdToken() throws Exception {
		return getToken("admin","password","token id_token", "id_token");
	}
	
	protected String getToken(String username , String password, String responseType, String responseParam) throws Exception {
		//Test data based upon in memory default db scripts
		String nonce = UUID.randomUUID().toString();
		String state = UUID.randomUUID().toString();
	    String clientId = "client";
	    MockHttpSession mockSession = new MockHttpSession(context.getServletContext(), UUID.randomUUID().toString());
	    
		//Request authorize, expect login redirect
        mockMvc.perform(
			get("/authorize")
			.session(mockSession)
			.param("response_type", responseType)
			.param("client_id", clientId)
			.param("redirect_uri", "http://localhost/")
			.param("scope", "openid profile email")
			.param("state", state)
			.param("nonce", nonce)
			.param("aud", "client_id")
			)
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("http://localhost/login"))
			;     

        //Login to establish session
        String authorizeLocationHeader = mockMvc.perform(
			post("/login")
			.session(mockSession)
			.with(csrf())
			.param("username", username)
			.param("password", password))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("http://localhost/authorize*")) 
			.andReturn()
			.getResponse()
			.getHeader("Location")
			;
        
        //Follow redirect back to authorize view
        mockMvc.perform(
			get(authorizeLocationHeader)
			.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk())
			;
        
        MvcResult result = mockMvc.perform(
			post("/authorize")
			.session(mockSession)
			.with(csrf())
			.param("scope_openid", "openid")
			.param("scope_profile", "profile")
			.param("remember", "none")
			.param("user_oauth_approval", "true")
			.param("authorize", "Authorize")
			)
			.andDo(print())	
			.andExpect(redirectedUrlPattern("http://localhost/*"))
			.andReturn()
			;
        
        //In this flow the tokens are URL fragments on the redirect URI
        String redirectUrl = result.getResponse().getRedirectedUrl().replace("#","?");
        List<NameValuePair> queryParams = URLEncodedUtils.parse(new URI(redirectUrl), "UTF-8");
        HashMap<String, Object>  queryParamsByName  = new HashMap<String,Object>();
        for(NameValuePair pair: queryParams) {
        	queryParamsByName.put(pair.getName(), pair.getValue());
        }
        return queryParamsByName.get(responseParam).toString();
		
	}
	
	protected String getClientAccessToken() throws Exception {
		 return getClientAccessToken("client", "secret");
	}
	
    protected String getClientAccessToken(final String clientId, final String secret) throws Exception{
        final MvcResult result = mockMvc
                .perform(post("/token")
                .with(httpBasic(clientId, secret))
                .param("grant_type", "client_credentials")
                .param("response_type", "token id_token")
                .param("client_id", clientId)
                .param("client_secret", secret)
                .param("scope", "openid profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("token_type", is("Bearer")))
                .andExpect(jsonPath("scope", is("openid profile")))
                .andExpect(jsonPath("expires_in", lessThan(3600)))
                .andReturn();
        
        final String json = result.getResponse().getContentAsString();
        final HashMap<String, Object> map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>(){
        });
        return map.get("access_token").toString();

    }
	
}
