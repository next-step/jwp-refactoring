package kitchenpos.tablegroup.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.ErrorMessage.NOT_COMPLETED_ORDER;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTableRequest> orderTablesRequest = request.getOrderTables();
        OrderTables orderTables = OrderTables.from(findOrderTables(orderTablesRequest));
        List<OrderTableResponse> orderTableResponses = orderTables.findOrderTables()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.of());
        orderTables.group(tableGroup.getId());
        return TableGroupResponse.from(tableGroup, orderTableResponses);
    }

    private List<OrderTable> findOrderTables(final List<OrderTableRequest> orderTables) {
        List<Long> orderTableIds = mapToOrderTableIds(orderTables);

        validateOrderTableIds(orderTableIds);

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTableIds(final List<Long> orderTableIds) {
        List<Order> orders =
                orderRepository.findAllByOrderStatusInAndOrderTableIdIn(OrderStatus.onGoingOrderStatus(), orderTableIds);

        if (!orders.isEmpty()) {
            throw new IllegalArgumentException(NOT_COMPLETED_ORDER.getMessage());
        }
    }

    private List<Long> mapToOrderTableIds(final List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Order> orders = findAllOrderByOrderTableIds(orderTables);
        orderValidator.validateOnGoingOrderStatus(orders);
        tableGroup.unGroup(orderTables);
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                        .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND_TABLE_GROUP.getMessage(), tableGroupId)));
    }


    private List<Order> findAllOrderByOrderTableIds(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderRepository.findAllByOrderTableIdIn(orderTableIds);
    }
}
