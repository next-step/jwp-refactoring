package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private List<OrderTable> orderTables;

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
