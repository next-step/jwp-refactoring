package kitchenpos.table.domain;

public enum CustomerStatus {

    EMPTY, PLACE, ORDERED, FINISH;

    public static CustomerStatus valueOf(boolean empty) {
        if (empty) {
            return EMPTY;
        }
        return PLACE;
    }

    boolean isEmpty() {
        return this == EMPTY;
    }

    boolean isFull() {
        return !isEmpty();
    }

    public boolean isOrdered() {
        return this == ORDERED;
    }
}
