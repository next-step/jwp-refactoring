package kitchenpos.api.application.grouping;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.table.OrderTableRepository;
import kitchenpos.common.domain.tablegroup.event.TableGroupUngroupedEvent;

@Component
public class OrderTableUngroupedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableUngroupedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupUngroupedEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        orderTables.forEach(OrderTable::ungroup);
    }
}
