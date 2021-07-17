package kitchenpos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
    "kitchenpos.order"
})
@SpringBootTest
class TableModuleApplicationTest {

    @Test
    public void contextLoads() {
    }
}
