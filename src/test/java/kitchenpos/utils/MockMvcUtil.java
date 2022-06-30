package kitchenpos.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestComponent
public class MockMvcUtil<T> {

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

    public <T> ResultActions post(String urlTemplate, T body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(urlTemplate)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body))
        );
    }

    public <T> ResultActions put(String urlTemplate, T body, T... path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(urlTemplate, path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body))
        );
    }

    public T as(ResultActions resultActions, Class<T> clazz) throws Exception {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), clazz);
    }
}
