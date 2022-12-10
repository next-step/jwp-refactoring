package kitchenpos.order.dto;

public class UpdateOrderStatusRequest {
    private String orderStatus;

    protected UpdateOrderStatusRequest() {}

    private UpdateOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static UpdateOrderStatusRequest of(String orderStatus) {
        return new UpdateOrderStatusRequest(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
