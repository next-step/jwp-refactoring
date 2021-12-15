package kitchenpos.domain.order.fixture;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderLineItem;
import kitchenpos.domain.order.domain.OrderStatus;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.order.dto.OrderLineItemRequest;
import kitchenpos.domain.order.dto.OrderRequest;
import kitchenpos.domain.order.dto.OrderStatusRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderFixture {


    public static Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        return new Order(orderTable.getId(), orderStatus, orderLineItems);
    }

    public static Order 주문(OrderTable orderTable, OrderLineItem orderLineItem, OrderStatus orderStatus) {
        return 주문(orderTable, Arrays.asList(orderLineItem), orderStatus);
    }

    public static Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return 주문(orderTable, orderLineItems, OrderStatus.COOKING);
    }

    public static Order 주문(OrderTable orderTable, OrderLineItem orderLineItem) {
        return 주문(orderTable, Arrays.asList(orderLineItem), OrderStatus.COOKING);
    }

    public static OrderRequest 주문_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getMenu().getId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList()));
    }

    public static OrderRequest 주문_요청(Long orderTableId, OrderLineItem orderLineItem) {
        return 주문_요청(orderTableId, Arrays.asList(orderLineItem));
    }

    public static OrderRequest 주문_요청(OrderTable orderTable, OrderLineItem orderLineItem) {
        return 주문_요청(orderTable.getId(), Arrays.asList(orderLineItem));
    }

    public static OrderRequest 주문_요청(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return 주문_요청(orderTable.getId(), orderLineItems);
    }

    public static OrderStatusRequest 주문_상태_변경_요청(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }
}
