package kitchenpos.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:/db/sql/truncate.sql")
abstract class AcceptanceTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    protected void setup() {
        RestAssured.port = port;
    }
}
