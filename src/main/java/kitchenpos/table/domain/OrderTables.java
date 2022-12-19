package kitchenpos.table.domain;

import kitchenpos.table.message.OrderTableMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {

    private final List<OrderTable> orderTableItems;

    protected OrderTables() {
        orderTableItems = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTableItems) {
        this.orderTableItems = new ArrayList<>(orderTableItems);
    }

    public void groupBy(Long tableGroupId) {
        validateIsOrderTableSize();
        validateIsGroupedOtherTableGroup();
        validateIsEmptyOrderTables();
        this.orderTableItems.forEach(orderTable -> orderTable.groupBy(tableGroupId));
    }

    private void validateIsOrderTableSize() {
        if(orderTableItems.size() < 2) {
            throw new IllegalArgumentException(OrderTableMessage.GROUP_ERROR_MORE_THAN_TWO_ORDER_TABLES.message());
        }
    }

    private void validateIsGroupedOtherTableGroup() {
        if(this.orderTableItems.stream().anyMatch(OrderTable::isGrouped)) {
            throw new IllegalArgumentException(OrderTableMessage.GROUP_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());
        }
    }

    private void validateIsEmptyOrderTables() {
        if(this.orderTableItems.stream().anyMatch(OrderTable::isNotEmpty)) {
            throw new IllegalArgumentException(OrderTableMessage.GROUP_ERROR_ORDER_TABLE_IS_NOT_EMPTY.message());
        }
    }

    public void unGroup() {
        this.orderTableItems.forEach(OrderTable::unGroup);
    }

    public List<OrderTable> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(this.orderTableItems));
    }

    public boolean isSameSize(int size) {
        return this.orderTableItems.size() == size;
    }

    public List<Long> getIds() {
        return this.orderTableItems.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
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
