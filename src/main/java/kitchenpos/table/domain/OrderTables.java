package kitchenpos.table.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public void addAll(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables;
    }

    public void unGroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
        }
    }
}
