package kitchenpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
    "kitchenpos.order"
})
@SpringBootApplication
public class TableModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableModuleApplication.class, args);
    }
}