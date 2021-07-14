package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTables {

    private List<OrderTable> orderTables = new ArrayList<>();


    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public void checkValidEqualToRequestSize(List<Long> orderTableIds) {
        if (orderTables.isEmpty() || orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void checkValidEmptyTableGroup() {
        if (orderTables.stream().anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()))) {
            throw new IllegalArgumentException();
        }
    }

    public void updateGrouping(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.withTableGroup(tableGroup.getId());
        }
    }

    public List<OrderTable> orderTables() {
        return orderTables;
    }

    public List<Long> generateOrderTableIds() {
        return orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }

    public void updateUnGroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderTables that = (OrderTables) object;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
