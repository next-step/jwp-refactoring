package kitchenpos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(DatabaseCleanup.class)
public abstract class ServiceTest {

    @BeforeEach
    public void setUp(@Autowired DatabaseCleanup databaseCleanup) {
        databaseCleanup.execute();
    }
}
