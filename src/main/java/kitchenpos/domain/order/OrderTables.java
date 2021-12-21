package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }


    public void changeTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.changeTableGroup(tableGroup));
    }

    public void ungroup(List<Order> orders) {
        orderTables.forEach(
            orderTable -> orderTable.ungroup(getMatchOrders(orders, orderTable)));
    }

    private Orders getMatchOrders(List<Order> orders, OrderTable orderTable) {
        return orders.stream()
            .filter(order -> order.isMatchOrderTable(orderTable))
            .collect(Collectors.collectingAndThen(Collectors.toList(), Orders::of));
    }

}