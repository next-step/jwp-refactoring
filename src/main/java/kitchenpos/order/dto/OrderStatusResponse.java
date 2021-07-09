package kitchenpos.order.dto;

public class OrderStatusResponse {
    private String orderStatus;

    public OrderStatusResponse(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusResponse from(String orderStatus) {
        return new OrderStatusResponse(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
