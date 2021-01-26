package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.order.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.AlreadyGroupedTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderService orderService;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderService orderService,
                             TableService tableService,
                             TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTables()
                .stream()
                .map(orderTableId -> tableService.findOrderTableById(orderTableId.getId()))
                .collect(Collectors.toList());

        for (final OrderTable savedOrderTable : orderTables) {
            checkTableIsNotEmpty(savedOrderTable);
            checkTableHasGroup(savedOrderTable);
        }
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        return TableGroupResponse.of(tableGroup, orderTables);
    }

    private void checkTableHasGroup(OrderTable savedOrderTable) {
        if (savedOrderTable.hasTableGroup()) {
            throw new AlreadyGroupedTableException(savedOrderTable.getId());
        }
    }

    private void checkTableIsNotEmpty(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalStateException("Cannot create table group if table is not empty");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = tableService.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderService.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalStateException("Cannot change empty state on cooking or meal");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungrouping();
        }
    }
}
