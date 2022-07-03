package kitchenpos.table.event;

import kitchenpos.order.domain.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;

import static kitchenpos.order.domain.OrderStatus.UNCOMPLETED_STATUSES;

@Component
public class UngroupEventHandler {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public UngroupEventHandler(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void ungroupEvent(UngroupEvent event) {
        final OrderTables orderTables = OrderTables.of(
                orderTableRepository.findAllByTableGroupId(event.getTableGroupId()));
        validateOfUngroup(orderTables);
        orderTables.ungroup();
    }

    private void validateOfUngroup(OrderTables orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables.getIds(), UNCOMPLETED_STATUSES)) {
            throw new IllegalArgumentException("조리중 또는 식사중 상태에서는 그룹 해제가 불가능합니다");
        }
    }
}
