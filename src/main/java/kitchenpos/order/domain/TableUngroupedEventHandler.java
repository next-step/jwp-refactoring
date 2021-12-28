package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.UNGROUP_IMPOSSIBLE_ORDER_STATUS;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableUngroupedEventHandler {

    private final OrderRepository orderRepository;

    public TableUngroupedEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableUngroupedEvent event) {
        validateForUngroup(event.getOrderTables());
    }

    private void validateForUngroup(OrderTables orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables.extractIds(),
            UNGROUP_IMPOSSIBLE_ORDER_STATUS)) {
            throw new IllegalArgumentException("주문 상태가 요리중 또는 식사 상태여서 그룹 해제가 불가능합니다");
        }
    }
}
