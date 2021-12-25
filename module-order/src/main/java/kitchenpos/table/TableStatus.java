package kitchenpos.table;

public enum TableStatus {
    EMPTY,SEATED,ORDERED,COMPLETION;

    public static TableStatus valueOfEmpty(Boolean empty) {
        if (Boolean.TRUE.equals(empty)) {
            return EMPTY;
        }
        return SEATED;
    }
}
