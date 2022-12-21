package kitchenpos.ordertable.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderTableAcceptanceTestFixture extends AcceptanceTest {

    public OrderTableRequest 주문_테이블_1;
    public OrderTableRequest 주문_테이블_2;
    public OrderTableRequest 주문_테이블_3;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_1 = new OrderTableRequest(null, 0, true);
        주문_테이블_2 = new OrderTableRequest(null, 0, true);
        주문_테이블_3 = new OrderTableRequest(null, 4, false);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static OrderTableResponse 주문_테이블_생성_되어있음(OrderTableRequest orderTableRequest) {
        return 주문_테이블(주문_테이블_생성_요청(orderTableRequest));
    }

    public static OrderTableResponse 주문_테이블(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", OrderTableResponse.class);
    }

    public static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static List<OrderTableResponse> 주문_테이블_목록(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", OrderTableResponse.class);
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 빈_테이블_여부_수정_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all()
                .extract();
    }

    public static void 빈_테이블_여부_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 빈_테이블_여부_수정되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isIn(Arrays.asList(HttpStatus.BAD_REQUEST.value()
                , HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    public static ExtractableResponse<Response> 방문한_손님_수_수정_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 방문한_손님_수_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 방문한_손님_수_수정되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isIn(Arrays.asList(HttpStatus.BAD_REQUEST.value()
                , HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
