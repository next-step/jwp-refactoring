package kitchenpos.acceptance;

import io.restassured.RestAssured;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {
    public static OrderTable 테이블_등록되어_있음(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract()
                .as(OrderTable.class);
    }
}
