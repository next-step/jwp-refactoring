package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private Long orderTableId = 1L;
    private int numberOfGuests = 4;
    private OrderTable orderTable;
    private Order order;
    private long menuId = 1L;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(orderTableId,null, numberOfGuests, false);
        order = Order.of(null, OrderStatus.COOKING.name(), LocalDateTime.now());
        orderLineItem = OrderLineItem.of(order, Menu.of(menuId, "menu", BigDecimal.valueOf(1000)));
        order.addOrderLineItem(orderLineItem);
    }

    @Test
    @DisplayName("주문의 상태가 완료일 경우 exception을 발생시킴")
    void checkCompleted() {
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.throwIfCompleted()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태가 변경됨")
    void changeOrderStatus() {
        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}