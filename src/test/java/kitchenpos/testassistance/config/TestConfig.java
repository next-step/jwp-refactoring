package kitchenpos.testassistance.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestConfig {
    @LocalServerPort
    int port;

    @Autowired
    DBCleaner dbCleaner;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        dbCleaner.DbDataInitialize();
    }
}
