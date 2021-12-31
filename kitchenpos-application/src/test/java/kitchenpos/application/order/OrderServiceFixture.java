package kitchenpos.application.order;

import kitchenpos.core.domain.OrderLineItem;
import kitchenpos.application.order.dto.OrderLineItemRequest;
import kitchenpos.application.order.dto.OrderRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceFixture {
    private OrderServiceFixture() {
    }

    public static List<OrderLineItemRequest> getOrderLineRequests(OrderLineItem... orderLineItems) {
        return Arrays.stream(orderLineItems)
                .map(OrderServiceFixture::getOrderLineRequest)
                .collect(Collectors.toList());
    }

    public static OrderLineItemRequest getOrderLineRequest(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public static OrderRequest getChangeRequest(String status) {
        return new OrderRequest(status);
    }

    public static OrderRequest getCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }
}
