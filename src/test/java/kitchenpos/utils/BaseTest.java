package kitchenpos.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@EnableMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(MockMvcUtil.class)
@ActiveProfiles("test")
public class BaseTest {

    @Autowired
    protected MockMvcUtil mockMvcUtil;
}

