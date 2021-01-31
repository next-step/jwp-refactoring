package api.kitchenpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"domain.kitchenpos"})
@EntityScan(basePackages = {"domain.kitchenpos"})
@SpringBootApplication
public class KitchenposApplication {
	public static void main(String[] args) {
		SpringApplication.run(KitchenposApplication.class, args);
	}
}
