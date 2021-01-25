package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    private Order order;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        orderLineItems.add(OrderLineItem.empty());
        orderLineItems.add(OrderLineItem.empty());
        order = new Order(new OrderTable(0, false), orderLineItems);
    }

    @Test
    public void changeStatus() {
        order.changeOrderStatus(OrderStatus.COOKING);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);

        order.changeOrderStatus(OrderStatus.COMPLETION);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    public void checkOrderStatus() {
        order.changeOrderStatus(OrderStatus.COMPLETION);
        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
