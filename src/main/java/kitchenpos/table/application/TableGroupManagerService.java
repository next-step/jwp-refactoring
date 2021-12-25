package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroupManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupManagerService implements TableGroupManager {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupManagerService(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public void grouping(Long tableGroupId, List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        OrderTables orderTables = OrderTables.of(savedOrderTables, orderTableIds.size());
        orderTables.group(tableGroupId);
    }

    public void ungrouping(Long tableGroupId) {
        List<OrderTable> savedOrderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.ungroup(orderTableValidator);
    }
}
