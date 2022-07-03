package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.request.OrderTableRequest;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceSupport {

    public static ExtractableResponse<Response> 주문_테이블_등록요청(OrderTableRequest orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }
}
