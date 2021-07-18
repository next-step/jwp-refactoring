package kitchenpos.order.domain;

import kitchenpos.order.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 항목 entity 테스트")
class OrdersLineItemTest {

    private OrderLineItem 주문_항목;

    @BeforeEach
    void setUp() {
        주문_항목 = new OrderLineItem(1L, 1L, 1);
    }

    @Test
    void 주문_항목에_주문_entity_추가() {
        Orders orders = new Orders(1L, 1L, OrderStatus.COOKING, LocalDateTime.now());
        주문_항목.withOrder(orders);
        assertThat(주문_항목.getOrders()).isNotNull();
        assertThat(주문_항목.getOrders().getId()).isEqualTo(orders.getId());
        assertThat(주문_항목.getOrders().getOrderStatus()).isEqualTo(orders.getOrderStatus());
        assertThat(주문_항목.getOrders().getOrderedTime()).isEqualTo(orders.getOrderedTime());
        assertThat(주문_항목.getOrders().getOrderTableId()).isEqualTo(orders.getOrderTableId());
    }
}
