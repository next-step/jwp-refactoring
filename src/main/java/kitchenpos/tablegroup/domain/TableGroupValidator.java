package kitchenpos.tablegroup.domain;

import kitchenpos.common.exception.MinimumOrderTableNumberException;
import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.common.exception.OrderStatusNotCompletedException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Component
public class TableGroupValidator {
    public static final int MINIMUM_ORDER_TABLE_NUMBER = 2;

    private final TableGroupService tableGroupService;

    public TableGroupValidator(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    public void validateCreateTableGroup(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTables();

        validateMinimumOrderTableNumber(orderTables);
        validateOrderTables(orderTables);
    }

    public void validateUnGroupTableGroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();
        for (OrderTable orderTable: orderTables) {
            validateCompleted(orderTable.getOrders());
        }
    }

    private void validateCompleted(List<Order> orders) {
        for (Order order: orders) {
            validateCompleted(order.getOrderStatus());
        }
    }

    private void validateCompleted(OrderStatus orderStatus) {
        if (!OrderStatus.isCompleted(orderStatus)) {
            throw new OrderStatusNotCompletedException();
        }
    }

    private void validateMinimumOrderTableNumber(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_ORDER_TABLE_NUMBER) {
            throw new MinimumOrderTableNumberException();
        }
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateAddableOrderTable(orderTable);
        }
    }

    private void validateAddableOrderTable(OrderTable orderTable) {
        TableGroup tableGroup = tableGroupService.findByTableGroupId(orderTable.getTableGroupId());
        if (!orderTable.isEmpty() || Objects.nonNull(tableGroup)) {
            throw new NotEmptyOrderTableStatusException();
        }
    }
}
