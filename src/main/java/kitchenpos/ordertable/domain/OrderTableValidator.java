package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.ClosedTableOrderException;
import kitchenpos.ordertable.exception.OrderIsNotCompleteException;
import kitchenpos.ordertable.exception.TableNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderTableRepository orderTableRepository,
        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void validateNotOrderClosedTable(Long tableId) {
        OrderTable orderTable = orderTableRepository.findById(tableId)
            .orElseThrow(TableNotFoundException::new);
        if (orderTable.isOrderClose()) {
            throw new ClosedTableOrderException();
        }
    }

    public void validateAllOrdersInTableComplete(Long tableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(tableId);
        orders.stream()
            .forEach(order -> validateOrderIsComplete(order));
    }

    private void validateOrderIsComplete(Order order) {
        if (!order.isCompleteStatus()) {
            throw new OrderIsNotCompleteException();
        }
    }
}
