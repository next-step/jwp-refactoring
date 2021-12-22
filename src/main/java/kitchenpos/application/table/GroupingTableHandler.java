package kitchenpos.application.table;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.event.tablegroup.GroupingOrderTableEvent;
import kitchenpos.event.tablegroup.UngroupOrderTableEvent;
import kitchenpos.vo.TableGroupId;

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
