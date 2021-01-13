package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {
    private Order order;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderLineItems.add(new OrderLineItem(3L, 2));
        orderLineItems.add(new OrderLineItem(1L, 1));
        order = new Order(2L, orderLineItems);
    }

    @DisplayName("주문을 관리한다")
    @Test
    void manageOrder() {
        //주문 등록
        ExtractableResponse<Response> createResponse = 주문_등록_요청(order);
        주문_등록됨(createResponse);
        //주문 내역 조회
        ExtractableResponse<Response> findResponse = 주문내역_조회_요청();
        주문내역_조회됨(findResponse);
        //주문 상태 변경

        order.setOrderStatus(OrderStatus.MEAL.toString());
        ExtractableResponse<Response> changeResponse = 주문상태_변경_요청(createResponse, order);
        주문상태_변경됨(changeResponse);
    }

    private ExtractableResponse<Response> 주문상태_변경_요청(ExtractableResponse<Response> response, Order order) {
        String uri = response.header("Location");
        return RestAssured.given().log().all().
                body(order).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().put(uri + "/order-status").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문_등록_요청(Order order) {
        return RestAssured.given().log().all().
                body(order).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/orders").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문내역_조회_요청() {
        return RestAssured.given().log().all().
                when().get("/api/orders").
                then().log().all().
                extract();
    }

    private void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(Order.class).getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(response.as(Order.class).getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        assertThat(response.as(Order.class).getOrderLineItems().
                stream().map(orderLineItem -> orderLineItem.getMenuId()).
                collect(Collectors.toList())).
                containsExactly(3L, 1L);
    }

    private void 주문내역_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Order> orders = response.jsonPath().getList(".", Order.class);
        List<Long> orderTableIds = orders.stream().map(order -> order.getOrderTableId()).collect(Collectors.toList());

        assertThat(orderTableIds).contains(order.getOrderTableId());
    }

    private void 주문상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(Order.class).getOrderStatus()).isEqualTo(OrderStatus.MEAL.toString());
    }



}
