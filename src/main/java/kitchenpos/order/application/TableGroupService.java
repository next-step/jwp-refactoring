package kitchenpos.order.application;

import kitchenpos.order.domain.*;
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

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateOrderTableCountSmallerThanTwo(tableGroupRequest);
        List<Long> orderTableIds = getOrderTableIds(tableGroupRequest);
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTable(tableGroupRequest, orderTables);
        TableGroup tableGroup = new TableGroup(orderTables);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private void validateOrderTable(TableGroupRequest tableGroupRequest, List<OrderTable> orderTables) {
        validateOrderTableCount(tableGroupRequest, orderTables);
        validateOrderTableIsNotEmpty(orderTables);
        validateOrderTableInTableGroup(orderTables);
    }

    private void validateOrderTableInTableGroup(List<OrderTable> orderTables) {
        if (orderTables.stream()
                .filter(orderTable -> Objects.nonNull(orderTable.getTableGroup()))
                .findAny()
                .isPresent()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableIsNotEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream()
                .filter(orderTable -> !orderTable.isEmpty())
                .findAny()
                .isPresent()) {
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

    public void ungroup(final java.lang.Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        final List<java.lang.Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTableRepository.save(orderTable);
        }
    }
}
