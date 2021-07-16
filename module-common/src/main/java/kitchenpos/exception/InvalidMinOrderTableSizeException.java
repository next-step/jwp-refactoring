package kitchenpos.exception;

public class InvalidMinOrderTableSizeException extends RuntimeException {

    public InvalidMinOrderTableSizeException() {
    }

    public InvalidMinOrderTableSizeException(String message) {
        super(message);
    }
}
