package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.tablegroup.event.GroupEvent;
import kitchenpos.tablegroup.event.GroupInfo;
import kitchenpos.tablegroup.event.UngroupEvent;
import kitchenpos.tablegroup.exception.GroupTablesException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GroupingService {

    private static final String ERROR_MESSAGE_NOT_ENOUGH_TABLES = "주문 테이블이 2개 이상일때 그룹화 가능 합니다.";
    private static final String ERROR_MESSAGE_NOT_EMPTY_TABLE = "그룹화를 위해선 테이블들이 주문종료 상태여야 합니다.";
    private static final String ERROR_MESSAGE_TABLE_ALREADY_IN_GROUP = "테이블 그룹에 이미 속해있는 주문테이블은 그룹화할 수 없습니다..";
    private static final String ERROR_MESSAGE_DUPLICATE_TALBES = "그룹대상 테이블에 중복이 존재합니다.";
    private static final int MIN_NUMBER_OF_TABLES_IN_GROUP = 2;

    private final OrderTableRepository orderTableRepository;

    public GroupingService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
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
            throw new GroupTablesException(ERROR_MESSAGE_DUPLICATE_TALBES);
        }

        if (orderTables.size() < MIN_NUMBER_OF_TABLES_IN_GROUP) {
            throw new GroupTablesException(ERROR_MESSAGE_NOT_ENOUGH_TABLES);
        }
    }

    private void validateEmptyTable(OrderTable orderTable) {
        if (!orderTable.isOrderClose()) {
            throw new GroupTablesException(ERROR_MESSAGE_NOT_EMPTY_TABLE);
        }
    }

    private void validateNotInAnyGroup(OrderTable orderTable) {
        if (orderTable.hasGroup()) {
            throw new GroupTablesException(ERROR_MESSAGE_TABLE_ALREADY_IN_GROUP);
        }
    }

    @EventListener
    @Transactional
    @Async
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
