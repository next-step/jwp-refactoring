package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private final List<OrderTable> orderTables = new ArrayList<>();

    public void addOrderTable(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
