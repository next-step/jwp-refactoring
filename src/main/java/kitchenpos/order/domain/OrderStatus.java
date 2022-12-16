package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(OrderStatus orderStatus) {
        return COMPLETION.equals(orderStatus);
    }

    public static boolean isCooking(OrderStatus orderStatus) {
        return COOKING.equals(orderStatus);
    }
}
