package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderTables {

    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    public void addAll(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public boolean isEmpty() {
        return orderTables.isEmpty();
    }

    public Stream<OrderTable> stream() {
        return orderTables.stream();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void groupOrderTables(Long tableGroupId) {
        orderTables.forEach(
                orderTable -> orderTable.groupOrderTable(tableGroupId)
        );
    }

    public void ungroupOrderTables() {
        orderTables.forEach(OrderTable::ungroupOrderTable);
    }
}
