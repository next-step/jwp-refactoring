package kitchenpos.tablegroup.domain;

import kitchenpos.common.exception.CannotUpdateException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import kitchenpos.tablegroup.dto.TableGroupRequest;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.exception.Message.*;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;

@Component
public class TableGroupValidator {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    TableGroupValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void validateGrouping(TableGroupRequest tableGroupRequest) {
        validateOrderTablesForGrouping(getOrderTablesById(tableGroupRequest), tableGroupRequest);
    }

    private List<Order> getOrdersByOrderTable(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return orderRepository.findAllByOrderTableIdIn(orderTableIds);
    }

    private void validateOrderTablesForGrouping(List<OrderTable> orderTables, TableGroupRequest tableGroupRequest) {
        if (orderTables == null || orderTables.size() < TableGroup.MINIMUM_REQUIRED_NUMBER_OF_TABLES) {
            throw new IllegalArgumentException(ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES.showText());
        }

        if (orderTables.size() != tableGroupRequest.getOrderTableIds().size()) {
            throw new IllegalArgumentException(ERROR_TABLES_SHOULD_ALL_BE_REGISTERED_TO_BE_GROUPED.showText());
        }

        for (OrderTable orderTable : orderTables) {
            checkReadinessforGroupAssignment(orderTable);
        }
    }

    private void checkReadinessforGroupAssignment(OrderTable orderTable) {
        if (orderTable.isAssignedToTableGroup() || !orderTable.isEmpty()) {
            throw new IllegalArgumentException(ERROR_TABLES_CANNOT_BE_GROUPED.showText());
        }
    }

    private List<OrderTable> getOrderTablesById(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> validateUngrouping(Long tableGroupId) {
        List<OrderTable> orderTables = getOrderTablesByTableGroupId(tableGroupId);
        List<Order> orders = getOrdersByOrderTable(orderTables);
        validateOrderStatusToUngroup(orders);
        return orderTables;
    }

    private List<OrderTable> getOrderTablesByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    private void validateOrderStatusToUngroup(List<Order> orders) {
        for (Order order : orders) {
            checkReadinessforUngrouping(order);
        }
    }

    private void checkReadinessforUngrouping(Order order) {
        if (order.getOrderStatus() != COMPLETION) {
            throw new CannotUpdateException(ERROR_TABLE_GROUP_CANNOT_BE_UNGROUPED_WHEN_ORDERS_NOT_COMPLETED);
        }
    }
}
