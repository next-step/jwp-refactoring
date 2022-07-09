package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean enabledTableClear() {
        return this.equals(COMPLETION);
    }
}
