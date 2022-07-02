package kitchenpos.order.dto;

public class ChangeOrderStatusRequest {
    private String orderStatus;

    protected ChangeOrderStatusRequest() {
    }

    private ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static ChangeOrderStatusRequest of(String orderStatus) {
        return new ChangeOrderStatusRequest(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
