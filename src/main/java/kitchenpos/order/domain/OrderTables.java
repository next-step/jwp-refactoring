package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;
    
    protected OrderTables() {
        orderTables = new ArrayList<OrderTable>();
    }
    
    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
    
    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }
    
    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }
    
    public int count() {
        return orderTables.size();
    }
    
    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
    
    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.ungroup());
        orderTables = new ArrayList<OrderTable>();
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
