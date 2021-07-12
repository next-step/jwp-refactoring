package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public OrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        this.orderTables.addAll(orderTables);
        changeTableGroup(tableGroup);
    }

    public boolean isEmptyTablesNotMore(int limit) {
        long count = orderTables.stream()
                .filter(OrderTable::isEmpty)
                .count();
        return count < limit;
    }

    public boolean hasOtherOrderTable() {
        return orderTables.stream()
                .anyMatch(OrderTable::hasOtherOrderTable);
    }

    private void changeTableGroup(TableGroup tableGroup) {
        this.orderTables.forEach(orderTable -> orderTable.changeTableGroup(tableGroup));
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public boolean isEmpty() {
        return orderTables.isEmpty();
    }
}