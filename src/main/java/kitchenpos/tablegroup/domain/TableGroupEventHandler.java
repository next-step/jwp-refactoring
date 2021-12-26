package kitchenpos.tablegroup.domain;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;

@Component
public class TableGroupEventHandler {

    private final OrderTableRepository orderTableRepository;

    public TableGroupEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void createTargetGroupHandle(TableGroupSavedEvent event) {
        List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        for (OrderTable findOrderTable : findOrderTables) {
            findOrderTable.changeTableGroup(event.getTableGroupId());
        }
    }

    @EventListener
    @Transactional
    public void ungroupHandle(TableUngroupEvent event) {
        List<OrderTable> findOrderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        for (OrderTable findOrderTable : findOrderTables) {
            findOrderTable.deleteTableGroup();
        }
    }
}
