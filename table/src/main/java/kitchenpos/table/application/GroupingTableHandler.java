package kitchenpos.table.application;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.common.event.GroupingOrderTableEvent;
import kitchenpos.common.event.UngroupOrderTableEvent;

@Component
public class GroupingTableHandler {
    private final OrderTableRepository orderTableRepository;

    public GroupingTableHandler(
        OrderTableRepository orderTableRepository
    ) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(GroupingOrderTableEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(event.getOrderTableIds());

        orderTables.stream().forEach(
            orderTable -> orderTable.groupingTable(TableGroupId.of(event.getTableGroupId()))
        );
    }

    @EventListener
    public void handle(UngroupOrderTableEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(event.getOrderTableIds());

        orderTables.stream().forEach(
            orderTable -> orderTable.unGroupTable()
        );
    }
}
