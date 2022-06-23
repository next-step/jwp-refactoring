package kitchenpos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:trancate.sql")
@ActiveProfiles("test")
public class AcceptanceTest extends RestAssuredTest {
    @BeforeEach
    public void setUp() {
        super.setUp();
    }
}
