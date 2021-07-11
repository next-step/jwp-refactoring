package kitchenpos.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public abstract class ControllerTest<T> {

    private MockMvc mockMvc;

    abstract protected Object controller();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    private void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    protected ResultActions postRequest(String path, T request) throws Exception {
        return mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));
    }

    protected ResultActions getRequest(String path) throws Exception {
        return mockMvc.perform(get(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected ResultActions putRequest(String path, T request) throws Exception {
        return mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));
    }

    protected ResultActions deleteRequest(String path) throws Exception {
        return mockMvc.perform(delete(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected void 조회성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }

    protected void 생성성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isCreated());
    }

    protected void 수정성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }

    protected void 삭제성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isNoContent());
    }
}
