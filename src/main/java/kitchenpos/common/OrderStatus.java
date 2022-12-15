package kitchenpos.common;

public enum OrderStatus {
    조리("COOK"),
    식사("MEAL"),
    계산완료("COMPLETION");

    private String orderStatus;

    OrderStatus(String status) {
        this.orderStatus = status;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
