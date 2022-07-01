package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void changeOrderTable() {
        for (OrderTable emptyTable : orderTables) {
            emptyTable.changeOrderTable();
        }
    }

    public boolean containsOrderTable() {
        return orderTables.stream()
                .anyMatch(OrderTable::isOrderTable);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int size() {
        return orderTables.size();
    }

    public void clear() {
        orderTables.clear();
    }

}
