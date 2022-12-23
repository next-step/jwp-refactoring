package kitchenpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        System.out.println("ORDER-APPLICATION-START");
        SpringApplication.run(OrderApplication.class, args);
    }
}
