package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addAll(TableGroup tableGroup, List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroup);
        this.orderTables.clear();
    }
}
