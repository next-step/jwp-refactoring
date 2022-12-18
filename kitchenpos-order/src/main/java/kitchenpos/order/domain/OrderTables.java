package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public boolean isNotEmpty() {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());
    }

    public boolean hasGroup() {
        return orderTables.stream()
                .anyMatch(OrderTable::hasTableGroup);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(orderTables);
    }
}
