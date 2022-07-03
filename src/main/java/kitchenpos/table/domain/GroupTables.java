package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class GroupTables {
    @OneToMany(mappedBy = "tableGroup")
    @Column(insertable = false)
    private List<OrderTable> orderTables = new ArrayList<>();

    public void addAll(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
        orderTables.clear();
    }

    public boolean isEmpty() {
        return orderTables.isEmpty();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
