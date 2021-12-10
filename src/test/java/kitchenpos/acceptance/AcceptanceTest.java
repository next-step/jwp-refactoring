package kitchenpos.acceptance;

import static io.restassured.RestAssured.UNDEFINED_PORT;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("/truncate.sql")
public abstract class AcceptanceTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    void beforeEach() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
    }
}
