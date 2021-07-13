package kitchenpos.order.service;

import kitchenpos.order.domain.entity.*;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.domain.value.OrderTables;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
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

    private void validateOrderTable(TableGroupRequest tableGroupRequest, List<OrderTable> orderTables) {
        validateOrderTableCount(tableGroupRequest, orderTables);
        validateOrderTableIsNotEmpty(orderTables);
        validateOrderTableInTableGroup(orderTables);
    }

    private void validateOrderTableInTableGroup(List<OrderTable> orderTables) {
        if (orderTables.stream()
                .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroup()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableIsNotEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableCount(TableGroupRequest tableGroupRequest, List<OrderTable> orderTables) {
        if (tableGroupRequest.getOrderTables().size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableCountSmallerThanTwo(TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.getOrderTables().size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getOrderTableIds(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables()
                .stream().map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrderStatus(orderTableIds);
        orderTables.forEach(OrderTable::unGroup);
    }

    private void validateOrderStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
