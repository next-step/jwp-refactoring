package kitchenpos.order.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class OrderRequest {
    private Long orderTableId;

    @NotEmpty
    private List<OrderLineItemRequest> orderLineItems;


    public OrderRequest() {
        // empty
    }

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
}
