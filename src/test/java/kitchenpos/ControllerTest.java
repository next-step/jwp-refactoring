package kitchenpos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ControllerTest {
    @Autowired
    protected MockMvc webMvc;

    @Autowired
    private WebApplicationContext context;

    protected ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.webMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }
}
