package kitchenpos.client.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
public class ClientApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientApiApplication.class, args);
    }
}
