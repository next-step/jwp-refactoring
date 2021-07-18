package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroupOrderTables() {
        this.orderTables.forEach(OrderTable::ungroup);
    }

    public boolean isSameSize(int size) {
        return this.orderTables.size() == size;
    }

    public void groupOrderTables(Long tableGroupId) {
        this.orderTables.forEach(orderTable -> orderTable.groupBy(tableGroupId));
    }
}
