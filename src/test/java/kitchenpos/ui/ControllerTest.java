package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest
abstract class ControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilters(new CharacterEncodingFilter("utf-8", true))
            .alwaysDo(print())
            .build();
    }

    protected ResultActions perform(RequestBuilder requestBuilder) {
        try {
            return mockMvc.perform(requestBuilder);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    protected String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new RuntimeException(jsonProcessingException);
        }
    }

    protected MockHttpServletRequestBuilder postAsJson(String uri, Object object) {
        return post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(toJson(object));
    }

    protected MockHttpServletRequestBuilder putAsJson(String uri, Object object) {
        return put(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(toJson(object));
    }

}
