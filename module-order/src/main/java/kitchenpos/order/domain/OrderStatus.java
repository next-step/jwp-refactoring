package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING("조리중"),
    MEAL("식사중"),
    COMPLETION("계산완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
