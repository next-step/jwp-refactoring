package kitchenpos.table.acceptance.utils;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
    }
}
