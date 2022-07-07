package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private List<OrderTable> orderTables;

    private OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(final List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void upgroupAll() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
