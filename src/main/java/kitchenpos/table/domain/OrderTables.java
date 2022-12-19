package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTableItems;

    protected OrderTables() {
        orderTableItems = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTableItems) {
        this.orderTableItems = new ArrayList<>(orderTableItems);
    }

    public void groupBy(Long tableGroupId) {
        this.orderTableItems.forEach(orderTable -> orderTable.groupBy(tableGroupId));
    }

    public void unGroup() {
        this.orderTableItems.forEach(OrderTable::unGroup);
    }

    public List<OrderTable> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(this.orderTableItems));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTables that = (OrderTables) o;

        return orderTableItems.equals(that.orderTableItems);
    }

    @Override
    public int hashCode() {
        return orderTableItems.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(orderTableItems);
    }
}
