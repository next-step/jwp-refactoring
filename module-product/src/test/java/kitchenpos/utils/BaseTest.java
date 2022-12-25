package kitchenpos.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BaseTest {
    @LocalServerPort
    private int localPort;
    protected static String basePath;
    protected static TestRestTemplate testRestTemplate;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        basePath = "http://localhost:"+localPort;
        testRestTemplate = new TestRestTemplate();
        databaseCleanup.execute();
    }
}
