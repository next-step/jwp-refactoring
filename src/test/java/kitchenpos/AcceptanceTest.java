package kitchenpos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    public static TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        String baseUri = "http://localhost:" + port;
        testRestTemplate = new TestRestTemplate(new RestTemplateBuilder().rootUri(baseUri));
        databaseCleanup.execute();
    }
}
