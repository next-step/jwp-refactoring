package order.dto;

public class OrderStatusUpdateRequest {
    private String orderStatus;

    protected OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusUpdateRequest of(String name) {
        return new OrderStatusUpdateRequest(name);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
