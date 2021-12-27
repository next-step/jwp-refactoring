package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;

public class OrderResponses {
    private List<OrderResponse> orderResponses;

    private OrderResponses(List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrderResponses from(List<Order> orders) {
        return new OrderResponses(orders.stream()
        .map(OrderResponse::from)
        .collect(Collectors.toList()));
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
