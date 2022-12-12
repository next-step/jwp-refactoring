package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.springframework.http.MediaType;

public class TableRestAssured {

    public static ExtractableResponse<Response> 주문_테이블_등록되어_있음(OrderTableRequest orderTableRequest) {
        return 주문_테이블_생성_요청(orderTableRequest);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_비어있는지_여부_변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_방문_손님_수_변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }
}
