package kitchenposNew.order;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompletion() {
        return this == COMPLETION;
    }

    public boolean isCookingOrMeal() {
        return this == COOKING || this == MEAL;
    }
}
