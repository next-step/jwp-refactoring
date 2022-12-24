package kitchenpos.common;

public enum TableStatus {
    EMPTY(true),
    USING(false);

    private boolean tableStatus;

    TableStatus(boolean tableStatus) {
        this.tableStatus = tableStatus;
    }

    public boolean isTableEmpty() {
        return tableStatus;
    }
}
