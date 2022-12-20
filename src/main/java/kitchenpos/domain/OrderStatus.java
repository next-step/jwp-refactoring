package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCooking() {
        return this == COOKING;
    }

    public boolean isMeal() {
        return this == MEAL;
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
