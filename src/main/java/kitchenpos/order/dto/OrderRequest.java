package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;

    @JsonProperty("orderLineItems")
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Order toOrder(List<OrderLineItem> orderLineItems) {
        return new Order.Builder()
                .orderTableId(orderTableId)
                .orderLineItems(orderLineItems)
                .build();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }


    public static class Builder {

        private Long orderTableId;
        private OrderStatus orderStatus;
        private List<OrderLineItemRequest> orderLineItemRequests;

        public Builder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderLineItemRequests(List<OrderLineItemRequest> orderLineItemRequests) {
            this.orderLineItemRequests = orderLineItemRequests;
            return this;
        }

        public OrderRequest build() {
            return new OrderRequest(orderTableId, orderStatus, orderLineItemRequests);
        }
    }

}
