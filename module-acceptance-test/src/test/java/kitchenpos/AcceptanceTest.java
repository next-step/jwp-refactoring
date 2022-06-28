package kitchenpos;

import kitchenpos.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private static TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate = new TestRestTemplate(new RestTemplateBuilder().rootUri("http://localhost:" + port));
        databaseCleanup.execute();
    }

    public static TestRestTemplate restTemplate() {
        return restTemplate;
    }
}
