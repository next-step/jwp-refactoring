package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private final List<OrderTable> values;

    public OrderTables() {
        this(new ArrayList<>());
    }

    public OrderTables(List<OrderTable> values) {
        this.values = values;
    }

    public void add(OrderTable orderTable) {
        values.add(orderTable);
    }

    public List<Long> getIds() {
        return values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

    }

    public List<OrderTable> getOrderTables() {
        return values;
    }
}
