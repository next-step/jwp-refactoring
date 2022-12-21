package kitchenpos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ProductApplication.class)
@ActiveProfiles("test")
class ProductApplicationTest {
    @Test
    void contextLoads() {
    }
}
