package kitchenpos.table.application;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUngroup(List<Long> orderTableIds) {
        List<Order> orders = orderRepository.findByOrderTableIdIn(orderTableIds);
        orders.forEach(Order::checkCookingOrMeal);
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_TABLE_ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
        }

        List<Order> orders = findAllByOrderTableId(orderTable.getId());
        orders.forEach(Order::checkCookingOrMeal);
    }

    private List<Order> findAllByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId);
    }
}
