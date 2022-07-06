package kitchenpos.menu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"kitchenpos.product", "kitchenpos.menu"})
@EntityScan(value = {"kitchenpos.product.domain", "kitchenpos.menu"})
@EnableJpaRepositories(value = {"kitchenpos.product.repository", "kitchenpos.menu.repository"})
class ModuleMenuApplicationTests {
    @Test
    void contextLoads() {
    }
}
