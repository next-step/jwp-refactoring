package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;

public class OrderFactory {
    public static Order createOrder(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime,
                                    List<OrderLineItem> orderLineItems) {
        Order order = new Order(id, orderTable, OrderStatus.valueOf(orderStatus), orderedTime, orderLineItems);
        return order;
    }

    public static OrderLineItem createOrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem(seq, order, menuId, quantity);
        return orderLineItem;
    }

    public static OrderRequest createOrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }

    public static OrderStatusRequest createOrderStatusRequest(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus.name());
    }

    public static OrderLineItemRequest createOrderLineItemRequest(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
