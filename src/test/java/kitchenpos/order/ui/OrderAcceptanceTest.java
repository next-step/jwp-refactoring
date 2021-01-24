package kitchenpos.order.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private List<OrderLineItem> orderLineItems;
    private Order first_order;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderLineItems = Collections.singletonList(OrderLineItem.of(1L, 등록된_menu_id, 2));

        first_order = 주문_생성_요청(Order.of(비어있지_않은_orderTable_id, orderLineItems)).as(Order.class);
    }

    @Test
    void createOrder() {
        Order order = Order.of(비어있지_않은_orderTable_id, orderLineItems);

        ExtractableResponse<Response> response = 주문_생성_요청(order);

        주문_생성됨(response);
    }

    @DisplayName("선택한 주문 항목의 메뉴가 등록되어 있지 않으면 생성할 수 없다.")
    @Test
    void createOrderException1() {
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 등록되어_있지_않은_menu_id, 2);
        Order order = Order.of(비어있지_않은_orderTable_id, Collections.singletonList(orderLineItem));

        ExtractableResponse<Response> response = 주문_생성_요청(order);

        주문_생성_실패(response);
    }

    @DisplayName("해당 주문 테이블이 등록되어 있지 않으면 생성할 수 없다.")
    @Test
    void createOrderException2() {
        Order order = Order.of(등록되어_있지_않은_orderTable_id, orderLineItems);

        ExtractableResponse<Response> response = 주문_생성_요청(order);

        주문_생성_실패(response);
    }

    @Test
    void getOrders() {
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        주문_목록_조회됨(response);
        주문_목록_크기_일치(response, 1);
    }

    @Test
    void changeOrderStatus() {
        first_order.setOrderStatus(OrderStatus.MEAL.name());

        ExtractableResponse<Response> response = 주문_상태_변경_요청(first_order);

        주문_상태_변경됨(response, first_order);
    }

    @DisplayName("주문이 등록되어 있지 않으면 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusException() {
        Order changeOrder = Order.of(비어있지_않은_orderTable_id, orderLineItems);
        changeOrder.setId(2L);

        ExtractableResponse<Response> response = 주문_상태_변경_요청(changeOrder);

        주문_상태_변경_실패(response);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_상태_변경_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/{orderId}/order-status", order.getId())
                .then().log().all()
                .extract();
    }

    private static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 주문_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_목록_크기_일치(ExtractableResponse<Response> response, int size) {
        List<Order> orders = response.jsonPath().getList(".", Order.class);

        assertThat(orders.size()).isEqualTo(size);
    }

    private static void 주문_상태_변경됨(ExtractableResponse<Response> response, Order order) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Order result = response.as(Order.class);
        assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    private static void 주문_상태_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
