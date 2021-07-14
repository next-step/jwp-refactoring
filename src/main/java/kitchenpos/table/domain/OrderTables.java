package kitchenpos.table.domain;

import kitchenpos.table.exception.IllegalOrderTablesSizeException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTables {
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        this.orderTables = Collections.unmodifiableList(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalOrderTablesSizeException();
        }
    }

    public List<Long> toOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }

    public void ungroup() {
        if (CollectionUtils.isEmpty(orderTables)) {
            return;
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.upgroup();
        }
    }

    public int size() {
        return orderTables.size();
    }

    public void grouped(Long tableGroupId) {
        orderTables.stream()
                  .forEach(orderTable -> orderTable.grouped(tableGroupId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
