package kitchenpos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TableApplication.class)
@ActiveProfiles("test")
class TableApplicationTest {
    @Test
    void contextLoads() {
    }
}
