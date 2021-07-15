package kitchenpos.ordertable.domain;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupedEvent;
import kitchenpos.tablegroup.domain.TableUngroupedEvent;

@Component
public class TableGroupingEventHandler {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupingEventHandler(TableGroupRepository tableGroupRepository,
            OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupedEvent event) {
        tableGroupRepository.save(new TableGroup());
        List<OrderTable> orderTables = orderTableRepository.findAllById(event.getOrderTableIds());
        orderTables.forEach(orderTable -> orderTable.setTableGroupId(event.getTableGroupId()));
    }

    @EventListener
    @Transactional
    public void handle(TableUngroupedEvent event) {
        orderTableRepository.findAllByTableGroupId(event.getTableGroupId())
            .forEach(OrderTable::leaveTableGroup);
    }
}
