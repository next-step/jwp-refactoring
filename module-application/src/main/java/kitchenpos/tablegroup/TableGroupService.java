package kitchenpos.tablegroup;

import kitchenpos.exception.NotFoundException;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private static final int LESS_THAN_TWO = 2;
    private static final String ERR_TEXT_ORDER_TABLES_SIZE_MUST_BE_GREATER_THAN_TWO = "주문 테이블이 2개 이상이어야 합니다.";
    private static final String ERR_TEXT_CONTAINS_INVALID_ORDER_TABLE_IN_REQUEST = "유효하지 않은 주문 테이블이 포함되어 있습니다.";
    private static final String ERR_TEXT_ALREADY_CONTAINS_OTHER_GROUP = "이미 다른 그룹에 포함되어 있습니다.";
    private static final String ERR_TEXT_CAN_NOT_UNGROUP_WHEN_ORDER_STATUS_IS_COOKING_OR_MEAL = "주문 테이블 상태가 조리, 또는 식사인 경우 그룹을 해제할 수 없습니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        moreThanOneOrderTableIsRequired(orderTableIds);

        isNotRequiredThatContainsInOtherTableGroup(getAllRequestOrderTablesThatExist(orderTableIds));

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.createInstance());
        savedTableGroup.groupingOrderTable(orderTableRepository.findAllByIdIn(orderTableIds));

        return new TableGroupResponse(savedTableGroup);
    }

    private void moreThanOneOrderTableIsRequired(final List<Long> orderTableIds) {
        if (orderTableIds.size() < LESS_THAN_TWO) {
            throw new IllegalArgumentException(ERR_TEXT_ORDER_TABLES_SIZE_MUST_BE_GREATER_THAN_TWO);
        }
    }

    private List<OrderTable> getAllRequestOrderTablesThatExist(final List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException(ERR_TEXT_CONTAINS_INVALID_ORDER_TABLE_IN_REQUEST);
        }
        return savedOrderTables;
    }

    private void isNotRequiredThatContainsInOtherTableGroup(final List<OrderTable> savedOrderTables) {
        if (savedOrderTables.stream()
            .anyMatch(OrderTable::alreadyContainsInOtherTableGroup)) {
            throw new IllegalArgumentException(ERR_TEXT_ALREADY_CONTAINS_OTHER_GROUP);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        canNotUngroupWhenOrderStatusIsCookingOrMeal(orderTableIds);

        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(NotFoundException::new);

        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    private void canNotUngroupWhenOrderStatusIsCookingOrMeal(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ERR_TEXT_CAN_NOT_UNGROUP_WHEN_ORDER_STATUS_IS_COOKING_OR_MEAL);
        }
    }
}
