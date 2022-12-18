package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class OrderValidator {

    public static void validateCreateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTableNotEmpty(orderTable);
    }

    private static void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }
    }
}
