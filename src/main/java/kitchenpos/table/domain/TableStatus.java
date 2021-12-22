package kitchenpos.table.domain;

public enum TableStatus {
    EMPTY,SEATED,ORDERED;

    public static TableStatus valueOfEmpty(Boolean empty) {
        if (Boolean.TRUE.equals(empty)) {
            return EMPTY;
        }
        return SEATED;
    }
}
