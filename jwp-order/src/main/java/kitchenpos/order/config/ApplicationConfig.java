package kitchenpos.order.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "kitchenpos.menu")
@ComponentScan(basePackages = "kitchenpos.order")
public class ApplicationConfig {
}
