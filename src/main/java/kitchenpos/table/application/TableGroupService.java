package kitchenpos.table.application;

import java.util.Arrays;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infrastructure.OrderRepository;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.infrastructure.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.infrastructure.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private static final int MINIMUM_SIZE = 2;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = validateEmpty(tableGroupRequest);
        final List<OrderTable> savedOrderTables = validateExistsOrderTables(orderTableIds);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.registerGroupTable(tableGroup);
        }
        return TableGroupResponse.of(tableGroup);
    }

    private List<Long> validateEmpty(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTables().stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException("단체 지정은 최소 2개 테이블 이상이어야 합니다.");
        }
        return orderTableIds;
    }

    private List<OrderTable> validateExistsOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
        return savedOrderTables;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        final List<OrderTable> orderTables = findCompleteOrderTable(tableGroupId);

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroupTable();
        }
    }

    private List<OrderTable> findCompleteOrderTable(Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderStatus(orderTables);
        return orderTables;
    }

    private void validateOrderStatus(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("계산 완료하여야 단체 지정을 해제할 수 있습니다.");
        }
    }
}
