package kitchenpos.order.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<Long> getMenuIds() {
        if (orderLineItems != null) {
            return orderLineItems.stream()
                    .map(OrderLineItemRequest::getMenuId)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

}
