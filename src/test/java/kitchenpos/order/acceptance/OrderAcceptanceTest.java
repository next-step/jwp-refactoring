package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;

import static kitchenpos.ordertable.acceptance.OrderTableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderRequest orderRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderTableResponse orderTableResponse = 테이블_등록되어_있음(new OrderTableRequest(2, false));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
        orderRequest = new OrderRequest(
                orderTableResponse.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItemRequest));
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // when
        ExtractableResponse<Response> response = 주문_등록_요청(orderRequest);

        // then
        주문_등록됨(response);
    }

    @Test
    @DisplayName("주문의 목록을 조회한다.")
    void list() {
        // given
        주문_등록되어_있음(orderRequest);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(response);
    }

    @Test
    @DisplayName("주문의 주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderResponse savedOrderResponse = 주문_등록되어_있음(orderRequest);
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        // when
        ExtractableResponse<Response> response = 주문의_주문_상태_변경_요청(savedOrderResponse.getId(), orderRequest);

        // then
        주문의_주문_상태_변경됨(response);
    }

    public static OrderResponse 주문_등록되어_있음(OrderRequest orderRequest) {
        return 주문_등록_요청(orderRequest).as(OrderResponse.class);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        return post("/api/orders", orderRequest);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get("/api/orders");
    }

    public static ExtractableResponse<Response> 주문의_주문_상태_변경_요청(Long id, OrderRequest orderRequest) {
        return put("/api/orders/{orderId}/order-status", orderRequest, id);
    }

    private void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", Order.class).size()).isPositive();
    }

    private void 주문의_주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
