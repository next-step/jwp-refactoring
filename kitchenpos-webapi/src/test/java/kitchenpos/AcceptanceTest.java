package kitchenpos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;

@Sql("/DatabaseCleanUp.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

}
