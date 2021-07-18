package kitchenpos.order.event;

import kitchenpos.common.exception.CannotUpdateException;
import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static kitchenpos.common.exception.Message.ERROR_ORDER_TABLE_CANNOT_BE_EMPTY_WHEN_ORDERS_IN_COOKING_OR_MEAL;

@Component
public class TableEmptyStatusChangedEventHandler {

    private final OrderRepository orderRepository;

    public TableEmptyStatusChangedEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTableEmptyStatusChangedEvent(TableEmptyStatusChangedEvent event) {
        Long orderTableId = event.getOrderTableRequest().getId();
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (isNotCompleteStatus(orders)) {
            throw new CannotUpdateException(ERROR_ORDER_TABLE_CANNOT_BE_EMPTY_WHEN_ORDERS_IN_COOKING_OR_MEAL);
        }
    }

    private boolean isNotCompleteStatus(List<Order> orders) {
        return orders.stream()
                .anyMatch(Order::cannotBeChanged);
    }
}
