package kitchenpos.table.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean enabledOrderCancel() {
        return this.equals(COMPLETION);
    }
}
