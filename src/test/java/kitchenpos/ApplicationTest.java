package kitchenpos;

import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    void contextLoads() {
    }
}
