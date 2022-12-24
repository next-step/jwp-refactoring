package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableEmptyChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableEmptyChangedEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableEmptyChangedEventHandler(OrderTableRepository orderTableRepository,
        OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void handle(TableEmptyChangedEvent event) {
        OrderTable orderTable = findById(event.getOrderTableId());

        if (hasUncompletedOrder(orderTable.getId())) {
            throw new IllegalArgumentException("그룹을 해제할 오더 테이블이 없습니다.");
        }
        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("이미 그룹에 포함되어 있습니다.");
        }
    }

    private boolean hasUncompletedOrder(Long orderTableIds) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableIds, OrderStatus.getNotCompletedStatuses());
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("오더 테이블을 찾을 수 없습니다."));
    }
}
