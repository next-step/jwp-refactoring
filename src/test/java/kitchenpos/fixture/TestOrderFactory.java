package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TestOrderFactory {
    public static Order createCompleteOrder() {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now());
        orderTable.updateEmpty(true, Arrays.asList(order));
        return order;
    }

    public static Order createMealOrder() {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        return new Order(orderTable, OrderStatus.MEAL, LocalDateTime.now());
    }
}
