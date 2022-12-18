package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderMenuRequest> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderMenuRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
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

    public List<OrderMenuRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
