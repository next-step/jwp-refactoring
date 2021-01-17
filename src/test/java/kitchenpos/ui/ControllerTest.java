package kitchenpos.ui;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    protected final String MENU_GROUP_URI = "/api/menu-groups";

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {

    }
}
