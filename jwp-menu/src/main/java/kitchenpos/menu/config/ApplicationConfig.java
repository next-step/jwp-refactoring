package kitchenpos.menu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "kitchenpos.menu")
@ComponentScan(basePackages = "kitchenpos.product")
public class ApplicationConfig {
}
