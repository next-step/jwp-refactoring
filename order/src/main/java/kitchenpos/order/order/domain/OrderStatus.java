package kitchenpos.order.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompleted() {
        return this == COMPLETION;
    }
}
