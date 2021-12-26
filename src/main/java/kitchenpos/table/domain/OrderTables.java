package kitchenpos.table.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void group(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.addGroup(tableGroup);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.removeGroup();
        }
    }

    public List<OrderTable> asList() {
        return orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
