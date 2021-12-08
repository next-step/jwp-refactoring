package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;

import static kitchenpos.acceptance.TableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private Order order;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderTable orderTable;
        orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        orderTable = 테이블_등록되어_있음(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(2);

        order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // when
        ExtractableResponse<Response> response = 주문_등록_요청(order);

        // then
        주문_등록됨(response);
    }

    @Test
    @DisplayName("주문의 목록을 조회한다.")
    void list() {
        // given
        주문_등록되어_있음(order);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(response);
    }

    @Test
    @DisplayName("주문의 주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        Order savedOrder = 주문_등록되어_있음(order);

        Order modifyOrder = new Order();
        modifyOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when
        ExtractableResponse<Response> response = 주문의_주문_상태_변경_요청(savedOrder.getId(), modifyOrder);

        // then
        주문의_주문_상태_변경됨(response);
    }

    public static Order 주문_등록되어_있음(Order order) {
        return 주문_등록_요청(order).as(Order.class);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(Order order) {
        return post("/api/orders", order);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get("/api/orders");
    }

    public static ExtractableResponse<Response> 주문의_주문_상태_변경_요청(Long id, Order order) {
        return put("/api/orders/{orderId}/order-status", order, id);
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
