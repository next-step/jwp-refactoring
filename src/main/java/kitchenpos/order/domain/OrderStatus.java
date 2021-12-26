package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION, NONE;

    public static boolean isCompleted(OrderStatus orderStatus) {
        return OrderStatus.COMPLETION == orderStatus;
    }

    public static boolean isMeal(OrderStatus orderStatus) {
        return OrderStatus.MEAL == orderStatus;
    }

    public static boolean isCooking(OrderStatus orderStatus) {
        return OrderStatus.COOKING == orderStatus;
    }
}
