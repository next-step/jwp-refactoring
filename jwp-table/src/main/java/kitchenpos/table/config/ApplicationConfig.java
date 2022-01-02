package kitchenpos.table.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "kitchenpos.order")
@ComponentScan(basePackages = "kitchenpos.table")
public class ApplicationConfig {
}
