package kitchenpos.order.dto;

public class OrderStatusRequest {
    private String orderStatus;

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
