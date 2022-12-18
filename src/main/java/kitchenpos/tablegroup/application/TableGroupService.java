package kitchenpos.tablegroup.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.ErrorMessage.NOT_COMPLETED_ORDER;
import static kitchenpos.common.ErrorMessage.TABLE_HAVE_ONGOING_ORDER;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTableRequest> orderTablesRequest = request.getOrderTables();
        List<OrderTable> orderTables = findOrderTables(orderTablesRequest);
        validateOrderTable(orderTables.size(), request.getOrderTables().size());
        TableGroup tableGroup = TableGroup.of(OrderTables.from(orderTables));

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private static void validateOrderTable(final int countOfOrderTableRequest, final int countOfOrderTable) {
        if(countOfOrderTableRequest != countOfOrderTable) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_ORDER_TABLE_INFO.getMessage());
        }
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
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrderStatus(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
//            orderTableRepository.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(TABLE_HAVE_ONGOING_ORDER.getMessage());
        }
    }
}
