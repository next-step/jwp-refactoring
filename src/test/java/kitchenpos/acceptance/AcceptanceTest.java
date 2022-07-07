package kitchenpos.acceptance;

import io.restassured.RestAssured;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db/migration/Initial_tables.sql"})
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static Stream<Arguments> cookingAndMeal() {
        return Stream.of(
                Arguments.of(OrderStatus.COOKING),
                Arguments.of(OrderStatus.MEAL)
        );
    }
}
