package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        this.orderTables.addAll(orderTables);
        assign(tableGroup);
    }

    private void assign(final TableGroup tableGroup) {
        this.orderTables.forEach(orderTable -> orderTable.assign(tableGroup.getId()));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
