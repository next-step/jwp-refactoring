package kitchenpos.order.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTableTestFixture {
    public static ExtractableResponse<Response> 주문테이블_손님수_수정_요청(Long orderTableId, int numberOfGuests) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_빈테이블_여부_수정_요청(Long orderTableId, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(empty);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_생성_요청(int numberOfGuest, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuest, empty);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 주문테이블_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문테이블_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문테이블_수정_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문테이블_empty_확인됨(ExtractableResponse<Response> response, boolean empty) {
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertThat(orderTableResponse.isEmpty()).isEqualTo(empty);
    }

    public static void 주문테이블_손님수_확인됨(ExtractableResponse<Response> response, int numberOfGuests) {
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
