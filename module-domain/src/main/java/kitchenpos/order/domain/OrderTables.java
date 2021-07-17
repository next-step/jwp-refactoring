package kitchenpos.order.domain;

import javax.persistence.JoinColumn;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> values() {
        return Collections.unmodifiableList(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public void updateTableGroup(Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.addTableGroup(tableGroupId);
        }
    }

    public void ungroup() {
        orderTables.stream()
            .forEach(OrderTable::ungroup);
        this.orderTables = Collections.EMPTY_LIST;
    }
}
