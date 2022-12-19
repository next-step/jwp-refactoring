package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTablesValidator {
    private final OrderRepository orderRepository;

    public OrderTablesValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCookingAndMeal(OrderTables orderTables) {
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(findOrderIds(orderTables));
        orders.forEach(Order::validateCookingAndMeal);
    }

    private List<Long> findOrderIds(OrderTables orderTables) {
        return orderTables.list().stream()
                .map(OrderTable::id)
                .collect(Collectors.toList());
    }
}
