package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class MockMvcTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	protected MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(print())
				.build();
	}

	protected MockHttpServletRequestBuilder postAsJson(String url, Object object) {
		return post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(object))
				.accept(MediaType.APPLICATION_JSON);
	}

	protected MockHttpServletRequestBuilder putAsJson(String url, Object object) {
		return put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(object))
				.accept(MediaType.APPLICATION_JSON);
	}

	private String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> T toObject(MvcResult mvcResult, Class<T> tClass) {
		try {
			return toObject(mvcResult.getResponse().getContentAsString(), tClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> T toObject(String json, Class<T> tClass) {
		try {
			return objectMapper.readValue(json, tClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> List<T> toList(MvcResult mvcResult, Class<T> tClass) {
		try {
			return toList(mvcResult.getResponse().getContentAsString(), tClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> List<T> toList(String json, Class<T> tClass) {
		try {
			return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
