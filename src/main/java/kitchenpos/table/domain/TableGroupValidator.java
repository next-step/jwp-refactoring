package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.table.exception.NotValidOrderException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupValidator {

    public void availableUnGroup(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            checkOrder(orderTable);
        }
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
}
