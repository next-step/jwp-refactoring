package kitchenpos.order.domain;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    boolean isCompletion() {
        return this == COMPLETION;
    }
}
