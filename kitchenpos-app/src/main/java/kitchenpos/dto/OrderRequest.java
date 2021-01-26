package kitchenpos.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderRequest {
    @NotNull
    private Long orderTableId;
    @Size(min = 1)
    private List<OrderLineItemRequest> orderLineItems;

    @SuppressWarnings("unused")
    protected OrderRequest() {}

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public static OrderRequestBuilder builder() {
        return new OrderRequestBuilder();
    }

    public static final class OrderRequestBuilder {
        private Long orderTableId;
        private List<OrderLineItemRequest> orderLineItems;

        private OrderRequestBuilder() {}

        public OrderRequestBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderRequestBuilder orderLineItems(List<OrderLineItemRequest> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public OrderRequest build() {
            return new OrderRequest(orderTableId, orderLineItems);
        }
    }
}
