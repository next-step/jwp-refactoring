package kitchenpos.common.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class BaseRestControllerTest {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

}
