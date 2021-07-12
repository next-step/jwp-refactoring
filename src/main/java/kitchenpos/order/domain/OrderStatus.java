package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING("조리"), MEAL("식사"), COMPLETION("계산 완료");

    private final String remark;

    OrderStatus(final String remark) {
        this.remark = remark;
    }

    public String remark() {
        return remark;
    }
}
