package kitchenpos.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.config.JacksonConfig;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestComponent
public class MockMvcUtil {

    private final MockMvc mockMvc;
    private static ObjectMapper objectMapper;

    public MockMvcUtil(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        objectMapper = getInstance();
    }

    public ResultActions get(String urlTemplate) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public ResultActions post(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder);
    }

    public ResultActions put(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder);
    }

    public <T> ResultActions delete(String urlTemplate, Object... path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );
    }

    public static <T> MockHttpServletRequestBuilder postRequestBuilder(String urlTemplate, T body, Object... path)
        throws Exception {
        return MockMvcRequestBuilders.post(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body));
    }

    public static <T> MockHttpServletRequestBuilder putRequestBuilder(String urlTemplate, T body, Object... path)
        throws Exception {
        return MockMvcRequestBuilders.put(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body));
    }

    public static MockHttpServletRequestBuilder getRequestBuilder(String urlTemplate, Object... path) {
        return MockMvcRequestBuilders.get(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
    }

    public static <T> T as(ResultActions resultActions, Class<T> clazz) throws Exception {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), clazz);
    }

    private static class SingletonHelper {
        private static final ObjectMapper INSTANCE = new JacksonConfig().serializingObjectMapper();
    }

    public static ObjectMapper getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
