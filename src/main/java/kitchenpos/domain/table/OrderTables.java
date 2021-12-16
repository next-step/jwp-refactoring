package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public final class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
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
}
