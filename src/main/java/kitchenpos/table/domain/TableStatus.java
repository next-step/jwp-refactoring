package kitchenpos.table.domain;

public enum TableStatus {

    EMPTY, FULL;

    public static TableStatus valueOf(boolean empty) {
        if (empty) {
            return EMPTY;
        }
        return FULL;
    }

    boolean isEmpty() {
        return this == EMPTY;
    }
}
