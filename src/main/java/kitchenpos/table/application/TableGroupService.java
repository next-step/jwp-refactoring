package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.common.exception.NotExistTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
        final List<OrderTable> orderTables = getOrderTables(tableGroupRequest);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> getOrderTables(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTables()
                .stream()
                .map(orderTableRequest -> findByOrderTable(orderTableRequest.getId()))
                .collect(Collectors.toList());

        return orderTables;
    }

    private OrderTable findByOrderTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NotExistTableException("존재하지 않는 테이블입니다."));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);

        Orders findOrders = getOrders(orderTables);

        findOrders.ungroupValidation();
        orderTables.forEach(OrderTable::ungroup);
    }

    private Orders getOrders(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());

        List<Order> findOrderTables = orderRepository.findByOrderTableIdIn(orderTableIds);
        return new Orders(findOrderTables);
    }
}
