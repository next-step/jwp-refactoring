package kitchenpos.order.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.entity.OrderRepository;
import kitchenpos.order.domain.entity.OrderTable;
import kitchenpos.order.domain.entity.OrderTableRepository;
import kitchenpos.order.domain.entity.TableGroup;
import kitchenpos.order.domain.entity.TableGroupRepository;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.domain.value.OrderTables;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.exception.NotFoundOrderTableException;
import kitchenpos.order.exception.NotFoundTableGroupException;
import kitchenpos.order.exception.OrderStatusInCookingOrMealException;
import kitchenpos.order.exception.OrderTableCountException;
import kitchenpos.order.exception.OrderTableHasTableGroupException;
import kitchenpos.order.exception.OrderTableIsNotEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

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
        validateOrderTableCountSmallerThanTwo(tableGroupRequest);
        List<Long> orderTableIds = getOrderTableIds(tableGroupRequest);
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTable(tableGroupRequest, orderTables);
        TableGroup tableGroup = new TableGroup(new OrderTables(orderTables));
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
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
        if (tableGroupRequest.getOrderTables().size() < 2) {
            throw new OrderTableCountException();
        }
    }

    private List<Long> getOrderTableIds(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables()
            .stream().map(OrderTableRequest::getId)
            .collect(Collectors.toList());
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository
            .findAllByTableGroupId(tableGroup.getId());
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrderStatus(orderTableIds);
        orderTables.forEach(OrderTable::unGroup);
    }

    private void validateOrderStatus(List<Long> orderTableIds) {
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
