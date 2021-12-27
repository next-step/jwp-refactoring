package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 단위 테스트")
class OrderTest {

    @Test
    @DisplayName("주문을 등록한다")
    void createOrderTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        OrderTable orderTable = new OrderTable(5, true);


        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(orderLineItem)));
        orderLineItem.receiveOrder(order);

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderLineItems()).contains(orderLineItem)
        );
    }

    @Test
    @DisplayName("주문상태를 변경한다")
    void modifyOrderTest() {

        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        OrderTable orderTable = new OrderTable(5, true);

        Order order = new Order(orderTable, new OrderLineItems(Arrays.asList(orderLineItem)));
        order.startMeal();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

}
