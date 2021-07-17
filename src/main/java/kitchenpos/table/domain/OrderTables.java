package kitchenpos.table.domain;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @OneToMany
    @JoinColumn(name = "table_group_id")
    private final List<OrderTable> orderTables;

    protected OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getUnmodifiableList() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public void groupBy(Long orderTableId) {
        orderTables.forEach(orderTable -> orderTable.registerGroup(orderTableId));
    }
}
