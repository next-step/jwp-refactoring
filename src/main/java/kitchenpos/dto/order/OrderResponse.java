package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;

import static java.util.stream.Collectors.*;

import java.util.List;

public class OrderResponse {
    private Long id;
    private String orderStatus;
    private List<OrderLineMenuResponse> orderLineMenuResponses;

    public OrderResponse(Long id, OrderStatus orderStatus, List<OrderLineMenuResponse> orderLineMenuResponses) {
        this.id = id;
        this.orderStatus = orderStatus.name();
        this.orderLineMenuResponses = orderLineMenuResponses;
    }

    public static OrderResponse of(Orders order) {
        return new OrderResponse(order.getId(), order.getOrderStatus(), OrderLineMenuResponse.ofList(order.getOrderLineMenus()));
    }

    public static List<OrderResponse> ofList(List<Orders> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineMenuResponse> getOrderLineMenuResponses() {
        return orderLineMenuResponses;
    }
}
