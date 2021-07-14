package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompleted() {
        return this.equals(COMPLETION);
    }

    public boolean inProgress() {
        return !this.equals(COMPLETION);
    }
}
