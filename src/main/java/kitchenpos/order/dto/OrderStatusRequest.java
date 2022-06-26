package kitchenpos.order.dto;

public class OrderStatusRequest {
    private String orderStatus;

    protected OrderStatusRequest() {
    }

    private OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusRequest from(String orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
