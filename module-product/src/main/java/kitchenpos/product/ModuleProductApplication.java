package kitchenpos.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ModuleProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleProductApplication.class, args);
    }

}
