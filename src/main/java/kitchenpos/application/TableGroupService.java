package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.orderTable.OrderTables;
import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import kitchenpos.dto.tableGroup.TableGroupRequest;
import kitchenpos.dto.tableGroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        OrderTables orderTables = new OrderTables(findOrderTablesByIdIn(orderTableIds));
        final OrderTables savedOrderTables = new OrderTables(findOrderTablesByIdIn(orderTableIds));

        orderTables.validateForCreateTableGroup(savedOrderTables);
        savedOrderTables.updateEmpty(false);

        TableGroup tableGroup = tableGroupRequest.toTableGroup(orderTables);
        tableGroup.addOrderTables(savedOrderTables);

        return TableGroupResponse.of(saveTableGroup(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(findOrderTablesByTableGroupId(tableGroupId));

        if (isExistsOrderTablesAndNotCompletion(orderTables)) {
            throw new IllegalArgumentException();
        }

        orderTables.updateTableGroup(null);
    }

    private List<OrderTable> findOrderTablesByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    private TableGroup saveTableGroup(TableGroup tableGroup) {
        return tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> findOrderTablesByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private boolean isExistsOrderTablesAndNotCompletion(OrderTables orderTables) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.findOrderTableIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
