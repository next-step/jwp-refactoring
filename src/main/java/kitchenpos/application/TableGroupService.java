package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        TableGroup tableGroup = TableGroup.of(orderTables);

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private static void validateOrderTable(final int countOfOrderTableRequest, final int countOfOrderTable) {
        if(countOfOrderTableRequest != countOfOrderTable) {
            throw new IllegalArgumentException("존재하지 않는 주문테이블 정보가 포함되어 있습니다.");
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
            throw new IllegalArgumentException("주문상태가 완료되지 않은 주문이 포함되어 있습니다.");
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
            orderTableRepository.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("해당 테이블에는 아직 진행중인 주문이 존재합니다.");
        }
    }
}
