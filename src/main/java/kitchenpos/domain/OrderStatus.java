package kitchenpos.domain;

public enum OrderStatus {
    COOKING("COOKING"), MEAL("MEAL"), COMPLETION("COMPLETION");

    private String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
