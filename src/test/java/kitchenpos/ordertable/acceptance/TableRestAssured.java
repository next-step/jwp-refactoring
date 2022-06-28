package kitchenpos.ordertable.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.http.MediaType;

public class TableRestAssured {
    public static ExtractableResponse<Response> 주문테이블_등록_요청(OrderTableRequest orderTable) {
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

    public static ExtractableResponse<Response> 주문테이블_비어있는지여부_변경_요청(OrderTableResponse targetOrderTable,
                                                                    OrderTableRequest orderTableRequest,
                                                                    boolean empty) {
        orderTableRequest.setEmpty(empty);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/{orderTableId}/empty", targetOrderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_손님수_변경_요청(OrderTableResponse targetOrderTable,
                                                                OrderTableRequest orderTableRequest,
                                                                int numberOfGuests) {
        orderTableRequest.setNumberOfGuests(numberOfGuests);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/{orderTableId}/number-of-guests", targetOrderTable.getId())
                .then().log().all()
                .extract();
    }
}
