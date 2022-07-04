package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(final OrderStatus orderStatus) {
        return orderStatus == COMPLETION;
    }
}
