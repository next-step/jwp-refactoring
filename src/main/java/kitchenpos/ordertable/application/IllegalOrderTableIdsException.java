package kitchenpos.ordertable.application;

public class IllegalOrderTableIdsException extends IllegalArgumentException {
    public IllegalOrderTableIdsException(String message) {
        super(message);
    }
}
