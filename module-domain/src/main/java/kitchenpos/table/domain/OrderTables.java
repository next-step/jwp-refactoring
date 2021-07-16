package kitchenpos.table.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public void add(final OrderTable orderTable) {
        if (!this.orderTables.contains(orderTable)) {
            this.orderTables.add(orderTable);
        }
    }

    public void ungroup() {
        this.orderTables.clear();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
