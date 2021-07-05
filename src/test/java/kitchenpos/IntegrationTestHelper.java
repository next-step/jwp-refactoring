package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Disabled
public abstract class IntegrationTestHelper {

    private static final String ENCODING = "UTF-8";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected abstract Object controller();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller())
                                      .addFilters(new CharacterEncodingFilter(ENCODING, true))
                                      .alwaysDo(print())
                                      .build();
    }

    protected ResultActions postRequest(final String uri, Object body) throws Exception {
        return mockMvc.perform(post(uri)
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .content(objectMapper.writeValueAsString(body)));
    }

    protected ResultActions getRequest(final String uri) throws Exception {
        return mockMvc.perform(get(uri));
    }
}
