package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemDto> orderLineItems;

    public OrderRequest() {}

    public OrderRequest(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public OrderRequest(Long orderTableId, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public OrderRequest(Long orderTableId, String orderStatus, List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
