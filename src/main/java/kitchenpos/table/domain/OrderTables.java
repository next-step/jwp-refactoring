package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void clear() {
        orderTables.clear();
    }

    public List<OrderTable> asList() {
        return orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
