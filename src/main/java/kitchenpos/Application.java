package kitchenpos;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Flyway flyway = Flyway.configure().dataSource("DB_URL", "User", "Password").load();
        flyway.migrate();
    }
}
