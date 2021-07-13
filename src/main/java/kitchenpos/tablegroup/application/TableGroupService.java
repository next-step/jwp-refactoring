package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional
public class TableGroupService {
    private static final int ORDER_TABLE_MINIMUM_SIZE = 2;
    private final TableService tableService;
    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(final TableService tableService, final OrderService orderService, final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository) {
        this.tableService = tableService;
        this.orderService = orderService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findByTableGroupId(tableGroupId));
        orderService.validateExistsOrdersStatusIsCookingOrMeal(orderTables.getOrderTableIds());
        orderTables.ungroupOrderTables();
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = getOrderTables(tableGroupRequest);
        TableGroup tableGroup = makeTableGroup(orderTables);
        return TableGroupResponse.of(tableGroup);
    }

    private List<OrderTable> getOrderTables(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        validateMinimumOrderTableSize(orderTableIds);
        List<OrderTable> orderTables = tableService.findOrderTablesByIds(orderTableIds);
        validateMisMatchedOrderTableSize(orderTableIds, orderTables);
        return orderTables;
    }

    private TableGroup makeTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableIsEmptyOrHasTableGroup(orderTable);
            tableGroup.addOrderTable(orderTable);
        }
        return tableGroup;
    }

    private void validateOrderTableIsEmptyOrHasTableGroup(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.");
        }
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
        }
    }

    private void validateMinimumOrderTableSize(List<Long> orderTableIds) {
        if (orderTableIds.size() < ORDER_TABLE_MINIMUM_SIZE) {
            throw new IllegalArgumentException("정산 그룹 생성은 2개 이상의 테이블만 가능합니다.");
        }
    }

    private void validateMisMatchedOrderTableSize(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new MisMatchedOrderTablesSizeException("입력된 항목과 조회결과가 일치하지 않습니다. size1 : " + orderTables.size() + ", size2 : " + orderTableIds.size());
        }
    }
}
