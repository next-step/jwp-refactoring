package kitchenpos.order.dto;

public class ChangeOrderStatusRequest {
    private String orderStatus;

    public ChangeOrderStatusRequest() { }

    public ChangeOrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ChangeOrderStatusDto toDomainDto() {
        return new ChangeOrderStatusDto(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
