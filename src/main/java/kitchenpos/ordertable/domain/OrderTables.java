package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTables {
    private List<OrderTable> orderTables;

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validateOrderTableNotEmpty();
    }

    public void validateOrderTableNotEmpty() {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
