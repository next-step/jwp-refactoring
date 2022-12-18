package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class TableAcceptanceTestFixture {
    public static long 주문_테이블_생성되어_있음(int numberOfGuests, boolean empty) {
        Map<String, Object> request = new HashMap<>();
        request.put("numberOfGuests", numberOfGuests);
        request.put("empty", empty);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/tables")
                .then().statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().jsonPath().getLong("id");
    }
}
