package kitchenpos.ordertable.validator;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validator(OrderTable orderTable) {
        List<Order> orders = findAllOrderByOrderTableId(orderTable);
        validateNotCompleteOrders(orders);
    }

    private List<Order> findAllOrderByOrderTableId(OrderTable orderTable) {
        return orderRepository.findAllByOrderTableId(orderTable.getId());
    }

    private void validateNotCompleteOrders(List<Order> orders) {
        orders.forEach(Order::validateNotCompleteOrder);
    }
}
