package kitchenpos.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.event.TableGroupedEvent;
import kitchenpos.tablegroup.event.TableUnGroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableGroupEventListener {

    private final OrderTableRepository orderTableRepository;

    public TableGroupEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void onUnGroupedEvent(TableUnGroupedEvent tableUnGroupedEvent) {
        orderTableRepository.findListByTableGroupId(tableUnGroupedEvent
                .getTableGroupId())
                .ifPresent(orderTables -> orderTables
                        .forEach(orderTable -> orderTable
                                .changeTableGroupId(null)));
    }

    @EventListener
    public void onGroupedEvent(TableGroupedEvent tableGroupedEvent) {
        List<OrderTable> orderTables = tableGroupedEvent.getOrderTables();
        orderTables.forEach(orderTable -> {
            orderTable.changeEmpty(false);
            orderTable.changeTableGroupId(tableGroupedEvent.getTableGroupId());
        });
    }
}
