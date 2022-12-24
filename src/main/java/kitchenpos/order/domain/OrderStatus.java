package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING("조리"),
    MEAL("식사"),
    COMPLETION("계산 완료");

    OrderStatus(String description) {
    }
}
