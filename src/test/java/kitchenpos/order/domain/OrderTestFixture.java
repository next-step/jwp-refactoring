package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTestFixture {

    public static Order generateOrder(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTable);
        return Order.of(id, orderTable.getId(), orderLineItems);
    }

    public static Order generateOrder(OrderTable orderTable, OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        return Order.of(orderTable.getId(), orderLineItems);
    }

    public static Order generateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTable);
        return Order.of(orderTable.getId(), OrderLineItems.from(orderLineItems));
    }

    public static OrderRequest generateOrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
    }

    private static void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.주문_테이블은_비어있으면_안됨.getErrorMessage());
        }
    }
}
