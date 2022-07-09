package kitchenpos.order.event;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.event.TableUnGroupEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class TableUnGroupEventHandler {
    private final OrderRepository orderRepository;

    public TableUnGroupEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableUnGroupEventPublisher event) {
        OrderTables orderTables = event.getOrderTables();
        validateOrderTableStatus(orderTables.extractIds());
        orderTables.changeTableGroup();
    }

    public void validateOrderTableStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new BadRequestException(ErrorCode.CAN_NOT_CHANGE_COOKING_AND_MEAL);
        }
    }
}
