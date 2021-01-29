package kitchenpos.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.order.OrderAcceptanceTest.주문_상태_변경_요청;
import static kitchenpos.order.OrderAcceptanceTest.주문_생성_요청;
import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {
    @Test
    void createTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(등록되어_있지_않은_orderTable_id, 1, false);

        ExtractableResponse<Response> response = 주문_테이블_생성_요청(orderTableRequest);

        주문_테이블_생성됨(response);
    }

    @Test
    void changeEmpty() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(등록된_menu_id, 2));
        OrderRequest orderRequest = new OrderRequest(1L, 비어있지_않은_orderTable_id, orderLineItemRequests);
        주문_생성_요청(orderRequest);

        orderRequest = new OrderRequest(1L, 비어있지_않은_orderTable_id, OrderStatus.COMPLETION, orderLineItemRequests);
        주문_상태_변경_요청(orderRequest);

        OrderTableRequest orderTableRequest = new OrderTableRequest(비어있지_않은_orderTable_id, 0, true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_상태_변경_요청(orderTableRequest);

        // then
        주문_테이블_상태_변경됨(response, orderTableRequest.isEmpty());
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(등록되어_있지_않은_orderTable_id, 1, false);

        ExtractableResponse<Response> response = 주문_테이블_상태_변경_요청(orderTableRequest);

        주문_테이블_상태_변경_실패(response);
    }

    @Test
    void changeNumberOfGuests() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(비어있지_않은_orderTable_id, 3, false);

        ExtractableResponse<Response> response = 주문_테이블_손님수_변경_요청(orderTableRequest);

        주문_테이블_사람수_변경됨(response, orderTableRequest.getNumberOfGuests());
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 사람 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(등록되어_있지_않은_orderTable_id, 1, false);

        ExtractableResponse<Response> response = 주문_테이블_손님수_변경_요청(orderTableRequest);

        주문_테이블_사람수_변경_실패(response);
    }

    private static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_테이블_상태_변경_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/{orderTableId}/empty", orderTableRequest.getId())
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_테이블_손님수_변경_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableRequest.getId())
                .then().log().all()
                .extract();
    }

    private static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 주문_테이블_상태_변경됨(ExtractableResponse<Response> response, boolean empty) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTableResponse result = response.as(OrderTableResponse.class);
        assertThat(result.isEmpty()).isEqualTo(empty);
    }

    private static void 주문_테이블_상태_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 주문_테이블_사람수_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTableResponse result = response.as(OrderTableResponse.class);
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    private static void 주문_테이블_사람수_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
