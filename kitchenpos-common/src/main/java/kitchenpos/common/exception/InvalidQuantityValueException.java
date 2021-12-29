package kitchenpos.common.exception;

public class InvalidQuantityValueException extends RuntimeException {

    private static final String ERROR_MESSAGE_INVALID_QUANTITY_VALUE = "수량은 0개 이상이어야 합니다.";

    public InvalidQuantityValueException() {
        super(ERROR_MESSAGE_INVALID_QUANTITY_VALUE);
    }
}
