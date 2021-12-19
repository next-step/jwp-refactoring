package kitchenpos.domain.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isComplete() {
        return this == COMPLETION;
    }
}
