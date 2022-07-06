package kitchenpos.menu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(value = "kitchenpos.*")
@EntityScan(value = "kitchenpos.*")
@EnableJpaRepositories(value = "kitchenpos.*")
class ModuleMenuApplicationTests {
    @Test
    void contextLoads() {
    }
}
