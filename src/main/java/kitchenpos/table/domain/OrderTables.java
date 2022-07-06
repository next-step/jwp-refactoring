package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private final List<OrderTable> orderTables;

    public OrderTables() {
        this(new ArrayList<>());
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> values() {
        return orderTables;
    }

    public void addTableGroup(TableGroup tableGroup) {
        orderTables.forEach(o -> o.changeTableGroup(tableGroup));
    }

    public int size() {
        return orderTables.size();
    }

    public boolean existsEmptyTable() {
        return orderTables.stream()
                .anyMatch(OrderTable::isEmpty);
    }

    public boolean existsTableGroup() {
        return orderTables.stream()
                .anyMatch(o -> o.getTableGroup() != null);
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
