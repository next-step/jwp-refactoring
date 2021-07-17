package kitchenpos.utils;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest {
    @Autowired
    private Flyway flyway;

    @BeforeEach
    protected void setUp() {
        flyway.clean();
        flyway.migrate();
    }
}
