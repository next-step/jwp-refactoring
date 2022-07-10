package kitchenpos.order.event;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.event.OrderTableChangeEmptyEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class OrderTableEmptyChangeHandler {
    private final OrderRepository orderRepository;

    public OrderTableEmptyChangeHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(OrderTableChangeEmptyEventPublisher event) {
        OrderTable orderTable = event.getOrderTable();
        validateOrderTableStatus(orderTable);
    }

    private void validateOrderTableStatus(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new BadRequestException(ErrorCode.CAN_NOT_CHANGE_COOKING_AND_MEAL);
        }
    }
}
