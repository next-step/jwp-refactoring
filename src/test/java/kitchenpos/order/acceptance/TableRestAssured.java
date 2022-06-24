package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import org.springframework.http.MediaType;

public class TableRestAssured {
    public static ExtractableResponse<Response> 주문테이블_등록_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_비어있는지여부_변경_요청(OrderTable targetOrderTable, boolean empty) {
        targetOrderTable.setEmpty(empty);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(targetOrderTable)
                .when().put("/api/tables/{orderTableId}/empty", targetOrderTable.id())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_손님수_변경_요청(OrderTable targetOrderTable, int numberOfGuests) {
        targetOrderTable.setNumberOfGuests(NumberOfGuests.of(numberOfGuests));
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(targetOrderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests", targetOrderTable.id())
                .then().log().all()
                .extract();
    }
}
