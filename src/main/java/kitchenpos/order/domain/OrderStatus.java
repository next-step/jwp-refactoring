package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isComplete() {
        return this.equals(COMPLETION);
    }
}
