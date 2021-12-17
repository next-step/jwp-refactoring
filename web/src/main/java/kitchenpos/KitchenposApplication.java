package kitchenpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KitchenposApplication {
    public static void main(String[] args) {
        SpringApplication.run(KitchenposApplication.class, args);
    }
}
