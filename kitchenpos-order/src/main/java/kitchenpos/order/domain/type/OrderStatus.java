package kitchenpos.order.domain.type;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompletion() {
        return this == OrderStatus.COMPLETION;
    }
}
