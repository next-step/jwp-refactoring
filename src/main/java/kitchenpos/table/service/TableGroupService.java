package kitchenpos.table.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.entity.OrderRepository;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.domain.entity.OrderTableRepository;
import kitchenpos.table.domain.entity.TableGroup;
import kitchenpos.table.domain.entity.TableGroupRepository;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.domain.value.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.order.exception.NotFoundTableGroupException;
import kitchenpos.order.exception.OrderStatusInCookingOrMealException;
import kitchenpos.table.exception.OrderTableCountException;
import kitchenpos.table.exception.OrderTableHasTableGroupException;
import kitchenpos.table.exception.OrderTableIsNotEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private static final int MIN_GROUP_SIZE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableRepository
            .findAllById(getOrderTableIds(tableGroupRequest));
        validateOrderTable(tableGroupRequest, orderTables);
        return TableGroupResponse
            .of(tableGroupRepository.save(new TableGroup(new OrderTables(orderTables))));
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        List<OrderTable> orderTables = findOrderTables(tableGroup);
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrderStatusInCookingOrMeal(orderTableIds);
        orderTables.forEach(OrderTable::unGroup);
    }

    private List<OrderTable> findOrderTables(TableGroup tableGroup) {
        return orderTableRepository
            .findAllByTableGroupId(tableGroup.getId());
    }

    private void validateOrderTable(TableGroupRequest tableGroupRequest,
        List<OrderTable> orderTables) {
        validateOrderTableCount(tableGroupRequest, orderTables);
        validateOrderTableIsNotEmpty(orderTables);
        validateOrderTableHasTableGroup(orderTables);
    }

    private void validateOrderTableHasTableGroup(List<OrderTable> orderTables) {
        if (orderTables.stream()
            .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroup()))) {
            throw new OrderTableHasTableGroupException();
        }
    }

    private void validateOrderTableIsNotEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream()
            .anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new OrderTableIsNotEmptyException();
        }
    }

    private void validateOrderTableCount(TableGroupRequest tableGroupRequest,
        List<OrderTable> orderTables) {
        if (tableGroupRequest.getOrderTables().size() != orderTables.size()) {
            throw new NotFoundOrderTableException();
        }
    }

    private void validateOrderTableCountSmallerThanTwo(TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.getOrderTables().size() < MIN_GROUP_SIZE) {
            throw new OrderTableCountException();
        }
    }

    private List<Long> getOrderTableIds(TableGroupRequest tableGroupRequest) {
        validateOrderTableCountSmallerThanTwo(tableGroupRequest);
        return tableGroupRequest.getOrderTables()
            .stream().map(OrderTableRequest::getId)
            .collect(Collectors.toList());
    }

    private void validateOrderStatusInCookingOrMeal(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderStatusInCookingOrMealException();
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(NotFoundTableGroupException::new);
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}
