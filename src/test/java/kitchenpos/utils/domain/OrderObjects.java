package kitchenpos.utils.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderObjects {
    private final OrderTableObjects orderTableObjects;
    private final Order order1;
    private final Order order2;
    private final Order order3;
    private final Order order4;
    private final Order order5;

    public OrderObjects() {
        orderTableObjects = new OrderTableObjects();
        order1 = new Order();
        order2 = new Order();
        order3 = new Order();
        order4 = new Order();
        order5 = new Order();

        order1.setId(1L);
        order1.setOrderStatus(OrderStatus.COOKING.name());
        order1.setOrderTableId(orderTableObjects.getOrderTable1().getId());

        order2.setId(2L);
        order2.setOrderStatus(OrderStatus.COOKING.name());
        order2.setOrderTableId(orderTableObjects.getOrderTable2().getId());

        order3.setId(3L);
        order3.setOrderStatus(OrderStatus.COOKING.name());
        order3.setOrderTableId(orderTableObjects.getOrderTable3().getId());

        order4.setId(4L);
        order4.setOrderStatus(OrderStatus.COOKING.name());
        order4.setOrderTableId(orderTableObjects.getOrderTable4().getId());

        order5.setId(5L);
        order5.setOrderStatus(OrderStatus.COOKING.name());
        order5.setOrderTableId(orderTableObjects.getOrderTable5().getId());
    }

    public Order getOrder1() {
        return order1;
    }

    public Order getOrder2() {
        return order2;
    }

    public Order getOrder3() {
        return order3;
    }

    public Order getOrder4() {
        return order4;
    }

    public Order getOrder5() {
        return order5;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(Arrays.asList(order1, order2, order3, order4, order5));
    }
}
