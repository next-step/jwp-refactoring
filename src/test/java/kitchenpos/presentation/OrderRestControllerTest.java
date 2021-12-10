package kitchenpos.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.testassistance.config.TestConfig;

@DisplayName("주문 API기능에 관한")
public class OrderRestControllerTest extends TestConfig {
    @DisplayName("주문이 저장된다.")
    @Test
    void save_order() {
        // given
        Order order = 저장될_주문생성();

        // when
        ExtractableResponse<Response> response = 주문_저장요청(order);

        // then
        주문_저장됨(response);
    }

    @DisplayName("주문이 조회된다.")
    @Test
    void search_order() {
        // when
        ExtractableResponse<Response> response = 주문_조회요청();

        // then
        주문_조회됨(response);
    }

    @DisplayName("주문의 상태가 변경된다.")
    @Test
    void update_orderState() {
        // given
        Order order = 저장될_주문생성();

        // when
        Order createdOrder = 주문_저장요청(order).as(Order.class);

        // given
        createdOrder.changeOrderStatus(OrderStatus.MEAL);

        // when
        ExtractableResponse<Response> response = 주문_상태변경요청(createdOrder);

        // then
        주문_상태변경됨(response);
    }

    private Order 저장될_주문생성() {
        OrderTable orderTable = 반테이블들_조회됨().get(0);
        orderTable.changeNumberOfGuests(10);

        OrderTable changedOrderTable = TableRestControllerTest.주문테이블_빈테이블_변경요청(orderTable).as(OrderTable.class);

        Menu[] menus = MenuRestControllerTest.메뉴_조회요청().as(Menu[].class);

        List<OrderLineItem> orderLineItems = 주문명세서_생성(List.of(menus[0], menus[1]));

        return Order.of(changedOrderTable, null, null, orderLineItems);
    }

    private List<OrderLineItem> 주문명세서_생성(List<Menu> menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final Menu menu : menus) {
            orderLineItems.add(OrderLineItem.of(null, menu, 1L));
        }

        return orderLineItems;
    }

    private List<OrderTable> 반테이블들_조회됨() {
        return List.of(TableRestControllerTest.주문테이블_조회요청().as(OrderTable[].class)).stream()
                                .filter(OrderTable::isEmpty)
                                .collect(Collectors.toList());
    }

    private void 주문_저장됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_저장요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문_조회요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_상태변경됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_상태변경요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/" + order.getId() + "/order-status")
                .then().log().all()
                .extract();
    }
}
