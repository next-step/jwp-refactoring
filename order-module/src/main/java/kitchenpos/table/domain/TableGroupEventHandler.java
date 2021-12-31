package kitchenpos.table.domain;

import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupDeleteEvent;
import kitchenpos.tablegroup.domain.TableGroupSaveEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupEventHandler(final OrderTableRepository orderTableRepository, final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @EventListener
    public void tableGroupSaveEventHandler(TableGroupSaveEvent tableGroupSaveEvent) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupSaveEvent.getOrderTableId());
        orderTables.stream()
                .forEach(orderTable -> orderTable.changeTableGroup(tableGroupSaveEvent.getTableGroupId()));
    }

    @EventListener
    public void tableGroupDeleteEventHandler(TableGroupDeleteEvent tableGroupDeleteEvent) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroupDeleteEvent.getTableGroupId());
        orderTables.stream()
                .forEach(orderTable -> orderTable.ungroup(orderTableValidator));
    }
}
