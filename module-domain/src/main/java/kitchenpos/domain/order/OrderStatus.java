package kitchenpos.domain.order;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isComplete() {
        return this == COMPLETION;
    }
}
