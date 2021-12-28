package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.tablegroup.event.GroupEvent;
import kitchenpos.tablegroup.event.GroupInfo;
import kitchenpos.tablegroup.event.UngroupEvent;
import kitchenpos.tablegroup.exception.DuplicateTablesException;
import kitchenpos.tablegroup.exception.IllegalGroupingTableStateException;
import kitchenpos.tablegroup.exception.NotEnoughTablesException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class GroupingEventHandler {

    private static final int MIN_NUMBER_OF_TABLES_IN_GROUP = 2;

    private final OrderTableRepository orderTableRepository;

    public GroupingEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleGroupTables(GroupEvent groupEvent) {
        GroupInfo groupInfo = groupEvent.getGroupInfo();
        List<Long> orderTableIds = groupInfo.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);
        validateAtLeastTwoDistinctTables(orderTables, orderTableIds);
        validateGroupingCondition(orderTables);

        groupTables(orderTables, groupInfo.getTableGroupId());
    }

    protected void groupTables(List<OrderTable> orderTables, Long tableGroupId) {
        orderTables.stream()
            .forEach(orderTable -> orderTable.groupIn(tableGroupId));
    }

    private void validateGroupingCondition(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validateEmptyTable(orderTable);
            validateNotInAnyGroup(orderTable);
        }
    }

    private void validateAtLeastTwoDistinctTables(List<OrderTable> orderTables,
        List<Long> orderTableIds) {

        if (orderTables.size() != orderTableIds.size()) {
            throw new DuplicateTablesException();
        }

        if (orderTables.size() < MIN_NUMBER_OF_TABLES_IN_GROUP) {
            throw new NotEnoughTablesException();
        }
    }

    private void validateEmptyTable(OrderTable orderTable) {
        if (!orderTable.isOrderClose()) {
            throw new IllegalGroupingTableStateException();
        }
    }

    private void validateNotInAnyGroup(OrderTable orderTable) {
        if (orderTable.hasGroup()) {
            throw new IllegalGroupingTableStateException();
        }
    }

    @EventListener
    public void handleUnGroupTables(UngroupEvent ungroupEvent) {
        Long tableGroupId = ungroupEvent.getTableGroupId();
        ungroupTables(tableGroupId);
    }

    protected void ungroupTables(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        orderTables.stream()
            .forEach(OrderTable::unGroup);
    }
}
