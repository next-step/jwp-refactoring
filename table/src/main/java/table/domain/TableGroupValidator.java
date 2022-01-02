package table.domain;

import kitchenpos.order.domain.Order;
import table.exception.NotCreatedOrderTablesException;
import table.exception.NotValidOrderException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupValidator {

    public void availableUnGroup(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            checkOrder(orderTable);
        }
    }

    public void availableCreate(OrderTables orderTables, List<Long> orderTableIds) {
        orderTables.validateOrderTable();
        isNotCreatedOrderTables(orderTableIds, orderTables);
    }

    private void checkOrder(OrderTable orderTable) {
        for (Order order : orderTable.getOrders()) {
            checkOrderStatus(order);
        }
    }

    private void checkOrderStatus(Order order) {
        if (order.isProcessing()) {
            throw new NotValidOrderException();
        }
    }

    private static void isNotCreatedOrderTables(List<Long> orderTableIds, OrderTables orderTables) {
        if (!orderTables.isCreatedOrderTable(orderTableIds.size())) {
            throw new NotCreatedOrderTablesException();
        }
    }
}
