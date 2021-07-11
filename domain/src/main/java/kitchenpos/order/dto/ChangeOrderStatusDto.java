package kitchenpos.order.dto;

public class ChangeOrderStatusDto {
    private String orderStatus;

    public ChangeOrderStatusDto() { }

    public ChangeOrderStatusDto(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
