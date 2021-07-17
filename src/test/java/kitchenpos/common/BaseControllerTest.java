package kitchenpos.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter((request, response, chain) -> {
                response.setCharacterEncoding("UTF-8"); // this is crucial
                chain.doFilter(request, response);
            }, "/*").build();
    }
}
