package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderValidator {
    private OrderRepository orderRepository;

    public OrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public static void validateCreateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTableNotEmpty(orderTable);
    }

    private static void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
