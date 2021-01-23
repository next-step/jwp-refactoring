package kitchenpos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTestBase {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    protected void setUp() {
        databaseCleanup.execute();
    }
}
