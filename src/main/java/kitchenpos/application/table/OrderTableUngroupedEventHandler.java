package kitchenpos.application.table;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.event.TableGroupUngroupedEvent;
import kitchenpos.domain.tablegroup.TableGroup;

@Component
public class OrderTableUngroupedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableUngroupedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupUngroupedEvent event) {
        TableGroup tableGroup = event.getTableGroup();
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        orderTables.forEach(OrderTable::ungroup);
    }
}
