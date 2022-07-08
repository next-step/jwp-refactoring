package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void group(TableGroup tableGroup) {
        for(OrderTable orderTable: orderTables) {
            orderTable.group(tableGroup);
        }
    }

    public void ungroup() {
        for(OrderTable orderTable: orderTables) {
            orderTable.ungroup();
        }

        orderTables.clear();
    }
}
