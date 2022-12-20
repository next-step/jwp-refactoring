package kitchenpos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MenuApplication.class)
@ActiveProfiles("test")
class MenuApplicationTest {
    @Test
    void contextLoads() {
    }
}
