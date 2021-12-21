package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;

@Embeddable
public final class OrderTables {
    private final List<OrderTable> orderTables;

    protected OrderTables() {
        orderTables = new ArrayList<>();
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }
    
    public boolean remove(OrderTable orderTable) {
        return this.orderTables.remove(orderTable);
    }
    
    public OrderTable remove(int index) {
        return this.orderTables.remove(index);
    }
    
    public boolean add(OrderTable orderTable) {
        return this.orderTables.add(orderTable);
    }

    public int size() {
        return this.orderTables.size();
    }

    public OrderTable get(int index) {
        return this.orderTables.get(index);
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream()
                                .map(OrderTable::getId)
                                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return this.orderTables.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderTables)) {
            return false;
        }
        OrderTables orderTables = (OrderTables) o;
        return Objects.equals(this.orderTables, orderTables.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderTables);
    }
}
