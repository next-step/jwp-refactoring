package kitchenpos.order.application;

import kitchenpos.table.domain.TableUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableUngroupEventListener {
    private static final String INVALID_CHANGE_ORDER_STATUS = "변경 할 수 없는 주문 상태입니다";
    private final OrderService orderService;

    public TableUngroupEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @EventListener
    public void ungroupTable(TableUngroupedEvent tableUngroupedEvent) {
        List<Long> orderTableIds = tableUngroupedEvent.getOrderTableIds();
        if (orderService.isUpgroupTableStatus(orderTableIds)) {
            throw new IllegalStateException(INVALID_CHANGE_ORDER_STATUS);
        }
    }
}
