package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.exception.CannotChangeTableEmptyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void changeEmptyValidator(OrderTable orderTable) {
        if (orderTable.getTableGroup() != null) {
            throw new CannotChangeTableEmptyException(String.format("table id is %d", orderTable.getId()));
        }
        orderStatusValidator(orderTable);
    }

    public void ungroupValidator(OrderTable orderTable) {
        orderStatusValidator(orderTable);
    }

    private void orderStatusValidator(OrderTable orderTable) {
        List<Order> orders = orderRepository.findByOrderTable(orderTable.getId());
        orders.stream()
                .map(Order::getOrderStatus)
                .filter(orderStatus -> orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL))
                .findFirst()
                .ifPresent(orderStatus -> {
                    throw new IllegalArgumentException();
                });
    }
}
