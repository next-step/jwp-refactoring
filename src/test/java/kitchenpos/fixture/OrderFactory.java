package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;

public class OrderFactory {
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
