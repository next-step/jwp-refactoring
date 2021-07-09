package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.table.exception.NotInitOrderTablesException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTables orderTables = new OrderTables(request.getOrderTables());
        checkInitOrderTables(request.getOrderTables().size(), orderTables.toOrderTableIds());
        final TableGroup savedTableGroup = tableGroupRepository.save(
                new TableGroup(LocalDateTime.now(), request.getOrderTables()));

        return TableGroupResponse.from(savedTableGroup);
    }

    private void checkInitOrderTables(int size, List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        if (size != savedOrderTables.size()) {
            throw new NotInitOrderTablesException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            checkOrderTableEmptyOrGroupIdNull(savedOrderTable);
        }
    }

    private void checkOrderTableEmptyOrGroupIdNull(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalOrderTableException();
        }
    }

    public void ungroup(final Long tableGroupId) {

        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTableRepository.save(orderTable);
        }
    }
}
