package kitchenpos.tobe.orders.domain.ordertable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public void group(final TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
        orderTables.clear();
    }

    public List<OrderTable> asList() {
        return Collections.unmodifiableList(orderTables);
    }
}
