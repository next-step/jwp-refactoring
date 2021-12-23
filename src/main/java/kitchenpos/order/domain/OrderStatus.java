package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isEqualsCompletion(OrderStatus changeOrderStatus) {
        return COMPLETION.equals(changeOrderStatus);
    }
}
