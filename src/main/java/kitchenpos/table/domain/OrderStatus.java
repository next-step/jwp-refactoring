package kitchenpos.table.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean enabledOrderCancle() {
        return !(this.equals(COOKING) || this.equals(MEAL));
    }
}
