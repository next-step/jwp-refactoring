package kitchenpos.table.domain;

import java.util.List;


public class OrderTables {

    private List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getValues() {
        return orderTables;
    }
}