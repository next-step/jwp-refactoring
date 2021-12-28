package kitchenpos.table.ui;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.OrderTableDomainValidation;
import kitchenpos.table.domain.event.GroupByEvent;
import kitchenpos.table.domain.event.GroupTable;
import kitchenpos.table.domain.event.UnGroupByEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableEvent {

    private final OrderTableDao orderTableDao;
    private final OrderTableDomainValidation orderTablesValidation;

    public OrderTableEvent(OrderTableDao orderTableDao,
        OrderTableDomainValidation tablesValidation) {
        this.orderTableDao = orderTableDao;
        this.orderTablesValidation = tablesValidation;
    }

    @EventListener
    @Transactional
    public void groupByOrderTable(GroupByEvent groupByEvent) {
        GroupTable groupTable = groupByEvent.getGroupTable();
        Long tableGroupId = groupTable.getTableGroupId();
        List<Long> orderTableIds = groupTable.getOrderTableIds();

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);

        orderTablesValidation.valid(orderTables);
        orderTables.forEach(s -> s.group(tableGroupId));
    }

    @EventListener
    @Transactional
    public void unGroupByOrderTable(UnGroupByEvent unGroupByEvent) {
        List<OrderTable> orderTables = orderTableDao.findTableGroupById(
            unGroupByEvent.getGroupTableId());
        orderTables.forEach(OrderTable::unGroup);
    }
}
