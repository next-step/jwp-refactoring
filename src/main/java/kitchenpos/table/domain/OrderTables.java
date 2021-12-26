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

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public static OrderTables empty() {
        return new OrderTables();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTables)) return false;
        OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
