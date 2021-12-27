package kitchenpos.order.mapper;

import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus().name(), order.getCreatedDate(), getOrderLineIte(order));
    }

    public static List<OrderResponse> toOrderResponses(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    private static List<OrderResponse.OrderLineItem> getOrderLineIte(final Order order) {
        return order.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderResponse.OrderLineItem(orderLineItem.getSeq(), orderLineItem.getOrder().getId(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

    }
}
