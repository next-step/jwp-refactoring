package kitchenpos.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "kitchenpos.infra")
@EntityScan(basePackages = "kitchenpos.core")
@ComponentScan(value = {"kitchenpos.core", "kitchenpos.infra"})
public class InfraApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfraApplication.class, args);
    }
}
