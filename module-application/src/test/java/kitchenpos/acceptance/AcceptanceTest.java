package kitchenpos.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import kitchenpos.acceptance.utils.DatabaseCleanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUtils databaseCleanup;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.config = RestAssured.config()
                                        .objectMapperConfig(
                                            new ObjectMapperConfig().jackson2ObjectMapperFactory(
                                                (cls, charset) -> objectMapper));
        databaseCleanup.cleanUp();
    }
}
