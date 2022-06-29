package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
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
