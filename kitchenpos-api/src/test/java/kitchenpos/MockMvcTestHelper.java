package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.configuration.JpaAuditingConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Disabled
public abstract class MockMvcTestHelper {

    private static final String ENCODING = "UTF-8";

    @MockBean
    private JpaAuditingConfiguration jpaAuditingConfiguration;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected abstract Object controller();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller())
                                      .addFilters(new CharacterEncodingFilter(ENCODING, true))
                                      .alwaysDo(MockMvcResultHandlers.print())
                                      .build();
    }

    protected ResultActions postRequest(final String uri, Object body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(uri)
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(body)));
    }

    protected ResultActions getRequest(final String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(uri));
    }

    protected ResultActions putRequest(final String uri, Object body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(uri)
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(body)));
    }

    protected ResultActions deleteRequest(final String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(uri));
    }
}
