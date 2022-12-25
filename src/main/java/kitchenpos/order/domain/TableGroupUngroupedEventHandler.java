package kitchenpos.order.domain;

import kitchenpos.table.domain.TableGroupUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TableGroupUngroupedEventHandler {
    private final OrderRepository orderRepository;

    public TableGroupUngroupedEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @EventListener
    public void handle(TableGroupUngroupedEvent event) {
        List<Long> orderTableIds = event.getOrderTableIds();
        if (hasUncompletedOrder(orderTableIds)) {
            throw new IllegalArgumentException("그룹을 해제할 오더 테이블이 없습니다.");
        }
    }

    private boolean hasUncompletedOrder(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, OrderStatus.getNotCompletedStatuses());
    }
}
