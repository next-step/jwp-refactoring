package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 단위 테스트")
class OrderTest {

    @Test
    @DisplayName("주문을 등록한다")
    void createOrderTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(orderLineItem)));
        orderLineItem.receiveOrder(1L);

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderLineItems()).contains(orderLineItem)
        );
    }

    @Test
    @DisplayName("주문상태를 변경한다 조리중 ->> 식사중")
    void modifyOrderTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(orderLineItem)));
        order.updateOrderStatus(OrderStatus.MEAL);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문상태를 변경한다 조리중 ->> 완료")
    void modifyEndOrderTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(orderLineItem)));
        order.updateOrderStatus(OrderStatus.COMPLETION);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문상태를 변경한다 완료 ->> 식사중")
    void modifyEndToOtherOrderTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(orderLineItem)));
        order.endOrder();

        assertThatThrownBy(() -> {
            order.updateOrderStatus(OrderStatus.MEAL);
        }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER.errorMessage());
    }

}
