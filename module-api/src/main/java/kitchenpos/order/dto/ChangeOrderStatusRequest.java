package kitchenpos.order.dto;

public class ChangeOrderStatusRequest {

    private String orderStatus;

    public ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ChangeOrderStatusRequest() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
