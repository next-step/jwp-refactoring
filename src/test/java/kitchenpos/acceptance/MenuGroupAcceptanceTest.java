package kitchenpos.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class MenuGroupAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach(){

    }


}
