package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCooking() {
        return this == COOKING;
    }

    public boolean isMeal() {
        return this == MEAL;
    }
}
