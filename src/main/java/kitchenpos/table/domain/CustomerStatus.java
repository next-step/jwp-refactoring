package kitchenpos.table.domain;

public enum CustomerStatus {

    EMPTY, SEATED, ORDERED, FINISH;

    boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isOrdered() {
        return this == ORDERED;
    }
}
