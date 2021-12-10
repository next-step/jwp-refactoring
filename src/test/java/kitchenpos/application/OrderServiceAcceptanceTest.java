package kitchenpos.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("주문 테스트")
class OrderServiceAcceptanceTest extends AcceptanceTest {

    OrderTable createdOrderTable;
    List<OrderLineItem> orderLineItems;

    @BeforeEach
    public void setUp() {
        super.setUp();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(1L);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);

        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTable);
        createdOrderTable = 주문테이블이_생성됨(createResponse);

    }

    @DisplayName("주문을 등록한다")
    @Test
    void createTest() {

        Order order = new Order();
        order.setOrderTableId(createdOrderTable.getId());
        order.setOrderLineItems(orderLineItems);

        ExtractableResponse<Response> createOrderResponse = OrderFactory.주문_생성_요청(order);

        assertThat(createOrderResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("주문을 조회한다")
    @Test
    void getListTest() {

        Order order = new Order();
        order.setOrderTableId(createdOrderTable.getId());
        order.setOrderLineItems(orderLineItems);

        Order createdOrder = OrderFactory.주문_생성_요청(order).as(Order.class);

        ExtractableResponse<Response> response = OrderFactory.주문_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @DisplayName("주문을 변경한다")
    @Test
    void changeOrderStatusTest() {

        Order order = new Order();
        order.setOrderTableId(createdOrderTable.getId());
        order.setOrderLineItems(orderLineItems);

        Order createdOrder = OrderFactory.주문_생성_요청(order).as(Order.class);
        createdOrder.setOrderStatus(OrderStatus.MEAL.name());

        ExtractableResponse<Response> response = OrderFactory.주문_상태변경_요청(createdOrder, createdOrder.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(Order.class).getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    public static OrderTable 주문테이블이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(OrderTable.class);
    }


}
