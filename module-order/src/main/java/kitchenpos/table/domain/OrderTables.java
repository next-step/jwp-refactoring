package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables empty() {
        return new OrderTables();
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.setTableGroupId(null));
    }
}
