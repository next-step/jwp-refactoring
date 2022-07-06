package kitchenpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MenuProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MenuProductApplication.class, args);
    }
}
