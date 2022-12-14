package kitchenpos.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("/truncate.sql")
public abstract class AcceptanceTest {
	@LocalServerPort
	int port;


	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}
}
