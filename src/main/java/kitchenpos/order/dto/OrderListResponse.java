package kitchenpos.order.dto;

import java.util.List;

public class OrderListResponse {
    List<OrderResponse> orderResponses;

    public OrderListResponse() {}

    public OrderListResponse(List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
