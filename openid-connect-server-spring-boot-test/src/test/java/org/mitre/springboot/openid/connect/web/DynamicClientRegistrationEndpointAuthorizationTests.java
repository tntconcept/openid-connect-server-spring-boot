package org.mitre.springboot.openid.connect.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.ClientDetailsEntity.AppType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;


@Transactional
public class DynamicClientRegistrationEndpointAuthorizationTests extends ApiAuthorizationTestsBase {

	
	private static final Logger log = LoggerFactory.getLogger(DynamicClientRegistrationEndpointAuthorizationTests.class);
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void registerDynamicClientSuccess() throws Exception{
		//userSession();
		mapper.setSerializationInclusion(Include.NON_NULL);	
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		String postContent = mapper.writeValueAsString(getClientDetailsEntity("https://test.url.com"));
		mockMvc.perform(
				post("/register")
 				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(postContent)
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
				.andDo(print())
				.andExpect(status().is(201))
				;
	}
	
	@Test
	public void readDynamicClientSuccess() throws Exception{
		//userSession();
		mapper.setSerializationInclusion(Include.NON_NULL);	
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		String postContent = mapper.writeValueAsString(getClientDetailsEntity("https://test.url.com"));
		String result = mockMvc.perform(
				post("/register")
 				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(postContent)
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
				.andDo(print())
				.andExpect(status().is(201))
				.andReturn().getResponse().getContentAsString()
				;

		Map<String,String> resultMap = mapper.readValue(result, new TypeReference<Map<String,Object>>(){});
		
		log.info("access token is " + resultMap.get("registration_access_token"));
		String client_id = resultMap.get("client_id");
		mockMvc.perform(
				get("/register/"+client_id)
 				.contentType(MediaType.APPLICATION_JSON_VALUE)
 				.header("Authorization", "Bearer " + resultMap.get("registration_access_token"))
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
				
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn().getResponse().getContentAsString();
		;
		//log.info("get response = " + responseContent);
		
	}

	@Test
	public void readDynamicClientAnonymousFail() throws Exception{
		//userSession();
		mapper.setSerializationInclusion(Include.NON_NULL);	
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		String postContent = mapper.writeValueAsString(getClientDetailsEntity("https://test.url.com"));
		String result = mockMvc.perform(
				post("/register")
 				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(postContent)
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
				.andDo(print())
				.andExpect(status().is(201))
				.andReturn().getResponse().getContentAsString()
				;
		Map<String,Object> resultMap = mapper.readValue(result, new TypeReference<HashMap<String,Object>>(){});
		log.info("access token is " + resultMap.get("registration_access_token"));
		String client_id = (String)resultMap.get("client_id");
		mockMvc.perform(
				get("/register/"+client_id)
 				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.session(mockSession).locale(Locale.ENGLISH)
				.with(csrf()))
				.andDo(print())
				.andExpect(status().is(401));
		//log.info("get response = " + responseContent);
		
	}
	
	//TODO:  test registry with software statement, and other permutations of various clientdetails that can be set.
	protected ClientDetailsEntity getClientDetailsEntity(String url){
		ClientDetailsEntity cde = new ClientDetailsEntity();
		cde.setAccessTokenValiditySeconds(5000);
		cde.setAllowIntrospection(true);
		cde.setApplicationType(AppType.WEB);
		cde.setClearAccessTokensOnRefresh(true);
		cde.setClientDescription("A test client");
		cde.setClientName("TestClient");
		cde.setClientUri(url);
		Set<String> redirectUris = new HashSet<String>();
		redirectUris.add(url);
		cde.setRedirectUris(redirectUris);
		return cde;
	}
}

