package kitchenpos.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestComponent
public class MockMvcUtil {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public MockMvcUtil(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public <T> ResultActions get(String urlTemplate, T... Params) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public <T> ResultActions post(String urlTemplate, T body, T... path) throws Exception {
        return post(postRequestBuilder(urlTemplate, body, path));
    }

    public <T> ResultActions post(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder);
    }

    public <T> ResultActions put(String urlTemplate, T body, T... path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body))
        );
    }

    public static <T> MockHttpServletRequestBuilder postRequestBuilder(String urlTemplate, T body, T... path) throws Exception {
        return MockMvcRequestBuilders.post(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(body));
    }

    public <T> T as(ResultActions resultActions, Class<T> clazz) throws Exception {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), clazz);
    }
}
