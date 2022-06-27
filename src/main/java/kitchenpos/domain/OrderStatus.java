package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isNotCompletion() {
        return !this.equals(COMPLETION);
    }
}
