package kitchenpos.application.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.order.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void checkOrderStatusCookingOrMeal(OrderTable orderTable) {

        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        orders
                .forEach(Order::checkOrderStatusCookingOrMeal);
    }
}
