package kitchenpos.order.domain;

import kitchenpos.exception.OrderException;
import kitchenpos.order.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 entity 테스트")
class OrdersTest {

    private Orders 주문;
    private List<OrderLineItem> 주문_항목;

    @BeforeEach
    void setUp() {
        주문 = new Orders(1L, 1L, OrderStatus.COOKING, LocalDateTime.now());
        주문_항목 = Arrays.asList(
                new OrderLineItem(1L, 1L, 1),
                new OrderLineItem(2L, 2L, 1)
        );
    }

    @Test
    void 주문_상태_변경() {
        주문.updateOrderStatus(OrderStatus.MEAL);
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문_entity에_주문_항목_리스트_추가() {
        주문.addOrderLineItems(주문_항목);
        List<OrderLineItem> orderLineItems = 주문.getOrderLineItems().orderLineItems();
        assertThat(orderLineItems.get(0).getOrders()).isNotNull();
        assertThat(orderLineItems.get(1).getOrders()).isNotNull();
        assertThat(orderLineItems.size()).isEqualTo(2);
    }
}
