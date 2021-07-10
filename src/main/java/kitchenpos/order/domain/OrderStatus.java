package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompletedOrder() {
        return this == COMPLETION;
    }

    public boolean isNotCompletedOrder() {
        return this != COMPLETION;
    }
}
