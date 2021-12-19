package kichenpos.table.table.domain;

enum CustomerStatus {

    EMPTY, SEATED, ORDERED, FINISH;

    boolean isEmpty() {
        return this == EMPTY;
    }

    boolean isOrdered() {
        return this == ORDERED;
    }
}
