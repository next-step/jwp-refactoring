package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus get(String orderStatus) {
        return OrderStatus.valueOf(orderStatus);
    }
}
