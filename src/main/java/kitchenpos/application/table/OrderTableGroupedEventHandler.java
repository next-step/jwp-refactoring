package kitchenpos.application.table;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.event.TableGroupGroupedEvent;
import kitchenpos.domain.tablegroup.TableGroup;

@Component
public class OrderTableGroupedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(TableGroupGroupedEvent event) {
        TableGroup tableGroup = event.getTableGroup();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());

        orderTables.forEach(orderTable -> orderTable.alignTableGroup(tableGroup.getId()));
    }
}
