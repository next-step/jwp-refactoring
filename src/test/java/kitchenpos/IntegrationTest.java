package kitchenpos;

import kitchenpos.utils.DatabaseCleanup;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "spring.flyway.locations=classpath:integration/db/migration")
@SpringBootTest
public class IntegrationTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }
}
