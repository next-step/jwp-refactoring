package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String s) {
        return OrderStatus.valueOf(s.toUpperCase());
    }
}
