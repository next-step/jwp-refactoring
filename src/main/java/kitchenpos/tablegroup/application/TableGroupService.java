package kitchenpos.tablegroup.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

//    @Transactional
//    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
//        List<OrderTable> orderTables = findAllOrderTablesById(tableGroupRequest.getOrderTables());
//        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(OrderTables.from(orderTables)));
//        return TableGroupResponse.from(tableGroup);
//    }
//
//    @Transactional
//    public void ungroup(final Long tableGroupId) {
//        TableGroup tableGroup = findTableGroupById(tableGroupId);
//        List<Order> orders = findAllOrderByOrderTableIds(tableGroup.getOrderTables());
//        tableGroup.ungroup(orders);
//    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = OrderTables.from(findAllOrderTablesById(tableGroupRequest.getOrderTables()));
        List<OrderTableResponse> orderTableResponses = orderTables.getOrderTables()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.from());
        orderTables.registerTableGroup(tableGroup.getId());
        return TableGroupResponse.from(tableGroup, orderTableResponses);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        OrderTables orderTables = OrderTables.from(findAllOrderTableByTableGroupId(tableGroupId));
        List<Order> orders = findAllOrderByOrderTableIds(orderTables.getOrderTables());
        tableGroup.ungroup(orders, orderTables);
    }

    private List<OrderTable> findAllOrderTablesById(List<Long> ids) {
        return ids.stream()
            .map(this::findOrderTableById)
            .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    private List<Order> findAllOrderByOrderTableIds(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        return orderRepository.findAllByOrderTableIdIn(orderTableIds);
    }

    private List<OrderTable> findAllOrderTableByTableGroupId(Long id) {
        return orderTableRepository.findAllByTableGroupId(id);
    }
}
