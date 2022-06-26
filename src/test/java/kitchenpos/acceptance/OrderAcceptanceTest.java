package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse orderTable;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTable = 주문_테이블_등록_요청(false, 4).as(OrderTableResponse.class);
    }

    @Test
    @DisplayName("주문 관리")
    void order() {
        // when
        ExtractableResponse<Response> response = 주문_등록_요청(orderTable.getId(), Arrays.asList(OrderLineItemRequest.of(1L, 1)));
        OrderResponse createdOrder = response.as(OrderResponse.class);
        // then
        주문_등록됨(response);

        // when
        response = 주문_조회_요청();
        // then
        주문_목록_조회됨(response);

        // when
        response = 주문_상태_변경_요청(createdOrder.getId(), OrderStatus.MEAL);
        // then
        주문_상태_변경됨(response, OrderStatus.MEAL);
    }

    private ExtractableResponse<Response> 주문_등록_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequest) {
        return RestAssured
                .given().log().all()
                .body(OrderRequest.of(orderTableId, orderLineItemRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderStatus orderStatus) {
        return RestAssured
                .given().log().all()
                .body(OrderStatusRequest.of(orderStatus))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then().log().all().extract();
    }

    public static void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(orderStatus);
    }
}
