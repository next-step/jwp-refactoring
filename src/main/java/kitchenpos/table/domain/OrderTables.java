package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private final List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public static OrderTables createInstance() {
        return new OrderTables();
    }

    public void add(final OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    public void add(final List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void linkTableGroupId(final Long tableGroupId) {
        this.orderTables.forEach(orderTable -> orderTable.linkTableGroup(tableGroupId));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::unGroup);
        this.orderTables.clear();
    }
}
