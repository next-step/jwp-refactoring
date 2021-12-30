package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class ControllerTest {

    protected MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();


    protected ResultActions get(String url, MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                ;
    }

    protected ResultActions post(String url, Object body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                ;
    }

    protected ResultActions put(String url, MultiValueMap<String, String> params, Object body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .content(objectMapper.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                ;
    }

    protected ResultActions delete(String url, MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent())
                ;
    }
}
