package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UnGroupWithGroupEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public UnGroupWithGroupEventHandler(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(OrderTableUnGroupEvent event) {
        final OrderTables orderTables = getOrderTables(event);
        validateExists(orderTables.getIds());
        orderTables.unGroup();
    }

    private OrderTables getOrderTables(OrderTableUnGroupEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        return new OrderTables(orderTables);
    }

    private void validateExists(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getCookingAndMeal())) {
            throw new IllegalArgumentException();
        }
    }
}
