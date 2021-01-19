package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order(2L, null, new OrderTable());
    }

    @Test
    public void changeStatus() {
        order.changeOrderStatus(OrderStatus.COOKING);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        order.changeOrderStatus(OrderStatus.COMPLETION);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    public void checkOrderStatus() {
        order.changeOrderStatus(OrderStatus.COMPLETION);
        assertThatThrownBy(() -> {
            order.checkOrderStatus();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
