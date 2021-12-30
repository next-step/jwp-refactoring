package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemResponse> orderLineItems = new ArrayList<>();

    public OrderResponse() {

    }

    private OrderResponse(Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus().name();
        this.orderLineItems = OrderLineItemResponse.ofList(order.getOrderLineItems());
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order);
    }

    public static List<OrderResponse> ofList(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

}
