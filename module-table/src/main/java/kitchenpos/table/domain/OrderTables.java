package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.requireNonNull;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public void addAll(Long tableGroupId, List<OrderTable> orderTables) {
        requireNonNull(tableGroupId, "tableGroupId");
        requireNonNull(orderTables, "orderTables");
        for (OrderTable orderTable : orderTables) {
            add(tableGroupId, orderTable);
        }
    }

    private void add(Long tableGroupId, OrderTable orderTable) {
        if (!this.orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
        orderTable.bindTo(tableGroupId);
    }

    public List<Long> getIds() {
        return orderTables.stream()
                          .map(OrderTable::getId)
                          .collect(Collectors.toList());
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(orderTables);
    }

    public void clear() {
        orderTables.forEach(OrderTable::unbind);
        orderTables.clear();
    }
}
