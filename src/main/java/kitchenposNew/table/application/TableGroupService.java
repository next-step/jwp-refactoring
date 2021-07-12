package kitchenposNew.table.application;

import kitchenposNew.order.domain.Order;
import kitchenposNew.order.domain.OrderRepository;
import kitchenposNew.order.exception.NotFoundOrder;
import kitchenposNew.order.exception.NotFoundOrderTable;
import kitchenposNew.table.domain.OrderTable;
import kitchenposNew.table.domain.OrderTableRepository;
import kitchenposNew.table.domain.TableGroup;
import kitchenposNew.table.domain.TableGroupRepository;
import kitchenposNew.table.dto.OrderTableResponse;
import kitchenposNew.table.dto.TableGroupRequest;
import kitchenposNew.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds)
                .orElseThrow(() -> new NotFoundOrderTable());
        TableGroup persistTableGroup = tableGroupRequest.toTableGroup(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(persistTableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public List<OrderTableResponse> ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId)
                .orElseThrow(() -> new NotFoundOrderTable());
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final List<Order> orders = orderRepository.existsByOrderTableIdIn(orderTableIds)
                .orElseThrow(() -> new NotFoundOrder());
        orders.forEach(
                order -> order.ungroup()
        );
        tableGroupRepository.deleteById(tableGroupId);
        return orderTables.stream()
                .map(orderTable -> OrderTableResponse.of(orderTable))
                .collect(Collectors.toList());
    }
}
