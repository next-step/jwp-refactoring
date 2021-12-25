package kitchenpos.order.domain;

public enum OrderStatus {
    READY, COOKING, MEAL, COMPLETION;

    public boolean isCompleted() {
        return this == COMPLETION;
    }
}
