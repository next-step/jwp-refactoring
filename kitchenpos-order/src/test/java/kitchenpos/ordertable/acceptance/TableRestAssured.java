package kitchenpos.ordertable.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.springframework.http.MediaType;

public class TableRestAssured {
    private TableRestAssured() {
    }

    public static ExtractableResponse<Response> 주문_테이블_등록되어_있음(OrderTableRequest request) {
        return 주문_테이블_생성_요청(request);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_상태_변경_요청(Long orderTableId, OrderTableRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_방문한_손님_수_변경_요청(Long orderTableId, OrderTableRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }
}
