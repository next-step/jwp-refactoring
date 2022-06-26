package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.STARTED_ORDER_READY_STATUS;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTables = tableGroupRequest.getOrderTables();
        final List<OrderTable> savedOrderTables = findAllOrderTable(orderTables);

        validateTableGroup(orderTables, savedOrderTables);

        TableGroup tableGroup = new TableGroup(savedOrderTables);
        tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateTableGroupUnGroup(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateTableGroupUnGroup(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, STARTED_ORDER_READY_STATUS)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTableGroup(List<OrderTableRequest> orderTables, List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable: savedOrderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("테이블 중 비어있지 않거나 이미 다른 그룹에 포함되어 있는 테이블이 있습니다.");
            }
        }
    }

    private List<OrderTable> findAllOrderTable(List<OrderTableRequest> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

}
