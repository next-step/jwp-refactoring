package kitchenpos.order.domain;

import kitchenpos.common.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {
    Quantity quantity;
    OrderLineItemRequest orderLineItemRequest;
    OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        quantity = new Quantity(10L);
        orderLineItemRequest = new OrderLineItemRequest(1L, quantity.value());
        orderRequest = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));
    }

    @Test
    @DisplayName("Order 인스턴스를 생성한다")
    void of() {
        // when
        Order order = Order.of(orderRequest);

        // then
        assertAll(
                () -> assertThat(order.getOrderLineItems().values().get(0).getMenuId()).isEqualTo(1L),
                () -> assertThat(order.getOrderLineItems().values().get(0).getQuantity()).isEqualTo(quantity)
        );
    }

    @Test
    void changeOrderStatus() {
        // given
        Order order = Order.of(orderRequest);

        // when
        order.changeOrderStatus(MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(MEAL);
    }
}
