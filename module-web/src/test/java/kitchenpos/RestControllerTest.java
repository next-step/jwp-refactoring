package kitchenpos;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@MockMvcTestConfig
public abstract class RestControllerTest<T> {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions post(String path, T request) throws Exception {
        return post(path, new String(objectMapper.writeValueAsBytes(request)));
    }

    protected ResultActions post(String path, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());
    }

    protected ResultActions put(String path, T request) throws Exception {
        return put(path, new String(objectMapper.writeValueAsBytes(request)));
    }

    protected ResultActions put(String path, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(path)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());
    }

    protected ResultActions get(String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(path)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    protected ResultActions delete(String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(path)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
