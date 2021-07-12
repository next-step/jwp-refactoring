package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderAlreadyExistsException;
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
public class TableGroupService {
    private static final int ORDER_TABLE_MINIMUM_SIZE = 2;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findByTableGroupId(tableGroupId));
        validateOrderStatusIsCookingOrMeal(orderTables.getOrderTableIds());
        orderTables.ungroupOrderTables();
    }

    @Transactional
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
            throw new MisMatchedOrderTablesSizeException();
        }
    }

    private void validateOrderStatusIsCookingOrMeal(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderAlreadyExistsException("수정할 수 없는 주문이 존재합니다.");
        }
    }
}
