package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceFactory {
    public static Order 주문생성(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }
    public static OrderRequest 주문요청생성(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems.stream()
                .map(OrderLineItemRequest::new)
                .collect(Collectors.toList()));
    }
    public static OrderLineItem 주문항목생성(Long menuId, long quantity) {
        return new OrderLineItem(menuId,Long.valueOf(quantity).intValue());
    }

    public static OrderTable 테이블생성(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests,empty);
        return orderTable;
    }
}
