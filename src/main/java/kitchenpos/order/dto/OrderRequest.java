package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemDto> orderLineItemDtos;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemDto> orderLineItemDtos) {
        this.orderTableId = orderTableId;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }
}
