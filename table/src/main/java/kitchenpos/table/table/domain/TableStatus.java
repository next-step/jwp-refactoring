package kitchenpos.table.table.domain;

public enum TableStatus {
	EMPTY, SEATED, ORDERED, FINISH;

	boolean isEmpty() {
		return this == EMPTY;
	}

	boolean isOrdered() {
		return this == ORDERED;
	}
}
