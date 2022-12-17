package kitchenpos.common.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.validator.OrderTableValidator;
import kitchenpos.tablegroup.event.TableGroupedEvent;
import kitchenpos.tablegroup.event.TableUnGroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableGroupEventListener {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupEventListener(OrderTableRepository orderTableRepository,
                                   OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
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
            orderTable.changeEmpty(false, orderTableValidator);
            orderTable.changeTableGroupId(tableGroupedEvent.getTableGroupId());
        });
    }
}
