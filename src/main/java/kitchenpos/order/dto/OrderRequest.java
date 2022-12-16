package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderMenuRequest> orderMenuRequests;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderMenuRequest> orderMenuRequests) {
        this.orderTableId = orderTableId;
        this.orderMenuRequests = orderMenuRequests;
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

    public List<OrderMenuRequest> getOrderMenuRequests() {
        return orderMenuRequests;
    }
}
