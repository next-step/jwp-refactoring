package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompletion() {
        return OrderStatus.COMPLETION == this;
    }

    public boolean isCookingOrMeal() {
        return OrderStatus.COOKING == this || OrderStatus.MEAL == this;
    }
}
